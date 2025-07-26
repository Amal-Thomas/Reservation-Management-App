package learn.accomodation.domain;

import learn.accomodation.data.GuestRepositoryDouble;
import learn.accomodation.data.HostRepositoryDouble;
import learn.accomodation.data.ReservationRepositoryDouble;
import learn.accomodation.models.Host;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {

    HostService service = new HostService(new HostRepositoryDouble());

    @Test
    void shouldFindByEmail() {
        Host actual = service.findByEmail("eyearnes0@sfgate.com");
        Host expected = HostRepositoryDouble.HOST;
        assertEquals(actual, expected);
    }

    @Test
    void shouldNotFindByEmail() {
        Host actual = service.findByEmail("yearnes0@sfgate.com");
        Host expected = HostRepositoryDouble.HOST;
        assertNotEquals(actual, expected);
    }
}