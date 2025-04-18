package com.shieldteq.user.service;

import com.shieldteq.user.dto.UserDto;
import com.shieldteq.user.repository.UserRepository;
import com.shieldteq.user.util.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public Flux<UserDto> getAllUsers() {
        return userRepository.findAll().map(EntityDtoMapper::toUserDto);
    }

    public Mono<UserDto> getUserById(String id) {
        return userRepository.findById(id).map(EntityDtoMapper::toUserDto);
    }

    public Mono<UserDto> createUser(Mono<UserDto> userDto) {
        return userDto.map(EntityDtoMapper::toEntity)
                .flatMap(userRepository::save)
                .map(EntityDtoMapper::toUserDto);
    }

    public Mono<UserDto> updateUser(String id, Mono<UserDto> userDto) {
        return userRepository.findById(id)
                .flatMap(u -> userDto.map(d -> EntityDtoMapper.copyEntity(u.id(), d)))
                .flatMap(userRepository::save)
                .map(EntityDtoMapper::toUserDto);
    }

    public Mono<Void> deleteUser(String id) {
        return userRepository.deleteById(id).doFinally(x -> System.out.println("Deleted user: " + id));
    }
}
