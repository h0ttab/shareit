package ru.practicum.shareit.user.dto.mapper;

import java.util.List;

import org.mapstruct.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toUserDto(User user);

    User fromUserDto(UserDto userDto);

    List<UserDto> toUserDtoList(List<User> userList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUserFromDto(UserDto userDto, @MappingTarget User user);
}