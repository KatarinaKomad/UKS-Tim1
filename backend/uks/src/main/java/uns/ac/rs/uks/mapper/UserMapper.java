package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.request.RegistrationRequest;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.model.User;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

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

    public static User toUserFromRequest(RegistrationRequest request){
        return User.builder()
                .id(UUID.randomUUID())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .blockedByAdmin(false)
                .deleted(false)
                .build();
    }

    public static List<UserDTO> toDTOs(List<User> users){
        return users.stream().map(UserMapper::toDTO).toList();
    }
}
