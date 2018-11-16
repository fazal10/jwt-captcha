package com.jwt;

import java.util.Calendar;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@RestController
public class GenerateJWT {
	
	@RequestMapping("/generateToken")
	public String generateToken() {
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, 50);
		    Algorithm algorithm = Algorithm.HMAC256("app-secret");
		    String token = JWT.create()
		        .withIssuer("app")
		        .withExpiresAt(cal.getTime())
		        .sign(algorithm);
		    return token;
		} catch (JWTCreationException exception){
			exception.printStackTrace();
			return exception.toString();
		}
	}
	
	@RequestMapping("/validateToken")
	public String validateToken(@RequestParam(name="token") String token) {
		try {
		    Algorithm algorithm = Algorithm.HMAC256("app-secret");
		    JWTVerifier verifier = JWT.require(algorithm)
		        .withIssuer("app")
		        .build(); //Reusable verifier instance
		    DecodedJWT jwt = verifier.verify(token);
		    return jwt.toString();
		} catch (JWTVerificationException exception){
			exception.printStackTrace();
			return exception.toString();
		}
	}

}