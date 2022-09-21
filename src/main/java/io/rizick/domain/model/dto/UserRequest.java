package io.rizick.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "Nome nao pode estar em branco")
    private String name;
    @NotNull(message = "Idade nao pode ser nula")
    private Integer age;


}
