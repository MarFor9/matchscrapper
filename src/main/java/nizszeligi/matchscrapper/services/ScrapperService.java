package nizszeligi.matchscrapper.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import nizszeligi.matchscrapper.configs.SeleniumConfig;
import nizszeligi.matchscrapper.model.TeamResult;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ScrapperService {
    private static final String TABLE_CSS_CLASS_NAME = "main2";

    private static final String SITE_URL = "http://www.90minut.pl/liga/1/liga12457.html";
    private static final int NUMBER_ROWS_TO_SKIP = 4;
    private static final int SKIP_LAST_ROW = 1;
    private static final String TD = "td";
    private static final String TBODY_TR = "tbody tr";

    private final SeleniumConfig seleniumConfig;

    public ScrapperService(SeleniumConfig seleniumConfig) {
        this.seleniumConfig = seleniumConfig;
    }

    @PostConstruct
    public synchronized List<TeamResult> getTeamResult() {
        ChromeDriver driver = seleniumConfig.getDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.get(SITE_URL);

        WebElement resultsTable = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.className(TABLE_CSS_CLASS_NAME)));

        List<WebElement> rows = resultsTable.findElements(By.cssSelector(TBODY_TR));

        rows = rows.subList(NUMBER_ROWS_TO_SKIP, rows.size() - SKIP_LAST_ROW); //make sub list


        List<TeamResult> results = new ArrayList<>();

        for (var teamRow : rows) {
            TeamResult result = parseTeamRecord(teamRow);
            results.add(result);
        }
        results.forEach(System.out::println);
        return results;
    }

    private static TeamResult parseTeamRecord(WebElement teamRow) {
        List<WebElement> columns = teamRow.findElements(By.cssSelector(TD));

        int placeInTable = Integer.parseInt(columns.get(0).getText().replace(".",""));
        String teamName = columns.get(1).getText();
        int numberOfMatches = Integer.parseInt(columns.get(2).getText());
        int points = Integer.parseInt(columns.get(3).getText());
        int wins = Integer.parseInt(columns.get(4).getText());
        int draw = Integer.parseInt(columns.get(5).getText());
        int lost = Integer.parseInt(columns.get(6).getText());
        String goals = columns.get(7).getText();

        return TeamResult.builder()
                .placeInTable(placeInTable)
                .teamName(teamName)
                .numberOfMatches(numberOfMatches)
                .points(points)
                .wins(wins)
                .draws(draw)
                .lost(lost)
                .goals(goals)
                .build();
    }
}
