package uns.ac.rs.uks.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PullRequestRequest {

    private Long originId;
    private Long targetId;
    private UUID authorId;
    private List<Long> labelIds;
    private String name;
    private String description;
    private UUID repoId;
    private Long milestoneId;
    private List<UUID> assigneeIds;

}