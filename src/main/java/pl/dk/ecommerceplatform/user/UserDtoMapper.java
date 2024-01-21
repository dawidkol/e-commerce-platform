package pl.dk.ecommerceplatform.user;

import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.user.dtos.RegisterUserDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

@Service
class UserDtoMapper {

    public User map(RegisterUserDto registerUserDto) {
        return User.builder()
                .id(registerUserDto.id())
                .firstName(registerUserDto.firstName())
                .lastName(registerUserDto.lastName())
                .email(registerUserDto.email())
                .password(registerUserDto.password())
                .build();
    }

    public UserDto map(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getUserRole().getName())
                .build();
    }
}
