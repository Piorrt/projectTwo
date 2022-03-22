package pl.sages.javadevpro.projecttwo.api.task.verification;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.sages.javadevpro.projecttwo.domain.assigment.AssigmentService;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserService;
import pl.sages.javadevpro.projecttwo.security.UserPrincipal;

import java.util.Arrays;

@Component
@Aspect
@RequiredArgsConstructor
class TaskAuthorizationVerifier {

    private final UserService userService;
    private final AssigmentService assigmentService;


    @Before(value = "@annotation(verifyTaskAuthorization)")
    public void userIsOwnerOfTask(JoinPoint joinPoint, VerifyTaskAuthorization verifyTaskAuthorization) {
        Authentication authentication = (Authentication) getParameterByName(joinPoint, verifyTaskAuthorization.authenticationParamName());
        String taskId = (String) getParameterByName(joinPoint, verifyTaskAuthorization.taskIdParamName());

        User user = userService.findByEmail(((UserPrincipal) authentication.getPrincipal()).getUsername());
        boolean authorizationConfirmed = assigmentService.isTaskAssignedToUser(user.getId(), taskId);
        if (!authorizationConfirmed) {
            throw new UserIsNotAuthorizedToThisTaskException("User is not authorized to this task.");
        }
    }

    private Object getParameterByName(JoinPoint joinPoint, String parameterName) {
        MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        if (args.length==0){
            throw new UserIsNotAuthorizedToThisTaskException("Parameter " + parameterName + " not provided.");
        }
        String[] parametersName = methodSig.getParameterNames();
        int index = Arrays.asList(parametersName).indexOf(parameterName);
        return args[index];
    }

}
