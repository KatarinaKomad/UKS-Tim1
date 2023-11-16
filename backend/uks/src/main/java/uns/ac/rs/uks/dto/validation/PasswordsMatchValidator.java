package uns.ac.rs.uks.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, Object> {

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
//        if (o instanceof RegistrationDTO dto)
//            return passwordsMatch(dto.getPassword(), dto.getPasswordConfirmation());
//        if (o instanceof PasswordChangeRequest dto)
//            return passwordsMatch(dto.getNewPassword(), dto.getPasswordConfirmation());
        return false;
    }

    private Boolean passwordsMatch(String password, String confirmation) {
        return password != null && password.equals(confirmation);
    }
}
