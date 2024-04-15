package dto;

public class LeagueListDto {
    private String leagueName;
    private String country;

    public LeagueListDto(String leagueName, String country) {
        this.leagueName = leagueName;
        this.country = country;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "리그명: " + leagueName + ", 소속국가: " + country;
    }
}
