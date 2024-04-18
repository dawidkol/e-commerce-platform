package pl.dk.ecommerceplatform.confirmationToken;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.confirmationToken.dtos.TokenDto;
import pl.dk.ecommerceplatform.error.exceptions.token.TokenNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.user.UserNotFoundException;
import pl.dk.ecommerceplatform.user.User;
import pl.dk.ecommerceplatform.user.UserRepository;
import pl.dk.ecommerceplatform.utils.UtilsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static pl.dk.ecommerceplatform.constant.TokenConstant.VALID_TIME;

@Service
@AllArgsConstructor
class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final Logger logger = UtilsService.getLogger(this.getClass());

    @Transactional
    public TokenDto generateConfirmationToken(String userEmail) {
        String token = UUID.randomUUID().toString();
        User user = userRepository.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
        Token tokenToSave = Token.builder()
                .token(token)
                .expiration(LocalDateTime.now().plusMinutes(VALID_TIME))
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

    @Async
    @Scheduled(cron = "${scheduler.token}")
    public void cleanInactiveTokens() {
        logger.debug("Starting deleting inactive tokens");
        List<Token> allExpiredTokens = tokenRepository.findAllExpiredTokens();
        if (!allExpiredTokens.isEmpty()) {
            tokenRepository.deleteAll(allExpiredTokens);
            logger.debug("Token table cleaned. Deleted {} records", allExpiredTokens.size());
        } else
            logger.debug("No tokens were expired");
    }
}
