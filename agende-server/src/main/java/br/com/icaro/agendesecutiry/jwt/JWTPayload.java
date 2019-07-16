package br.com.icaro.agendesecutiry.jwt;

import java.util.Date;

public class JWTPayload {

	private String subject;
	private Date expiration;
	
	public JWTPayload(String subject, Date expiration) {
		this.subject = subject;
		this.expiration = expiration;
	}
	
	public String getSubject() {
		return this.subject;
	}
	
	public Date getExpiration() {
		return this.expiration;
	}

	public void setSubject(String subject) {
		this.subject = subject;		
	}
}
