package pl.dk.ecommerceplatform.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.user.User;
import pl.dk.ecommerceplatform.user.UserRepository;

@Service
@AllArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    public Long getIdFromSecurityContextOrThrowException() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
                .findByEmail(email)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);
    }
}
