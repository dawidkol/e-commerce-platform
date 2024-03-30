package pl.dk.ecommerceplatform.security;

import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
class SecurityConfig {

    private final JwtService jwtService;

    @Bean
    public MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, MvcRequestMatcher.Builder mvc, AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getOrBuild();
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtService);
        BearerTokenFilter bearerTokenFilter = new BearerTokenFilter(jwtService);

        httpSecurity.authorizeHttpRequests(request -> request
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.GET, "/stats")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.GET, "/brands/{id}")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.GET, "/brands")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.GET, "/category")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.GET, "/products/{id}")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.GET, "/products")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.GET, "/products/search")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.GET, "/products/{id}/reviews")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.GET, "/products/promotion")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.POST, "/users")).permitAll()
                .requestMatchers(mvc.pattern("/payments/events")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.POST,"/email/contact")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.PATCH,"/users/{id}/{token}")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.POST, "/users/token")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.GET, "/images/{productId}")).permitAll()
                .requestMatchers(mvc.pattern(HttpMethod.GET, "/currency")).permitAll()
                .requestMatchers("/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**")
                .permitAll()
                .anyRequest()
                .authenticated());

        httpSecurity.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class);
        httpSecurity.addFilterBefore(bearerTokenFilter, AuthorizationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
