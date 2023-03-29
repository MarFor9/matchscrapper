package nizszeligi.matchscrapper.services;

import lombok.extern.slf4j.Slf4j;
import nizszeligi.matchscrapper.configs.SeleniumConfig;
import nizszeligi.matchscrapper.model.TeamResults;
import nizszeligi.matchscrapper.model.TeamRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ScrapperServiceImpl implements ScrapperService {

    @Value("${site.url}")
    private String siteUrl;

    private static final String TABLE_CSS_CLASS_NAME = "main2";
    private static final int NUMBER_ROWS_TO_SKIP = 4;
    private static final int SKIP_LAST_ROW = 1;
    private static final String TD = "td";
    private static final String TBODY_TR = "tbody tr";

    private final SeleniumConfig seleniumConfig;

    TeamResults teamResults = new TeamResults();

    public ScrapperServiceImpl(SeleniumConfig seleniumConfig) {
        this.seleniumConfig = seleniumConfig;
    }

    @Override
    public TeamResults getTeamResults() {
        return teamResults.getTeamRows() == null ||
                teamResults.getTeamRows().isEmpty() ? downloadTeamResult() : teamResults;
    }

    @Scheduled(cron = "0 0 6 * * *")
    private synchronized TeamResults downloadTeamResult() {
        log.info("Start getTeamResult");

        ChromeDriver driver = seleniumConfig.getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get(siteUrl);

        WebElement resultsTable = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.className(TABLE_CSS_CLASS_NAME)));

        List<WebElement> rows = resultsTable.findElements(By.cssSelector(TBODY_TR));

        rows = rows.subList(NUMBER_ROWS_TO_SKIP, rows.size() - SKIP_LAST_ROW); //make sub list

        List<TeamRow> results = new ArrayList<>();

        for (var teamRow : rows) {
            TeamRow result = parseTeamRecord(teamRow);
            results.add(result);
        }

        log.info(results.toString());
        log.info("Finish getTeamResult");
        teamResults.setTeamRows(results);

        return teamResults;
    }

    private static TeamRow parseTeamRecord(WebElement teamRow) {
        List<WebElement> columns = teamRow.findElements(By.cssSelector(TD));

        int placeInTable = Integer.parseInt(columns.get(0).getText().replace(".", ""));
        String teamName = columns.get(1).getText();
        int numberOfMatches = Integer.parseInt(columns.get(2).getText());
        int points = Integer.parseInt(columns.get(3).getText());
        int wins = Integer.parseInt(columns.get(4).getText());
        int draw = Integer.parseInt(columns.get(5).getText());
        int lost = Integer.parseInt(columns.get(6).getText());

        String goals = columns.get(7).getText();

        String[] scoredLostGoals = goals.split("-");
        int scoredGoals = Integer.parseInt(scoredLostGoals[0]);
        int lostGoals = Integer.parseInt(scoredLostGoals[1]);

        return TeamRow.builder()
                .placeInTable(placeInTable)
                .teamName(teamName)
                .numberOfMatches(numberOfMatches)
                .points(points)
                .wins(wins)
                .draws(draw)
                .lost(lost)
                .scoredGoals(scoredGoals)
                .lostGoals(lostGoals)
                .build();
    }
}
