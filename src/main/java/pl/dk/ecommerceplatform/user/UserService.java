package pl.dk.ecommerceplatform.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Optional;

import static pl.dk.ecommerceplatform.constant.UserRoleConstant.USER_ROLE;

@Service
@AllArgsConstructor
class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserDtoMapper userDtoMapper;
    private final ObjectMapper objectMapper;

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
            RegisterUserDto registerUserDto = this.applyPatch(user, patch);
            this.saveUser(registerUserDto);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ServerException();
        }
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

    private RegisterUserDto applyPatch(User user, JsonMergePatch jsonMergePatch) throws JsonPatchException, JsonProcessingException {
        JsonNode jsonNode = objectMapper.valueToTree(user);
        JsonNode userApply = jsonMergePatch.apply(jsonNode);
        return objectMapper.treeToValue(userApply, RegisterUserDto.class);
    }

}
