package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserRepository {

    public static void playerJoin(int userId, Connection con, Scanner sc) throws SQLException {
        String insertSql = "INSERT INTO PLAYER VALUES(PLAYER_SEQ.nextval, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS'), ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(insertSql);

        System.out.println();
        System.out.println("==== 선수등록 메뉴 입니다. ====");
        System.out.print("선수명을 입력하시오 >> ");
        pstmt.setString(1, sc.nextLine());
        System.out.print("나이를 입력하시오 >> ");
        pstmt.setInt(2, sc.nextInt());
        sc.nextLine();
        System.out.print("키를 입력하시오 >> ");
        pstmt.setInt(3, sc.nextInt());
        sc.nextLine();
        System.out.print("팀을 고르시오(1. ManUtd, 2.ManCity, 3.Bayern, 4.Dortmund, 5.Braca, 6.Real) >> ");
        pstmt.setInt(4, sc.nextInt());
        sc.nextLine();
        pstmt.setInt(5, userId);
        int result = pstmt.executeUpdate();

        if (result > 0) System.out.println("선수등록이 성공하셨습니다 !");
        else System.out.println("선수등록에 실패하셨습니다 !");

        pstmt.close();
        System.out.println();
    }

    public static void playerFindAll(Connection con) throws SQLException {
        System.out.println("\n==== 선수 목록 조회 메뉴 입니다. ====");
        List<String> playerNameList = new ArrayList<>();
        String sql = "SELECT PLAYER_NAME FROM PLAYER";

        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            playerNameList.add(rs.getString("PLAYER_NAME"));
        }

        for (int i = 0; i < playerNameList.size(); i++) {
            System.out.println((i + 1) + "번 째 선수 : " + playerNameList.get(i));
        }
        System.out.println();
        rs.close();
        pstmt.close();
    }

    public static void findToTeam(Connection con) throws SQLException {
        System.out.println("\n==== 팀 조회 메뉴 입니다. ====");
        List<String> teamList = new ArrayList<>();
        String sql = "SELECT TEAM_NAME FROM TEAM";

        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            teamList.add(rs.getString("TEAM_NAME"));
        }

        for (int i = 0; i < teamList.size(); i++) {
            System.out.println((i + 1) + "번 째 팀 : " + teamList.get(i));
        }

        System.out.println();
        rs.close();
        pstmt.close();
    }

    public static void findToLeague(Connection con) throws SQLException {
        System.out.println("\n==== 리그조회 메뉴 입니다. ====");
        List<String> leagueList = new ArrayList<>();
        String sql = "SELECT LEAGUE_NAME FROM LEAGUE";

        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            leagueList.add(rs.getString("LEAGUE_NAME"));
        }

        for (int i = 0; i < leagueList.size(); i++) {
            System.out.println((i + 1) + "번 째 팀 : " + leagueList.get(i));
        }

        System.out.println();
        rs.close();
        pstmt.close();
    }

    // 로그인한 유저가 만든 선수만 삭제할 수 있다 !!
    public static void deleteToPlayer(int userId, Connection con, Scanner sc) throws SQLException {
        System.out.println("\n==== 선수등록 해제 메뉴 입니다. ====");
        String deleteSql = "DELETE FROM PLAYER WHERE PLAYER_NAME=? AND USER_ID=?";
        PreparedStatement pstmt = con.prepareStatement(deleteSql);

        System.out.print("삭제할 선수명을 입력해주세요 >> ");
        pstmt.setString(1, sc.nextLine());
        pstmt.setInt(2, userId);

        int result = pstmt.executeUpdate();
        if (result > 0) System.out.println("선수 삭제가 완료되었습니다 !");
        else System.out.println("선수 삭제가 실패했습니다 !");

        System.out.println();
    }
}
