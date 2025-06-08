package learn.accomodation.data;

import learn.accomodation.models.Guest;
import learn.accomodation.models.Host;
import learn.accomodation.models.Reservation;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationFileRepository implements ReservationRepository{
    private static final String DELIMITER = ",";
    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;

    public ReservationFileRepository(String directory) {
        this.directory = directory;
    }

    @Override
    public List<Reservation> findForHost(Host host) {

        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(host.getHostId())))) {
            reader.readLine(); // read header
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Reservation reservation = deserialize(line, host.getHostId());
                if (reservation != null) {
                    result.add(reservation);
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return result;
    }

    public List<Reservation> findForGuestAndHost(Host host, Guest guest) {
        ArrayList<Reservation> reservationsForHost = new ArrayList<>(findForHost(host));
        ArrayList<Reservation> result = new ArrayList<>();
        for (Reservation reservation: reservationsForHost) {
            if (reservation.getGuest().getGuestId() == guest.getGuestId()) {
                result.add(reservation);
            }
        }
        return result;
    }

    public Reservation add(Reservation reservation) {
        List<Reservation> all = findForHost(reservation.getHost());
        reservation.setId(getNextId(all));
        all.add(reservation);
        writeAll(all, reservation.getHost().getHostId());
        return reservation;
    }

    public boolean update(Reservation reservation) throws DataException{
        List<Reservation> all = findForHost(reservation.getHost());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == reservation.getId()) {
                all.set(i, reservation);
                writeAll(all, reservation.getHost().getHostId());
                return true;
            }
        }
        return false;
    }

    public boolean delete(Reservation reservation) throws DataException {
        List<Reservation> all = findForHost(reservation.getHost());
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId() == reservation.getId) {
                all.remove(i);
                writeAll(all, reservation.getHost().getHostId());
                return true;
            }
        }
        return false;
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuest().getGuestId(),
                reservation.getTotal().toString());
    }

    private Reservation deserialize(String line, String hostId) {
        String[] fields = line.split(DELIMITER, -1);
        if (fields.length == 5) {
            Reservation reservation = new Reservation();
            reservation.setId(fields[0]);
            reservation.setStartDate(LocalDate.parse(fields[1]));
            reservation.setEndDate(LocalDate.parse(fields[2]));
            reservation.setTotal(new BigDecimal(fields[4]));

            Host host = new Host();
            host.setHostId(hostId);
            reservation.setHost(host);

            Guest guest = new Guest();
            guest.setGuestId(Integer.parseInt(fields[3]));
            reservation.setGuest(guest);
            return reservation;
        }
        return null;
    }

    private void writeAll(List<Reservation> reservations, String hostId) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(hostId))) {
            writer.println(HEADER);
            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private int getNextId(List<Reservation> reservations) {
        int nextId = 0;
        for (Reservation reservation : reservations) {
            nextId = Math.max(nextId, reservation.getId());
        }
        return nextId + 1;
    }

    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }
}
