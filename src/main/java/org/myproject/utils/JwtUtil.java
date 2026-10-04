package org.myproject.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretStr;

    @Value("${jwt.expire}")
    private long expireTime;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secretStr.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成JWT令牌
     * @param userId 用户id
     * @param username 用户名
     * @return jwt token
     */
    public String generateToken(String userId, String username) {
        long now = System.currentTimeMillis();
        Date expireDate = new Date(now + expireTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId)
                .setIssuedAt(new Date(now))
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 解析token，获取Claims（载荷）
     * @param token jwt字符串
     * @return Claims
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 校验token是否合法（签名、过期）
     * @param token jwt
     * @return true=有效，false=无效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (SignatureException e) {
            System.err.println("签名校验失败");
        } catch (ExpiredJwtException e) {
            System.err.println("token已过期");
        } catch (MalformedJwtException e) {
            System.err.println("token格式非法");
        } catch (UnsupportedJwtException e) {
            System.err.println("不支持的token");
        } catch (IllegalArgumentException e) {
            System.err.println("token内容为空");
        }
        return false;
    }
}