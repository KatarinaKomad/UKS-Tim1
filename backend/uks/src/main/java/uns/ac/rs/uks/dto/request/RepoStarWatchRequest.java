package uns.ac.rs.uks.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class RepoStarWatchRequest {

    @NotNull
    private UUID userId;
    @NotNull
    private UUID repoId;
}
