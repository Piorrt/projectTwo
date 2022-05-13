package pl.sages.javadevpro.projecttwo.api.task.verification;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
// TODO zastanowić się nad nazwą - done
public @interface AuthVerifyTask {

    String taskIdParamName() default "taskId";

}
