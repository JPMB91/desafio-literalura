package alurura.desafios.literalura.model;

import alurura.desafios.literalura.dto.BookDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @JsonAlias("title")
    private String titulo;

    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> autores;

    @JsonAlias("download_count")
    private int numeroDescargas;

    private List<String> idiomas;

    public Book() {
    }

    public Book(BookDTO bookDTO) {
        this.titulo = bookDTO.titulo();
        this.autores =
                bookDTO.autores().stream().map(authorDTO -> new Author(authorDTO.fallecimiento(),
                        authorDTO.nacimiento(), authorDTO.nombre())).toList();
        this.idiomas = Collections.singletonList(bookDTO.idiomas().get(0));
        this.numeroDescargas = bookDTO.numeroDescargas();
    }

    public String getTitulo() {
        return titulo;
    }

    public int getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setAutores(List<Author> autores) {
        this.autores = autores;
    }

    public List<Author> getAutores() {
        return autores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    @Override
    public String toString() {
        return """
                ------------LIBRO-----------------
                Titulo: %s
                Autor: %s
                Numero de descargas: %d
                Idiomas: %s
                ----------------------------------""".formatted(
                titulo,
                autores.stream().map(Author::getNombre).collect(Collectors.joining("\n")),
                numeroDescargas,
                String.join(",", idiomas)
        );
    }
}

