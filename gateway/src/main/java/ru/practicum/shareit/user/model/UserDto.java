package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.common.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    @NotEmpty(groups = {Create.class})
    private String name;
    @NotNull(groups = {Create.class})
    @Email(groups = {Create.class}, message = "Неверно указана почта")
    private String email;
}
