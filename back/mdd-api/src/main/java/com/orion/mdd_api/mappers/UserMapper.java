package com.orion.mdd_api.mappers;

import com.orion.mdd_api.dtos.UserDto;
import com.orion.mdd_api.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDto toDto(User user);
}
