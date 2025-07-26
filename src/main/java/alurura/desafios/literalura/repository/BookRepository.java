package alurura.desafios.literalura.repository;

import alurura.desafios.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTituloIgnoreCase(String titulo);

    @Query("""
            SELECT b
              FROM Book b
              JOIN b.idiomas i
             WHERE LOWER(i) = LOWER(:idioma)
            """)
    List<Book> obtenerLibrosPorIdioma(@Param("idioma") String idioma);

}
