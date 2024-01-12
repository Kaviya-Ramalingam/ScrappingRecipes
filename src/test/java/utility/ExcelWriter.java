package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {

	static int row = 0;

	public void writeToExcel(List<Object[]> recipeData) throws IOException {

		ZipSecureFile.setMinInflateRatio(0);
		 String filename = "scraped_recipes.xlsx"; // Filename variable
	        String filepath = "/Users/uvaraj/Downloads/scraped_recipes.xlsx"; // Filepath variable
	        XSSFWorkbook workbook;
		    File file = new File(filepath);

		    if (file.exists()) {
		        FileInputStream inputStream = new FileInputStream(file);
		        workbook = new XSSFWorkbook(inputStream);
		    } else {
		        workbook = new XSSFWorkbook();
		    }

		//XSSFWorkbook workbook = new XSSFWorkbook(filepath);
		XSSFSheet sheet = workbook.getSheet("Recipes");
		System.out.println("got existing recipews sheet");
		if (sheet == null) {
			sheet = workbook.createSheet("Recipes");
			
			 //createHeaderRow(sheet);
		}
		createHeaderRow(sheet);
		

	    int lastRow = sheet.getLastRowNum();
	    System.out.println(lastRow);
	    int rowNumber = lastRow == 0 ? 0 :lastRow + 1; // Start from the next row if there's existing data
	  

		// Add data to rows
		//int rowNumber = 1;
		XSSFCellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		

		for (Object[] rowData : recipeData) {

			XSSFRow row = sheet.createRow(rowNumber++);
			
			int cellNumber = 0;
			for (Object cellData : rowData) {
				XSSFCell cell = row.createCell(cellNumber++);
				if (cellData instanceof String)
					cell.setCellValue((String) cellData);
				if (cellData instanceof Integer)
					cell.setCellValue((Integer) cellData);
				if (cellData instanceof Boolean)
					cell.setCellValue((Boolean) cellData);
				cell.setCellStyle(style);
			}
		
		}

		// Save the workbook to a file

		FileOutputStream outputStream = new FileOutputStream(filepath);
		workbook.write(outputStream);
		workbook.close();
		System.out.println("Data saved to " + filepath);
	}
	private void createHeaderRow(XSSFSheet sheet) {
		
	    XSSFRow headerRow = sheet.createRow(0);
	    headerRow.createCell(0).setCellValue("RecipeID");
	    headerRow.createCell(1).setCellValue("Recipe Name");
	    headerRow.createCell(2).setCellValue("ingredients");
	    headerRow.createCell(3).setCellValue("PreparationMethod");
	    headerRow.createCell(4).setCellValue("NutrientValue");
	    headerRow.createCell(5).setCellValue("preparationTime");
	    headerRow.createCell(6).setCellValue("cookingTime");
	    headerRow.createCell(7).setCellValue("url");
	    headerRow.createCell(8).setCellValue("foodCategory");
	    headerRow.createCell(9).setCellValue("RecipeCategory");
	}
}
	