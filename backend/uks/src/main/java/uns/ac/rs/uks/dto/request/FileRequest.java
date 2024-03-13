package uns.ac.rs.uks.dto.request;

import lombok.Data;

import java.util.UUID;
@Data
public class FileRequest {
    private UUID repoId;
    private String branchName;
    private String filePath;
}
