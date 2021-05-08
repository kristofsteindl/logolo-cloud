package com.ksteindl.logolo.validator;

import com.ksteindl.logolo.domain.UserInput;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserInputValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return UserInput.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserInput userInput = (UserInput) o;
        if (userInput.getPassword().length() < 6) {
            errors.rejectValue("password", "Length", "Password must be at least 6 characters");
        }
        if (!userInput.getPassword().equals(userInput.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "Match", "Passwords must match");
        }

    }
}
