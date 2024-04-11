package service;

import domain.Users;
import org.mindrot.jbcrypt.BCrypt;
import util.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
