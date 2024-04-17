package dto;

public class TeamSearchDto {
    private int teamId;
    private String teamName;

    public TeamSearchDto(int teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    public String toString() {
        return "팀번호: " + teamId + ", 팀명: " + teamName;
    }
}
