package nizszeligi.matchscrapper.controller;

import lombok.AllArgsConstructor;
import nizszeligi.matchscrapper.model.TeamResults;
import nizszeligi.matchscrapper.services.ScrapperService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/lower/leagues")
@AllArgsConstructor
public class TeamResultController {

    private ScrapperService scrapperService;

    @GetMapping
    public TeamResults getTeamResults() {
        return scrapperService.getTeamResults();
    }
}
