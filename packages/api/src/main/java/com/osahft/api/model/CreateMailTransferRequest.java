package com.osahft.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class CreateMailTransferRequest {

    @NonNull
    private String mailSender;

    @NonNull
    private List<String> mailReceivers;

    private String title;

    private String message;

}
