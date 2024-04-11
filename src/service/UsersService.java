package service;

import domain.Users;
import org.mindrot.jbcrypt.BCrypt;
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
        String insertSql = "INSERT INTO USERS VALUES(USERS_SEQ.nextval, ?, ?, ?, ?)";

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
    public static void login(String username, String password,Scanner sc) throws ClassNotFoundException, SQLException {
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
                System.out.println("회원가입이 되어있지 않습니다 !!!");
                return;
            }
        }
        while (true) {
            System.out.println("==== 메뉴를 선택하세요 ====");
            System.out.println("1. 선수등록");
            System.out.println("2. 팀 조회");
            System.out.println("3. 리그 조회");
            System.out.println("4. 선수 등록 해제");
            System.out.println("5. 로그아웃");
            System.out.print(">>>>>>>>>> ");
            String select = sc.nextLine();

            switch (select) {
                case "1" -> UserRepository.playerJoin();
                case "2" -> UserRepository.findToTeam();
                case "3" -> UserRepository.findToLeague();
                case "4" -> UserRepository.deleteToPlayer();
            }
            if (select.equals("5")) {
                break;
            }
        }

        DBConnectionUtil.close(con, pstmt, rs);
    }
}
