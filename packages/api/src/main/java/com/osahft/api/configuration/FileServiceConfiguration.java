package com.osahft.api.configuration;


import com.osahft.api.exception.FileServiceException;
import com.osahft.api.service.DracoonClientService;
import com.osahft.api.service.FileServiceClientServiceIF;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileServiceConfiguration {

    @Value("${file.service}")
    private String fileService;

    @Bean("FileServiceClientServiceIF")
    public FileServiceClientServiceIF createFileServiceClientService() throws FileServiceException {
        if (fileService.equalsIgnoreCase("dracoon")) {
            return new DracoonClientService();
        } else {
            throw new FileServiceException("Unsupported value for property file.service: " + fileService + ".");
        }
    }


}
