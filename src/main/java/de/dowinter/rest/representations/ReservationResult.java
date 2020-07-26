package de.dowinter.rest.representations;

public class ReservationResult {
    boolean success;

    public ReservationResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
