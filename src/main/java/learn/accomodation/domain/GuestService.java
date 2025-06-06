package learn.accomodation.domain;

import learn.accomodation.data.GuestRepository;
import learn.accomodation.models.Host;

public class GuestService {
    private GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public Host findByEmail(String email) {
        return repository.findGuestByEmail(email);
    }
}
