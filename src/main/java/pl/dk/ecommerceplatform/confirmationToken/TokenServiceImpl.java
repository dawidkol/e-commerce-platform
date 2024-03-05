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
class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public TokenDto generateConfirmationToken(String userEmail) {
        String token = UUID.randomUUID().toString();
        User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        Token tokenToSave = Token.builder()
                .token(token)
                .expiration(LocalDateTime.now().plusMinutes(15L))
                .user(user)
                .build();
        Token savedToken = tokenRepository.save(tokenToSave);
        return TokenDtoMapper.map(savedToken);
    }

    @Override
    public TokenDto getToken(String token) {
        return tokenRepository.findByToken(token).map(TokenDtoMapper::map).orElseThrow(TokenNotFoundException::new);
    }

    @Override
    public TokenDto getTokenByUserEmail(String userEmail) {
        return tokenRepository.findTokenByUser_Email(userEmail).map(TokenDtoMapper::map).orElseThrow(TokenNotFoundException::new);
    }
}
