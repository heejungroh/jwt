package com.cos.jwt.domain.person;

import org.springframework.data.jpa.repository.JpaRepository;

// < , PK타입>
public interface PersonRepository extends JpaRepository<Person, Integer>{

	Person findByUsernameAndPassword(String username, String password);
	
}
