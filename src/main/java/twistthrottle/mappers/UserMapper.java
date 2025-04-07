package twistthrottle.mappers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import twistthrottle.dtos.UserDTO;
import twistthrottle.models.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
  UserDTO userToUserDTO(User user);
}
