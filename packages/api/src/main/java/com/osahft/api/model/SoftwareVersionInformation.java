package com.osahft.api.model;

import lombok.Data;
import lombok.NonNull;

import java.time.Instant;

@Data
public class SoftwareVersionInformation {
    @NonNull
    private String restApiVersion;
    @NonNull
    private Instant buildDate;

}
