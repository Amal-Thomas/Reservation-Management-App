package learn.accomodation.domain;

import learn.accomodation.data.DataException;
import learn.accomodation.data.GuestRepository;
import learn.accomodation.data.HostRepository;
import learn.accomodation.data.ReservationRepository;
import learn.accomodation.models.Guest;
import learn.accomodation.models.Host;
import learn.accomodation.models.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {

    private final GuestRepository guestRepository;
    private final HostRepository hostRepository;
    private final ReservationRepository reservationRepository;

    public ReservationService(GuestRepository guestRepository, HostRepository hostRepository, ReservationRepository reservationRepository) {
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findForHost(Host host) {
        return reservationRepository.findForHost(host);
    }

    public List<Reservation> findForGuestAndHost(Guest guest, Host host) {
        return reservationRepository.findForGuestAndHost(guest, host);
    }

    public List<Reservation> findFutureReservations(Guest guest, Host host) {
        return findForGuestAndHost(guest, host).stream()
                .filter(i -> i.getStartDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());
    }

    public Result add(Reservation reservation) throws DataException {
        Result result = validateAdd(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(reservationRepository.add(reservation));
        return result;
    }

    public Result update(Reservation reservation) throws DataException {
        Result result = validateUpdate(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        boolean isUpdated = reservationRepository.update(reservation);
        if (isUpdated) {
            result.setPayload(reservation);
        } else {
            result.addMessage("Reservation not found.");
        }

        return result;
    }

    public Result delete(Reservation reservation) throws DataException {
        Result result = validateDelete(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        boolean isDeleted = reservationRepository.delete(reservation);
        if (!isDeleted) {
            result.addMessage("Reservation not found.");
        }
        return result;
    }

    private Result validateAdd(Reservation reservation) {
        Result result = new Result();

        validateFields(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateChildrenExist(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateStartAndEnd(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateOverlap(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateStartDateInFuture(reservation, result);
        return result;
    }

    private Result validateUpdate(Reservation reservation) {
        Result result = new Result();

        validateFields(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateChildrenExist(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateStartAndEnd(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateOverlap(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateStartDateInFuture(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateFutureReservation(reservation, result);
        return result;
    }

    private Result validateDelete(Reservation reservation) {
        Result result = new Result();
        validateFutureReservation(reservation, result);
        return result;
    }

    private void validateFields(Reservation reservation, Result result) {
        //Guest, host, and start and end dates are required.
        if (reservation.getGuest() == null) {
            result.addMessage("Guest is required.");
        }
        if (reservation.getHost() == null) {
            result.addMessage("Host is required.");
        }
        if (reservation.getStartDate() == null) {
            result.addMessage("Start date is required.");
        }
        if (reservation.getEndDate() == null) {
            result.addMessage("End date is required.");
        }
    }

    private void validateChildrenExist(Reservation reservation, Result result) {
        //The guest and host must already exist in the "database".
        if (reservation.getHost().getEmail() == null
                || hostRepository.findByEmail(reservation.getHost().getEmail()) == null) {
            result.addMessage("Host does not exist.");
        }

        if (reservation.getGuest().getEmail() == null
                || guestRepository.findByEmail(reservation.getGuest().getEmail()) == null) {
            result.addMessage("Guest does not exist.");
        }
    }

    private void validateStartAndEnd(Reservation reservation, Result result) {
        //The start date must come before the end date.
        if (!reservation.getStartDate().isBefore(reservation.getEndDate())) {
            result.addMessage("The start date must come before the end date.");
        }
    }

    private void validateOverlap(Reservation reservation, Result result) {
        //The reservation may never overlap existing reservation dates.
        List<Reservation> reservations = reservationRepository.findForHost(reservation.getHost());

        for (Reservation r : reservations) {
            /* For updating a reservation, overlap should not be checked between the
            current reservation and the new reservation*/
            if (r.getId() == reservation.getId()) {
                continue;
            }

            /* for each existing reservation, both start and end dates should be either before
            the new reservation's start date or after the new reservation's end date */
            if (!((r.getStartDate().isBefore(reservation.getStartDate()) &&
                    r.getEndDate().isBefore(reservation.getStartDate())) ||
                    (r.getStartDate().isAfter(reservation.getEndDate()) &&
                            r.getEndDate().isAfter(reservation.getEndDate()))))
            {
                result.addMessage("The reservation must not overlap existing reservation dates.");
                break;
            }
        }
    }

    private void validateStartDateInFuture(Reservation reservation, Result result) {
        //The start date must be in the future.
        if (!reservation.getStartDate().isAfter(LocalDate.now())) {
            result.addMessage("The reservation start date must be in the future.");
        }
    }

    private void validateFutureReservation(Reservation reservation, Result result) {
        //The reservation to be edited/deleted must be in the future
        List<Reservation> reservations = reservationRepository.findForGuestAndHost(reservation.getGuest(), reservation.getHost());
        for (Reservation r : reservations) {
            if (r.getId() == reservation.getId()) {
                if (!r.getStartDate().isAfter(LocalDate.now())) {
                    result.addMessage("The reservation must be in the future.");
                    break;
                }
            }
        }
    }
}
