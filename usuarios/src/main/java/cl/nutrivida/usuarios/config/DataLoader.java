package cl.nutrivida.usuarios.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import cl.nutrivida.usuarios.model.Persona;
import cl.nutrivida.usuarios.model.Usuario;
import cl.nutrivida.usuarios.repo.PersonaRepository;
import cl.nutrivida.usuarios.repo.UsuarioRepository;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initData(
            UsuarioRepository usuarioRepo,
            PersonaRepository personaRepo,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String correoPaciente = "paciente@nutrivida.com";
            String rutPaciente = "218511414";

            if (usuarioRepo.findByCorreoIgnoreCase(correoPaciente).isEmpty()
                    && !personaRepo.existsByRutIgnoreCase(rutPaciente)) {

                Persona personaPaciente = new Persona(
                        null,
                        "Juan Aliaga",
                        rutPaciente,
                        LocalDate.of(2005, 5, 31),
                        "M");

                personaPaciente = personaRepo.save(personaPaciente);

                Usuario paciente = new Usuario();
                paciente.setPersona(personaPaciente);
                paciente.setCorreo(correoPaciente);
                paciente.setCelular("912345670");
                paciente.setContrasena(passwordEncoder.encode("nutrivida123"));
                paciente.setRol("PACIENTE");
                usuarioRepo.save(paciente);

                System.out.println("Cuenta de demostración cargada: paciente");
            } else {
                System.out.println("No se insertó el paciente: ya existe la cuenta o persona");
            }

            String correoAdmin = "admin@nutrivida.com";
            String rutAdmin = "16789432K";

            if (usuarioRepo.findByCorreoIgnoreCase(correoAdmin).isEmpty()
                    && !personaRepo.existsByRutIgnoreCase(rutAdmin)) {

                Persona personaAdmin = new Persona(
                        null,
                        "Administradora NutriVida",
                        rutAdmin,
                        LocalDate.of(1990, 1, 1),
                        "F");

                personaAdmin = personaRepo.save(personaAdmin);

                Usuario admin = new Usuario();
                admin.setPersona(personaAdmin);
                admin.setCorreo(correoAdmin);
                admin.setCelular("912345671");
                admin.setContrasena(passwordEncoder.encode("adminutrivida123"));
                admin.setRol("ADMINISTRADOR");
                usuarioRepo.save(admin);

                System.out.println("Cuenta de demostración cargada: administrador");
            } else {
                System.out.println("No se insertó el administrador: ya existe la cuenta o persona");
            }
        };
    }
}