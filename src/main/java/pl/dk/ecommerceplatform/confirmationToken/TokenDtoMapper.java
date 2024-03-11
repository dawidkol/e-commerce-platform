package pl.dk.ecommerceplatform.confirmationToken;

import pl.dk.ecommerceplatform.confirmationToken.dtos.TokenDto;

class TokenDtoMapper {

    public static TokenDto map(Token token) {
        return TokenDto.builder()
                .id(token.getId())
                .token(token.getToken())
                .expiration(token.getExpiration())
                .userId(token.getUser().getId())
                .build();
    }
}
