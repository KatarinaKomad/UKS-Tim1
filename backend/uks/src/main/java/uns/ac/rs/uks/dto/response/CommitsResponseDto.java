package uns.ac.rs.uks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommitsResponseDto implements Serializable {
    private String hash;
    private String message;
    private String gitUser;
    private String timeAgo;
}
