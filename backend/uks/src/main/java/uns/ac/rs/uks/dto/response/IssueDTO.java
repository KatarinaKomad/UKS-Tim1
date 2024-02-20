package uns.ac.rs.uks.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import uns.ac.rs.uks.model.State;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDTO extends SearchResponse implements Serializable {
    private UUID id;
    private Long counter;
    private String name;
    private String description;
    private State state;
    private UserDTO author;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private List<UserDTO> assignees;
    private MilestoneDTO milestone;
    private List<LabelDTO> labels;

    private List<UserDTO> participants;
    private RepoBasicInfoDTO repo;
//    private LocalDateTime updatedAt;

//    private List<CommentDTO> comments;

}
