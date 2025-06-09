package learn.accomodation.ui;

import learn.accomodation.models.Guest;
import learn.accomodation.models.Host;
import learn.accomodation.models.Reservation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class View {

    private final ConsoleIO io;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getMessage());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }
        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    public Reservation update(Reservation oldReservation) {
        Reservation newReservation = new Reservation();
        displayHeader(String.format(
                "Editing Reservation %d", oldReservation.getId()));
        LocalDate newStartDate = io.readLocalDate(String.format(
                "Start (%s): ", oldReservation.getStartDate().format(formatter)), true);
        LocalDate newEndDate = io.readLocalDate(String.format(
                "End (%s): ", oldReservation.getEndDate().format(formatter)), true);

        if (newStartDate == null && newEndDate == null) {
            return oldReservation;
        } //made no edits

        if (newStartDate != null) {
            newReservation.setStartDate(newStartDate);
        } else {
            newReservation.setStartDate(oldReservation.getStartDate());
        }
        if (newEndDate != null) {
            newReservation.setEndDate(newEndDate);
        } else {
            newReservation.setEndDate(oldReservation.getEndDate());
        }
        newReservation.setHost(oldReservation.getHost());
        newReservation.setGuest(oldReservation.getGuest());
        newReservation.setId(oldReservation.getId());

        if (newReservation.getStartDate().isBefore(newReservation.getEndDate())) {
            newReservation.setTotal(newReservation.calculateTotal());
        }
        displaySummary(newReservation);
        if (io.readBoolean("Is this okay? [y/n]: ")) {
            return newReservation;
        }
        return null;
    }

    public Reservation chooseReservation(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            io.println("No reservations found");
            return null;
        }

        int index = 1;
        for (Reservation reservation : reservations.stream().sorted(Comparator.comparing(Reservation::getStartDate)).toList()) {
            io.printf("%d. ID: %d, %s - %s, Guest: %s, %s, Email: %s%n",
                    index++,
                    reservation.getId(),
                    reservation.getStartDate().format(formatter),
                    reservation.getEndDate().format(formatter),
                    reservation.getGuest().getLastName(),
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getEmail()
            );
        }
        index--;

        io.println("0: Exit");
        String message = String.format("Select a reservation by their index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return reservations.get(index - 1);
    }

    public Reservation makeReservation(Guest guest, Host host) {
        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(io.readLocalDate("Start (MM/dd/yyyy): "));
        reservation.setEndDate(io.readLocalDate("End (MM/dd/yyyy): "));
        if (reservation.getStartDate().isBefore(reservation.getEndDate())) {
            reservation.setTotal(reservation.calculateTotal());
        }

        displaySummary(reservation);
        if (io.readBoolean("Is this okay? [y/n]: ")) {
            return reservation;
        }
        return null;
    }

    public String getHostEmail() {
        return io.readRequiredString("Host Email: ");
    }

    public String getGuestEmail() {
        return io.readRequiredString("Guest Email: ");
    }

    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    // display only
    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayReservations(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            io.println("No reservations found.");
            return;
        }
        for (Reservation reservation : reservations.stream().sorted(Comparator.comparing(Reservation::getStartDate)).toList()) {
            io.printf("ID: %d, %s - %s, Guest: %s, %s, Email: %s%n",
                    reservation.getId(),
                    reservation.getStartDate().format(formatter),
                    reservation.getEndDate().format(formatter),
                    reservation.getGuest().getLastName(),
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getEmail()
            );
        }
    }

    public void displayHostNameAndAddress(Host host) {
        displayHeader(String.format("%s: %s, %s", host.getLastName(), host.getCity(), host.getState()));
    }

    public void displaySummary(Reservation reservation) {
        displayHeader("Summary");
        io.println("Start: " + reservation.getStartDate().format(formatter));
        io.println("End: " + reservation.getEndDate().format(formatter));
        BigDecimal total = reservation.getTotal();
        if (total == null) {
            io.println("Total: ERROR in calculating Total");
        } else if (total.compareTo(new BigDecimal(0)) > 0) {
            io.println("Total: $" + total.setScale(2, RoundingMode.HALF_UP));
        } else {
            io.println("Total: ERROR in calculating Total");
        }
    }
}
