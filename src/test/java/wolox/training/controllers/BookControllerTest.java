package wolox.training.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.contants.ConstantsTest.AUTHOR_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.GENRE_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.IMAGE_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.ISBN_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.PAGES_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.PUBLISHER_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.SUBTITLE_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.TITLE_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.URL_BOOK;
import static wolox.training.contants.ConstantsTest.YEAR_BOOK_TEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private Book bookTest;
    private Book bookTestId;

    @BeforeEach
    public void setUp () {
        bookTest = new Book (null, GENRE_BOOK_TEST, AUTHOR_BOOK_TEST, IMAGE_BOOK_TEST, TITLE_BOOK_TEST,
                SUBTITLE_BOOK_TEST, PUBLISHER_BOOK_TEST, YEAR_BOOK_TEST, PAGES_BOOK_TEST, ISBN_BOOK_TEST);

        bookTestId = new Book(1L, GENRE_BOOK_TEST, AUTHOR_BOOK_TEST, IMAGE_BOOK_TEST, TITLE_BOOK_TEST,
                SUBTITLE_BOOK_TEST, PUBLISHER_BOOK_TEST, YEAR_BOOK_TEST, PAGES_BOOK_TEST, ISBN_BOOK_TEST);
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetAllBooks_thenReturnJsonArray() throws Exception {
        //Given
        List<Book> allBooksTest = Arrays.asList(bookTest);

        given(bookRepository.findAll()).willReturn(allBooksTest);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.get(URL_BOOK)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(bookTest.getTitle())));

    }

    @Test
    void whenGetAllBooksWithoutAuthentication_thenReturnUnauthorized() throws Exception {
        //Given
        List<Book> allBooksTest = Arrays.asList(bookTest);

        given(bookRepository.findAll()).willReturn(allBooksTest);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.get(URL_BOOK)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetABookByAuthor_thenReturnJsonArray() throws Exception {
        //Given
        given(bookRepository.findFirstByAuthor("Paolo")).willReturn(java.util.Optional.of(bookTest));

        // When + Then
        mvc.perform(MockMvcRequestBuilders.get(URL_BOOK+"?author="+AUTHOR_BOOK_TEST)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].author", is(bookTest.getAuthor())));
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetABookByAuthorNonExist_thenThrowBookNotFound() throws Exception {
        //Given
        given(bookRepository.findFirstByAuthor("Paolo")).willReturn(java.util.Optional.of(bookTest));

        // When + Then
        mvc.perform(MockMvcRequestBuilders.get(URL_BOOK+"?author=Francisco")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @WithMockUser(value = "iskandar")
    @Test
    void whenGetABookById_thenReturnJson() throws Exception {
        //Given
        given(bookRepository.findById(bookTestId.getId())).willReturn(java.util.Optional.of(bookTestId));

        // When + Then
        mvc.perform(MockMvcRequestBuilders.get(URL_BOOK+"/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$.*").exists())
                .andExpect(jsonPath("$.*", hasSize(is(10))))
                .andExpect(jsonPath("$.id", is(bookTestId.getId().intValue())))
                .andExpect(jsonPath("$.image", is(bookTestId.getImage())));
    }

    @Test
    void whenAddABook_thenReturnOk() throws Exception {
        //Given
        given(bookRepository.save(bookTestId)).willReturn(bookTestId);

        String json = mapper.writeValueAsString(bookTestId);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.post(URL_BOOK)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenUpdateABook_thenReturnOk() throws Exception {
        //Given
        given(bookRepository.save(bookTestId)).willReturn(bookTestId);

        bookTestId.setTitle("Chamber of Secrets");
        given(bookRepository.save(bookTestId)).willReturn(bookTestId);
        given(bookRepository.findById(bookTestId.getId())).willReturn(java.util.Optional.of(bookTestId));

        String json = mapper.writeValueAsString(bookTestId);

        // When + Then

        mvc.perform(MockMvcRequestBuilders.put(URL_BOOK+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenUpdateABookIdMismatch_thenThrowIdMismatched() throws Exception {
        //Given
        given(bookRepository.save(bookTestId)).willReturn(bookTestId);

        bookTestId.setTitle("Chamber of Secrets");
        given(bookRepository.save(bookTestId)).willReturn(bookTestId);
        given(bookRepository.findById(bookTestId.getId())).willReturn(java.util.Optional.of(bookTestId));

        String json = mapper.writeValueAsString(bookTestId);

        // When + Then

        mvc.perform(MockMvcRequestBuilders.put(URL_BOOK+"/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenDeleteABookById_thenReturnString() throws Exception {
        //Given
        given(bookRepository.findById(bookTestId.getId())).willReturn(java.util.Optional.of(bookTestId));

        // When + Then
        mvc.perform(MockMvcRequestBuilders.delete(URL_BOOK+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteABookByIdWithoutAuthentication_thenThrowUnauthorized() throws Exception {
        //Given
        given(bookRepository.findById(bookTestId.getId())).willReturn(java.util.Optional.of(bookTestId));

        // When + Then
        mvc.perform(MockMvcRequestBuilders.delete(URL_BOOK+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}
