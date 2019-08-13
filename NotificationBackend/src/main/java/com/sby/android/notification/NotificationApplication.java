package com.sby.android.notification;

import com.sby.android.notification.entities.User;
import com.sby.android.notification.repositories.UserRepository;
import com.sby.android.notification.services.NotificationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Locale;
import java.util.UUID;


@SpringBootApplication
@EnableScheduling
@EnableRetry
@EnableSwagger2
public class NotificationApplication {
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        SpringApplication.run(NotificationApplication.class, args);
    }

    @Bean
    public Docket notificationApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sby.android.notification.controllers"))
                .paths(PathSelectors.any())
                .build();
    }
}


@Component
class DataBootstrap implements CommandLineRunner {

    private UserRepository userRepository;
    private NotificationService notificationService;

    public DataBootstrap(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    public void run(String... args) throws Exception {
        var user = new User(UUID.fromString("3fb9c8d8-c703-4bd5-97bd-a0a117414f7b"),
                "cC6htAMkqAc:APA91bFcTW_fN1d8WATnrAGMyjBcEyO8owMsd802JDRV0WBq0pJmFsTVAysr7A4nk4lSdwir2qcaCXtpoJjH1cQkgPS4FxniK47GOWGyqY3MiTnHCqB4S1ws1Ve5u2312iL6fkbvWVhO",
                null);

        if(!userRepository.existsById(user.getId())) {
            userRepository.save(user);
        }


        // Init
        notificationService.InitFirebaseApp();
    }

}