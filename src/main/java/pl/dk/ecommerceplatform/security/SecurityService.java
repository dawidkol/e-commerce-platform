package pl.dk.ecommerceplatform.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.user.User;
import pl.dk.ecommerceplatform.user.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    public Long getIdFromSecurityContextOrThrowException() {
        String email = this.getEmailFromSecurityContext();
        return userRepository
                .findByEmail(email)
                .map(User::getId)
                .orElseThrow(UserNotFoundException::new);
    }

    public String getEmailFromSecurityContext() {
        return this.getAuthentication().getName();
    }

    public List<String> getCredentials() {
       return this.getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
