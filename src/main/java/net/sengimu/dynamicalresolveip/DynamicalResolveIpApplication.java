package net.sengimu.dynamicalresolveip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(InfoConfig.class)
@EnableScheduling
public class DynamicalResolveIpApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicalResolveIpApplication.class, args);
    }

}
