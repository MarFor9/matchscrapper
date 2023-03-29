package nizszeligi.matchscrapper;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MatchScrapperApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchScrapperApplication.class, args);
    }

}
