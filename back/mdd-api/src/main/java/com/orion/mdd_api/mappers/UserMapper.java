package com.orion.mdd_api.mappers;

import org.mapstruct.Mapper;
import com.orion.mdd_api.dtos.UserDto;
import com.orion.mdd_api.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);
}
