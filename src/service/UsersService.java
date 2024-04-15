package service;

import domain.Users;
import org.mindrot.jbcrypt.BCrypt;
import repository.AdminRepository;
import repository.UserRepository;
import util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UsersService {
    /* 회원가입 메서드
    1. 동일한 아이디가 있는지 체크해야 한다
    2. 동일한 아이디가 있다면 경고창을 출력하고 return
    3. 비밀번호 암호화 -> BC
    3. 동일한 아이디가 없다면 회원가입 로직을 날리고 성공했다고 반환
     */
    public static int join(Users user) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Class.forName("oracle.jdbc.driver.OracleDriver");
        con = DBConnectionUtil.getConnection();

        // 1. 동일한 아이디 체크
        String checkSql = "SELECT USER_NAME FROM USERS WHERE USER_NAME = ?";
        pstmt = con.prepareStatement(checkSql);

        pstmt.setString(1, user.getUsername());
        rs = pstmt.executeQuery();
        if (rs.next()) {
            String findUsername = rs.getString("USER_NAME");
            if (!findUsername.isEmpty()) {
                System.out.println("이미 회원가입이 된 유저입니다.");
                return -1;
            }
        }

        // 2. 비밀번호 암호화
        String encodePW = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        // 3. DB 저장
        String insertSql = "INSERT INTO USERS VALUES(USERS_SEQ.nextval, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS'), TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS'))";

        pstmt = con.prepareStatement(insertSql);
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, encodePW);
        pstmt.setString(3, user.getRole());
        pstmt.setString(4, user.getAddress());
        int result = pstmt.executeUpdate();
        DBConnectionUtil.close(con, pstmt, rs);

        return result; // 변경되는 값이 없으면 0 반환
    }

    /* 로그인 메서드
    1. ID(USER_NAME컬럼)을 입력받아 DB 에 존재하는지 체크(잘못된 유저면 return)
    2. 로그인한 유저는 성공 메시지를 보내고 무한루프를 돌린다.
    3. 팀 검색, 리그 검색, 선수등록/해제 기능 가능
    4. 루프를 탈출하는것은 로그아웃
     */
    public static void login(String username, String password, Scanner sc) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

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
                return;
            }
        } else { // 반환 컬럼이 없다면
            System.out.println("가입되어 있지 않은 회원입니다 !");
            return;
        }
        System.out.println("로그인에 성공하셨습니다!");

        // Admin 인지 Player 인지 분기가 필요
        String findRole = rs.getString("ROLE");
        int userId = rs.getInt("USER_ID");

        if (findRole.equals("Admin")) {
            while (true) {
                System.out.println("==== 메뉴를 선택하세요 ====");
                System.out.println("1. 리그 추가");
                System.out.println("2. 리그 수정");
                System.out.println("3. 리그 삭제");
                System.out.println("4. 팀 추가");
                System.out.println("5. 팀 수정");
                System.out.println("6. 팀 삭제");
                System.out.println("7. 로그아웃");
                System.out.print("번호를 입력하시오 >> ");
                String select = sc.nextLine();

                switch (select) {
                    case "1" -> AdminRepository.insertLeague(con, sc);
                    case "2" -> AdminRepository.updateLeague(con, sc);
                    case "3" -> AdminRepository.deleteLeague(con, sc);
                    case "4" -> AdminRepository.insertTeam(con, sc);
                    case "5" -> AdminRepository.updateTeam(con, sc);
                    case "6" -> AdminRepository.deleteTeam(con, sc);
                }
                if (select.equals("7")) {
                    System.out.println("로그아웃 되었습니다 !");
                    break;
                }
            }
        } else if (findRole.equals("Player")) {
            while (true) {
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
                    case "2" -> UserRepository.playerFindAll(con);
                    case "3" -> UserRepository.findToTeam(con);
                    case "4" -> UserRepository.findToLeague(con);
                    case "5" -> UserRepository.deleteToPlayer(userId, con, sc);
                    case "6" -> UserRepository.updateToPlayer(userId, con, sc);
                }
                if (select.equals("7")) {
                    System.out.println("로그아웃 되었습니다 !");
                    break;
                }
            }
        }

        DBConnectionUtil.close(con, pstmt, rs);
    }

    public static void deleteUser(String username, String password, Scanner sc) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

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
                return;
            }
        } else { // 반환 컬럼이 없다면
            System.out.println("가입되어 있지 않은 회원입니다 !");
            return;
        }

        String deleteSql = "DELETE FROM USERS WHERE USER_NAME=?";
        pstmt = con.prepareStatement(deleteSql);
        pstmt.setString(1, username);
        int result = pstmt.executeUpdate();
        if (result > 0) System.out.println("회원탈퇴를 성공하였습니다 !");
        else System.out.println("회원탈퇴를 실패하셨습니다 !");

        DBConnectionUtil.close(con, pstmt, rs);
    }
}
