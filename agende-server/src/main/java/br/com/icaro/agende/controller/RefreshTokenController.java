package br.com.icaro.agende.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.icaro.agende.service.exeception.AuthenticationFailedException;
import br.com.icaro.agendesecutiry.jwt.JWTUtils;

@RestController
@RequestMapping("/api/app/token/refresh")
public class RefreshTokenController {

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> refresh(@RequestBody String refreshToken) throws AuthenticationFailedException {
		return new ResponseEntity<String>(JWTUtils.refresh(refreshToken), HttpStatus.OK);
	}

}
