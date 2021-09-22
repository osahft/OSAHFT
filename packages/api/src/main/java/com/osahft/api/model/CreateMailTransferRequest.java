package com.osahft.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class CreateMailTransferRequest {

    @NonNull
    private String mailSender;

    @NonNull
    private String mailReceiver;

    private String title;

    private String message;

}
