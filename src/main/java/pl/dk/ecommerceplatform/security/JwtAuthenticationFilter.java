package pl.dk.ecommerceplatform.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;

import static pl.dk.ecommerceplatform.utils.UtilsService.*;

class JwtAuthenticationFilter extends HttpFilter {
    private final Logger logger = getLogger(JwtAuthenticationFilter.class);
    private static final RequestMatcher DEFAULT_ANT_PATCH_REQUEST_MATCHER = new AntPathRequestMatcher("/auth", "POST", false);
    private final AuthenticationManager authenticationManager;
    private final AuthenticationFailureHandler failureHandler;
    private final AuthenticationSuccessHandler successHandler;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        successHandler = new JwtAuthenticationSuccessHandler(jwtService);
        failureHandler = new JwtAuthenticationFailureHandler();
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!DEFAULT_ANT_PATCH_REQUEST_MATCHER.matches(request)) {
            chain.doFilter(request, response);
        } else {
            try {
                Authentication authenticationResult = this.attemptAuthentication(request);
                logger.debug("Authentication success for user: [%s]" .formatted(authenticationResult.getName()));
                this.successHandler.onAuthenticationSuccess(request, response, authenticationResult);
            } catch (AuthenticationException e) {
                logger.debug("Authentication failed %s" .formatted(e.getCause()));
                this.failureHandler.onAuthenticationFailure(request, response, e);
            }
        }
    }

    private Authentication attemptAuthentication(HttpServletRequest request) throws AuthenticationException, IOException {
        JwtAuthenticationToken jwtAuthenticationToken = new ObjectMapper().readValue(request.getInputStream(), JwtAuthenticationToken.class);
        logger.debug("Authentication: [%s] with password: [%s]" .formatted(jwtAuthenticationToken.username, jwtAuthenticationToken.password));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(jwtAuthenticationToken.username, jwtAuthenticationToken.password);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    private record JwtAuthenticationToken(String username, String password) {
    }
}
