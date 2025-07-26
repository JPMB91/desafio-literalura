package alurura.desafios.literalura.menu;

import alurura.desafios.literalura.model.Author;
import alurura.desafios.literalura.model.Book;
import alurura.desafios.literalura.model.Results;
import alurura.desafios.literalura.repository.AuthorRepository;
import alurura.desafios.literalura.repository.BookRepository;
import alurura.desafios.literalura.service.ApiConsumption;
import alurura.desafios.literalura.service.impl.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Menu {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private ApiConsumption apiConsumption = new ApiConsumption();
    private DataConverter dataConverter = new DataConverter();
    private String URL_BASE = "https://gutendex.com/books/";
    private final Scanner scanner = new Scanner(System.in);

    @Autowired

    public void showMenu() {

        int opcion = -1;

        while (opcion != 0) {
            var menu = """
                    _______________

                    Elija la opción a través de su número:
                    1 - buscar libro por título
                    2 - listar libros registrados
                    3 - listar autores registrados
                    4 - listar autores vivos en un determinado año
                    5 - listar libros por idioma
                    0 - salir
                    _______________

                    """;

            System.out.println(menu);
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    obtenerDatosLibros();
                    break;
                case 2:
                    obtenerTodosLibros();
                    break;
                case 3:
                    obtenerAutoresRegistrados();
                    break;
                case 4:
                    obtenerAutoresVivosDuranteFecha();
                    break;
                case 5:
                    obtenerLibrosPorIdioma();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación");
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }

    }

    private void obtenerDatosLibros() {
        System.out.println("Ingrese el nombre del libro que desee buscar");

        String nombreLibro = scanner.nextLine();

        nombreLibro = URLEncoder.encode(nombreLibro, StandardCharsets.UTF_8);

        var json = apiConsumption.getInformation(URL_BASE + "?search=" + nombreLibro);

        var resultsData = dataConverter.getData(json, Results.class);


        if (resultsData.books().isEmpty()) {
            System.out.println("No se ha encontrado un libro que coincida con la busqueda");
            return;
        }

        Book book = new Book(resultsData.books().get(0));
        System.out.println(book);

        Optional<Book> bookExist = bookRepository.findByTituloIgnoreCase(book.getTitulo());

        if (bookExist.isPresent()) {
            System.out.println("Este libro ya se encuentra en la base de datos");
            return;
        }

        List<Author> existingAuthor = book.getAutores().stream()
                .map(author -> authorRepository.findByNombreIgnoreCaseAndNacimiento(author.getNombre(),
                        author.getNacimiento())
                        .orElseGet(() -> authorRepository.save(author)))
                .toList();

        book.setAutores(existingAuthor);

        bookRepository.save(book);
        System.out.println("Libro guardado");

    }

    private void obtenerTodosLibros() {

        List<Book> libros = bookRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("Aún no hay libros almacenados");
        } else {

            libros.forEach(System.out::println);
        }
    }

    private void obtenerAutoresRegistrados() {
        List<Author> autores = authorRepository.obtenerTodosAutores();

        autores.stream().distinct().forEach(System.out::println);
    }

    private void obtenerAutoresVivosDuranteFecha() {
        System.out.println("Ingrese el año vivo de autor(es) que desea buscar");
        int fecha = scanner.nextInt();
        List<Author> autoresVivos = authorRepository.obtenerAutoresVivosDuranteFecha(fecha);

        if (autoresVivos.isEmpty()) {
            System.out.println("No hay registro de autores vivos en ese año por ahora");
        } else {
            autoresVivos.forEach(System.out::println);
        }
    }

    private void obtenerLibrosPorIdioma() {

        String idiomaSeleccionado = "";
        System.out.println(
                """
                        Ingrese el idioma para buscar libros:
                               
                        es - español
                        en - inglés
                        fr - francés
                        pt - portugues
                               
                        """);
        String idiomaBuscar = scanner.nextLine().toLowerCase();

        switch (idiomaBuscar) {
            case "en":
                idiomaSeleccionado = "en";
                break;
            case "es":
                idiomaSeleccionado = "es";
                break;
            case "fr":
                idiomaSeleccionado = "fr";
                break;
            case "pt":
                idiomaSeleccionado = "pt";
                break;
            default:
                System.out.println("Opción no válida");


        }
        List<Book> librosEncontrados = bookRepository.obtenerLibrosPorIdioma(idiomaSeleccionado);

        if (librosEncontrados.isEmpty()) {
            System.out.println("No hay registro de libros en ese idioma");
        } else {

            librosEncontrados.forEach(System.out::println);
        }
    }
}
