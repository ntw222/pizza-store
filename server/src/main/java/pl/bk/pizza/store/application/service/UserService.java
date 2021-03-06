package pl.bk.pizza.store.application.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.bk.common.dto.user.NewUserDTO;
import pl.bk.common.dto.user.UserDTO;
import pl.bk.pizza.store.application.mapper.customer.NewUserMapper;
import pl.bk.pizza.store.application.mapper.customer.UserMapper;
import pl.bk.pizza.store.domain.customer.user.User;
import pl.bk.pizza.store.domain.customer.user.UserRepository;
import pl.bk.pizza.store.infrastructure.security.jwt.JwtAuthenticationRequest;
import pl.bk.pizza.store.infrastructure.security.jwt.JwtAuthenticationResponse;
import pl.bk.pizza.store.infrastructure.security.jwt.JwtTokenUtil;
import reactor.core.publisher.Mono;

import static pl.bk.pizza.store.domain.validator.customer.UserValidator.userShouldExists;
import static pl.bk.pizza.store.domain.validator.customer.UserValidator.userShouldNotExists;
import static reactor.core.publisher.Mono.just;

@Service
@AllArgsConstructor
public class UserService
{
    private final UserRepository userRepository;
    private final NewUserMapper newUserMapper;
    private final UserMapper userMapper;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder encoder;
    
    public Mono<UserDTO> createUser(NewUserDTO userDTO)
    {
        return just(userDTO)
            .flatMap(it -> userRepository.findById(it.getEmail()))
            .flatMap(it -> userShouldNotExists(userDTO.getEmail()))
            .then(just(userDTO))
            .map(newUserMapper::mapFromDTO)
            .flatMap(userRepository::save)
            .map(userMapper::mapToDTO);
    }
    
    public Mono<Integer> getBonusPoints(String email)
    {
        return userRepository
            .findById(email)
            .switchIfEmpty(userShouldExists(email))
            .map(User::getPoints);
    }
    
    public Mono<UserDTO> getUser(String email)
    {
        return userRepository
            .findById(email)
            .switchIfEmpty(userShouldExists(email))
            .map(userMapper::mapToDTO);
    }
    
    public Mono<UserDTO> deactivateUser(String email)
    {
        return userRepository
            .findById(email)
            .switchIfEmpty(userShouldExists(email))
            .map(User::deactivateUser)
            .flatMap(userRepository::save)
            .map(userMapper::mapToDTO);
    }
    
    Mono<User> addPoints(String email, Integer points)
    {
        return userRepository
            .findById(email)
            .switchIfEmpty(userShouldExists(email))
            .map(user -> user.addPoints(points))
            .flatMap(userRepository::save);
    }
    
    public Mono<JwtAuthenticationResponse> generateToken(JwtAuthenticationRequest request)
    {
        return userRepository.findById(request.getUsername())
                             .switchIfEmpty(userShouldExists(request.getUsername()))
                             .filter(user -> encoder.matches(request.getPassword(), user.getPassword()))
                             .map(user -> new JwtAuthenticationResponse(request.getUsername(), jwtTokenUtil.generateToken(user)));
    }
}
