package uns.ac.rs.uks.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MergeBranchesRequest {

    private UUID id;

    @NotBlank
    @NotEmpty
    @NotNull
    private String originBranch;

    @NotBlank
    @NotEmpty
    @NotNull
    private String destinationBranch;
}
