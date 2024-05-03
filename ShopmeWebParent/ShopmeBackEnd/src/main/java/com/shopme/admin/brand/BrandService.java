package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Service
public class BrandService {

	@Autowired
	private BrandRepository repo; 
	
	
	public List<Brand> listAll() {
		
		return (List<Brand>) repo.findAll(); 
	}
	
	
	public Brand save(Brand brand) {
		return repo.save(brand); 
	}
	
	
	public String checkUnique(Integer id, String name) {
		
		boolean isCreatingNew = (id == null || id == 0);
		
		Brand brandByName = repo.findByName(name); 
		
		if(isCreatingNew) {
			
			if(brandByName != null) {
				return "DuplicateName"; 
			}
		} else {
			
			if(brandByName != null && brandByName.getId() != id) {
				return "DuplicateName"; 
			}
			
		}
		
		return "OK"; 
	}
	
	
	public Brand get(Integer id) throws BrandNotFoundException {
		// TODO Auto-generated method stub
		try {
			
			return repo.findById(id).get();
		} catch(NoSuchElementException ex) {
			
			throw new BrandNotFoundException("Could not find any Brand with ID " + id); 
		}
	}
	
	
	public void delete(Integer id) throws BrandNotFoundException {
		
		Long countById = repo.countById(id); 
		
		if(countById == 0 || countById == null) {
			throw new BrandNotFoundException("Could not find brand with ID: " + id); 
		}
		
		repo.deleteById(id);
	}
}
