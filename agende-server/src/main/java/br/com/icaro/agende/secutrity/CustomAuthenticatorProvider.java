package br.com.icaro.agende.secutrity;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import br.com.icaro.agende.model.User;
import br.com.icaro.agende.service.LoginService;
import br.com.icaro.agende.utils.Utils;


@Component
public class CustomAuthenticatorProvider implements AuthenticationProvider {

	@Autowired
	private LoginService loginService;
	private Logger logger = LoggerFactory.getLogger(CustomAuthenticatorProvider.class);

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		Object credentials = authentication.getCredentials();
		if (!(credentials instanceof String)) {
			throw new BadCredentialsException("Invalid password");
		}
		String password = credentials.toString();

		User user = null;
		try {
			user = loginService.login(username, Utils.cripty(password));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (user == null)
			throw new BadCredentialsException("Login ou senha invalidos");

		logger.debug("authenticate: " + user);

		Set<GrantedAuthority> autorizacoes = new HashSet<GrantedAuthority>();
		/*for (Perfil perfil : usuario.getPerfis()) {
			autorizacoes.addAll(perfil.getPermissoes());
		}*/
		return new UsernamePasswordAuthenticationToken(username, password, autorizacoes);
	}

	@Override
	public boolean supports(Class<?> auth) {
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}

}
