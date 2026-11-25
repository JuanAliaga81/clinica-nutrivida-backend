package cl.nutrivida.blog.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import cl.nutrivida.blog.model.Blog;
import cl.nutrivida.blog.repo.BlogRepository;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Value("${nutrivida.uploads.blogs-dir}")
    private String directorioImagenes;

    public List<Blog> listar() {
        return blogRepository.findAll();
    }

    public Blog buscarPorId(Long id) {
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No se encontró el blog con ID " + id));
    }

    public Blog insertarBlog(String titulo, String descripcion, MultipartFile imagen) {
        Blog blog = new Blog();
        blog.setTitulo(titulo);
        blog.setDescripcion(descripcion);
        blog.setImagen(guardarImagen(imagen));
        return blogRepository.save(blog);
    }

    public void eliminar(Long id) {
        Blog blog = buscarPorId(id);
        blogRepository.delete(blog);
    }

    public Blog actualizarBlog(
            Long id,
            String titulo,
            String descripcion,
            MultipartFile imagen) {
        Blog blogExistente = buscarPorId(id);
        blogExistente.setTitulo(titulo);
        blogExistente.setDescripcion(descripcion);

        if (imagen != null && !imagen.isEmpty()) {
            blogExistente.setImagen(guardarImagen(imagen));
        }

        return blogRepository.save(blogExistente);
    }

    private String guardarImagen(MultipartFile imagen) {
        if (imagen == null || imagen.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Debes adjuntar una imagen");
        }

        String tipoContenido = imagen.getContentType();
        String extension = extensionPermitida(tipoContenido);
        Path directorio = Paths.get(directorioImagenes).toAbsolutePath().normalize();
        String nombreArchivo = UUID.randomUUID() + extension;
        Path destino = directorio.resolve(nombreArchivo).normalize();

        if (!destino.startsWith(directorio)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nombre de archivo no permitido");
        }

        try {
            Files.createDirectories(directorio);
            try (InputStream contenido = imagen.getInputStream()) {
                Files.copy(contenido, destino);
            }
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "No se pudo guardar la imagen", e);
        }

        return "/uploads/blogs/" + nombreArchivo;
    }

    private String extensionPermitida(String tipoContenido) {
        if (tipoContenido == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El archivo debe ser una imagen PNG, JPG o WEBP");
        }

        return switch (tipoContenido.toLowerCase(Locale.ROOT)) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Solo se aceptan imágenes PNG, JPG o WEBP");
        };
    }
}