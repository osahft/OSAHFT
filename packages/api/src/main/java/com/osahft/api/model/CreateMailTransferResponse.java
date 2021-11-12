package com.osahft.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class CreateMailTransferResponse implements Serializable {
    @NotBlank
    private String mailTransferId;
}
