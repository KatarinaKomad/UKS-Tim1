package uns.ac.rs.uks.dto.response;

import lombok.*;
import uns.ac.rs.uks.model.State;

import java.io.Serializable;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IssueBasicInfoDTO extends SearchResponse implements Serializable {
    private UUID id;
    private Long counter;
    private String name;
    private String description;
    private State state;

}
