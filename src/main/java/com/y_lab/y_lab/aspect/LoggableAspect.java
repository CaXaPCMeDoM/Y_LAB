package com.y_lab.y_lab.aspect;

import com.y_lab.y_lab.annotation.Loggable;
import com.y_lab.y_lab.config.ServiceContainer;
import com.y_lab.y_lab.entity.enums.ActionType;
import com.y_lab.y_lab.security.UserContext;
import com.y_lab.y_lab.service.logger.AuditService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


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
        Object result = joinPoint.proceed();

        Loggable loggable = getLoggableAnnotation(joinPoint);
        if (loggable == null) {
            throw new IllegalStateException("Missing @Loggable annotation");
        } else {
            ActionType actionType = loggable.action_type();
            auditService.log(UserContext.getCurrentUser().getUserId(), actionType);
        }

        long endTimer = System.currentTimeMillis() - startTimer;
        System.out.println("Execution time: " + endTimer + "ms");

        return result;
    }

    private Loggable getLoggableAnnotation(ProceedingJoinPoint joinPoint) {
        try {
            Class<?> targetClass = joinPoint.getTarget().getClass();
            String methodName = joinPoint.getSignature().getName();
            Class<?>[] parameterTypes = ((org.aspectj.lang.reflect.MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
            return targetClass.getMethod(methodName, parameterTypes).getAnnotation(Loggable.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
