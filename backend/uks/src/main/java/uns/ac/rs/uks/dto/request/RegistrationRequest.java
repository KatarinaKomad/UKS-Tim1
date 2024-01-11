package uns.ac.rs.uks.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import uns.ac.rs.uks.dto.validation.PasswordsMatch;


@Data
@PasswordsMatch
public class RegistrationRequest {
    @Email
    @NotBlank
    @NotEmpty
    @NotNull
    private String email;
    @NotBlank
    @Size(min = 8, message = "Password must be longer than 8 characters.")
    private String password;
    @NotBlank
    private String passwordConfirmation;
    @NotBlank
    @Size(max = 40, message = "First name cannot be longer than 40 characters.")
    private String firstName;
    @NotBlank
    @Size(max = 40, message = "Last name cannot be longer than 40 characters.")
    private String lastName;

}
