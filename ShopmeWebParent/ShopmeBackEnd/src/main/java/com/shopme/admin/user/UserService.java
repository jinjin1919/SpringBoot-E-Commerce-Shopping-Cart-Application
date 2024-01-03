package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
@Transactional
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
		
		boolean isUpdatingUser = (user.getId() != null); 
		
		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get(); 
			
			if(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			}else {
				encodePassword(user); 
			}
		}else {
			encodePassword(user); 
		}
		
		userRepo.save(user); 
		
	}
	
	private void encodePassword(User user) {
		
		String encodedPassword = passwordEncoder.encode(user.getPassword()); 
		user.setPassword(encodedPassword);
		
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		
		User user = userRepo.getUserByEmail(email); 
		
		if(user == null) return true; 
		
		boolean isCreatingNew = (id == null); 
		
		if(isCreatingNew) {
			// in creating new mode 
			if(user != null) return false; 
		}else {
			
			if(user.getId() != id) {
				return false; 
			}
		}
		
		
		return true; 
		
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
	
	public void delete(Integer id) throws UserNotFoundException {
		
		Long countById = userRepo.countById(id); 
		
		if(countById == 0 || countById == null) {
			
			throw new UserNotFoundException("Could not find any user with ID " + id); 
		}
		
		userRepo.deleteById(id);
	}
	
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		
		userRepo.updateEnabledStatus(id, enabled);
	}
	

}
