package pl.dk.ecommerceplatform.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserExistsException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.user.dtos.RegisterUserDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.util.Optional;

import static pl.dk.ecommerceplatform.constant.UserRoleConstant.CUSTOMER_ROLE;
import static pl.dk.ecommerceplatform.constant.UserRoleConstant.CUSTOMER_ROLE_DESCRIPTION;

@Service
@AllArgsConstructor
class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserDtoMapper userDtoMapper;
    private final UtilsService utils;

    @Transactional
    public UserDto register(RegisterUserDto registerUserDto) {
        Optional<User> user = userRepository.findByEmail(registerUserDto.email());
        if (user.isPresent()) {
            throw new UserExistsException();
        }
        User savedUser = saveUser(registerUserDto);
        return userDtoMapper.map(savedUser);
    }

    @Transactional
    public void updateUser(Long id, JsonMergePatch patch) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        try {
            RegisterUserDto registerUserDto = utils.applyPatch(user, patch, RegisterUserDto.class);
            this.saveUser(registerUserDto);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ServerException();
        }
    }

    private User saveUser(RegisterUserDto registerUserDto) {
        User userToSave = userDtoMapper.map(registerUserDto);
        userRoleRepository.findByName(CUSTOMER_ROLE).ifPresentOrElse(
                userToSave::setUserRole,
                () -> {
                    UserRole userRole = userRoleRepository.save(UserRole.builder()
                            .name(CUSTOMER_ROLE)
                            .description(CUSTOMER_ROLE_DESCRIPTION)
                            .build());
                    userToSave.setUserRole(userRole);
                }
        );
        return userRepository.save(userToSave);
    }

}
