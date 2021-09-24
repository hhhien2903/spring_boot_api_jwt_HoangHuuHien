package com.example.util;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.authen.UserPrincipal;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component

public class JwtUtil {

	private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	private static final String SECRET = "SECRET_KEY!_2A9ED2E12A7D35E13D633838B2BB3";

	public String generateToken(UserPrincipal user) {
		String token = null;
		try {
			JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

			builder.claim("data", user);
			builder.expirationTime(generateExpirationDate());
			JWTClaimsSet claimsSet = builder.build();

			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256),
					claimsSet);
			JWSSigner signer = new MACSigner(SECRET.getBytes());
			signedJWT.sign(signer);

			token = signedJWT.serialize();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return token;
	}

	public Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + 864000000);
	}

}
