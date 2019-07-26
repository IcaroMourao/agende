package br.com.icaro.agende.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.icaro.agende.model.User;
import br.com.icaro.agende.service.UserService;

@RestController
@RequestMapping("/api/app/login")
public class LoginController {

	@Autowired
	private UserService usuarioService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> getUser(Principal principal) {
		try {
			User user = usuarioService.getByUsername(principal.getName());
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
		}
	}

}
