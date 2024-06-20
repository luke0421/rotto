package com.rezero.rotto.utils;

public class AuthenticatedMatchers {

    private AuthenticatedMatchers() {}

    public static final String[] matcherArray = {
            "/",
            "/user/signup",
            "/user/phone-num-check",
            "/user/email-check",
            "/auth/login",
            "/auth/logout",
            "/auth/refresh",
            "/auth/decrypt",
            "/sse/**",
            "/alert/send",
            "/firebase/**",
            "/api-docs",
            "/api-docs/json",
            "/api-docs/**",
            "/swagger-ui/index.html",
            "/swagger-ui.html",
            "/swagger-ui/**"

    };
}
