package pl.dk.ecommerceplatform.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.user.dtos.LoginUserDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

import java.util.Optional;
import java.util.function.Predicate;

@Service
@AllArgsConstructor
class UserCredentialsValidator implements Predicate<LoginUserDto> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean test(LoginUserDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.email());
        if (optionalUser.isPresent()) {
            String encodedPassword = optionalUser.get().getPassword();
            return passwordEncoder.matches(userDto.password(), encodedPassword);
        } else {
            return false;
        }
    }
}
