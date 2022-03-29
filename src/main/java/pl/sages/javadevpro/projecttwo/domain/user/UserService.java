package pl.sages.javadevpro.projecttwo.domain.user;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EncodingService encoder;

    public User save(User user) {
        return userRepository.save(
            user.withPassword(
                encoder.encode(user.getPassword())
            )
        );
    }

    public User update(User user) {
        return userRepository.update(user)
            .orElseThrow(UserNotFoundException::new);
    }

    public User removeById(String id) {
        return userRepository.remove(id)
            .orElseThrow(UserNotFoundException::new);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
    }

    public User findById(String id) {
        return userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }

    //todo - pageowanie
    public List<User> findAll() {
        return userRepository.findAll();
    }
}