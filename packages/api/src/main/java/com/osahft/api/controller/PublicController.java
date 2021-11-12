package com.osahft.api.controller;

import com.osahft.api.constant.ApiConstants;
import com.osahft.api.model.SoftwareVersionInformation;
import com.osahft.api.service.PublicServiceIF;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "public", description = "Public API (software information)")
@RequestMapping(ApiConstants.API_VERSION + "/public")
public class PublicController {

    @Autowired
    private PublicServiceIF publicService;

    @Operation(summary = "Retrieve software version information", description = "public/getPublicSoftwareVersion.md")
    @GetMapping(value = "/software/version", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public SoftwareVersionInformation retrieveSoftwareVersionInformation() {
        return publicService.retrieveSoftwareVersionInformation();
    }
}
