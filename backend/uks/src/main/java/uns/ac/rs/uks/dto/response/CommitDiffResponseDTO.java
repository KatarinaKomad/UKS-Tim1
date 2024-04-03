package uns.ac.rs.uks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitDiffResponseDTO {

    private String stats;
    private List<FileChangeResponseDTO> fileChanges = new ArrayList<>();
}
