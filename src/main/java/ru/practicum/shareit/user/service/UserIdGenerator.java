package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Component;

@Component
public class UserIdGenerator {
    private Long userBaseId = 0L;

    public long generateUserId() {
        return ++userBaseId;
    }

    public void setUserBaseId(Long userBaseId) {
        this.userBaseId = userBaseId;
    }
}