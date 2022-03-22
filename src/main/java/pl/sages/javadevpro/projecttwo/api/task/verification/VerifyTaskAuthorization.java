package pl.sages.javadevpro.projecttwo.api.task.verification;

import pl.sages.javadevpro.projecttwo.security.UserPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.security.Principal;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyTaskAuthorization {

    String taskIdParamName();
    String authenticationParamName();
}
