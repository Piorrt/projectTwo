package pl.sages.javadevpro.projecttwo.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserService;
import pl.sages.javadevpro.projecttwo.security.UserPrincipal;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users",
        produces = "application/json",
        consumes = "application/json"
)
public class UserController {

    private final UserService userService;
    private final UserDtoMapper dtoMapper;

    @GetMapping( path = "/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        User user = userService.findById(id);
        return ResponseEntity
                .ok(dtoMapper.toDto(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> userList = userService.findAll().stream()  // TODO 7. dadac do mappera
            .map(dtoMapper::toDto)
            .collect(Collectors.toList());

        return ResponseEntity
            .ok(userList);
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto dto) {
        User user = userService.save(dtoMapper.toDomain(dto));
        return ResponseEntity
            .ok(dtoMapper.toDto(user));
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto dto) {
        User user = userService.update(dtoMapper.toDomain(dto));
        return ResponseEntity
                .ok(dtoMapper.toDto(user));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeUser(@PathVariable String id){
        userService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("me")
    public ResponseEntity<UserDto> aboutMe(Authentication authentication) {
        //todo 6. klasa ze statyczna metodą getPrincipal np: Security.getPrincipal()
        User user = userService.findByEmail(((UserPrincipal) authentication.getPrincipal()).getUsername());
        return ResponseEntity
            .ok(dtoMapper.toDto(user));
    }
}