package uns.ac.rs.uks.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uns.ac.rs.uks.dto.validation.PasswordsMatch;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PasswordsMatch
public class PasswordUpdateRequest {
    @Email
    @NotBlank
    @NotNull
    private String email;
    @NotBlank
    @NotNull
//    @Pattern(regexp="^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^a-zA-Z0-9]).{12,256}$")
    private String currentPassword;

    @NotBlank
    @Size(min = 8, message = "Password must be longer than 8 characters.")
    private String password;
    @NotBlank
    private String passwordConfirmation;
}
