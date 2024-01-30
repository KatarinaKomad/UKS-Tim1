package uns.ac.rs.uks.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class EditRepoRequest {

    @NotNull
    private UUID repoId;
    @NotNull
    private UUID userId;

}
