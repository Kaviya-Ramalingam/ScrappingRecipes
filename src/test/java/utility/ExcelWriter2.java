package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter2 {

	// static int row = 0;

	public void writeToExcel(List<Object[]> recipeData) throws IOException {
		ZipSecureFile.setMinInflateRatio(0);
		String filename = "Recipes_list.xlsx"; // Filename variable
		// String filepath = "/Users/uvaraj/Downloads/Recipes_list.xlsx"; // Filepath
		// variable
		String filepath = "/Users/uvaraj/eclipse-workspace/ScrappingRecipes/Recipes_list.xlsx";
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filepath);
		} catch (FileNotFoundException e) { // File doesn't exist, create a new one
			inputStream = null;
		}

		XSSFWorkbook workbook;
		if (inputStream == null) {
			workbook = new XSSFWorkbook();
		} else {
			workbook = new XSSFWorkbook(inputStream);
		}

		XSSFSheet sheet = workbook.getSheet("Recipes");
		System.out.println("got existing" + sheet + "");
		if (sheet == null) {
			sheet = workbook.createSheet("Recipes");
			createHeaderRow(sheet);
			System.out.println("Recipes " + sheet + " created");
		}

		int lastRowNum = sheet.getLastRowNum(); // Get the last used row number
		System.out.println(lastRowNum);
		int rowNumber = lastRowNum + 1; // Start appending data from the next row

		XSSFCellStyle style = workbook.createCellStyle();
		style.setWrapText(true);

		for (Object[] rowData : recipeData) {
			XSSFRow row = sheet.createRow(rowNumber++);

			int cellNumber = 0;
			for (Object cellData : rowData) {
				XSSFCell cell = row.createCell(cellNumber++);
				if (cellData instanceof String) {
					cell.setCellValue((String) cellData);
				} else if (cellData instanceof Integer) {
					cell.setCellValue((Integer) cellData);
				} else if (cellData instanceof Boolean) {
					cell.setCellValue((Boolean) cellData);
				}
				cell.setCellStyle(style);
			}
		}

		// Save the workbook to the same file
		FileOutputStream outputStream = new FileOutputStream(filepath);
		workbook.write(outputStream);
		workbook.close();
		System.out.println("Data appended to " + filepath);
		System.out.println("Workbook has " + workbook.getNumberOfSheets() + " sheets.");
		System.out.println("Sheet 'Recipes' exists: " + (sheet != null));
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
