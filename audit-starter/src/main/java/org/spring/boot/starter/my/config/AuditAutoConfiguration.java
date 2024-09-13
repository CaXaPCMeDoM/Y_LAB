package org.spring.boot.starter.my.config;

import org.spring.boot.starter.my.aspect.AuditableAspect;
import org.spring.boot.starter.my.service.DefaultService;
import org.spring.boot.starter.my.service.LogService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
public class AuditAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(AuditableAspect.class)
    public AuditableAspect auditAspect(LogService logService) {
        return new AuditableAspect(logService);
    }

    @Bean
    @ConditionalOnMissingBean(LogService.class)
    public LogService logService() {
        return new DefaultService();
    }
}
