package com.osahft.api.repository;


import com.osahft.api.document.MailTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MailTransferRepository extends ElasticsearchRepository<MailTransfer, String> {
    Page<MailTransfer> findMailTransferById(String id, Pageable pageable);
}
