package pl.dk.ecommerceplatform.confirmationToken;

import org.junit.jupiter.api.Test;
import pl.dk.ecommerceplatform.confirmationToken.dtos.TokenDto;
import pl.dk.ecommerceplatform.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static pl.dk.ecommerceplatform.constant.TokenConstant.VALID_TIME;

class TokenDtoMapperTest {

    @Test
    void itShouldMapToTokenDto() {
        // Given
        Token token = Token.builder()
                .id(1L)
                .token(UUID.randomUUID().toString())
                .expiration(LocalDateTime.now().plusMinutes(VALID_TIME))
                .user(User.builder().id(1L).build()).build();

        // When
        TokenDto tokenDto = TokenDtoMapper.map(token);

        // Then
        assertAll(
                () -> assertThat(tokenDto.id()).isEqualTo(token.getId()),
                () -> assertThat(tokenDto.token()).isEqualTo(token.getToken()),
                () -> assertThat(tokenDto.userId()).isEqualTo(token.getUser().getId()),
                () -> assertThat(tokenDto.expiration()).isEqualTo(token.getExpiration())
        );
    }
}