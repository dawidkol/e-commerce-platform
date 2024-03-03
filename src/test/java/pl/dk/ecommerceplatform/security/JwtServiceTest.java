package pl.dk.ecommerceplatform.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.TemporalOffset;
import org.assertj.core.data.TemporalUnitLessThanOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import pl.dk.ecommerceplatform.error.exceptions.security.JwtAuthenticationException;

import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.DURATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService underTest;
    private JWSSigner jwsSigner;
    private JWSVerifier jwsVerifier;
    private String sharedKeyForTests = "8e55772b-26c5-4114-bbe9-cb6d44af2ce4";

    @BeforeEach
    void init() throws JOSEException {
        jwsSigner = new MACSigner(sharedKeyForTests.getBytes());
        jwsVerifier = new MACVerifier(sharedKeyForTests.getBytes());
        underTest = new JwtService(sharedKeyForTests);
    }

    @Test
    void itShouldCreteJWT() throws ParseException {
        // Given
        String username = "john@john@test.pl";
        List<String> authorities = List.of("CUSTOMER");
        int expirationTime = 30 * 24 * 60 * 60;

        // When
        String jwt = underTest.createSignedJwt(username, authorities);

        // Then
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        List<String> authoritiesFromCreatedJWT = signedJWT.getJWTClaimsSet().getStringListClaim("authorities");
        Date expirationTimeFromCratedJWT = signedJWT.getJWTClaimsSet().getExpirationTime();
        String subjectFromCreatedJWT = signedJWT.getJWTClaimsSet().getSubject();

        assertAll(
                () -> assertThat(subjectFromCreatedJWT).isEqualTo(username),
                () -> assertThat(authoritiesFromCreatedJWT).isEqualTo(authorities),
                () -> assertThat(expirationTimeFromCratedJWT.toInstant()).isBeforeOrEqualTo(Instant.now().plusSeconds(expirationTime))
        );
    }

    @Test
    void itShouldVerifySignature() throws ParseException {
        // Given
        String username = "john@john@test.pl";
        List<String> authorities = List.of("CUSTOMER");
        String jwt = underTest.createSignedJwt(username, authorities);
        SignedJWT signedJWT = SignedJWT.parse(jwt);

        // When
        // Then
        assertDoesNotThrow(() -> underTest.verifySignature(signedJWT));
    }

    @Test
    void itShouldVerifyExpirationTime() throws ParseException {
        // Given
        String username = "john@john@test.pl";
        List<String> authorities = List.of("CUSTOMER");
        String jwt = underTest.createSignedJwt(username, authorities);
        SignedJWT signedJWT = SignedJWT.parse(jwt);

        // When
        // Then
        assertDoesNotThrow(() -> underTest.verifyExpirationTime(signedJWT));
    }

    @Test
    void itShouldCreateAuthentication() throws ParseException {
        // Given
        String username = "john@john@test.pl";
        List<String> authorities = List.of("CUSTOMER");
        String jwt = underTest.createSignedJwt(username, authorities);
        SignedJWT signedJWT = SignedJWT.parse(jwt);

        // When
        Authentication authentication = underTest.createAuthentication(signedJWT);

        Optional<? extends GrantedAuthority> grantedAuthority = authentication.getAuthorities().stream().findAny();
        // Then
        assertAll(
                () -> assertThat(authentication.getName()).isEqualTo(username),
                () -> assertThat(grantedAuthority).isPresent().hasValueSatisfying(
                        granted -> assertThat(granted.getAuthority()).isEqualTo(authorities.stream().findAny().get()))
        );
    }

    @Test
    void itShouldThrowJwtAuthenticationException() throws ParseException {
        // Given
        SignedJWT signedJWT = mock(SignedJWT.class);

        // When
        // Then
        assertThrows(JwtAuthenticationException.class, () -> underTest.verifySignature(signedJWT));
    }

}