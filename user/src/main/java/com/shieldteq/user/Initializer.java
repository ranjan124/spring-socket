package com.shieldteq.user;

import com.shieldteq.user.entity.User;
import com.shieldteq.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {
    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        User user = new User("test", "test", 100);
        User user2 = new User("test2", "test2", 100);
        User user3 = new User("test3", "test3", 100);
        Flux.just(user, user2, user3).flatMap(userRepository::save).subscribe();
    }
}
