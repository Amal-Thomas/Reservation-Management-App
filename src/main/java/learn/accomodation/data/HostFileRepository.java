package learn.accomodation.data;

import learn.accomodation.models.Guest;
import learn.accomodation.models.Host;
import learn.accomodation.models.Reservation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class HostFileRepository implements HostRepository {
    private static final String DELIMITER = ",";
    private final String filePath;

    public HostFileRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Host findByEmail(String email) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Host host = deserialize(line);
                if (host != null) {
                    if (host.getEmail().equalsIgnoreCase(email)) {
                        return host;
                    }
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return null;
    }

    @Override
    public Host findById(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            reader.readLine(); // read header

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                Host host = deserialize(line);
                if (host != null) {
                    if (host.getHostId().equalsIgnoreCase(id)) {
                        return host;
                    }
                }
            }
        } catch (IOException ex) {
            // don't throw on read
        }
        return null;
    }

    private Host deserialize(String line) {
        String[] fields = line.split(DELIMITER, -1);
        if (fields.length == 10) {
            Host host = new Host();
            host.setHostId(fields[0]);
            host.setLastName(fields[1]);
            host.setEmail(fields[2]);
            host.setPhone(fields[3]);
            host.setAddress(fields[4]);
            host.setCity(fields[5]);
            host.setState(fields[6]);
            host.setPostalCode(fields[7]);
            host.setStandardRate(new BigDecimal(fields[8]));
            host.setWeekendRate(new BigDecimal(fields[9]));
            return host;
        }
        return null;
    }
}
