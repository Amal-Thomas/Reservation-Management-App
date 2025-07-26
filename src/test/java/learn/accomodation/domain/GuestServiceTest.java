package learn.accomodation.domain;

import learn.accomodation.data.GuestRepositoryDouble;
import learn.accomodation.data.HostRepositoryDouble;
import learn.accomodation.models.Guest;
import learn.accomodation.models.Host;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {

    GuestService service = new GuestService(new GuestRepositoryDouble());

    @Test
    void shouldFindByEmail() {
        Guest actual = service.findByEmail("slomas0@mediafire.com");
        Guest expected = GuestRepositoryDouble.GUEST;
        assertEquals(actual, expected);
    }

    @Test
    void shouldNotFindByEmail() {
        Guest actual = service.findByEmail("somas0@mediafire.com");
        Guest expected = GuestRepositoryDouble.GUEST;
        assertNotEquals(actual, expected);
    }
}