package pl.sages.javadevpro.projecttwo.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.sages.javadevpro.projecttwo.api.user.UserDto;
import pl.sages.javadevpro.projecttwo.api.user.UserDtoMapper;
import pl.sages.javadevpro.projecttwo.domain.UserService;
import pl.sages.javadevpro.projecttwo.domain.user.User;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserEndpoint {

    private final UserService userService;
    private final UserDtoMapper dtoMapper;

    @GetMapping(
        path = "/{id}",
        produces = "application/json",
        consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDto> getUser(@PathVariable(name = "id") String id) {
        User user = userService.getUserById(id);

        return ResponseEntity
            .ok(dtoMapper.toDto(user));
    }

    @GetMapping(
        produces = "application/json",
        consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> userList = userService.getAllUsers().stream()
            .map(dtoMapper::toDto)
            .collect(Collectors.toList());

        return ResponseEntity
            .ok(userList);
    }

    @PostMapping(
        produces = "application/json",
        consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto dto) {
        User user = userService.saveUser(dtoMapper.toDomain(dto));
        return ResponseEntity
            .ok(dtoMapper.toDto(user));
    }

    @PutMapping(
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto dto) {
        User user = userService.updateUser(dtoMapper.toDomain(dto));
        return ResponseEntity
                .ok(dtoMapper.toDto(user));
    }

    @DeleteMapping(
            produces = "application/json",
            consumes = "application/json"
    )
    @Secured("ROLE_ADMIN")
    public ResponseEntity removeUser(@RequestBody UserDto dto){
        userService.removeUser(dtoMapper.toDomain(dto));
        return ResponseEntity.ok(dto);
    }

    @GetMapping(
        path = "/me",
        produces = "application/json",
        consumes = "application/json"
    )
    @Secured("ROLE_STUDENT")
    public ResponseEntity<UserDto> aboutMe(Authentication authentication) {
        User user = userService.getUserByEmail((String) authentication.getPrincipal());
        return ResponseEntity
            .ok(dtoMapper.toDto(user));
    }
}
