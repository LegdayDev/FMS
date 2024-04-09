package repository;

import util.DBConnectionUtil;

import java.sql.*;
import java.util.Scanner;

public class JdbcRepositoryTest {
    public static void main(String[] args) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Scanner sc = new Scanner(System.in);
        String sql = "";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DBConnectionUtil.getConnection();

            // 데이터 조회
            sql = "SELECT * FROM PLAYER";
            pstmt = con.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(rs.getString(2));
            }

            // 데이터 삽입
            sql = "INSERT INTO PLAYER VALUES(PLAYER_SEQ.nextval, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS'), ?)";
            pstmt = con.prepareStatement(sql);
            System.out.print("선수명을 입력하시오 >> ");
            pstmt.setString(1, sc.nextLine());

            System.out.print("나이를 입력하시오 >> ");
            pstmt.setInt(2, sc.nextInt());
            sc.nextLine();

            System.out.print("신장을 입력하시오(cm) >> ");
            pstmt.setInt(3, sc.nextInt());
            sc.nextLine();

            System.out.print("팀 번호를 입력하시오(1. ManUtd, 2.ManCity, 3.Bayern, 4.Dortmund, 5.Braca, 6.Real) >> ");
            pstmt.setInt(4, sc.nextInt());

            int resultInsert = pstmt.executeUpdate();

            if (resultInsert != 0) System.out.println("INSERT 쿼리가 성공했습니다 !");
            else System.out.println("쿼리 실패 !");


            sql = "UPDATE PLAYER SET TEAM_ID = ? WHERE PLAYER_NAME = ?";
            pstmt = con.prepareStatement(sql);

            System.out.print("변경할 팀을 고르시오(1. ManUtd, 2.ManCity, 3.Bayern, 4.Dortmund, 5.Braca, 6.Real) >> ");
            pstmt.setInt(1, sc.nextInt());
            sc.nextLine();

            System.out.print("선수명을 입력하시오 >> ");
            pstmt.setString(2, sc.nextLine());

            int resultUpdate = pstmt.executeUpdate();
            if (resultUpdate != 0) System.out.println("UPDATE 쿼리가 성공했습니다 !");
            else System.out.println("쿼리 실패 !");


            sql = "DELETE FROM PLAYER WHERE PLAYER_NAME=?";
            pstmt = con.prepareStatement(sql);

            System.out.print("삭제할 선수명을 입력하시오 >> ");
            pstmt.setString(1, sc.nextLine());

            int resultDelete = pstmt.executeUpdate();
            if (resultDelete != 0) System.out.println("DELETE 쿼리가 성공했습니다!");
            else System.out.println("쿼리 실패 !!");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(con, pstmt, rs);
        }
    }

    public static void close(Connection con, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
