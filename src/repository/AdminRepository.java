package repository;

import oracle.jdbc.proxy.annotation.Pre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminRepository {
    /* 리그 추가 메서드(리그명, 연고지, 생성시간)
    1. 리그명 중복 체크
    2. 중복이 아니라면 저장
     */
    public static void insertLeague(Connection con, Scanner sc) throws SQLException {
        System.out.println("\n==== 리그 추가 메뉴입니다. ====");
        System.out.print("새로 추가하는 리그명을 입력해주세요 >> ");
        String leagueName = sc.nextLine();
        System.out.print("국가를 입력해주세요 >> ");
        String country = sc.nextLine();

        // 리그명 중복 체크
        String checkSql = "SELECT * FROM LEAGUE WHERE LEAGUE_NAME = ?";
        PreparedStatement pstmt = con.prepareStatement(checkSql);
        pstmt.setString(1, leagueName);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String checkColumn = rs.getString("LEAGUE_NAME");
            if (!checkColumn.isEmpty()) {
                System.out.println("리그명이 중복되었습니다! 다른 리그명을 사용하십시오.");
                return;
            }
        }

        String insertSql = "INSERT INTO LEAGUE(LEAGUE_ID, LEAGUE_NAME, COUNTRY, CREATED_AT) VALUES(LEAGUE_SEQ.nextval,?,?,TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS'))";
        pstmt = con.prepareStatement(insertSql);
        pstmt.setString(1, leagueName);
        pstmt.setString(2, country);
        int result = pstmt.executeUpdate();
        if (result > 0) System.out.println("리그가 정상적으로 추가되었습니다 !");
        else System.out.println("리그등록에 실패하셨습니다 !");

        rs.close();
        pstmt.close();
    }

    /* 리그명 수정 메서드
    1. 리그는 리그명만 수정할 수 있음.(제약)
    2. 수정 시 MODIFIED_AT 컬럼에 변경하는 시간을 넣어준다.
     */
    public static void updateLeague(Connection con, Scanner sc) throws SQLException {
        System.out.println("\n==== 리그명 수정 메뉴입니다. ====");
        System.out.print("변경하실 리그명을 입력해주세요 >> ");
        String originalLeague = sc.nextLine();
        System.out.print("새로운 리그명을 입력해주세요 >> ");
        String newLeague = sc.nextLine();

        String updateSql = "UPDATE LEAGUE SET LEAGUE_NAME = ? WHERE LEAGUE_NAME = ?";
        PreparedStatement pstmt = con.prepareStatement(updateSql);
        pstmt.setString(1, newLeague);
        pstmt.setString(2, originalLeague);
        int result = pstmt.executeUpdate();

        if (result > 0) System.out.println("리그명 변경이 완료되었습니다 !");
        else System.out.println("리그명 변경에 실패하셨습니다 !");
    }

    /* 리그 삭제 메서드
    1. 리그명이 존재하는지 체크
    2. 리그명으로 삭제
     */
    public static void deleteLeague(Connection con, Scanner sc) throws SQLException {
        System.out.println("\n==== 리그 삭제 메뉴입니다. ====");
        System.out.print("삭제하실 리그명을 입력해주세요 >> ");
        String deleteLeagueName = sc.nextLine();

        // 리그명이 존재하는지 체크
        String checkSql = "SELECT LEAGUE_NAME FROM LEAGUE WHERE LEAGUE_NAME = ?";
        PreparedStatement pstmt = con.prepareStatement(checkSql);
        pstmt.setString(1, deleteLeagueName);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            String checkColumn = rs.getString("LEAGUE_NAME");
            if (checkColumn.isEmpty()) {
                System.out.println("삭제하실 리그명이 존재하지 않습니다 !");
                return;
            }
        }

        String deleteSql = "DELETE FROM LEAGUE WHERE LEAGUE_NAME = ?";
        pstmt = con.prepareStatement(deleteSql);
        pstmt.setString(1, deleteLeagueName);
        int result = pstmt.executeUpdate();
        if (result > 0) System.out.println("리그삭제가 성공하였습니다 !");
        else System.out.println("리그삭제가 실패하였습니다 !");

        rs.close();
        pstmt.close();
    }

    /* 팀 추가 메서드
    1. 팀명, 구장명, 연고지, 소속리그, 생성시간 컬럼 추가
    2. 팀명 안겹치게 체크
    3. 이상없으면 추가
     */
    public static void insertTeam(Connection con, Scanner sc) throws SQLException {
        System.out.println("\n==== 팀 추가 메뉴입니다. ====");
        System.out.print("추가할 팀 명을 입력하세요 >> ");
        String teamName = sc.nextLine();

        // 팀명이 안겹치는지 체크
        String checkSql = "SELECT * FROM TEAM WHERE TEAM_NAME = ?";
        PreparedStatement pstmt = con.prepareStatement(checkSql);
        pstmt.setString(1, teamName);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            String checkColumn = rs.getString("TEAM_NAME");
            if (!checkColumn.isEmpty()) {
                System.out.println("팀명이 중복됩니다 !!");
                return;
            }
        }

        System.out.print("구장명을 입력하세요 >> ");
        String stadiumName = sc.nextLine();
        System.out.print("연고지를 입력하세요 >> ");
        String loc = sc.nextLine();
        System.out.print("소속 리그를 입력하세요(1.Premier League 2.Bundesliga 3.Laliga) >> ");
        int league_id = sc.nextInt();
        sc.nextLine();

        String insertSql = "INSERT INTO TEAM(TEAM_ID, TEAM_NAME, STADIUM_NAME, LOC, LEAGUE_ID, CREATED_AT) " +
                "VALUES(TEAM_SEQ.nextval, ?, ?, ?, ?, TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS'))";
        pstmt = con.prepareStatement(insertSql);
        pstmt.setString(1, teamName);
        pstmt.setString(2, stadiumName);
        pstmt.setString(3, loc);
        pstmt.setInt(4, league_id);

        int result = pstmt.executeUpdate();
        if (result > 0) System.out.println("팀 추가에 성공하셨습니다 !");
        else System.out.println("팀 추가가 실패하셨습니다 !");

        rs.close();
        pstmt.close();
    }

    /* 팀 수정 메서드
    1. 팀명 또는 구장명만 수정할 수 있다.
    2. 팀명 안겹치게 체크
    3. 수정 시 MODIFIED_AT 컬럼에 수정시간 추가 !
     */
    public static void updateTeam(Connection con, Scanner sc) throws SQLException {
        System.out.println("\n==== 팀 수정 메뉴입니다. ====");
        System.out.print("수정하실 정보를 선택해주세요(1.팀명 2.구장명) >> ");
        int select = sc.nextInt();
        sc.nextLine();

        switch (select) {
            case 1 -> { // 팀명수정
                System.out.print("수정하실 팀명을 입력해주세요 >> ");
                String teamName = sc.nextLine();

                System.out.print("새로운 팀명을 입력해주세요 >> ");
                String newTeamName = sc.nextLine();

                String checkSql = "SELECT * FROM TEAM WHERE TEAM_NAME = ?";
                PreparedStatement pstmt = con.prepareStatement(checkSql);
                pstmt.setString(1, newTeamName);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    String checkColumn = rs.getString("TEAM_NAME");
                    if (!checkColumn.isEmpty()) {
                        System.out.println("이미 존재하는 팀명입니다 !");
                        return;
                    }
                }

                String updateSql = "UPDATE TEAM SET TEAM_NAME = ?, MODIFIED_AT = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS') WHERE TEAM_NAME = ?";
                pstmt = con.prepareStatement(updateSql);
                pstmt.setString(1, newTeamName);
                pstmt.setString(2, teamName);
                int result = pstmt.executeUpdate();

                if (result > 0) System.out.println("팀명 변경을 성공하셨습니다 !");
                else System.out.println("팀명 변경을 실패하셨습니다 !");

                rs.close();
                pstmt.close();
            }
            case 2 -> { // 구장명 수정
                System.out.print("수정하실 팀을 입력해주세요 >> ");
                String teamName = sc.nextLine();

                System.out.print("새로운 구장명읍 입력해주세요 >> ");
                String newStadiumName = sc.nextLine();

                String updateSql = "UPDATE TEAM SET STADIUM_NAME = ?, MODIFIED_AT = TO_CHAR(SYSDATE, 'YYYY-MM-DD HH:MI:SS') WHERE TEAM_NAME = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSql);
                pstmt.setString(1, newStadiumName);
                pstmt.setString(2, teamName);
                int result = pstmt.executeUpdate();
                if (result > 0) System.out.println("구장명 변경을 성공하셨습니다 !");
                else System.out.println("수정할 팀이 존재하지 않거나 구장명 변경에 실패하셨습니다 !");

                pstmt.close();
            }
        }
    }

    /* 팀 삭제 메서드
    1. 팀명을 입력받아서 삭제 !
    2. 팀명이 존재하는지 체크
     */
    public static void deleteTeam(Connection con, Scanner sc) throws SQLException {
        System.out.println("\n==== 팀 삭제 메뉴입니다. ====");
        System.out.print("삭제할 팀명을 입력해주세요 >> ");
        String teamName = sc.nextLine();

        String deleteSql = "DELETE FROM TEAM WHERE TEAM_NAME = ?";
        PreparedStatement pstmt = con.prepareStatement(deleteSql);
        pstmt.setString(1, teamName);
        int result = pstmt.executeUpdate();
        if (result > 0) System.out.println("팀 삭제가 성공하였습니다 !");
        else System.out.println("삭제할 팀이 존재하지 않거나 팀삭제가 실패하였습니다 !");
    }
}
