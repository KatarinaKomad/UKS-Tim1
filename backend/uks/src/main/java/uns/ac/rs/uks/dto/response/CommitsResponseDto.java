package uns.ac.rs.uks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitsResponseDto {
    private String hash;
    private String message;
    private String gitUser;
    private String timeAgo;
}
