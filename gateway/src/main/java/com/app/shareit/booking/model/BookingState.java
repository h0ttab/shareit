package com.app.shareit.booking.model;

public enum BookingState {
    CURRENT,
    PAST,
    FUTURE,
    WAITING,
    REJECTED,
    ALL;

    public static BookingState from(String stateParam) {
        for (BookingState state : values()) {
            if (state.name().equalsIgnoreCase(stateParam)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown state: " + stateParam);
    }
}
