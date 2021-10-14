package com.osahft.api.test.service;

import com.osahft.api.model.SoftwareVersionInformation;
import com.osahft.api.service.PublicServiceIF;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static com.osahft.api.internal.assertion.Assertions.assertThat;


@SpringBootTest
class PublicServiceTests {
    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private PublicServiceIF publicService;

    @Test
    void retrieveSoftwareVersionInformationTest() {
        SoftwareVersionInformation softwareVersionInformation = publicService.retrieveSoftwareVersionInformation();

        // not null (because buildProperties.getVersion(), buildProperties.getTime() could be null as well)
        assertThat(softwareVersionInformation).isNotNull();
        Assertions.assertThat(softwareVersionInformation.getRestApiVersion()).isNotNull();
        Assertions.assertThat(softwareVersionInformation.getBuildDate()).isNotNull();

        // not empty (because buildProperties.getVersion() could be empty as well)
        Assertions.assertThat(softwareVersionInformation.getRestApiVersion()).isNotEmpty();


        assertThat(softwareVersionInformation)
                .hasRestApiVersion(buildProperties.getVersion())
                .hasBuildDate(buildProperties.getTime());
    }

}
