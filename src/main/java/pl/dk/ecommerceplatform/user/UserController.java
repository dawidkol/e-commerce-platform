package pl.dk.ecommerceplatform.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.ecommerceplatform.user.dtos.RegisterUserDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

import java.net.URI;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
class UserController {

    private final UserService userService;

    @PostMapping("")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        UserDto registeredUser = userService.register(registerUserDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(registeredUser.id())
                .toUri();
        return ResponseEntity.created(uri).body(registeredUser);
    }

}
