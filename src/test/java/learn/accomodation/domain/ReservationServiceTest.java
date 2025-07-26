package learn.accomodation.domain;

import learn.accomodation.data.DataException;
import learn.accomodation.data.GuestRepositoryDouble;
import learn.accomodation.data.HostRepositoryDouble;
import learn.accomodation.data.ReservationRepositoryDouble;
import learn.accomodation.models.Guest;
import learn.accomodation.models.Host;
import learn.accomodation.models.Reservation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {

    ReservationService service = new ReservationService(
            new GuestRepositoryDouble(),
            new HostRepositoryDouble(),
            new ReservationRepositoryDouble());

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
        assertTrue(result.getMessages().contains("Guest is required."));
    }



    @Test
    void shouldNotAddNullHost() throws DataException{
        Reservation reservation = new Reservation(1,
                null,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Host is required."));
    }

    @Test
    void shouldNotAddNullStartDate() throws DataException{
        Reservation reservation = new Reservation(1,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                null,
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Start date is required."));
    }

    @Test
    void shouldNotAddNullEndDate() throws DataException{
        Reservation reservation = new Reservation(1,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2025, 7, 1),
                null,
                new BigDecimal(680));
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("End date is required."));
    }

    @Test
    void shouldNotAddGuestWithNullId() throws DataException {
        Guest guest = new Guest(
                0,
                "Sullivan",
                "Lomas",
                "slomas0@mediafire.com",
                "(702) 7768761",
                "NV");
        Reservation reservation = new Reservation(3,
                HostRepositoryDouble.HOST,
                guest,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Guest does not exist."));
    }

    @Test
    void shouldNotAddGuestWhichDoesNotExist() throws DataException {
        Guest guest = new Guest(
                1000,
                "Sullivan",
                "Lomas",
                "slomas0@mediafire.com",
                "(702) 7768761",
                "NV");
        Reservation reservation = new Reservation(3,
                HostRepositoryDouble.HOST,
                guest,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Guest does not exist."));
    }

    @Test
    void shouldNotAddHostWithNullId() throws DataException{
        Host host = new Host(
                null,
                "Yearnes",
                "eyearnes0@sfgate.com",
                "(806) 1783815",
                "3 Nova Trail",
                "Amarillo",
                "TX",
                "79182",
                new BigDecimal("340"),
                new BigDecimal("425"));
        Reservation reservation = new Reservation(3,
                host,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Host does not exist."));
    }

    @Test
    void shouldNotAddHostWhichDoesNotExist() throws DataException{
        Host host = new Host(
                java.util.UUID.randomUUID().toString(),
                "Yearnes",
                "eyearnes0@sfgate.com",
                "(806) 1783815",
                "3 Nova Trail",
                "Amarillo",
                "TX",
                "79182",
                new BigDecimal("340"),
                new BigDecimal("425"));
        Reservation reservation = new Reservation(3,
                host,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Host does not exist."));
    }

    @Test
    void shouldNotAddInvalidDates() throws DataException{
        Reservation reservation = new Reservation(3,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2025, 7, 3),
                LocalDate.of(2025, 7, 1),
                null);
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The start date must come before the end date."));
    }

    @Test
    void shouldNotAddWhenStartDateInsideExistingReservation() throws DataException{
        Reservation reservation = new Reservation(3,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2026, 6, 4),
                LocalDate.of(2026, 6, 9),
                null);
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The reservation must not overlap existing reservation dates."));
    }

    @Test
    void shouldNotAddWhenEndDateInsideExistingReservation() throws DataException{
        Reservation reservation = new Reservation(3,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2026, 5, 4),
                LocalDate.of(2026, 6, 4),
                null);
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The reservation must not overlap existing reservation dates."));
    }

    @Test
    void shouldNotAddWhenDatesSurroundExistingReservation() throws DataException{
        Reservation reservation = new Reservation(3,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2026, 5, 4),
                LocalDate.of(2026, 7, 4),
                null);
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The reservation must not overlap existing reservation dates."));
    }

    @Test
    void shouldNotAddWhenDatesInsideExistingReservation() throws DataException{
        Reservation reservation = new Reservation(3,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2026, 6, 3),
                LocalDate.of(2026, 6, 4),
                null);
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        System.out.println(result.getMessages().get(0));
        assertTrue(result.getMessages().contains("The reservation must not overlap existing reservation dates."));
    }

    @Test
    void shouldAddWhenDatesBeforeExistingReservation() throws DataException{
        Reservation reservation = new Reservation(3,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2026, 5, 3),
                LocalDate.of(2026, 5, 4),
                null);
        Result result = service.add(reservation);
        assertTrue(result.isSuccess());
        assertEquals(result.getPayload(), reservation);
    }

    @Test
    void shouldAddWhenDatesAfterExistingReservation() throws DataException{
        Reservation reservation = new Reservation(3,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2026, 7, 3),
                LocalDate.of(2026, 7, 4),
                null);
        Result result = service.add(reservation);
        assertTrue(result.isSuccess());
        assertEquals(result.getPayload(), reservation);
    }

    @Test
    void shouldNotAddWhenStartDateIsInPast() throws DataException{
        Reservation reservation = new Reservation(3,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2024, 7, 3),
                LocalDate.of(2024, 7, 4),
                null);
        Result result = service.add(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The reservation start date must be in the future."));
    }

    @Test
    void shouldNotUpdateNullGuest() throws DataException {
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                null,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Guest is required."));
    }



    @Test
    void shouldNotUpdateNullHost() throws DataException{
        Reservation reservation = new Reservation(2,
                null,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Host is required."));
    }

    @Test
    void shouldNotUpdateNullStartDate() throws DataException{
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                null,
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Start date is required."));
    }

    @Test
    void shouldNotUpdateNullEndDate() throws DataException{
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2025, 7, 1),
                null,
                new BigDecimal(680));
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("End date is required."));
    }

    @Test
    void shouldNotUpdateGuestWithNullId() throws DataException {
        Guest guest = new Guest(
                0,
                "Sullivan",
                "Lomas",
                "slomas0@mediafire.com",
                "(702) 7768761",
                "NV");
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                guest,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Guest does not exist."));
    }

    @Test
    void shouldNotUpdateGuestWhichDoesNotExist() throws DataException {
        Guest guest = new Guest(
                1000,
                "Sullivan",
                "Lomas",
                "slomas0@mediafire.com",
                "(702) 7768761",
                "NV");
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                guest,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Guest does not exist."));
    }

    @Test
    void shouldNotUpdateHostWithNullId() throws DataException{
        Host host = new Host(
                null,
                "Yearnes",
                "eyearnes0@sfgate.com",
                "(806) 1783815",
                "3 Nova Trail",
                "Amarillo",
                "TX",
                "79182",
                new BigDecimal("340"),
                new BigDecimal("425"));
        Reservation reservation = new Reservation(2,
                host,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Host does not exist."));
    }

    @Test
    void shouldNotUpdateHostWhichDoesNotExist() throws DataException{
        Host host = new Host(
                java.util.UUID.randomUUID().toString(),
                "Yearnes",
                "eyearnes0@sfgate.com",
                "(806) 1783815",
                "3 Nova Trail",
                "Amarillo",
                "TX",
                "79182",
                new BigDecimal("340"),
                new BigDecimal("425"));
        Reservation reservation = new Reservation(2,
                host,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2025, 7, 1),
                LocalDate.of(2025, 7, 3),
                new BigDecimal(680));
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("Host does not exist."));
    }

    @Test
    void shouldNotUpdateInvalidDates() throws DataException{
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2025, 7, 3),
                LocalDate.of(2025, 7, 1),
                null);
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The start date must come before the end date."));
    }

    @Test
    void shouldNotUpdateWhenStartDateInsideExistingReservation() throws DataException{
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2029, 6, 4),
                LocalDate.of(2029, 6, 9),
                null);
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The reservation must not overlap existing reservation dates."));
    }

    @Test
    void shouldNotUpdateWhenEndDateInsideExistingReservation() throws DataException{
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2029, 5, 4),
                LocalDate.of(2029, 6, 4),
                null);
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The reservation must not overlap existing reservation dates."));
    }

    @Test
    void shouldNotUpdateWhenDatesSurroundExistingReservation() throws DataException{
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2029, 5, 4),
                LocalDate.of(2029, 7, 4),
                null);
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The reservation must not overlap existing reservation dates."));
    }

    @Test
    void shouldNotUpdateWhenDatesInsideExistingReservation() throws DataException{
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2029, 6, 3),
                LocalDate.of(2029, 6, 4),
                null);
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The reservation must not overlap existing reservation dates."));
    }

    @Test
    void shouldUpdateWhenDatesBeforeExistingReservation() throws DataException{
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2029, 5, 3),
                LocalDate.of(2029, 5, 4),
                null);
        Result result = service.update(reservation);
        assertTrue(result.isSuccess());
        assertEquals(result.getPayload(), reservation);
    }

    @Test
    void shouldUpdateWhenDatesAfterExistingReservation() throws DataException{
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2029, 7, 3),
                LocalDate.of(2029, 7, 4),
                null);
        Result result = service.update(reservation);
        assertTrue(result.isSuccess());
        assertEquals(result.getPayload(), reservation);
    }

    @Test
    void shouldNotUpdateWhenStartDateIsInPast() throws DataException{
        Reservation reservation = new Reservation(2,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2024, 7, 3),
                LocalDate.of(2024, 7, 4),
                null);
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The reservation start date must be in the future."));
    }

    @Test
    void shouldNotUpdatePastReservation() throws DataException{
        Reservation reservation = new Reservation(1,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2030, 7, 3),
                LocalDate.of(2030, 7, 4),
                null);
        Result result = service.update(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The reservation must be in the future."));
    }

    @Test
    void shouldNotDeletePastReservation() throws DataException{
        Reservation reservation = new Reservation(1,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 7),
                new BigDecimal(2125));
        Result result = service.delete(reservation);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessages().contains("The reservation must be in the future."));
    }

    @Test
    void shouldDelete()  throws DataException{
        Reservation reservation = new Reservation(3,
                HostRepositoryDouble.HOST,
                GuestRepositoryDouble.GUEST,
                LocalDate.of(2029, 6, 1),
                LocalDate.of(2029, 6, 7),
                new BigDecimal(2210));
        Result result = service.delete(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldFindForHost() {
        List<Reservation> reservations = service.findForHost(HostRepositoryDouble.HOST);
        assertEquals(3, reservations.size());
    }

    @Test
    void shouldFindForGuestAndHost() {
        List<Reservation> reservations = service.findForGuestAndHost(GuestRepositoryDouble.GUEST, HostRepositoryDouble.HOST);
        assertEquals(3, reservations.size());
    }

    @Test
    void shouldFindFutureReservations() {
        List<Reservation> reservations = service.findFutureReservations(GuestRepositoryDouble.GUEST, HostRepositoryDouble.HOST);
        assertEquals(2, reservations.size());
    }
}