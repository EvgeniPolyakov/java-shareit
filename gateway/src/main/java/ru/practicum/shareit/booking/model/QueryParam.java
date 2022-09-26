package ru.practicum.shareit.booking.model;

import java.util.Optional;

public enum QueryParam {
    ALL,
    CURRENT,
    FUTURE,
    PAST,
    REJECTED,
    WAITING;

    public static Optional<QueryParam> from(String stringState) {
        for (QueryParam state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
