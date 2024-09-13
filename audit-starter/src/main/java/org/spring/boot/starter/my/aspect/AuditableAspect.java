package org.spring.boot.starter.my.aspect;

import org.spring.boot.starter.my.annotation.Auditable;
import org.spring.boot.starter.my.service.LogService;
import context.UserContext;
import entity.ActionType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * This aspect logs audit entries to the `audit_log` table.
 * It assumes that the current context
 * in {@link UserContext} contains an <B>authorized user.</B>
 */
@Aspect
public class AuditableAspect {
    private final LogService auditService;

    public AuditableAspect(LogService auditService) {
        this.auditService = auditService;
    }

    @Pointcut("@annotation(org.spring.boot.starter.my.annotation.Auditable)")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Auditable loggable = method.getAnnotation(Auditable.class);

        Object result = joinPoint.proceed();

        long userId = UserContext.getCurrentUser().getUserId();
        ActionType actionType = loggable.action_type();

        auditService.log(userId, actionType);

        return result;
    }
}
