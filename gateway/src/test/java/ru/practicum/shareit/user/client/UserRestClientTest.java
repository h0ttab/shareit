package ru.practicum.shareit.user.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.exception.ServerException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(properties = "shareit-server=http://localhost:9090")
class UserRestClientTest {

    private UserRestClient userClient;

    @Autowired
    @Qualifier("UserClient")
    private RestClient restClient;

    @Autowired
    private ObjectMapper mapper;

    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = restClient.mutate();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        userClient = new UserRestClient(builder.build());
    }

    @Test
    void getUserById_whenValid_thenReturnUser() throws Exception {
        UserDto expected = new UserDto(1L, "Ivan", "ivan@mail.com");

        mockServer.expect(requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mapper.writeValueAsString(expected), MediaType.APPLICATION_JSON));

        UserDto actual = userClient.getUserById(1L);
        assertEquals(expected, actual);
    }

    @Test
    void createUser_whenValid_thenReturnUser() throws Exception {
        UserDto expected = new UserDto(1L, "Ivan", "ivan@mail.com");

        mockServer.expect(requestTo("http://localhost:9090/users"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(mapper.writeValueAsString(expected), MediaType.APPLICATION_JSON));

        UserDto actual = userClient.createUser(new UserDto(null, "Ivan", "ivan@mail.com"));
        assertEquals(expected, actual);
    }

    @Test
    void updateUser_whenValid_thenReturnUser() throws Exception {
        UserDto expected = new UserDto(1L, "Ivan Updated", "ivan@mail.com");

        mockServer.expect(requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withSuccess(mapper.writeValueAsString(expected), MediaType.APPLICATION_JSON));

        UserDto actual = userClient.updateUser(expected, 1L);
        assertEquals(expected, actual);
    }

    @Test
    void deleteUser_whenValid_thenSuccess() {
        mockServer.expect(requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        assertDoesNotThrow(() -> userClient.deleteUser(1L));
    }

    @Test
    void getUserById_whenError_thenThrowsServerException() {
        String errorJson = "{\"statusCode\": 404, \"error\": \"User not found\"}";

        mockServer.expect(requestTo("http://localhost:9090/users/1"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(errorJson));

        ServerException exception = assertThrows(ServerException.class, () -> userClient.getUserById(1L));
        assertEquals(404, exception.getStatus());
        assertEquals("User not found", exception.getBody());
    }
}