package com.osahft.api.document;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
@Document(indexName = "blog")
public class MailTransfer {

    public enum State {
        STARTED,
        TRIGGERED,
        FINISHED
    }

    @Builder
    public MailTransfer(String mailSender, List<MailReceiverDownloadLinkMapping> mailReceiverDownloadLinkMapping, String title, String message, String dataDir) {
        this.mailSender = mailSender;
        this.mailReceiverDownloadLinkMapping = mailReceiverDownloadLinkMapping;
        this.title = title;
        this.message = message;
        this.dataDir = dataDir;
    }

    @Id
    private final String id = UUID.randomUUID().toString();

    private final Integer authenticationCode = ThreadLocalRandom.current().nextInt(100000, 999999 + 1);

    private final Date createdAt = new Date();

    private Boolean isAuthenticated = false;

    private String mailSender;

    private List<MailReceiverDownloadLinkMapping> mailReceiverDownloadLinkMapping;

    private String title;

    private String message;

    private String dataDir;

    private Long containerId;

    private State state = State.STARTED;

}

