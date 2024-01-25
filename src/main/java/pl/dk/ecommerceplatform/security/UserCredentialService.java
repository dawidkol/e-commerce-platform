package pl.dk.ecommerceplatform.security;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.user.UserRepository;
import pl.dk.ecommerceplatform.user.dtos.UserCredentialsDto;

import java.util.Optional;

@Service
@AllArgsConstructor
class UserCredentialService {

    private final UserRepository userRepository;

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::map);
    }
}
