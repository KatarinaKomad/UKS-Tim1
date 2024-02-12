package uns.ac.rs.uks.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.dto.response.BranchDTO;
import uns.ac.rs.uks.dto.response.CommitDTO;
import uns.ac.rs.uks.dto.response.UserDTO;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitRequest {
    private Long id;
    private String message;
    private String description;
    private LocalDateTime committedAt;

    private UserDTO committer;
    private CommitDTO parentCommit;
    private List<BranchDTO> branches;
    //private List<CommentDTO> comments;
}
