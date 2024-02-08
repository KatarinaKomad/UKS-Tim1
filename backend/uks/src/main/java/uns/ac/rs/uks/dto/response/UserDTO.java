package uns.ac.rs.uks.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends SearchResponse implements Serializable {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean blockedByAdmin;
    private String name;
    private String username;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
