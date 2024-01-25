package pl.dk.ecommerceplatform.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String signedJwt = jwtService.createSignedJwt(authentication.getName(), authorities);
        new ObjectMapper().writeValue(response.getWriter(), new JwtWrapper(authentication.getName(), signedJwt));
    }

    private record JwtWrapper(String username, String token) {
    }
}
