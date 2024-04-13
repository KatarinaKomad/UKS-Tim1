package uns.ac.rs.uks.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.model.Comment;
import uns.ac.rs.uks.model.Review;
import uns.ac.rs.uks.model.State;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PullRequestDTO implements Serializable {
    private UUID id;
    private String name;
    private UserDTO author;
    private MilestoneDTO milestone;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    private Long counter;
    private List<LabelDTO> labels;
    private List<UserDTO> assignees;
    private State state;
    private String description;
    private BranchDTO origin;
    private BranchDTO target;
    private RepoBasicInfoDTO repo;
    private List<Comment> comments;
    private List<Review> reviews;
}
