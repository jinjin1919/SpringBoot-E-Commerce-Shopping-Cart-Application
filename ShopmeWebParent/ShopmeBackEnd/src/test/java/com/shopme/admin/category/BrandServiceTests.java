package com.shopme.admin.category;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.admin.brand.BrandRepository;
import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class BrandServiceTests {
	
	@MockBean
	private BrandRepository repo; 
	
	@InjectMocks
	private BrandService service; 
	
	
	@Test
	public void testCheckNewModeReturnDuplicate() {
		Integer id = null; 
		String name = "Acer"; 
	
		
		Brand brand = new Brand(name); 
		
		Mockito.when(repo.findByName(name)).thenReturn(brand); 
	
		
		String result = service.checkUnique(id, name); 
		
		assertThat(result).isEqualTo("DuplicateName"); 
	}

}
