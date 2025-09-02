package com.bitemate.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bitemate.jwt")
@Data
public class JwtProperties {

    /**
     * Configuration for generating JWT tokens for admin side
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * Configuration for generating JWT tokens for user side
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;
}
