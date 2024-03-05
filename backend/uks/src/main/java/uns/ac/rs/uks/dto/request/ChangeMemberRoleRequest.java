package uns.ac.rs.uks.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.model.RepositoryRole;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeMemberRoleRequest {
    private RepositoryRole repositoryRole;
    private UUID userId;
    private UUID repoId;

}
