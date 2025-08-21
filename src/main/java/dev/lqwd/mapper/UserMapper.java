package dev.lqwd.mapper;

import dev.lqwd.dto.AuthRequestDto;
import dev.lqwd.dto.UserCreationRequestDto;
import dev.lqwd.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User toUser(UserCreationRequestDto userCreationRequestDto);

    @Mapping(target = "id", ignore = true)
    User toUser(AuthRequestDto authRequestDto);

}
