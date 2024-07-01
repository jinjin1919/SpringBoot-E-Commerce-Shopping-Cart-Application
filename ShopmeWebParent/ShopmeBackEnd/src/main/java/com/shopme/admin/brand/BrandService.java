package com.shopme.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;

@Service
public class BrandService {
	
	public static final int BRANDS_PER_PAGE = 5; 

	@Autowired
	private BrandRepository repo; 
	
	
	public List<Brand> listAll() {
		
		return (List<Brand>) repo.findAll(Sort.by("name")); 
	}
	
	
	public Page<Brand> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {
		
		Sort sort = Sort.by(sortField); 
		
		sort = sortDir.equals("asc") ? sort.ascending(): sort.descending(); 
		
		PageRequest pageable = PageRequest.of(pageNumber - 1, BRANDS_PER_PAGE, sort); 
		
		if(keyword != null) {
			return repo.search(keyword, pageable); 
		}
		
		return repo.findAll(pageable); 
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
