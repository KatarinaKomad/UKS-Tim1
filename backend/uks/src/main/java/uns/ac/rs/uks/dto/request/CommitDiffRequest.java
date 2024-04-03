package uns.ac.rs.uks.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitDiffRequest {

    @NotNull
    private UUID repoId;
    @NotBlank
    @NotEmpty
    @NotNull
    private String branchName;
    @NotBlank
    @NotEmpty
    @NotNull
    private String commit;
}
