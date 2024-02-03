package uns.ac.rs.uks.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RepoUpdateRequest {

    @NotBlank
    @NotEmpty
    @NotNull
    private String name;

    @NotNull
    private Boolean isPublic;

    @NotNull
    private  Long defaultBranch;
}
