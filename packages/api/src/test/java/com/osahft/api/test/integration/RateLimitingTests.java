package com.osahft.api.test.integration;

import com.osahft.api.model.ErrorResponse;
import com.osahft.api.model.SoftwareVersionInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.Objects;
import java.util.stream.Collectors;

import static com.osahft.api.constant.ApiConstants.X_RATE_LIMIT;
import static com.osahft.api.constant.ApiConstants.X_RATE_LIMIT_REMAINING;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
@DirtiesContext
class RateLimitingTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static final long RATE_LIMIT = 50;

    private <T> ResponseEntity<T> executeRequest(Class<T> clazz) {
        return restTemplate
                .exchange("http://localhost:" + port + "/api/v1/public/software/version", HttpMethod.GET, null, clazz);
    }

    private long getHeader(ResponseEntity<SoftwareVersionInformation> response, String key) {
        return Objects.requireNonNull(response.getHeaders().get(key))
                .stream()
                .map(Long::parseLong)
                .collect(Collectors.toList())
                .get(0);
    }


    @Test
    void testRateLimiting() {
        for (long i = RATE_LIMIT - 1; i >= 0; i--) {
            ResponseEntity<SoftwareVersionInformation> response = executeRequest(SoftwareVersionInformation.class);

            // check status code
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            // check headers
            long responseXRateLimit = getHeader(response, X_RATE_LIMIT);
            assertThat(responseXRateLimit).isEqualTo(RATE_LIMIT);
            long responseXRateLimitRemaining = getHeader(response, X_RATE_LIMIT_REMAINING);
            assertThat(responseXRateLimitRemaining).isEqualTo(i);
        }

        // check api responds with error
        ResponseEntity<ErrorResponse> errorResponseEntity = executeRequest(ErrorResponse.class);
        assertThat(errorResponseEntity.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        ErrorResponse errorResponse = errorResponseEntity.getBody();
        assertThat(errorResponse).isNotNull();
        assertThat(errorResponse.getCode()).isNotBlank();
        assertThat(errorResponse.getMessage()).isNotBlank();
        // is with @JsonIgnore annotated
        assertThat(errorResponse.getHttpStatus()).isNull();
    }

}
