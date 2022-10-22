package com.example.util;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.entity.UserPrincipal;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	private static final String SECRET_KEY = "vrerfewfewkpokpokfwdcegrgebveberberberberbberberbjj";
	// milliseconds
	private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000;

	private static JwtBuilder jwtBuilder = Jwts.builder();
	private static JwtParserBuilder jwtParserBuilder = Jwts.parserBuilder();

	public String generateAccessToken(UserPrincipal user) {
		String token = null;

		try {
			Map<String, Object> rawMap = new HashMap<>();
			rawMap.put("id", user.getId());
			rawMap.put("username", user.getUsername());
			rawMap.put("authorities", user.getAuthorities());

			jwtBuilder.setClaims(rawMap);
			jwtBuilder.setExpiration(
					new Date(System.currentTimeMillis() + EXPIRE_DURATION));
			jwtBuilder.setIssuedAt(new Date());
			jwtBuilder.signWith(getSigningKey(), SignatureAlgorithm.HS256);

			token = jwtBuilder.compact();

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return token;
	}

	public Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + 864000000);
	}

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public boolean verifyAccessToken(String token) {
		try {
			jwtParserBuilder.setSigningKey(getSigningKey()).build().parseClaimsJws(token);
			return true;
		} catch (ExpiredJwtException ex) {
			logger.error("JWT expired", ex.getMessage());
		} catch (IllegalArgumentException ex) {
			logger.error("Token is null, empty or only whitespace", ex.getMessage());
		} catch (MalformedJwtException ex) {
			logger.error("JWT is invalid", ex);
		} catch (UnsupportedJwtException ex) {
			logger.error("JWT is not supported", ex);
		} catch (Exception ex) {
			logger.error("Signature validation failed");
		}

		return false;
	}

	private Claims parseClaims(String token) {
		return jwtParserBuilder.setSigningKey(getSigningKey()).build()
				.parseClaimsJws(token).getBody();
	}

	public UserPrincipal getUserFromToken(String token) {
		UserPrincipal user = new UserPrincipal();
		try {
			Claims claims = parseClaims(token);
			if (claims != null) {
				user.setId(Long.valueOf(claims.get("id").toString()));
				user.setUsername((String) claims.get("username"));
				user.setAuthorities((Collection) claims.get("authorities"));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());

		}
		return user;
	}

}
