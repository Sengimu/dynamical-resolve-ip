package net.sengimu.dynamicalresolveip;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "info")
public class InfoConfig {

    private String email;
    private String gKey;
    private String name;
    private String type;
}
