import service.UsersService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        String title =
                """
                                 _______   ______     ______   .___________.   .______        ___       __       __     \s
                                |   ____| /  __  \\   /  __  \\  |           |   |   _  \\      /   \\     |  |     |  |    \s
                                |  |__   |  |  |  | |  |  |  | `---|  |----`   |  |_)  |    /  ^  \\    |  |     |  |    \s
                                |   __|  |  |  |  | |  |  |  |     |  |        |   _  <    /  /_\\  \\   |  |     |  |    \s
                                |  |     |  `--'  | |  `--'  |     |  |        |  |_)  |  /  _____  \\  |  `----.|  `----.
                                |__|      \\______/   \\______/      |__|        |______/  /__/     \\__\\ |_______||_______|
                                                                                                                        \s
                        """;
        System.out.println(title);

        while (true) {
            System.out.println("==== 메뉴를 선택해주세요 ====");
            System.out.println("1. 회원가입");
            System.out.println("2. 로그인");
            System.out.println("3. 회원탈퇴");
            System.out.println("4. 종료");
            System.out.print("번호를 입력하시오 >> ");

            String select = sc.nextLine();

            switch (select) {
                case "1" -> {
                    System.out.print("UserName(ID)를 입력하세요 >> ");
                    String username = sc.nextLine();
                    System.out.print("PW를 입력하세요 >> ");
                    String password = sc.nextLine();
                    System.out.print("ROLW 입력하세요(Admin or Player) >> ");
                    String role = sc.nextLine();
                    System.out.print("주소를 입력하세요 >> ");
                    String address = sc.nextLine();

                    UsersService.join(username, password, role, address);
                }
                case "2" -> {
                    System.out.print("아이디를 입력하시오 >> ");
                    String username = sc.nextLine();
                    System.out.print("비밀번호를 입력하시오 >> ");
                    String password = sc.nextLine();
                    UsersService.login(username, password, sc);
                }
                case "3" -> {
                    System.out.print("탈퇴할 아이디를 입력하시오 >> ");
                    String username = sc.nextLine();
                    System.out.print("탈퇴할 비밀번호를 입력하시오 >> ");
                    String password = sc.nextLine();
                    UsersService.deleteUser(username, password, sc);
                }
            }
            if (select.equals("4")) break;
        }
        sc.close();
        System.out.println("프로그램이 종료되었습니다");
    }
}