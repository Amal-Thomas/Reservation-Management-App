package learn.accomodation.ui;

import learn.accomodation.data.DataException;
import learn.accomodation.domain.GuestService;
import learn.accomodation.domain.HostService;
import learn.accomodation.domain.ReservationService;
import learn.accomodation.domain.Result;
import learn.accomodation.models.Guest;
import learn.accomodation.models.Host;
import learn.accomodation.models.Reservation;

import java.util.List;

public class Controller {

    private final GuestService guestService;
    private final HostService hostService;
    private final ReservationService reservationService;
    private final View view;

    public Controller(GuestService guestService, HostService hostService, ReservationService reservationService, View view) {
        this.guestService = guestService;
        this.hostService = hostService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to Don't Wreck My House");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayException(ex);
        }
        view.displayHeader("Goodbye.");
    }

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_FOR_HOST:
                    viewReservationsForHost();
                    break;
                case MAKE_A_RESERVATION:
                    addReservation();
                    break;
                case EDIT_A_RESERVATION:
                    updateReservation();
                    break;
                case CANCEL_A_RESERVATION:
                    deleteReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    // top level menu
    private void viewReservationsForHost() throws DataException {
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_FOR_HOST.getMessage());
        Host host = getHost();
        view.displayHostNameAndAddress(host);
        List<Reservation> reservations = reservationService.findForHost(host);
        view.displayReservations(reservations);
        view.enterToContinue();
    }

    private void addReservation() throws DataException {
        view.displayHeader(MainMenuOption.MAKE_A_RESERVATION.getMessage());
        Guest guest = getGuest();
        Host host = getHost();

        view.displayHostNameAndAddress(host);
        List<Reservation> reservations = reservationService.findForHost(host);
        view.displayReservations(reservations);
        Reservation reservation = view.makeReservation(guest, host);
        Result result = reservationService.add(reservation);

        if (!result.isSuccess()) {
            view.displayStatus(false, result.getMessages());
        } else {
            String successMessage = String.format("Reservation %s created.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
        view.enterToContinue();
    }

    private void updateReservation() throws DataException {
        view.displayHeader(MainMenuOption.EDIT_A_RESERVATION.getMessage());
        Guest guest = getGuest();
        Host host = getHost();

        view.displayHostNameAndAddress(host);
        List<Reservation> reservations = reservationService.findForGuestAndHost(host, guest);
        Reservation oldReservation = view.chooseReservation(reservations);
        if (oldReservation == null) {
            return;
        }
        Reservation newReservation = view.update(oldReservation);
        Result result = reservationService.update(newReservation);

        if (!result.isSuccess()) {
            view.displayStatus(false, result.getMessages());
        } else {
            String successMessage = String.format("Reservation %s updated.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
        view.enterToContinue();
    }

    private void deleteReservation() throws DataException {
        view.displayHeader(MainMenuOption.CANCEL_A_RESERVATION.getMessage());
        Guest guest = getGuest();
        Host host = getHost();

        view.displayHostNameAndAddress(host);
        List<Reservation> reservations = reservationService.findForGuestAndHost(host, guest);
        Reservation reservation = view.chooseReservation(reservations);
        if (reservation == null) {
            return;
        }
        Result result = reservationService.delete(reservation);

        if (!result.isSuccess()) {
            view.displayStatus(false, result.getMessages());
        } else {
            String successMessage = String.format("Reservation %s deleted.", result.getPayload().getId());
            view.displayStatus(true, successMessage);
        }
        view.enterToContinue();
    }

    // support methods
    private Guest getGuest() {
        String guestEmail = view.getGuestEmail();
        return guestService.findGuestByEmail(guestEmail);
    }

    private Host getHost() {
        String hostEmail = view.getHostEmail();
        return hostService.findHostByEmail(hostEmail);
    }
}
