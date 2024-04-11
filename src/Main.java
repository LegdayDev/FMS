import domain.Users;
import service.UsersService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        // 회원가입 테스트
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

        while(true){
            System.out.println("==== 메뉴를 선택해주세요 ====");
            System.out.println("1. 회원가입");
            System.out.println("2. 종료");

            String select = sc.nextLine();

            switch (select){
                case "1" -> { // 회원가입
                    System.out.print("UserName(ID)를 입력하세요 >> ");
                    String username = sc.nextLine();
                    System.out.print("PW를 입력하세요 >> ");
                    String password = sc.nextLine();
                    System.out.print("ROLW 입력하세요(Admin or Player) >> ");
                    String role = sc.nextLine();
                    System.out.print("주소를 입력하세요 >> ");
                    String address = sc.nextLine();

                    Users users = new Users(username, password, role, address);
                    int result = UsersService.join(users);
                    if(result > 0) System.out.println("회원가입에 성공하셨습니다.");
                    else System.out.println("회원가입에 실패하셨습니다!");
                }
            }
            if(select.equals("2")){
                break;
            }
        }
        System.out.println("프로그램이 종료되었습니다");
    }
}