package cl.nutrivida.gateway.config;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(autorizacion -> autorizacion
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/login", "/api/usuarios/registro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios", "/api/usuarios/*").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/usuarios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/usuarios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.GET, "/api/contactos/tipos-asunto").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/contactos/usuario/**").access((autenticacion, contexto) -> {
                            var usuarioAutenticado = autenticacion.get();
                            boolean esAdministrador = usuarioAutenticado.getAuthorities().stream()
                                    .anyMatch(autoridad -> autoridad.getAuthority().equals("ROLE_ADMINISTRADOR"));
                            Object idUsuarioToken = usuarioAutenticado instanceof JwtAuthenticationToken jwt
                                    ? jwt.getToken().getClaims().get("idUsuario")
                                    : null;
                            String ruta = contexto.getRequest().getRequestURI();
                            String idSolicitado = ruta.substring(ruta.lastIndexOf('/') + 1);
                            boolean esPropietario = idUsuarioToken != null
                                    && idSolicitado.equals(idUsuarioToken.toString());
                            return new AuthorizationDecision(esAdministrador || esPropietario);
                        })
                        .requestMatchers(HttpMethod.POST, "/api/contactos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/contactos", "/api/contactos/*").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/contactos/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/contactos/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/blogs", "/api/blogs/**", "/api/servicios", "/api/servicios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/blogs", "/api/blogs/**", "/api/servicios", "/api/servicios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/blogs", "/api/blogs/**", "/api/servicios", "/api/servicios/**").hasRole("ADMINISTRADOR")
                        .anyRequest().permitAll())
                .oauth2ResourceServer(resource -> resource
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${nutrivida.jwt.secret}") String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("NUTRIVIDA_JWT_SECRET debe tener al menos 32 caracteres.");
        }
        SecretKey key = new SecretKeySpec(bytes, "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key).macAlgorithm(MacAlgorithm.HS256).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("roles");
        roles.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(roles);
        return converter;
    }


}