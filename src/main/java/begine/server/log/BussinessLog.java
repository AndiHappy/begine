package begine.server.log;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BussinessLog {
    /**
     * 业务的名称
     */
    String value() default "";

    /**
     * 是否将当前日志记录到数据库中
     */
    boolean save() default true;

}
