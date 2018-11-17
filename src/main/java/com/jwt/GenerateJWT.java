package com.jwt;

import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

@RestController
@ConfigurationProperties(prefix = "app")
public class GenerateJWT {
	
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
	
    @RequestMapping("/")
    public ModelAndView displayArticle(Map<String, Object> model) {

        model.put("title", "hello");

        return new ModelAndView("index", model);
    }
	
	@RequestMapping("/generateToken")
	public ModelAndView generateToken(Map<String, Object> model, HttpServletResponse response) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, 5);
		    Algorithm algorithm = Algorithm.HMAC256(this.secret);
		    String token = JWT.create()
		        .withIssuer(this.issuer)
		        .withExpiresAt(cal.getTime())
		        .sign(algorithm);
		    response.addCookie(new Cookie("token", token));
		    model.put("title", token);
		    return new ModelAndView("index", model);
		} catch (JWTCreationException exception){
			exception.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping("/ajax/validateToken")
	public String validateToken() {
		return "token is valid";
	}

}