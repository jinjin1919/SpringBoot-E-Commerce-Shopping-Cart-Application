package com.shopme.admin.product;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

@Service
@Transactional
public class ProductService {
	
	@Autowired
	private ProductRepository repo;
	
	public List<Product> listAll() {
		
		return (List<Product>) repo.findAll(); 
	}
	
	public Product save(Product product) {
		
		if (product.getId() == null) {
			product.setCreatedTime(new Date());
		}
		
		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-"); 
			product.setAlias(defaultAlias);
		} else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}
		
		product.setUpdatedTime(new Date());
		
		return repo.save(product); 
		
	}
	
	public String checkUnique(Integer id, String name) {
		
		boolean isCreatingNew = (id == null || id == 0);
		
		Product productByName = repo.findByName(name); 
		
		if(isCreatingNew) {
			
			if(productByName != null) {
				return "DuplicateName"; 
			}
		} else {
			
			if(productByName != null && productByName.getId() != id) {
				return "DuplicateName"; 
			}
			
		}
		
		return "OK"; 
	}
	
	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		
		repo.updateEnabledStatus(id, enabled);
	}
	
	public void delete(Integer id) throws ProductNotFoundException {
		
		Long countById = repo.countById(id); 
		
		if(countById == 0 || countById == null) {
			throw new ProductNotFoundException("Could not find category with ID: " + id); 
		}
		
		repo.deleteById(id);
	}
	
	

}
