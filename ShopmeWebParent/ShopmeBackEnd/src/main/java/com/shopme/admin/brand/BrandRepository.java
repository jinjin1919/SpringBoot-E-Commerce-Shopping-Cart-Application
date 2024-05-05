package com.shopme.admin.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

public interface BrandRepository extends CrudRepository<Brand, Integer>, PagingAndSortingRepository<Brand, Integer> {

	
	@Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
	public Page<Brand> search(String keyword, Pageable pageable); 
	
	public Brand findByName(String name); 
	
	public Long countById(Integer id); 
}
