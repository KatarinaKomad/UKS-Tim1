package uns.ac.rs.uks.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class RepoBasicInfoDTO  extends SearchResponse implements Serializable {
    private UUID id;
    private Boolean isPublic;
    private String name;
    private String description;
    private UserDTO owner;
    private Integer starCount;
    private Integer watchCount;
    private Integer forkCount;
    private Long defaultBranch;
    private ForkParentDTO forkParent;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private String cloneUri;
}
