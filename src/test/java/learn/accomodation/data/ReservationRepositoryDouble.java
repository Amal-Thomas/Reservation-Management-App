package learn.accomodation.data;

import learn.accomodation.models.Guest;
import learn.accomodation.models.Host;
import learn.accomodation.models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepositoryDouble implements ReservationRepository {

    public final static ArrayList<Reservation> RESERVATIONS = makeReservations();
    public final static Reservation RESERVATIONPAST = new Reservation(1,
            HostRepositoryDouble.HOST,
            GuestRepositoryDouble.GUEST,
            LocalDate.of(2025, 6, 1),
            LocalDate.of(2025, 6, 7),
            new BigDecimal(2125));
    public final static Reservation RESERVATIONFUTURE = new Reservation(2,
            HostRepositoryDouble.HOST,
            GuestRepositoryDouble.GUEST,
            LocalDate.of(2026, 6, 1),
            LocalDate.of(2026, 6, 7),
            new BigDecimal(2210));

    private static ArrayList<Reservation> makeReservations() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        reservations.add(RESERVATIONPAST);
        reservations.add(RESERVATIONFUTURE);
        return reservations;
    }

    @Override
    public List<Reservation> findForHost(Host host) {
        return RESERVATIONS;
    }

    @Override
    public List<Reservation> findForGuestAndHost(Guest guest, Host host) {
        return RESERVATIONS;
    }

    @Override
    public Reservation add(Reservation reservation) throws DataException {
        return reservation;
    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        return reservation.getId() == 2;
    }

    @Override
    public boolean delete(Reservation reservation) throws DataException {
        return reservation.getId() == 2;
    }
}
