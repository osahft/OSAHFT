package com.osahft.api.plugin;

import com.google.common.io.Resources;
import com.osahft.api.model.ErrorResponse;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.osahft.api.constant.ApiConstants.*;


@Slf4j
@Component
@SuppressWarnings({"UnstableApiUsage"})
public class ApiDescriptionPlugin implements OperationCustomizer {

    private static final String DESCRIPTION_BASE_PATH = "swagger/description/";

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiResponses responses = operation.getResponses();

        // document response http status 429 for every endpoint
        Schema<ErrorResponse> schema = new Schema<>();
        schema.set$ref(ErrorResponse.class.getSimpleName());
        MediaType mediaType = new MediaType();
        mediaType.setSchema(schema);
        Content content = new Content();
        content.addMediaType("*/*", mediaType);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setContent(content);
        apiResponse.setDescription(MESSAGE_429_TOO_MANY_REQUESTS);
        responses.addApiResponse(CODE_429_TOO_MANY_REQUESTS, apiResponse);


        // set rate limit headers
        Schema<Long> longSchema = new Schema<>();
        longSchema.setType("integer");
        longSchema.setFormat("int64");
        for (Pair<String, String> p : Arrays.asList(Pair.of(X_RATE_LIMIT, X_RATE_LIMIT_DESCRIPTION),
                Pair.of(X_RATE_LIMIT_REMAINING, X_RATE_LIMIT_REMAINING_DESCRIPTION))) {
            Header header = new Header();
            header.setSchema(longSchema);
            header.setDescription(p.getRight());
            responses.forEach((s, apiRes) -> apiRes.addHeaderObject(p.getLeft(), header));
        }

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
