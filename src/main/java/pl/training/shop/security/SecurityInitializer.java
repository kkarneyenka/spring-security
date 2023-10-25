package pl.training.shop.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.training.shop.security.users.JpaUserRepository;
import pl.training.shop.security.users.UserEntity;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityInitializer implements ApplicationRunner {

    public static final String USER_EMAIL = "admin@training.pl";
    public static final String USER_NAME = "admin";
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${mainPassword}")
    private String mainPassword;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByEmail(USER_EMAIL).isEmpty()) {
            var user = new UserEntity();
            user.setId(nextId());
            user.setEmail(USER_EMAIL);
            user.setName(USER_NAME);
            user.setPassword(passwordEncoder.encode(mainPassword));
            user.setRole(ADMIN_ROLE);
            user.setActive(true);
            userRepository.save(user);
        }
    }
    private String nextId() {
        return UUID.randomUUID().toString();
    }

}
