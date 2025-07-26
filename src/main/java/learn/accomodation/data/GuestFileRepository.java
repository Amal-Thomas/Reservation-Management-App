package learn.accomodation.data;

import learn.accomodation.models.Guest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GuestFileRepository implements GuestRepository {
    private static final String DELIMITER = ",";
    private final String filePath;

    public GuestFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Guest findByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Guest guest = deserialize(line);
                if (guest != null) {
                    if (guest.getEmail().equalsIgnoreCase(email)) {
                        return guest;
                    }
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return null;
    }

    @Override
    public Guest findById(int id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Guest guest = deserialize(line);
                if (guest != null) {
                    if (guest.getGuestId() == id) {
                        return guest;
                    }
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return null;
    }

    private Guest deserialize(String line) {
        String[] fields = line.split(DELIMITER, -1);
        if (fields.length == 6) {
            Guest guest = new Guest();
            guest.setGuestId(Integer.parseInt(fields[0]));
            guest.setFirstName(fields[1]);
            guest.setLastName(fields[2]);
            guest.setEmail(fields[3]);
            guest.setPhone(fields[4]);
            guest.setState(fields[5]);
            return guest;
        }
        return null;
    }
}