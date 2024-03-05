package pl.dk.ecommerceplatform.confirmationToken;

import pl.dk.ecommerceplatform.confirmationToken.dtos.TokenDto;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

public interface TokenService {
    TokenDto generateConfirmationToken(String userEmail);
    TokenDto getToken(String token);
    TokenDto getTokenByUserEmail(String email);
}
