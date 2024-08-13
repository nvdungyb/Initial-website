package com.practice.from_scratch.config;

import com.practice.from_scratch.dto.response.TokenDto;
import com.practice.from_scratch.entity.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTUtils {
    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);

    @Value("${jwtSecret}")
    private String jwtSecret;
    @Value("${accessTokenExpirationMs}")
    private long accessTokenExpirationMs;
    @Value("${refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;
    @Autowired
    private RedisTemplate<String, Object> template;

    public SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + accessTokenExpirationMs))
                .signWith(key())
                .compact();
    }

    public String getJwtToken(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        if (token != null && token.startsWith("Bearer "))
            return token.substring(7);

        return null;
    }

    public String getUsernameFromJwtToken(String jwt) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    public Date getExpiration(String jwt) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(jwt)
                .getBody()
                .getExpiration();
    }

    public String generateToken(UserDetails userDetails, long expirationTime) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(val -> val.getAuthority())
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationTime))
                .claim("roles", roles)
                .signWith(key())
                .compact();
    }

    public TokenDto createTokens(UserDetails userDetails) {
        String accessToken = generateToken(userDetails, accessTokenExpirationMs);
        String refreshToken = generateToken(userDetails, refreshTokenExpirationMs);

        // Save latest accessToken and refreshToken in to redis.
        // Remove current refreshToken, accessToken and save the newest one.
        if (template.opsForSet().size(userDetails.getUsername()) != 0)
            template.delete(userDetails.getUsername());
        template.opsForSet().add(userDetails.getUsername(), accessToken);
        template.opsForSet().add(userDetails.getUsername(), refreshToken);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken).build();
    }
}
