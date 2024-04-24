package fi.vm.sade.kayttooikeus.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "datasource")
public class DatasourceProperties {
    private String url;
    private String testUrl;
    private String user;
    private String password;
    private Integer maxActive;
    private Integer maxWait;
    private Integer maxLifetimeMillis;
}
