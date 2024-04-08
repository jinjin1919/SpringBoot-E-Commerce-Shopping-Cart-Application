package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.poi.util.SystemOutLogger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.brand.BrandRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests {

	@Autowired
	private BrandRepository brandRepo; 
	
	@Autowired
	private CategoryRepository repo; 
	
	
	@Test
	public void testCreateBrands() {
		
		Brand brand = new Brand("Acer", "brand-logo.png"); 
		brand.getCategories().add(repo.findByName("Laptops")); 
	//	brand.getCategories().add(repo.findByName("Internal Hard Drives")); 
		
		brandRepo.save(brand); 
		
	}
	
	@Test
	public void getBrandById() {
		
		Brand brand = brandRepo.findById(2).get();
		
		System.out.println(brand.getName());
		
		for(Category cat : brand.getCategories()) {
			System.out.println(cat.getName());
		}
		
		assertThat(brand.getCategories().size()).isGreaterThan(0); 
		
	}
	
	@Test
	public void updateBrand() {
		
		Brand brand = brandRepo.findByName("Samsung"); 
		
		brand.setName("Samsung Electronics");
		brandRepo.save(brand); 
		
		System.out.println(brand.getName());
	}
	
	@Test
	public void deleteBrand() {
		
//		brandRepo.delete(brandRepo.findByName("Apple"));
//		
//		Brand apple = brandRepo.findByName("Apple"); 
//		
//		assertThat(apple).isNull();
	}
}
