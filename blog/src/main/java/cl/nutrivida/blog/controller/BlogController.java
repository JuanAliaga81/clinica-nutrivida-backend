package cl.nutrivida.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cl.nutrivida.blog.model.Blog;
import cl.nutrivida.blog.service.BlogService;

@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "http://localhost:5173")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @GetMapping
    public ResponseEntity<List<Blog>> listarBlogs() {
        List<Blog> blogs = blogService.listar();
        if (blogs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(blogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Blog> buscarBlogPorId(@PathVariable Long id) {
        try {
            Blog blog = blogService.buscarPorId(id);
            return ResponseEntity.ok(blog);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Blog insertarBlog(
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("imagen") MultipartFile imagen) {
        return blogService.insertarBlog(titulo, descripcion, imagen);
    }

    @DeleteMapping("/{id}")
    public Blog eliminarBlog(@PathVariable Long id) {
        Blog blog = blogService.buscarPorId(id);
        blogService.eliminar(id);
        return blog;
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Blog actualizarBlog(
            @PathVariable Long id,
            @RequestParam("titulo") String titulo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        return blogService.actualizarBlog(id, titulo, descripcion, imagen);
    }
}