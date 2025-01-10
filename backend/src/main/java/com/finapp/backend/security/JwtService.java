package com.finapp.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.access-token-expiration}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.refresh-token-expiration}")
    private long REFRESH_TOKEN_EXPIRATION;

    @Value("${jwt.issuer}")
    private String JWT_ISSUER;

    @Value("${jwt.audience}")
    private String JWT_AUDIENCE;

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    //Extract any specific type of claim from token
    public <T> T extractAnyClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //calls the method to extract specific claim(username)
    public String extractUsername(String token) {
        return extractAnyClaim(token, Claims::getSubject);
    }
    //extract expiration
    private Date extractExpiration(String token) {
        return extractAnyClaim(token, Claims::getExpiration);
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuer(JWT_ISSUER)
                .setAudience(JWT_AUDIENCE)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private boolean isTokenExpired(String token) {

        try {
            return extractExpiration(token)
                    .before(new Date());
        } catch (Exception e){
            LOGGER.error("Token has expired. Login again.");
            return true;
        }

    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateAccessToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateAccessToken(UserDetails userDetails) {
        var claims = createUserClaims(userDetails);
        return generateAccessToken(claims, userDetails);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        var claims = createUserClaims(userDetails);
        return buildToken(claims, userDetails, REFRESH_TOKEN_EXPIRATION);
    }

    public Map<String, Object> createUserClaims(UserDetails userDetails){
        var claims = new HashMap<String, Object>();
        var authorities = userDetails.getAuthorities().stream().toList();
        claims.put("authorities", authorities);
        claims.put("username", userDetails.getUsername());
        return claims;
    }


}
