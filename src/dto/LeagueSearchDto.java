package dto;

public class LeagueSearchDto {
    private int league_id;
    private String league_name;

    public LeagueSearchDto(int league_id, String league_name) {
        this.league_id = league_id;
        this.league_name = league_name;
    }

    public int getLeague_id() {
        return league_id;
    }

    public String getLeague_name() {
        return league_name;
    }

    @Override
    public String toString() {
        return "리그번호: " + league_id + ", 리그명: " + league_name;
    }
}
