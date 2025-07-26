package alurura.desafios.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonAlias("name")
    private String nombre;

    @JsonAlias("birth_year")
    private int nacimiento;

    @JsonAlias("death_year")
    private int fallecimiento;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private List<Book> libros;

    public Author() {
    }

    public Author(int fallecimiento, int nacimiento, String nombre) {
        this.fallecimiento = fallecimiento;
        this.nacimiento = nacimiento;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public int getNacimiento() {
        return nacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public int getFallecimiento() {
        return fallecimiento;
    }

    public List<Book> getLibros() {
        return libros;
    }

    @Override
    public String toString() {
        return """
                           
                Autor: %s
                Fecha de nacimiento: %d
                Fecha de fallecimiento: %d
                Libros: %s
                """.formatted(

                nombre,
                nacimiento,
                fallecimiento,
                libros.stream()
                        .map(Book::getTitulo)
                        .toList()
        );
    }

}