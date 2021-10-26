package com.osahft.api.model;

import com.osahft.api.constant.ErrorConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    public String code;
    public String message;

    public ErrorResponse(ErrorConstants error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }
}
