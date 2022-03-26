package project.investmentservice.annotation;

import java.lang.annotation.*;

@Deprecated
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface LoginRequired {
    
}
