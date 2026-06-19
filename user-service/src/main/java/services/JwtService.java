package services;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.library.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	
	@Value("${jwt.secret}")
	private String secret;
	
	private SecretKey key() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}
	
	public String generateToken(User user) {
		 return Jwts.builder()
	                .subject(user.getEmail())
	                .claim("role", user.getRole().name())
	                .issuedAt(new Date())
	                .expiration(
	                    new Date(
	                        System.currentTimeMillis() + 86400000
	                    )
	                )
	                .signWith(
	                    key()
	                )
	                .compact();
	}
	
	public String extractUsername(String token) {

        return Jwts.parser()
                .verifyWith(
                    key()
                )
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            Date expiration = Jwts.parser()
                    .verifyWith(key())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
            return username.equals(userDetails.getUsername()) && expiration.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
