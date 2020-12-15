package wolox.training.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
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

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetAllUsers_thenReturnJsonArray() throws Exception {
        //Given
        User userTest = new User(null,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());
        List<User> allUsers = Arrays.asList(userTest);

        given(userRepository.findAll()).willReturn(allUsers);

        //When + Then
        mvc.perform(MockMvcRequestBuilders.get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(userTest.getName())))
                .andExpect(jsonPath("$[0].birthdate", is(userTest.getBirthdate().toString())));
        System.out.println("whenGetAllUsers Passed");
    }

    @Test
    void whenGetAllUsersWithoutAuthentication_thenThrowUnauthorized() throws Exception {
        //Given
        User userTest = new User(null,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());
        List<User> allUsers = Arrays.asList(userTest);

        given(userRepository.findAll()).willReturn(allUsers);

        //When + Then
        mvc.perform(MockMvcRequestBuilders.get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        System.out.println("whenGetAllUsersWithoutAuthentication Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetAnUserByUsername_thenReturnJsonArray() throws Exception {
        //Given
        User userTest = new User(null,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());

        given(userRepository.findByUsername(userTest.getUsername())).willReturn(java.util.Optional.of(userTest));

        //When + Then
        mvc.perform(MockMvcRequestBuilders.get("/api/users?username=Username")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(userTest.getUsername())));
        System.out.println("whenGetAUserByUsername Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetAnUserById_thenReturnJson() throws Exception {
        //Given
        User userTest = new User(1L,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());

        given(userRepository.findById(userTest.getId())).willReturn(java.util.Optional.of(userTest));

        //When + Then
        mvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$.*").exists())
                .andExpect(jsonPath("$.*", hasSize(6)))
                .andExpect(jsonPath("$.id", is(userTest.getId().intValue())))
                .andExpect(jsonPath("$.name", is(userTest.getName())));
        System.out.println("whenGetAnUserById Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetAnUserByIdNonExistent_thenThrowNotFound() throws Exception {
        //Given
        User userTest = new User(1L,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());

        given(userRepository.findById(userTest.getId())).willReturn(java.util.Optional.of(userTest));

        //When + Then
        mvc.perform(MockMvcRequestBuilders.get("/api/users/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        System.out.println("whenGetAnUserByIdNonExistent Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenGetAnUserByUsernameNonExistent_thenThrowNotFound() throws Exception {
        //Given
        User userTest = new User(1L,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());

        given(userRepository.findById(userTest.getId())).willReturn(java.util.Optional.of(userTest));

        //When + Then
        mvc.perform(MockMvcRequestBuilders.get("/api/users?username=NoExist")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        System.out.println("whenGetAnUserByUsernameNonExistent Passed");
    }

    @Test
    void whenAddAnUser_thenReturnOk() throws Exception {
        //Given
        User userTest = new User(1L,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());

        given(userRepository.save(userTest)).willReturn(userTest);

        String json = mapper.writeValueAsString(userTest);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        System.out.println("whenAddAnUser Passed");
    }

    @Test
    void whenAddAnUserWithUsernameAlreadyTaken_thenThrowConflict() throws Exception {
        //Given
        User userTest = new User(1L,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());
        User userTestException = new User(2L,"Username","Francisco", "s3cr3t",LocalDate.parse("2000-08-24"),new ArrayList<>());

        given(userRepository.findByUsername(userTestException.getUsername())).willReturn(java.util.Optional.of(userTest));

        String jsonException = mapper.writeValueAsString(userTestException);

        // When + Then

        mvc.perform(MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonException)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
        System.out.println("whenAddAnUserWithUsernameAlreadyTaken Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenUpdateAnUser_thenReturnOk() throws Exception {
        //Given
        User userTest = new User(1L,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());

        given(userRepository.save(userTest)).willReturn(userTest);

        userTest.setName("Santiago");
        given(userRepository.save(userTest)).willReturn(userTest);
        given(userRepository.findById(userTest.getId())).willReturn(java.util.Optional.of(userTest));

        String json = mapper.writeValueAsString(userTest);

        // When + Then

        mvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        System.out.println("whenUpdateAnUser Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenUpdateAnUserIdMismatch_thenThrowIdMismatched() throws Exception {
        //Given
        User userTest = new User(1L,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());

        given(userRepository.save(userTest)).willReturn(userTest);

        userTest.setName("Santiago");
        given(userRepository.save(userTest)).willReturn(userTest);
        given(userRepository.findById(userTest.getId())).willReturn(java.util.Optional.of(userTest));

        String json = mapper.writeValueAsString(userTest);

        // When + Then

        mvc.perform(MockMvcRequestBuilders.put("/api/users/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        System.out.println("whenUpdateAnUserIdMismatch Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenDeleteAnUserById_thenReturnString() throws Exception {
        //Given
        User userTest = new User(1L,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());

        given(userRepository.findById(userTest.getId())).willReturn(java.util.Optional.of(userTest));

        // When + Then
        mvc.perform(MockMvcRequestBuilders.delete("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        System.out.println("whenDeleteAnUserById Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenAddABookToAnUserLibrary_thenReturnOk() throws Exception {
        //Given
        User userTest = new User(1L,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());
        given(userRepository.findById(userTest.getId())).willReturn(java.util.Optional.of(userTest));

        Book bookTest = new Book(1L,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");
        userTest.addBookToUser(bookTest);
        given(userRepository.save(userTest)).willReturn(userTest);

        String json = mapper.writeValueAsString(userTest);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.post("/api/users/1/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        System.out.println("whenAddABookToAnUserLibrary Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenAddABookAlreadyOwnToAnUserLibrary_thenThrowBookAlreadyOwn() throws Exception {
        //Given
        User userTest = new User(1L,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());
        given(userRepository.findById(userTest.getId())).willReturn(java.util.Optional.of(userTest));

        Book bookTest = new Book(1L,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");
        userTest.addBookToUser(bookTest);
        given(userRepository.save(userTest)).willThrow(BookAlreadyOwnException.class);

        String json = mapper.writeValueAsString(userTest);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.post("/api/users/1/addBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        System.out.println("whenAddABookAlreadyOwnToAnUserLibrary Passed");
    }

    @WithMockUser(value = "iskandar")
    @Test
    void whenRemoveABookFromAnUserLibrary_thenReturnString() throws Exception {
        //Given
        User userTest = new User(1L,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());
        Book bookTest = new Book(1L,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");
        userTest.addBookToUser(bookTest);
        given(userRepository.findById(userTest.getId())).willReturn(java.util.Optional.of(userTest));
        userTest.removeBookToUser(bookTest);
        given(userRepository.save(userTest)).willReturn(userTest);

        String json = mapper.writeValueAsString(userTest);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.delete("/api/users/1/removeBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        System.out.println("whenRemoveABookFromAnUserLibrary Passed");
    }

    @Test
    void whenRemoveABookFromAnUserLibraryWithoutAuthentication_thenThrowUnauthorized() throws Exception {
        //Given
        User userTest = new User(1L,"Username","David", "s3cr3t",LocalDate.parse("2004-09-25"),new ArrayList<>());
        Book bookTest = new Book(1L,"Mystery","Paolo", "image.png", "Sorcerer's Stone", "-", "Salamander","2000", 250,"123456789");
        userTest.addBookToUser(bookTest);
        given(userRepository.findById(userTest.getId())).willReturn(java.util.Optional.of(userTest));
        userTest.removeBookToUser(bookTest);
        given(userRepository.save(userTest)).willReturn(userTest);

        String json = mapper.writeValueAsString(userTest);

        // When + Then
        mvc.perform(MockMvcRequestBuilders.delete("/api/users/1/removeBook")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        System.out.println("whenRemoveABookFromAnUserLibraryWithoutAuthentication Passed");
    }
}
