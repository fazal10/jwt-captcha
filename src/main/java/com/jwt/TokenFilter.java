package com.jwt;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

@WebFilter(urlPatterns = "/ajax/*")
@ConfigurationProperties(prefix = "app")
public class TokenFilter implements Filter {
	
	private JWTVerifier verifier;
	private String secret;
	private String issuer;
	
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	    Algorithm algorithm = Algorithm.HMAC256(this.secret);
	    this.verifier = JWT.require(algorithm)
	        .withIssuer(this.issuer)
	        .build();
	}
 	
	@Override
	public void doFilter(ServletRequest request, javax.servlet.ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		boolean isTokenValidBoolean = false;
        HttpServletRequest req = (HttpServletRequest) request;
        
        if(req.getCookies() != null) {
	        for(Cookie c: req.getCookies()) {
	        	System.out.println(c.getName() + " -> " + c.getValue());
	        	if(c.getName().equals("token")) {
	        		isTokenValidBoolean = this.isTokenValid(c.getValue());
	        		System.out.println("token validity => " + this.isTokenValid(c.getValue()));
	        	}
	        }
        }
        
		if(isTokenValidBoolean){
			chain.doFilter(request, response);
		} else {
			 ((HttpServletResponse) response).sendRedirect("/?message=token expired");
		}

	}
	
	private boolean isTokenValid(String token) {
		try {
		    this.verifier.verify(token);
		    return true;
		} catch (JWTVerificationException exception){
			//exception.printStackTrace();
			return false;
		}
	}
 
}