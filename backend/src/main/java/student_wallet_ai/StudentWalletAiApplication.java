package student_wallet_ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StudentWalletAiApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentWalletAiApplication.class, args);
    }
}