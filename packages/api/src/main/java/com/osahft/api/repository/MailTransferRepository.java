package com.osahft.api.repository;


import com.osahft.api.document.MailTransfer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MailTransferRepository extends ElasticsearchRepository<MailTransfer, String> {
}
