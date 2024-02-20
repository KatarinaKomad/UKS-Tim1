package uns.ac.rs.uks.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneRequest {

    private Long id;
    private String name;
    private UUID repoId;
    private LocalDateTime dueDate;
    private String description;
//    private List<ItemRequest> items;
}
