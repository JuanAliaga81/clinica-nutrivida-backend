package cl.nutrivida.blog.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cl.nutrivida.blog.model.Blog;
import cl.nutrivida.blog.repo.BlogRepository;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner cargarBlogs(BlogRepository blogRepository) {
        return args -> {
            if (blogRepository.count() == 0) {
                blogRepository.save(new Blog(
                        null,
                        "Los beneficios de un buen desayuno",
                        "Un desayuno equilibrado aporta energía y puede ayudarte a comenzar el día con mayor concentración. Incluye alimentos variados, como fruta, cereales integrales y una fuente de proteína.",
                        "/uploads/blogs/Beneficios.jfif"));

                blogRepository.save(new Blog(
                        null,
                        "5 mitos comunes sobre las dietas",
                        "No existe una única dieta adecuada para todas las personas. La alimentación debe adaptarse a tus necesidades y objetivos, con orientación profesional y hábitos sostenibles.",
                        "/uploads/blogs/Imagen2.jfif"));

                System.out.println("Blogs iniciales cargados.");
            } else {
                System.out.println("No se insertaron blogs: la tabla ya contiene datos.");
            }
        };
    }
}
