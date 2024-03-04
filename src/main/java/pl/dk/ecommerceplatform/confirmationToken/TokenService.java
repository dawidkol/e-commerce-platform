package pl.dk.ecommerceplatform.confirmationToken;

import pl.dk.ecommerceplatform.confirmationToken.dtos.TokenDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

public interface TokenService {
    String generateConfirmationToken(UserDto userDto);
    TokenDto getToken(String token);
}
