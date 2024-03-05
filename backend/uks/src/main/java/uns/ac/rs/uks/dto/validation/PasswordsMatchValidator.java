package uns.ac.rs.uks.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uns.ac.rs.uks.dto.request.PasswordUpdateRequest;
import uns.ac.rs.uks.dto.request.RegistrationRequest;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, Object> {

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o instanceof RegistrationRequest dto)
            return passwordsMatch(dto.getPassword(), dto.getPasswordConfirmation());
        if (o instanceof PasswordUpdateRequest dto)
            return passwordsMatch(dto.getPassword(), dto.getPasswordConfirmation());
        return false;
    }

    private Boolean passwordsMatch(String password, String confirmation) {
        return password != null && password.equals(confirmation);
    }
}
