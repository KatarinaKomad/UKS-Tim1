package uns.ac.rs.uks.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileChangeResponseDTO {
    private String fileName;
    private String changes; //-+
    private String stats; //@@
}
