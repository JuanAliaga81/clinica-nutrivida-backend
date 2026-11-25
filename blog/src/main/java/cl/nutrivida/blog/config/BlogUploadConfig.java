package cl.nutrivida.blog.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BlogUploadConfig implements WebMvcConfigurer {

    @Value("${nutrivida.uploads.blogs-dir}")
    private String directorioImagenes;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path directorio = Paths.get(directorioImagenes).toAbsolutePath().normalize();
        String ubicacion = directorio.toUri().toString();
        if (!ubicacion.endsWith("/")) {
            ubicacion += "/";
        }

        registry.addResourceHandler("/uploads/blogs/**")
                .addResourceLocations(ubicacion);
    }
}