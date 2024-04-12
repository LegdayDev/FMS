package repository;

import dto.PlayerListDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserRepository {

    public static void playerJoin(int userId, Connection con, Scanner sc) throws SQLException {
        String insertSql = "INSERT INTO PLAYER VALUES(PLAYER_SEQ.nextval, ?, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS'), TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS'))";
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
        List<PlayerListDto> dtoList = new ArrayList<>();
        String sql = "SELECT PLAYER_NAME, AGE, HEIGHT, TEAM_NAME FROM PLAYER INNER JOIN TEAM ON PLAYER.TEAM_ID = TEAM.TEAM_ID";

        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            String playerName = rs.getString("PLAYER_NAME");
            int age = rs.getInt("AGE");
            int height = rs.getInt("HEIGHT");
            String teamName = rs.getString("TEAM_NAME");
            dtoList.add(new PlayerListDto(playerName, age, height, teamName));
        }

        for (int i = 0; i < dtoList.size(); i++) {
            System.out.println((i+1) +"번 째 : " + dtoList.get(i));
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
        else System.out.println("선수명이 존재하지 않거나 삭제 권한이 없습니다!");

        System.out.println();
    }

    // 선수 정보 업데이트 메서드
    public static void updateToPlayer(int userId, Connection con, Scanner sc) throws SQLException {
        System.out.println("\n==== 선수정보 수정 메뉴 입니다. ====");
        System.out.print("수정할 정보를 골라주세요(1. 선수명, 2. 나이, 3. 팀) >> ");
        String select = sc.nextLine();

        switch (select) {
            case "1" -> {
                System.out.print("기존 선수명을 입력하세요 >> ");
                String originalName = sc.nextLine();
                System.out.print("새로운 선수명을 입력하세요 >> ");
                String newName = sc.nextLine();

                String updateSql = "UPDATE PLAYER SET PLAYER_NAME=? WHERE USER_ID=? AND PLAYER_NAME=?";
                PreparedStatement pstmt = con.prepareStatement(updateSql);

                pstmt.setString(1, newName);
                pstmt.setInt(2, userId);
                pstmt.setString(3, originalName);

                int result = pstmt.executeUpdate();
                if (result > 0) System.out.println("선수명 변경에 성공하셨습니다 !");
                else System.out.println("선수가 존재하지 않거나 수정권한이 없습니다 !");
            }
            case "2" -> {
                System.out.print("변경할 선수명을 입력해주세요 >> ");
                String playerName = sc.nextLine();
                System.out.print("변경할 나이를 입력하시오 >> ");
                int age = sc.nextInt();
                sc.nextLine();

                String updateSql = "UPDATE PLAYER SET AGE=? WHERE USER_ID=? AND PLAYER_NAME=?";
                PreparedStatement pstmt = con.prepareStatement(updateSql);
                pstmt.setInt(1, age);
                pstmt.setInt(2, userId);
                pstmt.setString(3, playerName);

                int result = pstmt.executeUpdate();
                if (result > 0) System.out.println(playerName + " 선수 나이 변경이 성공하였습니다 !");
                else System.out.println("선수가 존재하지 않거나 수정권한이 없습니다 !");
            }
            case "3" -> {
                System.out.print("변경할 선수명을 입력해주세요 >> ");
                String playerName = sc.nextLine();
                System.out.print("변경할 팀을 골라주세요(1. ManUtd, 2.ManCity, 3.Bayern, 4.Dortmund, 5.Braca, 6.Real) >> ");
                int teamId = sc.nextInt();
                sc.nextLine();

                String updateSql = "UPDATE PLAYER SET TEAM_ID=? WHERE PLAYER_NAME=? AND USER_ID=?";
                PreparedStatement pstmt = con.prepareStatement(updateSql);

                pstmt.setInt(1, teamId);
                pstmt.setString(2, playerName);
                pstmt.setInt(3, userId);

                int result = pstmt.executeUpdate();
                if (result > 0) System.out.println(playerName + "의 팀 변경이 성공하였습니다 !");
                else System.out.println("선수가 존재하지 않거나 수정권한이 없습니다 !");
            }
            default -> System.out.println("잘못 입력하셨습니다.");
        }
    }
}
