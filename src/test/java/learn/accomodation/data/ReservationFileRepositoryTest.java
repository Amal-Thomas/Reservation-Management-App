package learn.accomodation.data;

import learn.accomodation.models.Guest;
import learn.accomodation.models.Host;
import learn.accomodation.models.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepositoryTest {

    static final String SEED_FILE_PATH = "./data/reservations-seed-9d469342-ad0b-4f5a-8d28-e81e690ba29a.csv";
    static final String TEST_FILE_PATH = "./data/reservations_test/9d469342-ad0b-4f5a-8d28-e81e690ba29a.csv";
    static final String TEST_DIR_PATH = "./data/reservations_test";
    static final int FORAGE_COUNT = 54;

    final LocalDate date = LocalDate.of(2020, 6, 26);

    ReservationFileRepository repository = new ReservationFileRepository(TEST_DIR_PATH);

    @BeforeEach
    void setup() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void findForHost() {
        Host host = new Host();
        host.setHostId("9d469342-ad0b-4f5a-8d28-e81e690ba29a");
assertNotNull(repository.findForHost(host));
    }

    @Test
    void add() throws DataException{
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2025, 7, 1));
        reservation.setEndDate(LocalDate.of(2025, 7, 3));

        Host host = new Host();
        host.setHostId("9d469342-ad0b-4f5a-8d28-e81e690ba29a");
        reservation.setHost(host);

        Guest guest = new Guest();
        guest.setGuestId(7);
        reservation.setGuest(guest);
        reservation.setTotal(new BigDecimal(400));

        reservation = repository.add(reservation);

        assertEquals(2, reservation.getId());
    }

    @Test
    void update() throws DataException{
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2029, 6, 1));
        reservation.setEndDate(LocalDate.of(2029, 7, 3));

        Host host = new Host();
        host.setHostId("9d469342-ad0b-4f5a-8d28-e81e690ba29a");
        reservation.setHost(host);

        Guest guest = new Guest();
        guest.setGuestId(18);
        reservation.setGuest(guest);
        reservation.setTotal(new BigDecimal(400));
        reservation.setId(1);

        assertTrue(repository.update(reservation));
    }

    @Test
    void delete() throws DataException{
        Reservation reservation = new Reservation();

        Host host = new Host();
        host.setHostId("9d469342-ad0b-4f5a-8d28-e81e690ba29a");
        reservation.setHost(host);

        Guest guest = new Guest();
        guest.setGuestId(18);
        reservation.setGuest(guest);
        reservation.setTotal(new BigDecimal(400));
        reservation.setId(1);

        assertTrue(repository.delete(reservation));
    }
}