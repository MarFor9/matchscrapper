package nizszeligi.matchscrapper.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeamResult {
    private int placeInTable;
    private String teamName;
    private int numberOfMatches;
    private int points;
    private int wins;
    private int draws;
    private int lost;
    private String goals;
}
