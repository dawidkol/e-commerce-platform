package pl.dk.ecommerceplatform.security;

import pl.dk.ecommerceplatform.user.User;
import pl.dk.ecommerceplatform.security.dtos.UserCredentialsDto;

class UserCredentialsDtoMapper {

    public static UserCredentialsDto map(User user) {
        return UserCredentialsDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getUserRole().getName())
                .enabled(user.isEnabled())
                .build();
    }
}
