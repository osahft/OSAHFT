package com.osahft.api.constant;

public class ApiConstants {

    public static final String API_VERSION = "v1";

    // http status codes
    public static final String CODE_200_OK = "200";
    public static final String MESSAGE_200_OK = "OK";
    public static final String CODE_201_CREATED = "201";
    public static final String MESSAGE_201_CREATED = "Created";
    public static final String CODE_400_BAD_REQUEST = "400";
    public static final String MESSAGE_400_BAD_REQUEST = "Bad Request";
    public static final String CODE_401_UNAUTHORIZED = "401";
    public static final String MESSAGE_401_UNAUTHORIZED = "Unauthorized";
    public static final String CODE_403_FORBIDDEN = "403";
    public static final String MESSAGE_403_FORBIDDEN = "Forbidden";
    public static final String CODE_404_NOT_FOUND = "404";
    public static final String MESSAGE_404_NOT_FOUND = "Not Found";
    public static final String CODE_429_TOO_MANY_REQUESTS = "429";
    public static final String MESSAGE_429_TOO_MANY_REQUESTS = "Too Many Requests";
    public static final String CODE_503_SERVICE_UNAVAILABLE = "503";
    public static final String MESSAGE_503_SERVICE_UNAVAILABLE = "Service Unavailable";

    // headers
    public static final String X_RATE_LIMIT = "X-RATE-LIMIT";
    public static final String X_RATE_LIMIT_REMAINING = "X-RATE-LIMIT-REMAINING";
    // header descriptions
    public static final String X_RATE_LIMIT_DESCRIPTION = "Total rate limit.";
    public static final String X_RATE_LIMIT_REMAINING_DESCRIPTION = "Remaining rate limit.";

    private ApiConstants() {
    }
}
