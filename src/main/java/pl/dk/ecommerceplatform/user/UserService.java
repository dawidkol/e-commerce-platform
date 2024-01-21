package pl.dk.ecommerceplatform.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.error.exceptions.user.UserExistsException;
import pl.dk.ecommerceplatform.user.dtos.RegisterUserDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

import java.util.Optional;

import static pl.dk.ecommerceplatform.constant.UserRoleConstant.*;

@Service
@AllArgsConstructor
class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserDtoMapper userDtoMapper;

    @Transactional
    public UserDto register(RegisterUserDto registerUserDto) {
        Optional<User> user = userRepository.findByEmail(registerUserDto.email());
        if (user.isPresent()) {
            throw new UserExistsException();
        }
        User savedUser = saveUser(registerUserDto);
        return userDtoMapper.map(savedUser);
    }

    private User saveUser(RegisterUserDto registerUserDto) {
        User userToSave = userDtoMapper.map(registerUserDto);
        userRoleRepository.findByName(USER_ROLE).ifPresentOrElse(
                userToSave::setUserRole,
                () -> {
                    UserRole userRole = userRoleRepository.save(UserRole.builder()
                            .name(USER_ROLE)
                            .description("Basic authorities")
                            .build());
                    userToSave.setUserRole(userRole);
                }
        );
        return userRepository.save(userToSave);
    }

}
