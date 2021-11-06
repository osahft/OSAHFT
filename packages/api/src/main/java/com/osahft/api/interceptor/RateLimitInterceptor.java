package com.osahft.api.interceptor;

import com.osahft.api.exception.RateLimitInterceptorException;
import com.osahft.api.helper.ErrorHelper;
import io.github.bucket4j.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.osahft.api.constant.ApiConstants.X_RATE_LIMIT;
import static com.osahft.api.constant.ApiConstants.X_RATE_LIMIT_REMAINING;
import static java.lang.String.valueOf;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final long CAPACITY = 50;
    private static final Duration REFILL_INTERVAL = Duration.ofMinutes(5);
    private final Map<String, Bucket> ipBucketMapping = new HashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        // get ip address
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        Bucket bucket = ipBucketMapping.get(ipAddress);
        // if ip address is unknown create a new bucket and add it to the map
        if (bucket == null) {
            // do a full refill every interval
            Refill refill = Refill.intervally(CAPACITY, REFILL_INTERVAL);
            Bandwidth limit = Bandwidth.classic(CAPACITY, refill);
            bucket = Bucket4j.builder()
                    .addLimit(limit)
                    .build();
            ipBucketMapping.put(ipAddress, bucket);
        }

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        response.addHeader(X_RATE_LIMIT, valueOf(CAPACITY));
        response.addHeader(X_RATE_LIMIT_REMAINING, valueOf(probe.getRemainingTokens()));
        if (probe.isConsumed()) {
            return true;
        } else {
            throw new RateLimitInterceptorException(ErrorHelper.getTOO_MANY_REQUESTS(
                    "Rate limit exceeded. Rate limit will reset in " + (probe.getNanosToWaitForRefill() / 1000000) + " milliseconds."));
        }
    }
}
