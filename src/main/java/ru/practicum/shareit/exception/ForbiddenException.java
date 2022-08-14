package ru.practicum.shareit.exception;

public class ForbiddenException extends RuntimeException {
    private final String parameter;

    public ForbiddenException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
