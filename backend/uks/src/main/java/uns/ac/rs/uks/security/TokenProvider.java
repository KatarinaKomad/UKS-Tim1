package uns.ac.rs.uks.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uns.ac.rs.uks.exception.InvalidAccessTokenException;
import uns.ac.rs.uks.model.User;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;


@Service
public class TokenProvider {

    @Value("${app.auth.appName}")
    private String appName;

    @Value("${app.auth.tokenExpirationSeconds}")
    private Integer tokenExpirationSeconds;

    @Value("${app.auth.tokenSecret}")
    private String tokenSecret;

    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(tokenExpirationSeconds);

        return Jwts.builder()
                .setIssuer(appName)
                .setSubject(user.getEmail())
                .setAudience("web")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiresAt))
                .claim("role", user.getRoleNames())
                .signWith(getKey())
                .compact();
    }


    public String readTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasLength(authHeader) && authHeader.startsWith("Bearer "))
            return authHeader.substring(7);

        return null;
    }

    public String getUsernameFromToken(String token) {
        try {
            return readClaims(token).getSubject();
        } catch (ExpiredJwtException ex) {
            throw new InvalidAccessTokenException("Token expired");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Date getExpirationDateFromToken(String token) {
        try {
            return readClaims(token).getExpiration();
        } catch (ExpiredJwtException ex) {
            throw new InvalidAccessTokenException("Token expired");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Claims readClaims(String token) {
        JwtParser parser = Jwts.parserBuilder().setSigningKey(getKey()).build();
        return parser.parseClaimsJws(token).getBody();
    }

    private Key getKey() {
        byte[] keyBytes = tokenSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username != null && username.equals(userDetails.getUsername());
    }

}
