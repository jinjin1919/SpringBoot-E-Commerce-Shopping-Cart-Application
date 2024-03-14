package com.shopme.admin.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	
	@Autowired
	CategoryRepository repo; 
	
	public List<Category> listAllCategories() {
		
		return (List<Category>) repo.findAll(); 
	}
	
	
	public List<Category> listCategoriesUsedInForm(){
		List<Category> categoriesUsedInForm = new ArrayList<>(); 
		
		Iterable<Category> categoriesInDB = repo.findAll(); 
		
		for(Category category: categoriesInDB) {
			if (category.getParent() == null) {
				categoriesUsedInForm.add(new Category(category.getName())); 
				
				Set<Category> children = category.getChildren(); 
				
				for(Category subCategory: children) {
					String name = "--" + subCategory.getName(); 
					categoriesUsedInForm.add(new Category(name)); 
					listChildren(subCategory, 1, categoriesUsedInForm); 
				}
			}
		}
		
		return categoriesUsedInForm; 
	}
	
	private void listChildren(Category parent, int subLevel, List<Category> categoriesUsedInForm) {
		
		int newSubLevel = subLevel + 1;  
		
		Set<Category> children = parent.getChildren(); 
		
		for(Category subCategory: children) {
			String name = ""; 
			for(int i=0; i < newSubLevel; i++) {
				
				name += "--"; 
			}
			name += subCategory.getName();
			categoriesUsedInForm.add(new Category(name)); 
			
			listChildren(subCategory, newSubLevel, categoriesUsedInForm); 
		}
	}
	
	
}
