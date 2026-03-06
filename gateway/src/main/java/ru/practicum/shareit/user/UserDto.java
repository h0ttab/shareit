package ru.practicum.shareit.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(groups = Create.class, message = "Имя не может быть пустым")
    private String name;
    @NotBlank(groups = Create.class, message = "Email не может быть пустым")
    @Email(groups = {Create.class, Update.class}, message = "Указанное значение не соответствует формату email-адреса")
    private String email;

    interface Create {
    }

    interface Update {
    }
}
