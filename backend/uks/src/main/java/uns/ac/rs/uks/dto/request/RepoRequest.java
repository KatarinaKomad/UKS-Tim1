package uns.ac.rs.uks.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RepoRequest {

    @NotBlank
    @NotEmpty
    @NotNull
    private String name;
    @NotNull
    private UUID ownerId;

    private Boolean isPublic;

}
