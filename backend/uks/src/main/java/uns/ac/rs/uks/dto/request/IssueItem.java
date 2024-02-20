package uns.ac.rs.uks.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.model.State;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class IssueItem {

    private UUID authorId; // can't be updated

    private String name;
    private String description;
    private State state;
    private List<UUID> assigneeIds;
    private Long milestoneId;
    private List<Long> labelIds;
    // Comments
    // PR
    // Commit
}
