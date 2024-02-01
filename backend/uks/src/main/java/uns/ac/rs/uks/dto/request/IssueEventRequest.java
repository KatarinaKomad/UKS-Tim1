package uns.ac.rs.uks.dto.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import uns.ac.rs.uks.model.Issue;
import uns.ac.rs.uks.model.IssueEventType;
import uns.ac.rs.uks.model.State;
import uns.ac.rs.uks.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IssueEventRequest extends IssueItem {
    private UUID issueId;
    private IssueEventType type;


}
