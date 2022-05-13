package pl.sages.javadevpro.projecttwo.security;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserService;

@Component
// TODO final + required args constructor - done
@RequiredArgsConstructor
public class Security {

    private final UserService userService;

    public User getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.findByEmail(((UserPrincipal) authentication.getPrincipal()).getUsername());
    }
}
