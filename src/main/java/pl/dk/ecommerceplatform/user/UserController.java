package pl.dk.ecommerceplatform.user;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.security.SecurityService;
import pl.dk.ecommerceplatform.user.dtos.*;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.net.URI;
import java.time.LocalDateTime;

import static pl.dk.ecommerceplatform.user.Role.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityService securityService;
    private final Logger logger = UtilsService.getLogger(this.getClass());

    @PostMapping("")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        UserDto registeredUser = userService.register(registerUserDto, CUSTOMER.name());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(registeredUser.id())
                .toUri();
        return ResponseEntity.created(uri).body(registeredUser);
    }

    @PatchMapping("")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_CUSTOMER')")
    public ResponseEntity<?> updateUser(@RequestBody JsonMergePatch jsonMergePatch) {
        Long id = securityService.getIdFromSecurityContextOrThrowException();
        userService.updateUser(id, jsonMergePatch);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/employee")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> registerEmployee(@Valid @RequestBody RegisterUserDto registerUserDto) {
        UserDto registeredEmployee = userService.register(registerUserDto, EMPLOYEE.name());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(registeredEmployee.id())
                .toUri();
        return ResponseEntity.created(uri).body(registeredEmployee);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUserByEmail(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/{token}")
    public ResponseEntity<?> confirmAccount(@PathVariable Long id, @PathVariable String token) {
        userService.confirmAccount(id, token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/token")
    public ResponseEntity<ActivationLinkDto> regenerateAccountConfirmationToken(@RequestBody LoginUserDto userLoginDto) {
        UserTokenWrapper userTokenWrapper = userService.createToken(userLoginDto);
        URI activationLinkUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/users/{userId}/{token}")
                .buildAndExpand(userTokenWrapper.userDto().id(), userTokenWrapper.tokenDto().token())
                .toUri();
        UserDto userDto = userTokenWrapper.userDto();
        LocalDateTime expiration = userTokenWrapper.tokenDto().expiration();
        return ResponseEntity.created(activationLinkUri).body(new ActivationLinkDto(userDto, expiration));
    }

}
