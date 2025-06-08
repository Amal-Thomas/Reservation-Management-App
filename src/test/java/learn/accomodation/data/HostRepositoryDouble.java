package learn.accomodation.data;

import learn.accomodation.models.Host;

import java.math.BigDecimal;

public class HostRepositoryDouble implements HostRepository{
    public final static Host HOST = new Host(
            "3edda6bc-ab95-49a8-8962-d50b53f84b15",
            "Yearnes",
            "eyearnes0@sfgate.com",
            "(806) 1783815",
            "3 Nova Trail",
            "Amarillo",
            "TX",
            "79182",
            new BigDecimal("340"),
            new BigDecimal("425"));

    @Override
    public Host findByEmail(String email) {
        return HOST;
    }

    @Override
    public Host findById(String id) {
        return HOST;
    }
}
