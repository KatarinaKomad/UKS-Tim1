package uns.ac.rs.uks.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UserIssuesDTO {
    List<IssueDTO> createdIssues;
    List<IssueDTO> assignedIssues;

}
