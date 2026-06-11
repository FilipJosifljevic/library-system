package services;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.library.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	
	private final String SECRET =
            "very-long-secret-key-for-library-system";
	
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
	                    Keys.hmacShaKeyFor(
	                        SECRET.getBytes()
	                    )
	                )
	                .compact();
	}
	
	public String extractUsername(String token) {

        return Jwts.parser()
                .verifyWith(
                    Keys.hmacShaKeyFor(
                        SECRET.getBytes()
                    )
                )
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
