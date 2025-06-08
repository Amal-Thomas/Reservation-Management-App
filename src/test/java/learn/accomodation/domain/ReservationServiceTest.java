package learn.accomodation.domain;

import learn.accomodation.data.DataException;
import learn.accomodation.data.GuestRepositoryDouble;
import learn.accomodation.data.HostRepositoryDouble;
import learn.accomodation.data.ReservationRepositoryDouble;
import learn.accomodation.models.Reservation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service = new ReservationService(
            new GuestRepositoryDouble(),
            new HostRepositoryDouble(),
            new ReservationRepositoryDouble());

    @Test
    void findForHost() {
    }

    @Test
    void findForGuestAndHost() {
    }

    @Test
    void findFutureReservations() {
    }

    @Test
    void shouldNotAddNullGuest() throws DataException {
        Reservation reservation = new Reservation(1,
                HostRepositoryDouble.HOST,
                null,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertEquals(result.getMessages().size(), 1); //Just this error
        assertTrue(result.getMessages().contains("Guest is required."));
    }

    @Test
    void shouldNotAddNullHost() {

    }

    @Test
    void shouldNotAddNullStartDate() {

    }

    @Test
    void shouldNotAddNullEndDate() {

    }

    @Test
    void add() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}