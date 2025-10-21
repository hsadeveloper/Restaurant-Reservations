package tableservice.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationRequest {

    private LocalDate date;

    private LocalTime time;

    private int partySize;

    private String customerId;

    // Getters and setters

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getPartySize() {
        return partySize;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "CreateReservationRequest{" +
                "date=" + date +
                ", time=" + time +
                ", partySize=" + partySize +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}
