package y_lab;

import org.springframework.context.annotation.Import;
import y_lab.config.LoggableConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Import(LoggableConfig.class)
public @interface EnableLoggable {
}
