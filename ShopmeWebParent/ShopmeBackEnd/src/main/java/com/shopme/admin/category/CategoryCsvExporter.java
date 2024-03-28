package com.shopme.admin.category;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Category;

import jakarta.servlet.http.HttpServletResponse;

public class CategoryCsvExporter extends AbstractExporter{

public void export(List<Category> listCat, HttpServletResponse response) throws IOException {
		
		super.setResponseHeader(response, "text/csv", ".csv", "categories");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE); 
		
		String[] csvHeader = {"Category ID", "Name"};
		String[] fieldMapping = {"id", "name"}; 
		
		csvWriter.writeHeader(csvHeader); 
		
		for(Category cat: listCat) {
			cat.setName(cat.getName().replace("--", "  ")); 
			csvWriter.write(cat, fieldMapping); 
		}
		
		
		csvWriter.close(); 
	}
}
