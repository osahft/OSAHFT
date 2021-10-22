package com.osahft.api.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "blog")
public class MailTransfer {


    @Id
    private String id;

    private String mailSender;

    private List<String> mailReceivers;

    private String title;

    private String message;

    private String fileDirectory;

    private Boolean triggered = false;

    private long verificationCode;

}

