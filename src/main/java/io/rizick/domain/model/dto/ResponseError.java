package io.rizick.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolation;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ResponseError {

    public static final int UNPROCESSABLE_ENTITY_STATUS = 422;
    private String message;
    private Collection<FieldError> errors;

    public static <T> ResponseError createFromValidation(
            Set<ConstraintViolation<T>> violations) {
        List<FieldError> errors = violations
                .stream()
                .map(x -> new FieldError(x.getPropertyPath().toString(), x.getMessage()))
                .collect(Collectors.toList());

        String message = "Validation Error";
        var responseError = new ResponseError(message, errors);
        return responseError;


    }


}
