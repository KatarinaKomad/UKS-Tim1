package uns.ac.rs.uks.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelRequest {

    private Long id;
    private String name;
    private String color; // hex
    private String description;
    private UUID repoId;
}
