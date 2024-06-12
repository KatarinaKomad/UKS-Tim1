package uns.ac.rs.uks.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.model.IssueEventType;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PullRequestEventRequest extends IssueItem {

    private UUID prId;
    private IssueEventType type;
}
