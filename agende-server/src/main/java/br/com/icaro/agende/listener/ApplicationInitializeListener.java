package br.com.icaro.agende.listener;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.icaro.agende.model.User;
import br.com.icaro.agende.service.UserService;

@Component
public class ApplicationInitializeListener {

	@Autowired
	private UserService userService;

	@EventListener(classes = ApplicationReadyEvent.class)
	private void addAdmin() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		if (userService.getByUsername("admin") == null) {
			User usr = new User();
			usr.setUsername("admin");
			usr.setPassword("admin");
			userService.add(usr);
		}
	}
}
