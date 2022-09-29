package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.common.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class IncomingItemDto {
    @NotBlank(groups = {Create.class})
    @NotEmpty(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    @NotEmpty(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private Long requestId;
}