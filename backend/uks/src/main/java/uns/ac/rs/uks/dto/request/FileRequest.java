package uns.ac.rs.uks.dto.request;

import lombok.Data;

@Data
public class FileRequest {
    private String repoName;
    private String branchName;
    private String filePath;
}
