package nizszeligi.matchscrapper.model;

import lombok.Data;

import java.util.List;

@Data
public class TeamResults {
    private List<TeamRow> teamRows;
}
