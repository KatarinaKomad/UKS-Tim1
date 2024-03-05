package uns.ac.rs.uks.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepoUserRequest {

    @NotNull
    private UUID userId;
    @NotNull
    private UUID repoId;
}
