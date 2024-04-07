package pl.dk.ecommerceplatform.security;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import pl.dk.ecommerceplatform.error.exceptions.security.JwtAuthenticationException;

import java.io.IOException;
import java.text.ParseException;

import static pl.dk.ecommerceplatform.utils.UtilsService.*;

class BearerTokenFilter extends HttpFilter {

    private final Logger logger = getLogger(BearerTokenFilter.class);

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private static final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    private final AuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();
    private final JwtService jwtService;

    public BearerTokenFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header == null || header.isEmpty()) {
            chain.doFilter(request, response);
        } else {
            String compactJwt = header.substring(BEARER_PREFIX.length());
            SignedJWT signedJWT;
            try {
                signedJWT = SignedJWT.parse(compactJwt);
                verifyJwt(signedJWT);
                setSecurityContextHolder(signedJWT);
                chain.doFilter(request, response);
            } catch (JwtAuthenticationException e) {
                logger.debug(e.getMessage());
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            } catch (ParseException e) {
                JwtAuthenticationException authException = new JwtAuthenticationException("Bearer token could not be parsed");
                logger.debug(e.getMessage());
                authenticationFailureHandler.onAuthenticationFailure(request, response, authException);
            }
        }
    }

    private void verifyJwt(SignedJWT signedJWT) {
        jwtService.verifyEmailFromToken(signedJWT);
        jwtService.verifySignature(signedJWT);
        jwtService.verifyExpirationTime(signedJWT);
    }

    private void setSecurityContextHolder(SignedJWT signedJWT) {
        Authentication authentication = jwtService.createAuthentication(signedJWT);
        SecurityContext securityContext = securityContextHolderStrategy.getContext();
        securityContext.setAuthentication(authentication);
    }
}
