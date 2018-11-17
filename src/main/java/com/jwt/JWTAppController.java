package com.jwt;

import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@RestController
@ConfigurationProperties(prefix = "app")
public class JWTAppController {
	
	private String secret;
	private String issuer;
	private int tokenvalidity;
	

	public int getTokenvalidity() {
		return tokenvalidity;
	}

	public void setTokenvalidity(int tokenvalidity) {
		this.tokenvalidity = tokenvalidity;
	}

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
    public ModelAndView index(Map<String, Object> model, @RequestParam(required = false, name = "message") String message) {

        model.put("message", message);

        return new ModelAndView("index", model);
    }
	
	@RequestMapping("/generateToken")
	public void generateToken(Map<String, Object> model, HttpServletResponse response) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.SECOND, this.tokenvalidity);
		    Algorithm algorithm = Algorithm.HMAC256(this.secret);
		    String token = JWT.create()
		        .withIssuer(this.issuer)
		        .withExpiresAt(cal.getTime())
		        .sign(algorithm);
		    response.addCookie(new Cookie("token", token));
		    response.sendRedirect("/ajax/page1");
		} catch (Exception exception){
			exception.printStackTrace();
		}
	}
	
	@RequestMapping("/ajax/page1")
	public ModelAndView page1(Map<String, Object> model, @CookieValue("token") String token) {
		model.put("token", token);
		model.put("currentTime", Calendar.getInstance().getTime());
		model.put("expiresAt", JWT.decode(token).getExpiresAt());	
		return new ModelAndView("page1", model);
	}
	
	@RequestMapping("/ajax/page2")
	public ModelAndView page2(Map<String, Object> model, @CookieValue("token") String token) {
		model.put("token", token);
		model.put("currentTime", Calendar.getInstance().getTime());
		model.put("expiresAt", JWT.decode(token).getExpiresAt());		
		return new ModelAndView("page2", model);
	}

}