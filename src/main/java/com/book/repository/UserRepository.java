package com.book.repository;

import org.springframework.data.repository.CrudRepository;

import com.book.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findByUsername(String username);

	User findByEmail(String email);
}
