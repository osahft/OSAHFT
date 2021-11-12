package com.osahft.api.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailReceiverDownloadLinkMapping {
    private Long downloadLinkId;

    private String downloadLink;

    private String mailReceiver;
}
