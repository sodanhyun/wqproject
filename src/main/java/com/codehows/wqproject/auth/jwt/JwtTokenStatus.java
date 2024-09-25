package com.codehows.wqproject.auth.jwt;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.codehows.wqproject.auth.jwt.JwtTokenStatus.StatusCode.*;

@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class JwtTokenStatus {
    public enum StatusCode {
        OK,
        EXPIRED,
        REFRESH_EXPIRED,
        UNAUTHORIZED,
        UNKNOWN,
        UNSUPPORTED,
    }

    private StatusCode statusCode;

    public static JwtTokenStatus of(StatusCode status) {
        return makeTokenStatus(status);
    }

    public static JwtTokenStatus makeTokenStatus(StatusCode status) {
        if (OK.equals(status)) {
            return new JwtTokenStatus(OK);
        }
        if (UNAUTHORIZED.equals(status)) {
            return new JwtTokenStatus(UNAUTHORIZED);
        }
        if (EXPIRED.equals(status)) {
            return new JwtTokenStatus(EXPIRED);
        }
        if(UNSUPPORTED.equals(status)) {
            return new JwtTokenStatus(UNSUPPORTED);
        }
        return new JwtTokenStatus(UNKNOWN);
    }

}
