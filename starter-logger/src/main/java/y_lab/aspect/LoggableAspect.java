package y_lab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggableAspect {
    @Pointcut("@annotation(y_lab.annotaion.Loggable)")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTimer = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long currentTimer = System.currentTimeMillis() - startTimer;
        System.out.println("Execution time: " + currentTimer + "ms");

        return result;
    }
}
