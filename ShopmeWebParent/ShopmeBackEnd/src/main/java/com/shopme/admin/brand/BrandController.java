package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.User;


@Controller
public class BrandController {

	@Autowired
	private BrandService service; 
	
	@Autowired
	private CategoryService categoryService; 
	
	@GetMapping("/brands")
	public String listFirstPage(Model model) {
		
		return listByPage(1, model,"name", "asc", null); 
		
	}
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model, 
			@Param("sortField") String sortField, @Param("sortDir") String sortDir, 
			@Param("keyword") String keyword) {
		
		System.out.println("Sort Field: " + sortField); 
		System.out.println("Sort Order: " + sortDir); 
		
		Page<Brand> page = service.listByPage(pageNum, sortField, sortDir, keyword); 
		
		List<Brand> listBrands = page.getContent(); 
		
		long startCount = (pageNum - 1) * BrandService.BRANDS_PER_PAGE + 1; 
		long endCount = startCount + BrandService.BRANDS_PER_PAGE - 1; 
		
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements(); 
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc"; 
		
		model.addAttribute("currentPage", pageNum); 
		model.addAttribute("startCount", startCount); 
		model.addAttribute("endCount", endCount); 
		model.addAttribute("totalPages", page.getTotalPages()); 
		model.addAttribute("totalItems", page.getTotalElements()); 
		
		model.addAttribute("listBrands", listBrands); 
		
		model.addAttribute("sortField", sortField); 
		model.addAttribute("sortDir", sortDir); 
		model.addAttribute("reverseSortDir", reverseSortDir); 
		model.addAttribute("keyword", keyword); 
		
		return "brands/brands"; 
		
	}
	
	
	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm(); 
		
		model.addAttribute("listCategories", listCategories); 
		model.addAttribute("brand", new Brand()); 
		model.addAttribute("pageTitle", "Create New Brand"); 
		
		return "brands/brand_form"; 
		
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(Brand brand, 
			@RequestParam("fileImage") MultipartFile multipartFile, 
			RedirectAttributes ra) throws IOException {
		
		if(!multipartFile.isEmpty()) {
		
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); 
			brand.setLogo(fileName); 
			
			Brand savedBrand = service.save(brand); 
			String uploadDir = "../brand-logo/" + savedBrand.getId(); 
			FileUploadUtil.cleanDir(uploadDir); 
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile); 
		
		}else {
			service.save(brand); 
		}
		
		ra.addFlashAttribute("message", "The brand has been saved successfully."); 
		
		return "redirect:/brands"; 
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, Model model, 
			RedirectAttributes ra) {
		
		
		try {
			
			Brand brand = service.get(id); 
			List<Category> listCategories = categoryService.listCategoriesUsedInForm(); 

			model.addAttribute("brand", brand); 
			
			model.addAttribute("listCategories", listCategories); 
			
			model.addAttribute("pageTitle", "Edit Brand (ID: " + id + ")"); 
			
			return "brands/brand_form"; 
		
		} catch (BrandNotFoundException ex) {
			
			ra.addFlashAttribute("message", ex.getMessage()); 
			
			return "redirect:/brands"; 
		}
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		
		try {
			
			service.delete(id); 
			
			String brandDir = "../brand-logo/" + id; 
			FileUploadUtil.removeDir(brandDir); 
			
			redirectAttributes.addFlashAttribute("message", "The Brand ID " + id + " has been deleted successfully."); 
			
		
		} catch (BrandNotFoundException ex) {
			
			redirectAttributes.addFlashAttribute("message", ex.getMessage()); 
			
		}
		return "redirect:/brands"; 
	}
	
}
