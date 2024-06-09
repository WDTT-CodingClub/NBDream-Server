package nbdream.auth.infrastructure;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import nbdream.auth.exception.ExpiredTokenException;
import nbdream.auth.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${jwt.access-token.expire-length}")
    private long accessTokenExpireLength;
    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenExpireLength;
    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${jwt.token.secret-key}") String key) {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        this.secretKey =  Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long payload) {
        return createToken(payload, accessTokenExpireLength);
    }

    public String createRefreshToken(Long payload) {
        return createToken(payload, refreshTokenExpireLength);
    }

    public String createToken(Long payload, long expireLength) {
        Claims claims = Jwts.claims().setSubject(payload.toString());
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public String getPayload(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            throw new InvalidTokenException();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            validateTokenExpiration(claims.getBody().getExpiration());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private void validateTokenExpiration(Date tokenExpirationDate) {
        if (tokenExpirationDate.before(new Date())) {
            throw new ExpiredTokenException();
        }
    }
}
