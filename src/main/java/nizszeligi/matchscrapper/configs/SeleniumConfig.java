package nizszeligi.matchscrapper.configs;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
@Slf4j
public class SeleniumConfig {
    private static final String CHROME_DRIVER_EXE = "chromedriver.exe";
    private static final String WEBDRIVER_CHROME_DRIVER_PROPERTY = "webdriver.chrome.driver";

    private static final String[] PATHS = {"", "bin/", "target/classes"};

    private final ChromeDriver driver;

    public SeleniumConfig() {
        log.info("Trying to find local chrome webdriver");
        findDriverInLocalWorkdir();

        ChromeOptions browserOptions = new ChromeOptions();
        browserOptions.addArguments("--headless");
        browserOptions.addArguments("--disable-gpu");
        browserOptions.addArguments("disable-infobars");
        browserOptions.addArguments("--disable-extensions");
        browserOptions.addArguments("window-size=1200x600");
        browserOptions.addArguments("start-maximized"); // open Browser in maximized mode
        browserOptions.addArguments("--no-sandbox");
        browserOptions.addArguments("--remote-allow-origins=*");

        browserOptions.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
        driver = new ChromeDriver(browserOptions);
    }

    private void findDriverInLocalWorkdir() {
        System.setProperty(WEBDRIVER_CHROME_DRIVER_PROPERTY, findFile());
    }

    public ChromeDriver getDriver() {
        return driver;
    }

    private static String findFile() {
        for (String path : PATHS) {
            if (new File(path + SeleniumConfig.CHROME_DRIVER_EXE).exists())
                return path + SeleniumConfig.CHROME_DRIVER_EXE;
        }
        return "";
    }
}
