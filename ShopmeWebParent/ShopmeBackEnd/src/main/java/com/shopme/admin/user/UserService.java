package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo; 
	
	
	@Autowired
	private RoleRepository roleRepo; 
	
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder; 
	
	public List<User> listAll() {
		
		return (List<User>) userRepo.findAll(); 
	}
	
	
	public List<Role> listRoles() {
		
		return (List<Role>) roleRepo.findAll(); 
	}


	public void save(User user) {
		
		encodePassword(user); 
		userRepo.save(user); 
		
	}
	
	private void encodePassword(User user) {
		
		String encodedPassword = passwordEncoder.encode(user.getPassword()); 
		user.setPassword(encodedPassword);
		
	}
	
	public boolean isEmailUnique(String email) {
		
		User user = userRepo.getUserByEmail(email); 
		
		return user == null; 
		
//		if(user != null) {
//			return false; 
//		}
//		
//		return true; 
		
	}


	public User get(Integer id) throws UserNotFoundException {
		// TODO Auto-generated method stub
		try {
			
			return userRepo.findById(id).get();
		} catch(NoSuchElementException ex) {
			
			throw new UserNotFoundException("Could not find any user with ID " + id); 
		}
	}
	

}
