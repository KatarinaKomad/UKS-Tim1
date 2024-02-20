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

    private String origin;
    private String target;
    private String author;
    private List<Long> labels;
    private String name;
    private String description;
    private UUID repoId;
    private Long milestone;
    private List<String> assignees;

}
