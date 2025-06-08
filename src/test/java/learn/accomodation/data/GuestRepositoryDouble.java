package learn.accomodation.data;

import learn.accomodation.models.Guest;

public class GuestRepositoryDouble implements GuestRepository{

    public final static Guest GUEST = new Guest(
            1,
            "Sullivan",
            "Lomas",
            "slomas0@mediafire.com",
            "(702) 7768761",
            "NV");

    @Override
    public Guest findByEmail(String email) {
        return email.equalsIgnoreCase("slomas0@mediafire.com")?GUEST:null;
    }

    @Override
    public Guest findById(int id) {
        return id == 1 ? GUEST:null; }
}
