package cl.nutrivida.gateway.config;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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

                        // Acceso público: inicio de sesión, registro y catálogo de lectura.
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/login", "/api/usuarios/registro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios/renovar").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/servicios", "/api/servicios/**",
                                "/api/blogs", "/api/blogs/**", "/uploads/blogs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/contactos/tipos-asunto").permitAll()

                        // Un paciente solo consulta sus propios datos vinculados y citas.
                        .requestMatchers(HttpMethod.GET, "/api/usuarios-personas/usuario/**")
                                .access((auth, ctx) -> propietarioOAdministrador(auth.get(), ctx.getRequest()))
                        .requestMatchers(HttpMethod.POST, "/api/usuarios-personas/usuario/**")
                                .access((auth, ctx) -> propietarioOAdministrador(auth.get(), ctx.getRequest()))
                        .requestMatchers(HttpMethod.GET, "/api/citas/usuario/**")
                                .access((auth, ctx) -> propietarioOAdministrador(auth.get(), ctx.getRequest()))
                        .requestMatchers(HttpMethod.POST, "/api/citas/usuario/**")
                                .access((auth, ctx) -> propietarioOAdministrador(auth.get(), ctx.getRequest()))
                        .requestMatchers(HttpMethod.DELETE, "/api/citas/usuario/**")
                                .access((auth, ctx) -> propietarioOAdministrador(auth.get(), ctx.getRequest()))

                        // Cada paciente puede enviar mensajes y consultar su conversación.
                        .requestMatchers(HttpMethod.POST, "/api/contactos/usuario/**")
                                .access((auth, ctx) -> propietarioOAdministrador(auth.get(), ctx.getRequest()))
                        .requestMatchers(HttpMethod.GET, "/api/contactos/usuario/**")
                                .access((auth, ctx) -> propietarioOAdministrador(auth.get(), ctx.getRequest()))

                        // Las personas y las operaciones generales quedan para administración.
                        .requestMatchers("/api/personas", "/api/personas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/usuarios-personas", "/api/usuarios-personas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/usuarios", "/api/usuarios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/citas", "/api/citas/**").hasRole("ADMINISTRADOR")
                        .requestMatchers("/api/contactos", "/api/contactos/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/blogs", "/api/blogs/**",
                                "/api/servicios", "/api/servicios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/blogs", "/api/blogs/**",
                                "/api/servicios", "/api/servicios/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/blogs", "/api/blogs/**",
                                "/api/servicios", "/api/servicios/**").hasRole("ADMINISTRADOR")

                        // Una ruta nueva debe declararse expresamente antes de quedar disponible.
                        .anyRequest().denyAll())
                .oauth2ResourceServer(resource -> resource
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }

    private AuthorizationDecision propietarioOAdministrador(Authentication autenticacion,
            HttpServletRequest solicitud) {
        if (autenticacion == null || !autenticacion.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }

        boolean esAdministrador = autenticacion.getAuthorities().stream()
                .anyMatch(autoridad -> autoridad.getAuthority().equals("ROLE_ADMINISTRADOR"));
        if (esAdministrador) return new AuthorizationDecision(true);

        if (!(autenticacion instanceof JwtAuthenticationToken jwt)) {
            return new AuthorizationDecision(false);
        }

        Object idUsuarioToken = jwt.getToken().getClaims().get("idUsuario");
        String[] segmentos = solicitud.getRequestURI().split("/");
        // /api/{recurso}/usuario/{idUsuario}[/{idRecurso}]
        boolean rutaTieneId = segmentos.length >= 5 && "usuario".equals(segmentos[3]);
        boolean esPropietario = rutaTieneId && idUsuarioToken != null
                && segmentos[4].equals(idUsuarioToken.toString());
        return new AuthorizationDecision(esPropietario);
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
