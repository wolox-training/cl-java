package wolox.training.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.services.OpenLibraryService;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private OpenLibraryService openLibraryService;

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetAllBooks_thenReturnJsonArray() throws Exception {
        //Given
        Book bookTest = new Book(null,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");
        List<Book> allBooksTest = Arrays.asList(bookTest);

        given(bookRepository.findAll()).willReturn(allBooksTest);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(bookTest.getTitle())));

        System.out.println("whenGetAllBooks Passed");
    }

    @Test
    void whenGetAllBooksWithoutAuthentication_thenReturnUnauthorized() throws Exception {
        //Given
        Book bookTest = new Book(null,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");
        List<Book> allBooksTest = Arrays.asList(bookTest);

        given(bookRepository.findAll()).willReturn(allBooksTest);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.get("/api/books")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        System.out.println("whenGetAllBooksWithoutAuthentication Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetABookByAuthor_thenReturnJsonArray() throws Exception {
        //Given
        Book bookTest = new Book(null,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");

        given(bookRepository.findFirstByAuthor("Paolo")).willReturn(java.util.Optional.of(bookTest));

        // When + Then
        mvc.perform(MockMvcRequestBuilders.get("/api/books?author=Paolo")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is(bookTest.getAuthor())));
        System.out.println("whenGetABookByAuthor Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetABookByAuthorNonExist_thenThrowBookNotFound() throws Exception {
        //Given
        Book bookTest = new Book(null,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");

        given(bookRepository.findFirstByAuthor("Paolo")).willReturn(java.util.Optional.of(bookTest));

        // When + Then
        mvc.perform(MockMvcRequestBuilders.get("/api/books?author=Francisco")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        System.out.println("whenGetABookByAuthorNonExist Passed");
    }


    @WithMockUser(value = "iskandar")
    @Test
    void whenGetABookById_thenReturnJson() throws Exception {
        //Given
        Book bookTest = new Book(1L,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");

        given(bookRepository.findById(bookTest.getId())).willReturn(java.util.Optional.of(bookTest));

        // When + Then
        mvc.perform(MockMvcRequestBuilders.get("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$.*").exists())
                .andExpect(jsonPath("$.*", hasSize(is(10))))
                .andExpect(jsonPath("$.id", is(bookTest.getId().intValue())))
                .andExpect(jsonPath("$.image", is(bookTest.getImage())));
        System.out.println("whenGetABookById Passed");
    }

    @Test
    void whenAddABook_thenReturnOk() throws Exception {
        //Given
        Book bookTest = new Book(1L,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");

        given(bookRepository.save(bookTest)).willReturn(bookTest);

        String json = mapper.writeValueAsString(bookTest);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        System.out.println("whenAddABook Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenUpdateABook_thenReturnOk() throws Exception {
        //Given
        Book bookTest = new Book(1L,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");

        given(bookRepository.save(bookTest)).willReturn(bookTest);

        bookTest.setTitle("Chamber of Secrets");
        given(bookRepository.save(bookTest)).willReturn(bookTest);
        given(bookRepository.findById(bookTest.getId())).willReturn(java.util.Optional.of(bookTest));

        String json = mapper.writeValueAsString(bookTest);

        // When + Then

        mvc.perform(MockMvcRequestBuilders.put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        System.out.println("whenUpdateABook Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenUpdateABookIdMismatch_thenThrowIdMismatched() throws Exception {
        //Given
        Book bookTest = new Book(1L,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");

        given(bookRepository.save(bookTest)).willReturn(bookTest);

        bookTest.setTitle("Chamber of Secrets");
        given(bookRepository.save(bookTest)).willReturn(bookTest);
        given(bookRepository.findById(bookTest.getId())).willReturn(java.util.Optional.of(bookTest));

        String json = mapper.writeValueAsString(bookTest);

        // When + Then

        mvc.perform(MockMvcRequestBuilders.put("/api/books/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        System.out.println("whenUpdateABookIdMismatch Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenDeleteABookById_thenReturnString() throws Exception {
        //Given
        Book bookTest = new Book(1L,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");

        given(bookRepository.findById(bookTest.getId())).willReturn(java.util.Optional.of(bookTest));

        // When + Then
        mvc.perform(MockMvcRequestBuilders.delete("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        System.out.println("whenDeleteABookById Passed");
    }

    @Test
    void whenDeleteABookByIdWithoutAuthentication_thenThrowUnauthorized() throws Exception {
        //Given
        Book bookTest = new Book(1L,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");

        given(bookRepository.findById(bookTest.getId())).willReturn(java.util.Optional.of(bookTest));

        // When + Then
        mvc.perform(MockMvcRequestBuilders.delete("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        System.out.println("whenDeleteABookByIdWithoutAuthentication Passed");
    }

}
