package pl.dk.ecommerceplatform.user.dtos;

import lombok.Builder;
import pl.dk.ecommerceplatform.confirmationToken.dtos.TokenDto;

@Builder
public record UserTokenWrapper(UserDto userDto, TokenDto tokenDto) {
}
