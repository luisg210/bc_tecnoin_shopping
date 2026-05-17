package com.luis.bc_tecnoin.gateway_api.utils;

import java.util.List;

public class PublicRoutes {

    public static final List<String> EXCLUDED_PATHS = List.of(
            "/auth/",
            "/actuator"
    );

    public static boolean isExcluded(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::contains);
    }
}
