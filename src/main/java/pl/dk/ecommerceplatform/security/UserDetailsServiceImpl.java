package pl.dk.ecommerceplatform.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.security.dtos.UserCredentialsDto;

@Service
@AllArgsConstructor
class UserDetailsServiceImpl implements UserDetailsService {

    private final UserCredentialService userCredentialService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCredentialService.findCredentialsByEmail(username)
                .map(this::createUserDetails).orElseThrow(UserNotFoundException::new);
    }

    private UserDetails createUserDetails(UserCredentialsDto userCredentialsDto) {
        return User.builder()
                .username(userCredentialsDto.email())
                .password(userCredentialsDto.password())
                .roles(userCredentialsDto.role())
                .disabled(!userCredentialsDto.enabled())
                .build();
    }
}
