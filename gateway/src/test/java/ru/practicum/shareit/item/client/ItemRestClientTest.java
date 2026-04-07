package ru.practicum.shareit.item.client;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(properties = "shareit-server=http://localhost:9090")
class ItemRestClientTest {

    private static final String HEADER = "X-Sharer-User-Id";
    private ItemRestClient client;
    @Autowired
    @Qualifier("ItemClient")
    private RestClient restClient;
    @Autowired
    private ObjectMapper mapper;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = restClient.mutate();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        client = new ItemRestClient(builder.build());
    }

    @Test
    void getAllItemsByOwner_whenValid_thenReturnsList() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/items"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(List.of(new ItemDto())), MediaType.APPLICATION_JSON));

        List<ItemDto> actual = client.getAllItemsByOwner(1L);
        assertEquals(1, actual.size());
    }

    @Test
    void getItemById_whenValid_thenReturnsItem() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/items/2"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(new ItemDto()), MediaType.APPLICATION_JSON));

        ItemDto actual = client.getItemById(2L, 1L);
        assertEquals(new ItemDto(), actual);
    }

    @Test
    void searchAvailableItems_whenValid_thenReturnsList() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/items/search?text=query"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mapper.writeValueAsString(List.of()), MediaType.APPLICATION_JSON));

        List<ItemDto> actual = client.searchAvailableItems("query");
        assertTrue(actual.isEmpty());
    }

    @Test
    void createItem_whenValid_thenReturnsItem() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/items"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(new ItemDto()), MediaType.APPLICATION_JSON));

        ItemDto actual = client.createItem(1L, new ItemDto());
        assertEquals(new ItemDto(), actual);
    }

    @Test
    void createComment_whenValid_thenReturnsComment() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/items/2/comment"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(new CommentDto()), MediaType.APPLICATION_JSON));

        CommentDto actual = client.createComment(1L, 2L, new CommentDto());
        assertEquals(new CommentDto(), actual);
    }

    @Test
    void updateItem_whenValid_thenReturnsItem() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/items/2"))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(new ItemDto()), MediaType.APPLICATION_JSON));

        ItemDto actual = client.updateItem(1L, 2L, new ItemDto());
        assertEquals(new ItemDto(), actual);
    }

    @Test
    void deleteItem_whenValid_thenSuccess() {
        mockServer.expect(requestTo("http://localhost:9090/items/2"))
                .andExpect(method(HttpMethod.DELETE))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess());

        assertDoesNotThrow(() -> client.deleteItem(1L, 2L));
    }
}