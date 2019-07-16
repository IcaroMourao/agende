package br.com.icaro.agende.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.icaro.agende.model.User;
import br.com.icaro.agende.service.UserService;


@RestController
@RequestMapping("/api/app/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> addUser(@RequestBody User user) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		return new ResponseEntity<User>(userService.add(user), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Collection<User>> getAll() {
		return new ResponseEntity<Collection<User>>(userService.getAll(), HttpStatus.OK);
	}

}
