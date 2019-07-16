package br.com.icaro.agendesecutiry.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import br.com.icaro.agende.service.exeception.AuthenticationFailedException;

public class JWTAuthenticationFilter extends GenericFilterBean {
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			Authentication authentication = JWTUtils.getAuthentication((HttpServletRequest) request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(request, response);
		} catch (AuthenticationFailedException e) {
			response.setCharacterEncoding("UTF-8");
			HttpServletResponse HSResponse = (HttpServletResponse) response;
			HSResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			HSResponse.getWriter().print("{\n\t\"status\": 401,\n\t\"error\": \"Unauthorized\",\n\t\"message\": \"Token inválido\"\n}");
		}
	}
}