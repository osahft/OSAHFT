package com.osahft.api.service;

import com.osahft.api.model.SoftwareVersionInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;


@Service
public class PublicService implements PublicServiceIF {

    @Autowired
    private BuildProperties buildProperties;

    @Override
    public SoftwareVersionInformation retrieveSoftwareVersionInformation() {
        return new SoftwareVersionInformation(buildProperties.getVersion(), buildProperties.getTime());
    }
}
