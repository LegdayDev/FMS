package service;

import org.mindrot.jbcrypt.BCrypt;
import repository.AdminRepository;
import repository.UserRepository;
import util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import static util.TitleUtil.*;


public class UsersService {

    public static void join(String username, String password, String role, String address) throws Exception {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;

        Class.forName("oracle.jdbc.driver.OracleDriver");
        con = DBConnectionUtil.getConnection();

        // 1. 동일한 아이디 체크
        String checkSql = "SELECT USER_NAME FROM USERS WHERE USER_NAME = ?";
        pstmt = con.prepareStatement(checkSql);

        pstmt.setString(1, username);
        rs = pstmt.executeQuery();
        if (rs.next()) {
            String findUsername = rs.getString("USER_NAME");
            if (!findUsername.isEmpty()) {
                System.out.println("이미 회원가입이 된 유저입니다.");
                Thread.sleep(1500);
                return;
            }
        }

        String encodePW = BCrypt.hashpw(password, BCrypt.gensalt());

        String insertSql = "INSERT INTO USERS VALUES(USERS_SEQ.nextval, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS'), TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS'))";

        pstmt = con.prepareStatement(insertSql);
        pstmt.setString(1, username);
        pstmt.setString(2, encodePW);
        pstmt.setString(3, role);
        pstmt.setString(4, address);
        int result = pstmt.executeUpdate();

        if (result > 0) System.out.println("회원가입에 성공하셨습니다.");
        else System.out.println("회원가입에 실패하셨습니다!");
        Thread.sleep(1500);
        DBConnectionUtil.close(con, pstmt, rs);
    }


    public static void login(String username, String password, Scanner sc) throws Exception {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;

        Class.forName("oracle.jdbc.driver.OracleDriver");
        con = DBConnectionUtil.getConnection();

        // 올바른 User 인지 체크
        String checkSql = "SELECT * FROM USERS WHERE USER_NAME=?";
        pstmt = con.prepareStatement(checkSql);

        pstmt.setString(1, username);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            String findUsername = rs.getString("USER_NAME");
            String findPassword = rs.getString("PASSWORD");
            boolean checkPW = BCrypt.checkpw(password, findPassword); // 같지않으면 false
            if (findUsername.isEmpty() || !checkPW) {
                System.out.println("아이디 혹은 비밀번호가 맞지 않습니다 !");
                Thread.sleep(1500);
                return;
            }
        } else {
            System.out.println("가입되어 있지 않은 회원입니다 !");
            Thread.sleep(1500);
            return;
        }
        System.out.println("로그인에 성공하셨습니다!");
        Thread.sleep(1500);
        String findRole = rs.getString("ROLE");
        int userId = rs.getInt("USER_ID");

        if (findRole.equals("Admin") || findRole.equals("admin")) {
            while (true) {
                clearScreen();
                System.out.println("==== 메뉴를 선택하세요 ====");
                System.out.println("1. 리그 추가");
                System.out.println("2. 리그 조회");
                System.out.println("3. 리그 수정");
                System.out.println("4. 리그 삭제");
                System.out.println("5. 팀 추가");
                System.out.println("6. 팀 조회");
                System.out.println("7. 팀 수정");
                System.out.println("8. 팀 삭제");
                System.out.println("9. 로그아웃");
                System.out.print("번호를 입력하시오 >> ");
                String select = sc.nextLine();

                switch (select) {
                    case "1" -> AdminRepository.insertLeague(con, sc);
                    case "2" -> UserRepository.findToLeague(con, sc);
                    case "3" -> AdminRepository.updateLeague(con, sc);
                    case "4" -> AdminRepository.deleteLeague(con, sc);
                    case "5" -> AdminRepository.insertTeam(con, sc);
                    case "6" -> UserRepository.findToTeam(con, sc);
                    case "7" -> AdminRepository.updateTeam(con, sc);
                    case "8" -> AdminRepository.deleteTeam(con, sc);
                }
                if (select.equals("9")) {
                    System.out.println("로그아웃 되었습니다 !");
                    Thread.sleep(1500);
                    break;
                }
            }
        } else if (findRole.equals("Player") || findRole.equals("player")) {
            while (true) {
                clearScreen();
                System.out.println("==== 메뉴를 선택하세요 ====");
                System.out.println("1. 선수등록");
                System.out.println("2. 선수목록 조회");
                System.out.println("3. 팀 조회");
                System.out.println("4. 리그 조회");
                System.out.println("5. 선수 삭제");
                System.out.println("6. 선수 정보 수정");
                System.out.println("7. 로그아웃");
                System.out.print("번호를 입력하시오 >>  ");
                String select = sc.nextLine();

                switch (select) {
                    case "1" -> UserRepository.playerJoin(userId, con, sc);
                    case "2" -> UserRepository.playerFindAll(con, sc);
                    case "3" -> UserRepository.findToTeam(con, sc);
                    case "4" -> UserRepository.findToLeague(con, sc);
                    case "5" -> UserRepository.deleteToPlayer(userId, con, sc);
                    case "6" -> UserRepository.updateToPlayer(userId, con, sc);
                }
                if (select.equals("7")) {
                    System.out.println("로그아웃 되었습니다 !");
                    Thread.sleep(1500);
                    break;
                }
            }
        }

        DBConnectionUtil.close(con, pstmt, rs);
    }

    public static void deleteUser(String username, String password) throws ClassNotFoundException, SQLException, InterruptedException {
        Connection con;
        PreparedStatement pstmt;
        ResultSet rs;

        Class.forName("oracle.jdbc.driver.OracleDriver");
        con = DBConnectionUtil.getConnection();


        String checkSql = "SELECT * FROM USERS WHERE USER_NAME=?";
        pstmt = con.prepareStatement(checkSql);

        pstmt.setString(1, username);
        rs = pstmt.executeQuery();

        if (rs.next()) {
            String findUsername = rs.getString("USER_NAME");
            String findPassword = rs.getString("PASSWORD");
            boolean checkPW = BCrypt.checkpw(password, findPassword); // 같지않으면 false
            if (findUsername.isEmpty() || !checkPW) {
                System.out.println("아이디 혹은 비밀번호가 맞지 않습니다 !");
                Thread.sleep(1500);
                return;
            }
        } else {
            System.out.println("가입되어 있지 않은 회원입니다 !");
            Thread.sleep(1500);
            return;
        }

        String deleteSql = "DELETE FROM USERS WHERE USER_NAME=?";
        pstmt = con.prepareStatement(deleteSql);
        pstmt.setString(1, username);
        int result = pstmt.executeUpdate();

        if (result > 0) System.out.println("회원탈퇴를 성공하였습니다 !");
        else System.out.println("회원탈퇴를 실패하셨습니다 !");
        Thread.sleep(1500);
        DBConnectionUtil.close(con, pstmt, rs);
    }
}
