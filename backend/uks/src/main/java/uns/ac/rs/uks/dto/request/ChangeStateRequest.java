package uns.ac.rs.uks.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.model.State;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeStateRequest {

    private Long id;
    private State state;
}
