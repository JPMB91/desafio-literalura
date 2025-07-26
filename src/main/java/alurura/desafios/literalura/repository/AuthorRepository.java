package alurura.desafios.literalura.repository;

import alurura.desafios.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("Select distinct a from Author a join fetch a.libros")
    List<Author> obtenerTodosAutores();

    Optional<Author> findByNombreIgnoreCaseAndNacimiento(String nombre, int nacimiento);

    @Query("Select a from Author a where a.nacimiento <= :fecha AND  :fecha <= a.fallecimiento")
    List<Author> obtenerAutoresVivosDuranteFecha(@Param("fecha") int fecha);

}
