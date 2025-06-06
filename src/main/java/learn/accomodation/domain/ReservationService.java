package learn.accomodation.domain;

import learn.accomodation.data.ReservationRepository;
import learn.accomodation.models.Guest;
import learn.accomodation.models.Host;
import learn.accomodation.models.Reservation;

import java.time.LocalDate;
import java.util.List;

public class ReservationService {

    private ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public List<Reservation> findForHost(Host host) {
        return repository.findForHost(host);
    }

    public List<Reservation> findForGuestAndHost(Host host, Guest guest) {
        return repository.findForGuestAndHost(host, guest);
    }

    public Result add(Reservation reservation) {
        Result result = validateAddition(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(repository.add(reservation));
        return result;
    }

    public Result update(Reservation reservation) {
        Result result = validateUpdation(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        result.setPayload(repository.update(reservation));
        return result;
    }

    public Result delete(Reservation reservation) {
        Result result = validateDeletion(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        boolean isDeleted = repository.delete(reservation);
        if (!isDeleted) {
            result.addMessage("Reservation not found.");
        }
        return result;
    }

    private Result validateAddition(Reservation reservation) {
        Result result = new Result();
        validateFields(reservation, result); //Guest, host, and start and end dates are required.
        validateStartAndEnd(reservation, result); //The start date must come before the end date.
        validateOverlap(reservation, result); //The reservation may never overlap existing reservation dates.
        validateStartDateInFuture(reservation, result); //The start date must be in the future.
        return result;
    }

    private Result validateUpdation(Reservation reservation) {
        Result result = new Result();
        validateFields(reservation, result); //Guest, host, and start and end dates are required.
        validateStartAndEnd(reservation, result); //The start date must come before the end date.
        validateOverlap(reservation, result); //The reservation may never overlap existing reservation dates.
        return result;
    }

    private Result validateDeletion(Reservation reservation) {
        Result result = new Result();
        validateStartDateInFuture(reservation, result); //The start date must be in the future.
        return result;
    }

    private void validateFields(Reservation reservation, Result result) {
        if (reservation.guest == null) {
            result.addMessage("Guest is required.");
        }
        if (reservation.host == null) {
            result.addMessage("Host is required.");
        }
        if (reservation.startDate == null) {
            result.addMessage("Start date is required.");
        }
        if (reservation.endDate == null) {
            result.addMessage("End date is required.");
        }
    }

    private void validateStartAndEnd(Reservation reservation, Result result) {
        if (reservation.getEndDate().isBefore(reservation.getStartDate())) {
            result.addMessage("Start date must be before end date.");
        }
    }

    private void validateOverlap(Reservation reservation, Result result) {
        List<Reservation> reservations = repository.findForHost(reservation.getHost());
        boolean isOverlap = false;
        for (Reservation r: reservations) {
                if ((reservation.getStartDate().isAfter(r.getStartDate()) &&
                        reservation.getStartDate().isBefore(r.getEndDate())) ||
                        (reservation.getEndDate().isAfter(r.getStartDate()) &&
                                reservation.getEndDate().isBefore(r.getEndDate())) ||
                        reservation.getStartDate().isEqual(r.getStartDate()) ||
                        reservation.getEndDate().isEqual(r.getEndDate())
                ) {
                    isOverlap = true;
                }
                if ((r.getStartDate().isAfter(reservation.getStartDate()) &&
                        r.getStartDate().isBefore(reservation.getEndDate())) ||
                        (r.getEndDate().isAfter(reservation.getStartDate()) &&
                                r.getEndDate().isBefore(reservation.getEndDate())) ||
                        r.getStartDate().isEqual(reservation.getStartDate()) ||
                        r.getEndDate().isEqual(reservation.getEndDate())
                ) {
                    isOverlap = true;
                }
            if (isOverlap) {
                result.addMessage("The reservation must not overlap existing reservation dates.");
            }
        }
    }

    private void validateStartDateInFuture(Reservation reservation, Result result) {
        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addMessage("Reservation start date cannot be in the past.");
        }
    }
}
