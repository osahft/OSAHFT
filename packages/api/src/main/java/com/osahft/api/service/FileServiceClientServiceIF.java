package com.osahft.api.service;

import com.osahft.api.exception.FileServiceClientException;

import java.util.List;

public interface FileServiceClientServiceIF {
    // create a container to store files and upload files to it
    void uploadFiles(Long transferId) throws FileServiceClientException;

    // create download links (for every mailReceiver one link)
    List<String> createDownloadLinks(Long transferId) throws FileServiceClientException;

    // delete container and files
    void deleteFiles(Long transferId) throws FileServiceClientException;
}
