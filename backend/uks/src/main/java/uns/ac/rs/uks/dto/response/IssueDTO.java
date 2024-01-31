package uns.ac.rs.uks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueDTO {
    private UUID id;
    private String name;
    private String description;
    private State state;
    private UserDTO author;
    private LocalDateTime createdAt;
    private List<UserDTO> assignees;
    private MilestoneDTO milestone;
    private List<LabelDTO> labels;

    private List<UserDTO> participants;
//    private LocalDateTime updatedAt;

//    private List<CommentDTO> comments;

}
