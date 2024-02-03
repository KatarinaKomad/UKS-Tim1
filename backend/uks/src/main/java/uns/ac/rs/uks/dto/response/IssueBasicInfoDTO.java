package uns.ac.rs.uks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.model.State;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueBasicInfoDTO {
    private UUID id;
    private Long counter;
    private String name;
    private String description;
    private State state;

}
