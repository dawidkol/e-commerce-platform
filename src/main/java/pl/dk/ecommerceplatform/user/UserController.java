package pl.dk.ecommerceplatform.user;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.security.SecurityService;
import pl.dk.ecommerceplatform.user.dtos.RegisterUserDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

import java.net.URI;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
class UserController {

    private final UserService userService;
    private final SecurityService securityService;

    @PostMapping("")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        UserDto registeredUser = userService.register(registerUserDto);
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

}
