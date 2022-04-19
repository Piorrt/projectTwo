package pl.sages.javadevpro.projecttwo.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.sages.javadevpro.projecttwo.domain.user.User;
import pl.sages.javadevpro.projecttwo.domain.user.UserService;
import pl.sages.javadevpro.projecttwo.security.UserPrincipal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> pageUsers= userService.findAll(pageable);
//        List<UserDto> userList = pageUsers.stream()  // TODO 7. dadac do mappera
//                .map(dtoMapper::toDto)
//                .collect(Collectors.toList());

        List<UserDto> userList = pageUsers.getContent().stream()  // TODO 7. (Piotrek) dadac do mappera
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("users", userList);
        response.put("currentPage", pageUsers.getNumber());
        response.put("totalItems", pageUsers.getTotalElements());
        response.put("totalPages", pageUsers.getTotalPages());

        return ResponseEntity
                .ok(response);
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto dto) {
        User user = userService.save(dtoMapper.toDomain(dto));
        return ResponseEntity
            .ok(dtoMapper.toDto(user));
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody UserDto dto) {
        userService.update(dtoMapper.toDomain(dto));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeUser(@PathVariable String id){
        userService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("me")
    public ResponseEntity<UserDto> aboutMe(Authentication authentication) {
        //todo 6. (Piotrek) klasa ze statyczna metodą getPrincipal np: Security.getPrincipal()
        User user = userService.findByEmail(((UserPrincipal) authentication.getPrincipal()).getUsername());
        return ResponseEntity
            .ok(dtoMapper.toDto(user));
    }
}