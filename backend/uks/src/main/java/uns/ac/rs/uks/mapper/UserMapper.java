package uns.ac.rs.uks.mapper;

import uns.ac.rs.uks.dto.request.RegistrationRequest;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.model.User;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class UserMapper {
    public static UserDTO toDTO(User user){
        return user == null ? null :
            UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .name(user.getName())
                .username(user.getCustomUsername())
                .email(user.getEmail())
                .blockedByAdmin(user.getBlockedByAdmin())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static User toUserFromRequest(RegistrationRequest request){
        return request == null ? null :
             User.builder()
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
