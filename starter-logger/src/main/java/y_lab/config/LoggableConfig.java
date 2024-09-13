package y_lab.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import y_lab.aspect.LoggableAspect;

@Configuration
@EnableAspectJAutoProxy
public class LoggableConfig {
    @Bean
    @ConditionalOnMissingBean(LoggableAspect.class)
    public LoggableAspect loggableAspect() {
        return new LoggableAspect();
    }
}
