package dto;

public class PlayerListDto {
    private String playerName;
    private int age;
    private int height;
    private String teamName;

    public PlayerListDto(String playerName, int age, int height, String teamName) {
        this.playerName = playerName;
        this.age = age;
        this.height = height;
        this.teamName = teamName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getAge() {
        return age;
    }

    public int getHeight() {
        return height;
    }

    public String getTeamName() {
        return teamName;
    }

    @Override
    public String toString() {
        return "선수명: " + playerName + ", 나이: "+age+", 키: "+height+", 소속팀: "+teamName;
    }
}
