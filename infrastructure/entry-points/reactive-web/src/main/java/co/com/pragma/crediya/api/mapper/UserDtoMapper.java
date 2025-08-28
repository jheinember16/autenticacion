package co.com.pragma.crediya.api.mapper;

import co.com.pragma.crediya.api.dto.UserDTO;
import co.com.pragma.crediya.model.user.User;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    UserDTO toResponse(User user);

    List<UserDTO> toResponseList(List<User> user);

    User toModel(UserDTO userDTO);

    List<User> toModelList(List<UserDTO> userDTO);
}
