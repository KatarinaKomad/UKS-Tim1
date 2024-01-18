package uns.ac.rs.uks.dto.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uns.ac.rs.uks.dto.response.UserDTO;
import uns.ac.rs.uks.model.*;

import java.util.List;
import java.util.UUID;

@Data
public class RepoRequest {

    @NotBlank
    @NotEmpty
    @NotNull
    private String name;
    @NotNull
    private UUID ownerId;

    private Boolean isPublic;

}
