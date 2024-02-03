package uns.ac.rs.uks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepoBasicInfoDTO implements Serializable {
    private UUID id;
    private Boolean isPublic;
    private String name;
    private UserDTO owner;
    private Integer starCount;
    private Integer watchCount;
    private Integer forkCount;
    private Long defaultBranch;
}
