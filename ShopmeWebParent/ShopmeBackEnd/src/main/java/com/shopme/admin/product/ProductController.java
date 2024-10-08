package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {

	@Autowired
	private ProductService productService; 
	
	@Autowired
	private BrandService brandService; 
	
	@GetMapping("/products")
	public String listAll(Model model) {
		
		List<Product> listProducts = productService.listAll(); 
		
		model.addAttribute("listProducts", listProducts);
		
		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String newProduct(Model model) {
		
		List<Brand> listBrands = brandService.listAll(); 
		
		Product product = new Product(); 
		product.setEnabled(true); 
		product.setInStock(true); 
		
		model.addAttribute("product", product); 
		model.addAttribute("listBrands", listBrands); 
		model.addAttribute("pageTitle", "Create New Product"); 
		
 		
		return "products/product_form"; 
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes ra, 
			@RequestParam("fileImage") MultipartFile mainImageMultipart, 
			@RequestParam("extraImage") MultipartFile[] extraImageMultiparts) throws IOException {
		
		setMainImageName(mainImageMultipart, product); 
		setExtraImageNames(extraImageMultiparts, product); 
		
			
		Product savedProduct = productService.save(product);
		
		saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct); 
		
		
		
	
		
		ra.addFlashAttribute("message", "The product has been saved successfully."); 
		
		return "redirect:/products"; 
	}
	
	private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
			Product savedProduct) throws IOException {
		// TODO Auto-generated method stub
		if(!mainImageMultipart.isEmpty()) {
			
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename()); 
			String uploadDir = "../product-images/" + savedProduct.getId() ; 
			
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}
		
		if (extraImageMultiparts.length > 0) {
			
			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
			
			for(MultipartFile multipartFile : extraImageMultiparts) {
				
				if (multipartFile.isEmpty()) continue; 
				
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); 
				
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
				
			}
		}
	}

	private void setExtraImageNames(MultipartFile[] extraImageMultiparts, Product product) {
		
		if (extraImageMultiparts.length > 0) {
			
			for(MultipartFile multipartFile : extraImageMultiparts) {
				
				if (!multipartFile.isEmpty()) {
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()); 
					product.addExtraImage(fileName); 
				}
			}
		}
		
	}

	private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
		if(!mainImageMultipart.isEmpty()) {
			
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename()); 
			product.setMainImage(fileName); 
		}
	}
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String enableProductStatus(@PathVariable("id") Integer id, @PathVariable("status") Boolean enabled, 
			RedirectAttributes ra) {
		
		productService.updateProductEnabledStatus(id, enabled); 
		
		String status = enabled? "enabled": "disabled"; 
		String message = "The Product ID :" + id + " has been " + status; 
		ra.addFlashAttribute("message", message); 
		
		return "redirect:/products"; 
		
	}
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		
		try {
		
			productService.delete(id); 
			
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productImagesDir = "../product-images/" + id; 
			
			FileUploadUtil.removeDir(productExtraImagesDir); 
			FileUploadUtil.removeDir(productImagesDir); 
			
			redirectAttributes.addFlashAttribute("message", "The Product ID " + id + " has been deleted successfully.");
		
		} catch (ProductNotFoundException ex) {
			
			redirectAttributes.addFlashAttribute("message", ex.getMessage()); 
		}
		
		return "redirect:/products"; 
	}
}
