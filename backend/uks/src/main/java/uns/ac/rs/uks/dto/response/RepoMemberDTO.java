package uns.ac.rs.uks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.model.MemberInviteStatus;
import uns.ac.rs.uks.model.RepositoryRole;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepoMemberDTO implements Serializable {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private RepositoryRole repositoryRole;
    private String repoName;
    private UUID repoId;
    private LocalDateTime createdAt;
    private MemberInviteStatus inviteStatus;
}
