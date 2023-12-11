package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.model.User;

import java.util.List;

public class UserMapper {
    public static UserDTO toDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .name(user.getName())
                .email(user.getEmail())
                .blockedByAdmin(user.getBlockedByAdmin())
                .build();
    }

    public static List<UserDTO> toDTOs(List<User> users){
        return users.stream().map(UserMapper::toDTO).toList();
    }
}
