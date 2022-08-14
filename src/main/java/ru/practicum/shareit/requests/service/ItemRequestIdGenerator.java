package ru.practicum.shareit.requests.service;

import org.springframework.stereotype.Component;

@Component
public class ItemRequestIdGenerator {
    private Long requestBaseId = 0L;

    public long generateRequestId() {
        return ++requestBaseId;
    }

    public void setRequestBaseId(Long requestBaseId) {
        this.requestBaseId = requestBaseId;
    }
}