package com.app.shareit.user.dto.mapper;

import java.util.List;

import com.app.shareit.user.dto.UserDto;
import com.app.shareit.user.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toUserDto(User user);

    User fromUserDto(UserDto userDto);

    List<UserDto> toUserDtoList(List<User> userList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User updateUserFromDto(UserDto userDto, @MappingTarget User user);
}