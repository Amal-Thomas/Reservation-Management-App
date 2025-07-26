package learn.accomodation.models;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

public class Reservation {
    private int id;
    private Host host;
    private Guest guest;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal total;

    public Reservation() {
    }

    public Reservation(int id, Host host, Guest guest, LocalDate startDate, LocalDate endDate, BigDecimal total) {
        this.id = id;
        this.host = host;
        this.guest = guest;
        this.startDate = startDate;
        this.endDate = endDate;
        this.total = total;
    }

    public Host getHost() {
        return host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal calculateTotal() {
        BigDecimal standardRate = host.getStandardRate();
        BigDecimal weekendRate = host.getWeekendRate();
        BigDecimal total = new BigDecimal(0);
        if (standardRate == null || weekendRate == null || endDate.isBefore(startDate)) {
            return null;
        }
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.FRIDAY) {
                total = total.add(weekendRate);
            } else {
                total = total.add(standardRate);
            }
        }
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) && Objects.equals(host, that.host) && Objects.equals(guest, that.guest) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate) && Objects.equals(total, that.total);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, host, guest, startDate, endDate, total);
    }
}
