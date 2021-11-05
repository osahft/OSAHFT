package com.osahft.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CreateMailTransferResponse {
    @NotBlank
    private String mailTransferId;
}
