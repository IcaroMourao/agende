package br.com.icaro.agende.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.icaro.agende.model.User;
import br.com.icaro.agende.repository.UserRepository;

@Service
public class LoginService {

	@Autowired
	private UserRepository userRepository;

	private User userLoggedIn;

	public User login(String username, String password) {
		userLoggedIn = userRepository.findByUsernameAndPassword(username, password);
		return userLoggedIn;
	}

	public User getUsuario() {
		return userLoggedIn;
	}

}
