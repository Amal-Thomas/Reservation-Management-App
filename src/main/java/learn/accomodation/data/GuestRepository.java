package learn.accomodation.data;

import learn.accomodation.models.Guest;

public interface GuestRepository {
    Guest findByEmail(String email);
}
