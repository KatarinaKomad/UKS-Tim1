package uns.ac.rs.uks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDTO {
    private String name;
    private String path;
    private String content; // if not folder
    private Boolean isFolder;
    private List<CommitsResponseDto> commitHistory;
    private String parentPath;
}
