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

    public void update(User user) {
        userRepository.update(user);
    }

    public void removeById(String id) {
        userRepository.remove(id);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
    }

    public User findById(String id) {
        return userRepository.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }

    //todo 12. (Rafal) pageowanie
    public List<User> findAll() {
        return userRepository.findAll();
    }
}