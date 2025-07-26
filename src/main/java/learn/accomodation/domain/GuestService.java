package learn.accomodation.domain;

import learn.accomodation.data.GuestRepository;
import learn.accomodation.models.Guest;

public class GuestService {
    private final GuestRepository repository;

    public GuestService(GuestRepository repository) {
        this.repository = repository;
    }

    public Guest findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
