package com.shopme.admin.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	
	@Autowired
	CategoryRepository categoryRepo; 
	
	public List<Category> listAllCategories() {
		
		return (List<Category>) categoryRepo.findAll(); 
	}
	
	
}
