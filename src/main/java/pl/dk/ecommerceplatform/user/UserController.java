package pl.dk.ecommerceplatform.user;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.confirmationToken.TokenService;
import pl.dk.ecommerceplatform.confirmationToken.dtos.TokenDto;
import pl.dk.ecommerceplatform.email.EmailService;
import pl.dk.ecommerceplatform.security.SecurityService;
import pl.dk.ecommerceplatform.user.dtos.RegisterUserDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.net.URI;

import static pl.dk.ecommerceplatform.user.Role.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;
    private final SecurityService securityService;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final Logger logger = UtilsService.getLogger(this.getClass());

    @PostMapping("")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        UserDto registeredUser = userService.register(registerUserDto, CUSTOMER.name());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(registeredUser.id())
                .toUri();

        String token = tokenService.generateConfirmationToken(registeredUser);
        String tokenUri = ServletUriComponentsBuilder
                .fromUri(uri).path("/{token}")
                .buildAndExpand(token).toUriString();
        logger.debug("Generated token: {}", token);

        emailService.createAndSendRegistrationConfirmationEmail(registeredUser, tokenUri);
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

    @DeleteMapping("/{email}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUserByEmail(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/{token}")
    public ResponseEntity<?> confirmAccount(@PathVariable Long id, @PathVariable String token) {
        userService.confirmAccount(id, token);
        return ResponseEntity.noContent().build();
    }

}
