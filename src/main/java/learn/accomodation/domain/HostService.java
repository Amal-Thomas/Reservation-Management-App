package learn.accomodation.domain;

import learn.accomodation.data.HostRepository;
import learn.accomodation.models.Host;

public class HostService {
    private HostRepository repository;

    public HostService(HostRepository repository) {
        this.repository = repository;
    }

    public Host findByEmail(String email) {
        return repository.findByEmail(email);
    }
}
