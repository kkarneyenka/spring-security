package pl.training.shop.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Order(LOWEST_PRECEDENCE)
public class SecretsPostProcessor implements EnvironmentPostProcessor {

    private final String PROPERTY_SOURCE_NAME = "secrets";
    private final String SECRETS_FILE = "secrets.properties";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        var properties = new Properties();
        try {
            String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            properties.load(new FileInputStream(rootPath + SECRETS_FILE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var propertySource = new PropertiesPropertySource(PROPERTY_SOURCE_NAME, properties);
        environment.getPropertySources().addFirst(propertySource);
    }

}
