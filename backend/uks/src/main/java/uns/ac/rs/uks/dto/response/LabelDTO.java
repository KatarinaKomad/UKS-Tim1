package uns.ac.rs.uks.dto.response;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.model.Repo;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LabelDTO {

    private Long id;
    private String name;
    private String color; // hex
    private String description;
    private UUID repoId;
}
