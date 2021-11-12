package com.osahft.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@AllArgsConstructor
public class SoftwareVersionInformation {
    @NotBlank
    private String restApiVersion;
    @NotNull
    private Instant buildDate;

}
