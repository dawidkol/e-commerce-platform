package pl.dk.ecommerceplatform.confirmationToken;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.confirmationToken.dtos.TokenDto;
import pl.dk.ecommerceplatform.error.exceptions.token.TokenNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.user.User;
import pl.dk.ecommerceplatform.user.UserRepository;
import pl.dk.ecommerceplatform.user.dtos.UserDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
class TokenServiceImpl implements TokenService{

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public String generateConfirmationToken(UserDto userDto) {
        String token = UUID.randomUUID().toString();
        User user = userRepository.findByEmail(userDto.email()).orElseThrow(UserNotFoundException::new);
        Token tokenToSave = Token.builder()
                .token(token)
                .expiration(LocalDateTime.now().plusMinutes(15L))
                .user(user)
                .build();
        Token savedToken = tokenRepository.save(tokenToSave);
        TokenDto tokenDto = TokenDtoMapper.map(savedToken);
        return tokenDto.token();
    }

    @Override
    public TokenDto getToken(String token) {
        return tokenRepository.findByToken(token).map(TokenDtoMapper::map).orElseThrow(TokenNotFoundException::new);
    }
}
