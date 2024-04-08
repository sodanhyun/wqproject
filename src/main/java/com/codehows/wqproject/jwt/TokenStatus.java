package com.codehows.wqproject.jwt;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.codehows.wqproject.jwt.TokenStatus.StatusCode.*;

@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TokenStatus {
    public enum StatusCode {
        OK,
        EXPIRED,
        REFRESH_EXPIRED,
        UNAUTHORIZED,
        UNKNOWN,
        UNSUPPORTED,
    }

    private StatusCode statusCode;

    public static TokenStatus of(StatusCode status) {
        return makeTokenStatus(status);
    }

    public static TokenStatus makeTokenStatus(StatusCode status) {
        if (OK.equals(status)) {
            return new TokenStatus(OK);
        }
        if (UNAUTHORIZED.equals(status)) {
            return new TokenStatus(UNAUTHORIZED);
        }
        if (EXPIRED.equals(status)) {
            return new TokenStatus(EXPIRED);
        }
        if(UNSUPPORTED.equals(status)) {
            return new TokenStatus(UNSUPPORTED);
        }
        return new TokenStatus(UNKNOWN);
    }

}
