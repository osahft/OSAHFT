package com.osahft.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class CreateMailTransferResponse {
    @NonNull
    private long mailTransferId;
}
