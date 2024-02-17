package com.shopme.admin.category.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	
	@Autowired
	CategoryService categoryService; 
	
	@GetMapping("/categories")
	public String listByCategories(Model model) {
		
		List<Category> listCategories = categoryService.listAllCategories(); 
		
		model.addAttribute("listCategories", listCategories); 
		
		return "categories/categories"; 
	}

}