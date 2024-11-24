package twistthrottle.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import twistthrottle.dtos.UserDTO;
import twistthrottle.models.entities.User;

@Mapper
public abstract class UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDTO userToUserDTO(User user);
}
