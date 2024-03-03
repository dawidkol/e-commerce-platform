package pl.dk.ecommerceplatform.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.error.exceptions.user.RoleNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserExistsException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.user.dtos.RegisterUserDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.util.Optional;

import static pl.dk.ecommerceplatform.user.Role.*;


@Service
@AllArgsConstructor
class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserDtoMapper userDtoMapper;
    private final UtilsService utils;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto register(RegisterUserDto registerUserDto, String role) {
        Optional<User> user = userRepository.findByEmail(registerUserDto.email());
        if (user.isPresent()) {
            throw new UserExistsException();
        }
        User savedUser = this.saveUser(registerUserDto, role);
        return userDtoMapper.map(savedUser);
    }

    @Transactional
    public void updateUser(Long id, JsonMergePatch patch) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        try {
            RegisterUserDto registerUserDto = utils.applyPatch(user, patch, RegisterUserDto.class);
            this.saveUser(registerUserDto, user.getUserRole().getName());
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ServerException();
        }
    }

    private User saveUser(RegisterUserDto registerUserDto, String userRole) {
        Role roleEnum;
        try{
            roleEnum = Enum.valueOf(Role.class, userRole);
        } catch (IllegalArgumentException ex) {
            throw new RoleNotFoundException();
        }
        User userToSave = userDtoMapper.map(registerUserDto);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        userRoleRepository.findByName(roleEnum.name()).ifPresentOrElse(
                userToSave::setUserRole,
                () -> {
                    UserRole role = userRoleRepository.save(UserRole.builder()
                            .name(roleEnum.name())
                            .description(roleEnum.getDescription())
                            .build());
                    userToSave.setUserRole(role);
                }
        );
        return userRepository.save(userToSave);
    }

    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

}
