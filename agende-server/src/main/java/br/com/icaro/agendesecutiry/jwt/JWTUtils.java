package br.com.icaro.agendesecutiry.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import br.com.icaro.agende.model.User;
import br.com.icaro.agende.service.UserService;
import br.com.icaro.agende.service.exeception.AuthenticationFailedException;
import br.com.icaro.agende.service.exeception.LoginFailedException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JWTUtils {

	private static final long ACCESS_TOKEN_EXPIRATION_TIME = 1_200_000; //10 min
	private static final long REFRESH_TOKEN_EXPIRATION_TIME = 86_400_000; //10 days

	private static final String SECRET = "resources/secret";

	private static final String AUTHORIZATION_HEADER_FIELD = "Authorization";
	private static final String TOKEN_FIELD_PREFIX = "Bearer";

	private static final Pattern tokenPattern = Pattern.compile("([\\w_-]+\\.[\\w_-]+\\.[\\w_-]+)");

	private static UserService userService;


	@Autowired
    public JWTUtils(UserService userService) {
		JWTUtils.userService = userService;
    }


	public static Authentication getAuthentication(HttpServletRequest request) throws AuthenticationFailedException {
		String token = request.getHeader(AUTHORIZATION_HEADER_FIELD);
		
		if (token != null) {
			JWTPayload payload = getTokenPayload(token.replace(TOKEN_FIELD_PREFIX, ""));
			if (isValidPayload(payload))
				return new UsernamePasswordAuthenticationToken(payload.getSubject(), null, (Collection<? extends GrantedAuthority>) Collections.emptyList());
		}
		return null;
	}


	public static String refresh(String refreshToken) throws AuthenticationFailedException {
		Matcher m = tokenPattern.matcher(refreshToken);
		if (m.find()) {
			JWTPayload refreshPayload = getTokenPayload(m.group());
			String subject = getRefreshSubject(refreshPayload.getSubject());
			refreshPayload.setSubject(subject);
			
			if (isValidPayload(refreshPayload)) {
				long now = System.currentTimeMillis();
				return generateToken(subject, now, ACCESS_TOKEN_EXPIRATION_TIME);
			}	
		}
		throw new AuthenticationFailedException("Token invalido");
	}


	public static void addAuthentication(HttpServletResponse response, String username) {
		long now = System.currentTimeMillis();
		String ACCESS_TOKEN = generateToken(username, now, ACCESS_TOKEN_EXPIRATION_TIME);
		String refreshSubject = getRefreshSubject(username);
		String REFRESH_TOKEN = generateToken(refreshSubject, now, REFRESH_TOKEN_EXPIRATION_TIME);

		String body = "{\n    \"refresh\": \"" + REFRESH_TOKEN + "\",\n    \"access\": \"" + ACCESS_TOKEN +  "\"\n}";

		try {
			response.getWriter().write(body);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			throw new LoginFailedException("O processo de login falhou! Tente novamente mais tarde.");
		}
	}


	private static boolean isValidPayload(JWTPayload payload) {
		User user = userService.getByUsername(payload.getSubject());
		Date now = new Date(System.currentTimeMillis());
		return user != null && payload.getExpiration().after(now);
	}


	private static String generateToken(String username, long now, long expirationTime) {
		String token = Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(now + expirationTime))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		return token;
	}


	private static JWTPayload getTokenPayload(String token) throws AuthenticationFailedException {
		try {
			String subject = Jwts.parser().setSigningKey(SECRET)
					.parseClaimsJws(token)
					.getBody()
					.getSubject();

			Date expiration = Jwts.parser().setSigningKey(SECRET)
					.parseClaimsJws(token)
					.getBody()
					.getExpiration();

			return new JWTPayload(subject, expiration);

		} catch (MalformedJwtException 
				| SignatureException
				| PrematureJwtException
				| UnsupportedJwtException e) {
			throw new AuthenticationFailedException("Token inválido");
		} catch (ExpiredJwtException e) {
			throw new AuthenticationFailedException("Token expirado");
		}
		
	}


	private static String getRefreshSubject(String subject) {
		String refreshSubject = "";
        for(int i = subject.length() - 1; i >= 0; i--)
            refreshSubject = refreshSubject + subject.charAt(i);
        return refreshSubject;
	}

}