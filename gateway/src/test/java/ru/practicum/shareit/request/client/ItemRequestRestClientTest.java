package ru.practicum.shareit.request.client;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import ru.practicum.shareit.request.dto.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(properties = "shareit-server=http://localhost:9090")
class ItemRequestRestClientTest {

    private static final String HEADER = "X-Sharer-User-Id";
    private ItemRequestRestClient client;
    @Autowired
    @Qualifier("RequestsClient")
    private RestClient restClient;
    @Autowired
    private ObjectMapper mapper;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = restClient.mutate();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        client = new ItemRequestRestClient(builder.build());
    }

    @Test
    void createRequest_whenValid_thenReturnDto() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/requests"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(new ItemRequestFullDto()), MediaType.APPLICATION_JSON));

        ItemRequestFullDto actual = client.createRequest(new ItemRequestCreateDto("Test"), 1L);
        assertEquals(new ItemRequestFullDto(), actual);
    }

    @Test
    void getRequestsByRequestorId_whenValid_thenReturnList() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/requests"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(List.of()), MediaType.APPLICATION_JSON));

        List<ItemRequestFullDto> actual = client.getRequestsByRequestorId(1L);
        assertEquals(0, actual.size());
    }

    @Test
    void getAllRequests_whenValid_thenReturnList() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/requests/all"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(List.of()), MediaType.APPLICATION_JSON));

        List<ItemRequestLightDto> actual = client.getAllRequests(1L);
        assertEquals(0, actual.size());
    }

    @Test
    void getRequestById_whenValid_thenReturnDto() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/requests/2"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(new ItemRequestFullDto()), MediaType.APPLICATION_JSON));

        ItemRequestFullDto actual = client.getRequestById(2L, 1L);
        assertEquals(new ItemRequestFullDto(), actual);
    }
}