package learn.accomodation.domain;

import learn.accomodation.models.Reservation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Result {
    private ArrayList<String> messages = new ArrayList<>();
    private Reservation payload;

    public Reservation getPayload() {
        return payload;
    }

    public void setPayload(Reservation payload) {
        this.payload = payload;
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public boolean isSuccess() {
        return messages.size() == 0;
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result that = (Result) o;
        return Objects.equals(messages, that.messages) &&
                Objects.equals(payload, that.payload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messages, payload);
    }
}
