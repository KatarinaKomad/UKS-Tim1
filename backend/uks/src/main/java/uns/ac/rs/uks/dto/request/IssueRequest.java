package uns.ac.rs.uks.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.model.State;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IssueRequest extends IssueItem {
    private UUID repoId;        // can't be updated

}
