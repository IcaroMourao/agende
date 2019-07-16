package br.com.icaro.agende.repository;


import org.springframework.data.repository.CrudRepository;

import br.com.icaro.agende.model.User;


public interface UserRepository extends CrudRepository<User, String> {

	User findByUsernameAndPassword(String username, String password);

	User findByUsername(String username);
	
}
