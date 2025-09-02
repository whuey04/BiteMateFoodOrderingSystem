package com.bitemate.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     *  Generate JWT by using the HS256 algorithm with a fixed secret key
     *
     * @param secretKey The secret key for JWT
     * @param ttlMillis The expiration time of the JWT in milliseconds
     * @param claims The info to be set in the JWT payload
     * @return
     */
    public static String generateJWT(String secretKey, long ttlMillis, Map<String, Object> claims){
        // Specify the signature algorithm used for signing (used in the JWT header)
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // Calculate the expiration time
        long expirationMillis = System.currentTimeMillis() + ttlMillis;
        Date expiration = new Date(expirationMillis);

        // Build the JWT body
        JwtBuilder builder = Jwts.builder()
                // Set custom claims (must be set before standard claims to avoid overwriting them)
                .setClaims(claims)
                // Sign the JWT with the specified algorithm and secret key
                .signWith(signatureAlgorithm,secretKey.getBytes(StandardCharsets.UTF_8))
                // Set the expiration time
                .setExpiration(expiration);

        return builder.compact();
    }

    /**
     * Decrypts (parses) a JWT token.
     *
     * @param secretKey the secret key used to sign the JWT. This key must be securely stored on the server side and never exposed,
     *                  otherwise the token signature can be forged. If integrating with multiple clients, consider using different keys.
     * @param token  the encrypted JWT token string
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // Get the default JwtParser
        Claims claims = Jwts.parser()
                // Set the signing key used to verify the token
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // Parse the JWT token
                .parseClaimsJws(token).getBody();
        return claims;
    }
}
