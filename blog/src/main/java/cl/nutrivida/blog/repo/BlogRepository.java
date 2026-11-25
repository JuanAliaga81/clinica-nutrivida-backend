package cl.nutrivida.blog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.nutrivida.blog.model.Blog;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
}