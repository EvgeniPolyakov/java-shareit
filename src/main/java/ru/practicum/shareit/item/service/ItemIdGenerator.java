package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Component;

@Component
public class ItemIdGenerator {
    private Long itemBaseId = 0L;

    public long generateItemId() {
        return ++itemBaseId;
    }

    public void setItemBaseId(Long itemBaseId) {
        this.itemBaseId = itemBaseId;
    }
}