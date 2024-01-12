package utility;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EliminateRecipes {

	public void segregateData() throws Exception {
		String filename = "Hypertension.xlsx";

		String filepath = "/Users/uvaraj/eclipse-workspace/Team22_RecipeScraping/src/test/resources/DataFile/Hypertension.xlsx";
		ZipSecureFile.setMinInflateRatio(0);
		XSSFWorkbook workbook = new XSSFWorkbook(new File(filepath));

		XSSFSheet eliminateInSheet = workbook.getSheet("eliminate");

		ArrayList<String> eliminate = new ArrayList<>();

		for (int i = 0; i < eliminateInSheet.getLastRowNum(); i++) {
			eliminate.add(eliminateInSheet.getRow(i).getCell(0).getStringCellValue().toLowerCase());
		}
		System.out.println(eliminate);

		LoggerLoad.info("Ingredient to be excluded" + eliminate.toString().toLowerCase());

		XSSFSheet eliminatedsheet = workbook.getSheet("Eliminated");
		if (Objects.isNull(eliminatedsheet)) {
			eliminatedsheet = workbook.createSheet("Eliminated");
		}
		XSSFSheet recipiesAfterEliminatedSheet = workbook.getSheet("RecipiesAfterEliminated");
		if (Objects.isNull(recipiesAfterEliminatedSheet)) {
			recipiesAfterEliminatedSheet = workbook.createSheet("RecipiesAfterEliminated");
		}

		XSSFSheet fullSheet = workbook.getSheet("Recipes");

		String[] headers = { "RecipeID", "Recipe Name", "ingredients", "method", "nutrientValue", "preparationTime",
				"cookingTime", "url", "foodCategory","RecipeCategory" };
		System.out.println("Elimated last row::" + eliminatedsheet.getLastRowNum());

		XSSFRow eliminatedSheetHeaderRow = eliminatedsheet.getLastRowNum() == 0 ? eliminatedsheet.createRow(0) : null;
		XSSFRow recipiesAfterEliminatedHeaderRow = recipiesAfterEliminatedSheet.getLastRowNum() == 0
				? recipiesAfterEliminatedSheet.createRow(0)
				: null;
		try {
			for (int i = 0; i < headers.length; i++) {
				if (Objects.nonNull(eliminatedSheetHeaderRow))
					eliminatedSheetHeaderRow.createCell(i).setCellValue(headers[i]);

				if (Objects.nonNull(recipiesAfterEliminatedHeaderRow))
					recipiesAfterEliminatedHeaderRow.createCell(i).setCellValue(headers[i]);
			}
		} catch (Exception e) {
			System.out.print("Exception while creating headers");

		}
		int eliminatedRowNumber = 1;
		int missedRowNumber = 1;

		for (int i = 1; i < fullSheet.getLastRowNum(); i++) {
			String elimString = getMatchingSubstring(fullSheet.getRow(i).getCell(2).getStringCellValue().toLowerCase(),
					eliminate);
			if (elimString != null) {
				LoggerLoad.info(fullSheet.getRow(i).getCell(1).getStringCellValue() + " contains " + elimString
						+ " ingredients to be eliminated");
				//eliminatedsheet.createRow(eliminatedRowNumber++).copyRowFrom(fullSheet.getRow(i), new CellCopyPolicy());
			} else {
				recipiesAfterEliminatedSheet.createRow(missedRowNumber++).copyRowFrom(fullSheet.getRow(i),
						new CellCopyPolicy());
			}
		}

		LoggerLoad.info("Total Records processed : " + fullSheet.getLastRowNum());
		LoggerLoad.info("Eliminated Records : " + eliminatedsheet.getLastRowNum());
		LoggerLoad.info("RecipiesAfterEliminated Records : " + recipiesAfterEliminatedSheet.getLastRowNum());

		FileOutputStream outputStream = new FileOutputStream(filename, true);
		workbook.write(outputStream);
		workbook.close();
		System.out.println("Data saved to " + filename);

	}

	public static String getMatchingSubstring(String str, List<String> substrings) {

		for (String substring : substrings) {

			if (str.contains(substring)) {
				return substring;
			}
		}
		return null;
	}
}
