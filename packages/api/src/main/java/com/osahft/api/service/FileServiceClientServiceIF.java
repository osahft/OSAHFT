package com.osahft.api.service;

import com.osahft.api.exception.FileServiceClientServiceException;

public interface FileServiceClientServiceIF {
    // create a container to store files and upload files to it
    void uploadFiles(String transferId) throws FileServiceClientServiceException;

    // create download links (for every mailReceiver one link)
    void createDownloadLinks(String transferId) throws FileServiceClientServiceException;

    // delete container and files
    void deleteFiles(String transferId) throws FileServiceClientServiceException;
}
