package ru.practicum.shareit.booking.client;

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
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingReturnDto;
import ru.practicum.shareit.booking.model.BookingState;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(properties = "shareit-server=http://localhost:9090")
class BookingRestClientTest {

    private static final String HEADER = "X-Sharer-User-Id";
    private BookingClient client;
    private final RestClient restClient;
    private final ObjectMapper mapper;
    private MockRestServiceServer mockServer;

    public BookingRestClientTest(@Autowired @Qualifier("BookingsClient") RestClient client,
                                 @Autowired ObjectMapper mapper) {
        this.restClient = client;
        this.mapper = mapper;
    }

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = restClient.mutate();
        mockServer = MockRestServiceServer.bindTo(builder).build();
        client = new BookingRestClient(builder.build());
    }

    @Test
    void createBooking_whenValid_thenReturnBooking() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/bookings"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(new BookingReturnDto()), MediaType.APPLICATION_JSON));

        BookingReturnDto actual = client.createBooking(1L, new BookingCreateDto());
        assertEquals(new BookingReturnDto(), actual);
    }

    @Test
    void approveBooking_whenValid_thenReturnBooking() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/bookings/2?approved=true"))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(new BookingReturnDto()), MediaType.APPLICATION_JSON));

        BookingReturnDto actual = client.approveBooking(2L, true, 1L);
        assertEquals(new BookingReturnDto(), actual);
    }

    @Test
    void getBookingById_whenValid_thenReturnBooking() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/bookings/2"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(new BookingReturnDto()), MediaType.APPLICATION_JSON));

        BookingReturnDto actual = client.getBookingById(2L, 1L);
        assertEquals(new BookingReturnDto(), actual);
    }

    @Test
    void getBookingsByBooker_whenValid_thenReturnList() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/bookings?state=ALL"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(List.of()), MediaType.APPLICATION_JSON));

        List<BookingReturnDto> actual = client.getBookingsByBooker(BookingState.ALL, 1L);
        assertEquals(0, actual.size());
    }

    @Test
    void getBookingsByOwner_whenValid_thenReturnList() throws Exception {
        mockServer.expect(requestTo("http://localhost:9090/bookings/owner?state=ALL"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HEADER, "1"))
                .andRespond(withSuccess(mapper.writeValueAsString(List.of()), MediaType.APPLICATION_JSON));

        List<BookingReturnDto> actual = client.getBookingsByOwner(BookingState.ALL, 1L);
        assertEquals(0, actual.size());
    }
}