package cl.nutrivida.usuarios.config;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Configuration
public class JwtConfig {

    @Bean
    public JwtEncoder jwtEncoder(@Value("${nutrivida.jwt.secret}") String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("NUTRIVIDA_JWT_SECRET debe tener al menos 32 caracteres.");
        }
        SecretKey key = new SecretKeySpec(bytes, "HmacSHA256");
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }
}