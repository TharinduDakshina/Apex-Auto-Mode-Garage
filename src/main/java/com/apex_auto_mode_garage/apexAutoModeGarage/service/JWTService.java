package com.apex_auto_mode_garage.apexAutoModeGarage.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;


@Service
public class JWTService {

    private final String secretKey;

    public JWTService() {
        try {
            SecretKey key = KeyGenerator.getInstance("HmacSHA256").generateKey();
            secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public String getJWTToken(String userName, String userRole, String userId) {

        HashMap<String, Object> claims = setClaims(userRole, userId);

        return Jwts.builder()
                .header()
                .add("typ","JWT")
                .and()
                .claims()
                .add(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .and()
                .signWith(getKey())
                .compact();
    }

    public String getRefreshToken(String userName, String userRole, String userId) {

        HashMap<String, Object> claims = setClaims(userRole, userId);

        return Jwts.builder()
                .header()
                .add("typ","JWT")
                .and()
                .claims()
                .add(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
                .and()
                .signWith(getKey())
                .compact();
    }

    private HashMap<String, Object> setClaims(String userRole, String userId){
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role",userRole);
        claims.put("userId",userId);
        return claims;
    }

    private SecretKey getKey() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        SecretKey key = Keys.hmacShaKeyFor(bytes);
        System.out.println("keys is : "+key);
        return key;
    }

    public String getUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String getNewAccessToken(String refreshToken){
        Claims claims = extractClaims(refreshToken, Function.identity());

        String userId = claims.get("userId", String.class);
        String userName = claims.get("sub", String.class);
        String userRole = claims.get("role", String.class);

        //generate a new JJwt token
        return this.getJWTToken(userName,userRole,userId);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        System.out.println("claims  : "+claims);
        return  claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = getUsername(token);
        return userName.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token,Claims::getExpiration);
    }

}
