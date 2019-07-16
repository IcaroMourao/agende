package br.com.icaro.agende.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.icaro.agende.model.User;
import br.com.icaro.agende.repository.UserRepository;
import br.com.icaro.agende.utils.Utils;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository; 
	
	public User getByUsername(String username) {
		if (username != null)
			return userRepository.findByUsername(username);
		return null;
	}
	
	public User add(User user) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		User withEncryptefPassword = new User();
		withEncryptefPassword.setUsername(user.getUsername());
		withEncryptefPassword.setPassword(Utils.cripty(user.getPassword()));;
		return userRepository.save(withEncryptefPassword);
	}

	public Collection<User> getAll() {
		return (Collection<User>) userRepository.findAll();
	}

}
