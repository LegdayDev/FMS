package dto;

public class TeamListDto {
    private String teamName;
    private String stadiumName;
    private String loc;

    public TeamListDto(String teamName, String stadiumName, String loc) {
        this.teamName = teamName;
        this.stadiumName = stadiumName;
        this.loc = loc;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public String getLoc() {
        return loc;
    }

    @Override
    public String toString() {
        return "팀명: " + teamName + ", 구장명: " + stadiumName + ", 연고지: " + loc;
    }
}
