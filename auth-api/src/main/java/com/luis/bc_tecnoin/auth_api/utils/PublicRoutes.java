package com.luis.bc_tecnoin.auth_api.utils;

import java.util.List;

public class PublicRoutes {

    public static final List<String> EXCLUDED_PATHS = List.of(
            "/auth",
            "/actuator",
            "/swagger",
            "/v3/api-docs/",
            "/api-docs"
    );

    public static boolean isExcluded(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::contains);
    }
}
