import service.UsersService;

import java.util.Scanner;

import static util.TitleUtil.clearScreen;
import static util.TitleUtil.title;

public class Main {


    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int count = 0;
        System.out.println();

        System.out.println(title);
        while (true) {
            if (count != 0) clearScreen();
            System.out.println("==== 메뉴를 선택해주세요 ====");
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 회원탈퇴");
            System.out.println("4. 종료");
            System.out.print("번호를 입력하시오 >> ");

            String select = sc.nextLine();

            switch (select) {
                case "1" -> {
                    clearScreen();
                    System.out.println("==== 회원가입 메뉴입니다. ====");
                    System.out.print("UserName(ID)를 입력하세요 >> ");
                    String username = sc.nextLine();
                    System.out.print("PW를 입력하세요 >> ");
                    String password = sc.nextLine();
                    System.out.print("ROLE 입력하세요(Admin or Player) >> ");
                    String role = sc.nextLine();
                    System.out.print("주소를 입력하세요 >> ");
                    String address = sc.nextLine();

                    UsersService.join(username, password, role, address);
                }
                case "2" -> {
                    clearScreen();
                    System.out.println("==== 로그인 메뉴입니다. ====");
                    System.out.print("아이디를 입력하시오 >> ");
                    String username = sc.nextLine();
                    System.out.print("비밀번호를 입력하시오 >> ");
                    String password = sc.nextLine();
                    UsersService.login(username, password, sc);
                }
                case "3" -> {
                    clearScreen();
                    System.out.println("==== 회원탈퇴 메뉴입니다. ====");
                    System.out.print("탈퇴할 아이디를 입력하시오 >> ");
                    String username = sc.nextLine();
                    System.out.print("탈퇴할 비밀번호를 입력하시오 >> ");
                    String password = sc.nextLine();
                    UsersService.deleteUser(username, password);
                }
            }
            if (select.equals("4")) break;
            else {
                System.out.println("잘못 입력하셨습니다!");
                Thread.sleep(1000);
            }
            count++;
        }
        sc.close();
        System.out.println("프로그램이 종료되었습니다");
    }
}