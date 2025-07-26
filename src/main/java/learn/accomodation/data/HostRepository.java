package learn.accomodation.data;

import learn.accomodation.models.Host;

public interface HostRepository {
    Host findByEmail(String email);
    Host findById(String id);
}
