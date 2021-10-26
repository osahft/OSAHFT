package com.osahft.api.plugin;

import com.google.common.io.Resources;
import io.swagger.v3.oas.models.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.net.URL;
import java.nio.charset.StandardCharsets;


@Slf4j
@Component
@SuppressWarnings({"UnstableApiUsage"})
public class ApiDescriptionPlugin implements OperationCustomizer {

    private static final String DESCRIPTION_BASE_PATH = "swagger/description/";

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        String descriptionPath = DESCRIPTION_BASE_PATH + operation.getDescription();
        if (StringUtils.isBlank(operation.getDescription())) {
            log.warn("Documentation Operation.description for '{}' API is not set.", operation.getOperationId());
            operation.setDescription("Documentation not found.");
            return operation;
        }
        try {
            URL resource = Resources.getResource(descriptionPath);
            operation.setDescription(Resources.toString(resource, StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.warn("Documentation file '{}' for API '{}' not found.", descriptionPath, operation.getOperationId());
        }
        return operation;
    }
}
