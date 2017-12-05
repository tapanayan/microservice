package com.msn.poc.user.repository;

import org.springframework.data.repository.CrudRepository;

import com.msn.poc.user.entity.User;

public interface UserRepository extends CrudRepository<User, String> {

}
