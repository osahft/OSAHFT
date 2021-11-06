package com.osahft.api.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateMailTransferRequest {

    @Email
    @NotNull
    private String mailSender;

    @NotEmpty
    private List<@Email String> mailReceivers;

    @NotBlank
    private String title;

    @NotBlank
    private String message;

}
