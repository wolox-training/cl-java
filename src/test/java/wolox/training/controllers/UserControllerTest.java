package wolox.training.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static wolox.training.contants.ConstantsTest.AUTHOR_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.GENRE_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.IMAGE_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.ISBN_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.NAME_SECOND_USER_TEST;
import static wolox.training.contants.ConstantsTest.NAME_USER_TEST;
import static wolox.training.contants.ConstantsTest.PAGES_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.PASSWORD_USER_TEST;
import static wolox.training.contants.ConstantsTest.PUBLISHER_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.SUBTITLE_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.TITLE_BOOK_TEST;
import static wolox.training.contants.ConstantsTest.URL_USER;
import static wolox.training.contants.ConstantsTest.USERNAME_USER_TEST;
import static wolox.training.contants.ConstantsTest.YEAR_BOOK_TEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
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
import wolox.training.exceptions.responses.BookAlreadyOwnException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.UserRepository;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserRepository userRepository;

    private User userTest;
    private User userTestId;
    private User userTestException;
    private Book bookTest;

    @BeforeEach
    private void setUp() {
        userTest = new User(null,USERNAME_USER_TEST,NAME_USER_TEST, PASSWORD_USER_TEST,LocalDate.parse("2004-09-25"),new ArrayList<>());

        userTestId = new User(1L,USERNAME_USER_TEST,NAME_USER_TEST, PASSWORD_USER_TEST,LocalDate.parse("2004-09-25"),new ArrayList<>());

        userTestException = new User(2L,USERNAME_USER_TEST,NAME_SECOND_USER_TEST, PASSWORD_USER_TEST,LocalDate.parse("2000-08-24"),new ArrayList<>());

        bookTest = new Book(1L,GENRE_BOOK_TEST,AUTHOR_BOOK_TEST, IMAGE_BOOK_TEST, TITLE_BOOK_TEST, SUBTITLE_BOOK_TEST,
                PUBLISHER_BOOK_TEST,YEAR_BOOK_TEST, PAGES_BOOK_TEST,ISBN_BOOK_TEST);
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetAllUsers_thenReturnJsonArray() throws Exception {
        //Given
        List<User> allUsers = Arrays.asList(userTest);

        given(userRepository.findAll()).willReturn(allUsers);

        //When + Then
        mvc.perform(MockMvcRequestBuilders.get(URL_USER)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(userTest.getName())))
                .andExpect(jsonPath("$[0].birthdate", is(userTest.getBirthdate().toString())));
    }

    @Test
    void whenGetAllUsersWithoutAuthentication_thenThrowUnauthorized() throws Exception {
        //Given
        List<User> allUsers = Arrays.asList(userTest);

        given(userRepository.findAll()).willReturn(allUsers);

        //When + Then
        mvc.perform(MockMvcRequestBuilders.get(URL_USER)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetAnUserByUsername_thenReturnJsonArray() throws Exception {
        //Given
        given(userRepository.findByUsername(userTest.getUsername())).willReturn(java.util.Optional.of(userTest));

        //When + Then
        mvc.perform(MockMvcRequestBuilders.get(URL_USER+"?username="+USERNAME_USER_TEST)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(userTest.getUsername())));
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetAnUserById_thenReturnJson() throws Exception {
        //Given
        given(userRepository.findById(userTestId.getId())).willReturn(java.util.Optional.of(userTestId));

        //When + Then
        mvc.perform(MockMvcRequestBuilders.get(URL_USER+"/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$.*").exists())
                .andExpect(jsonPath("$.*", hasSize(6)))
                .andExpect(jsonPath("$.id", is(userTestId.getId().intValue())))
                .andExpect(jsonPath("$.name", is(userTestId.getName())));
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetAnUserByIdNonExistent_thenThrowNotFound() throws Exception {
        //Given
        given(userRepository.findById(userTestId.getId())).willReturn(java.util.Optional.of(userTestId));

        //When + Then
        mvc.perform(MockMvcRequestBuilders.get(URL_USER+"/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetAnUserByUsernameNonExistent_thenThrowNotFound() throws Exception {
        //Given
        given(userRepository.findById(userTestId.getId())).willReturn(java.util.Optional.of(userTestId));

        //When + Then
        mvc.perform(MockMvcRequestBuilders.get(URL_USER+"?username=NoExist")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenAddAnUser_thenReturnOk() throws Exception {
        //Given
        given(userRepository.save(userTestId)).willReturn(userTestId);

        String json = mapper.writeValueAsString(userTestId);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.post(URL_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenAddAnUserWithUsernameAlreadyTaken_thenThrowConflict() throws Exception {
        //Given
        given(userRepository.findByUsername(userTestException.getUsername())).willReturn(java.util.Optional.of(userTestId));

        String jsonException = mapper.writeValueAsString(userTestException);

        // When + Then

        mvc.perform(MockMvcRequestBuilders.post(URL_USER)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonException)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenUpdateAnUser_thenReturnOk() throws Exception {
        //Given
        given(userRepository.save(userTestId)).willReturn(userTestId);

        userTestId.setName("Santiago");
        given(userRepository.save(userTestId)).willReturn(userTestId);
        given(userRepository.findById(userTestId.getId())).willReturn(java.util.Optional.of(userTestId));

        String json = mapper.writeValueAsString(userTestId);

        // When + Then

        mvc.perform(MockMvcRequestBuilders.put(URL_USER+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenUpdateAnUserIdMismatch_thenThrowIdMismatched() throws Exception {
        //Given
        given(userRepository.save(userTestId)).willReturn(userTestId);

        userTestId.setName("Santiago");
        given(userRepository.save(userTestId)).willReturn(userTestId);
        given(userRepository.findById(userTestId.getId())).willReturn(java.util.Optional.of(userTestId));

        String json = mapper.writeValueAsString(userTestId);

        // When + Then

        mvc.perform(MockMvcRequestBuilders.put(URL_USER+"/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenDeleteAnUserById_thenReturnString() throws Exception {
        //Given
        given(userRepository.findById(userTestId.getId())).willReturn(java.util.Optional.of(userTestId));

        // When + Then
        mvc.perform(MockMvcRequestBuilders.delete(URL_USER+"/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenAddABookToAnUserLibrary_thenReturnOk() throws Exception {
        //Given
        given(userRepository.findById(userTestId.getId())).willReturn(java.util.Optional.of(userTestId));


        userTestId.addBookToUser(bookTest);
        given(userRepository.save(userTestId)).willReturn(userTestId);

        String json = mapper.writeValueAsString(userTestId);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.post(URL_USER+"/1/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenAddABookAlreadyOwnToAnUserLibrary_thenThrowBookAlreadyOwn() throws Exception {
        //Given
        given(userRepository.findById(userTestId.getId())).willReturn(java.util.Optional.of(userTestId));

        userTestId.addBookToUser(bookTest);
        given(userRepository.save(userTestId)).willThrow(BookAlreadyOwnException.class);

        String json = mapper.writeValueAsString(userTestId);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.post(URL_USER+"/1/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenRemoveABookFromAnUserLibrary_thenReturnString() throws Exception {
        //Given
        userTestId.addBookToUser(bookTest);
        given(userRepository.findById(userTestId.getId())).willReturn(java.util.Optional.of(userTestId));
        userTestId.removeBookToUser(bookTest);
        given(userRepository.save(userTestId)).willReturn(userTestId);

        String json = mapper.writeValueAsString(userTestId);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.delete(URL_USER+"/1/removeBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenRemoveABookFromAnUserLibraryWithoutAuthentication_thenThrowUnauthorized() throws Exception {
        //Given
        userTestId.addBookToUser(bookTest);
        given(userRepository.findById(userTestId.getId())).willReturn(java.util.Optional.of(userTestId));
        userTestId.removeBookToUser(bookTest);
        given(userRepository.save(userTestId)).willReturn(userTestId);

        String json = mapper.writeValueAsString(userTestId);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.delete(URL_USER+"/1/removeBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
