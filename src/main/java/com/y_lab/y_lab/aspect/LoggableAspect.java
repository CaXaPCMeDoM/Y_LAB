package com.y_lab.y_lab.aspect;

import com.y_lab.y_lab.config.ServiceContainer;
import com.y_lab.y_lab.entity.enums.ActionType;
import com.y_lab.y_lab.security.UserContext;
import com.y_lab.y_lab.service.logger.AuditService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;


@Aspect
public class LoggableAspect {
    private final AuditService auditService;

    public LoggableAspect() {
        this.auditService = ServiceContainer.getAuditService();
    }

    @Pointcut("@annotation(com.y_lab.y_lab.annotation.Loggable)")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTimer = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.y_lab.y_lab.annotation.Loggable loggable = method.getAnnotation(com.y_lab.y_lab.annotation.Loggable.class);
        ActionType actionType = loggable.action_type();

        Object result = joinPoint.proceed();

        auditService.log(UserContext.getCurrentUser().getUserId(), actionType);

        long endTimer = System.currentTimeMillis() - startTimer;
        System.out.println("Execution time: " + endTimer + "ms"); // с каждым дз всё больше хочется использовать NoSql для логов

        return result;
    }
}
