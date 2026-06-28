package cn.edu.uibe.courseops.auth;

import cn.edu.uibe.courseops.config.CourseOpsProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final CourseOpsProperties properties;

    public JwtService(CourseOpsProperties properties) {
        this.properties = properties;
    }

    public String issueToken(String userId, String role) {
        var now = Instant.now();
        var key = Keys.hmacShaKeyFor(properties.getJwtSecret().getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(60 * 60 * 12)))
                .signWith(key)
                .compact();
    }

    public Claims parseToken(String token) {
        var key = Keys.hmacShaKeyFor(properties.getJwtSecret().getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
