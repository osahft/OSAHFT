package com.osahft.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    @NotBlank
    private String code;
    @NotBlank
    private String message;
    @JsonIgnore
    private HttpStatus httpStatus;

}
