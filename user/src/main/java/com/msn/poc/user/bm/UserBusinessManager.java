package com.msn.poc.user.bm;

import org.apache.commons.lang3.StringUtils;

import com.msn.poc.user.entity.User;
import com.msn.poc.user.model.UserRegistrationDetails;
import com.msn.poc.user.repository.UserRepository;

public class UserBusinessManager {
	public boolean registerUser(UserRegistrationDetails registrationData,UserRepository userRepository){
		if (userRepository.findOne(registrationData.getUserId())==null) {
			User user = new User();
			user.setCity(registrationData.getCity());
			user.setContactNo(registrationData.getContactNo());
			user.setCountry(registrationData.getCountry());
			user.setDateOfBirth(registrationData.getDateOfBirth());
			user.setEmail(registrationData.getEmail());
			user.setFirstName(registrationData.getFirstName());
			user.setLastName(registrationData.getLastName());
			user.setMiddleName(registrationData.getMiddleName());
			user.setPassword(registrationData.getPassword());
			user.setPin(registrationData.getPin());
			user.setSecurityAnswer(registrationData.getSecurityAnswer());
			user.setSecurityQues(registrationData.getSecurityQuestion());
			user.setState(registrationData.getState());
			user.setStreetOne(registrationData.getStreetOne());
			user.setStreetTwo(registrationData.getStreetTwo());
			user.setUserId(registrationData.getUserId());
			userRepository.save(user);
			return true;
		}else{
			return false;
		}
	}
	
	public boolean authenticateUser(String userId, String password, UserRepository userRepository) {
		User user = userRepository.findOne(userId);
		return user != null && StringUtils.equals(password, user.getPassword());
	}


}
