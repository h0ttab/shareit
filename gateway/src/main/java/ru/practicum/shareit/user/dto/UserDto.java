package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotNull(groups = Create.class, message = "Имя пользователя обязательно для заполнения")
    @Pattern(groups = {Create.class, Update.class}, regexp = ".*\\S.*",
            message = "Имя пользователя не может быть пустым")
    private String name;

    @NotNull(groups = Create.class, message = "Email обязателен для заполнения")
    @Pattern(groups = {Create.class, Update.class}, regexp = ".*\\S.*",
            message = "Email не может быть пустым")
    @Email(groups = {Create.class, Update.class}, message = "Указанное значение не соответствует формату email-адреса")
    private String email;

    public interface Create {
    }

    public interface Update {
    }
}
