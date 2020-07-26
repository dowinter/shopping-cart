package de.dowinter.rest.representations;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

public class ReservationRequest {
    private Long itemId;
    private int reservationCount;

    @JsonbCreator
    public ReservationRequest(@JsonbProperty("itemId") Long itemId,
                              @JsonbProperty("reservationCount") int reservationCount) {
        this.itemId = itemId;
        this.reservationCount = reservationCount;
    }


    public Long getItemId() {
        return itemId;
    }

    public int getReservationCount() {
        return reservationCount;
    }
}
