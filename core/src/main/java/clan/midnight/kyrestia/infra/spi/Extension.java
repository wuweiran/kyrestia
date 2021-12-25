package clan.midnight.kyrestia.infra.spi;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Extension {
    String group();

    int priority() default 0;
}
