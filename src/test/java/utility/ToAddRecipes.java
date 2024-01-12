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

public class ToAddRecipes {

	public void AddedRecipesData() throws Exception {
		String filename = "Hypertension.xlsx";

		String filepath = "/Users/uvaraj/eclipse-workspace/Team22_RecipeScraping/src/test/resources/DataFile/Hypertension.xlsx";
		ZipSecureFile.setMinInflateRatio(0);
		XSSFWorkbook workbook = new XSSFWorkbook(new File(filepath));

		XSSFSheet ToAddList = workbook.getSheet("ToAdd");

		ArrayList<String> ToAddRecipes = new ArrayList<>();

		for (int i = 0; i < ToAddList.getLastRowNum(); i++) {
			ToAddRecipes.add(ToAddList.getRow(i).getCell(0).getStringCellValue().toLowerCase());
		}

		LoggerLoad.info("Ingredient to be included" + ToAddRecipes.toString().toLowerCase());

		XSSFSheet AddedRecipesSheet = workbook.getSheet("ToAddRecipes");
		if (Objects.isNull(AddedRecipesSheet)) {
			AddedRecipesSheet = workbook.createSheet("ToAddRecipes");
		}

		XSSFSheet recipiesAfterEliminatedSheet = workbook.getSheet("RecipiesAfterEliminated");

		String[] headers = { "ReceipeID", "Recipe Name", "Ingredients", "Method", "Nutrient Values", "Preparation Time",
				"Cooking Time" };

		System.out.println("Included last row::" + ToAddList.getLastRowNum());

		XSSFRow includedSheetHeaderRow = AddedRecipesSheet.getLastRowNum() == 0 ? AddedRecipesSheet.createRow(0) : null;

		try {
			for (int i = 0; i < headers.length; i++) {

				if (Objects.nonNull(includedSheetHeaderRow))
					includedSheetHeaderRow.createCell(i).setCellValue(headers[i]);

			}
		} catch (Exception e) {
			System.out.print("Exception while creating headers");

		}

		int includededRowNumber = 1;

		for (int i = 1; i < recipiesAfterEliminatedSheet.getLastRowNum(); i++) {
			// System.out.print(fullSheet.getRow(i).getCell(2).getStringCellValue().toLowerCase());

			String inclString = getMatchingSubstring(
					recipiesAfterEliminatedSheet.getRow(i).getCell(2).getStringCellValue().toLowerCase(), ToAddRecipes);

			if (inclString != null) {

				System.out.printf(recipiesAfterEliminatedSheet.getRow(i).getCell(1).getStringCellValue() + " contains "
						+ inclString + " ingredients to be included");
				AddedRecipesSheet.createRow(includededRowNumber++).copyRowFrom(recipiesAfterEliminatedSheet.getRow(i),
						new CellCopyPolicy());
			}

		}

		LoggerLoad.info("Total Records processed : " + recipiesAfterEliminatedSheet.getLastRowNum());

		LoggerLoad.info("Addon Records : " + AddedRecipesSheet.getLastRowNum());

		FileOutputStream outputStream = new FileOutputStream(filename, true);
		workbook.write(outputStream);
		workbook.close();
		System.out.println("Data saved to " + filename);

	}

	private static String getMatchingSubstring(String str, List<String> substrings) {

		for (String substring : substrings) {

			if (str.contains(substring)) {
				return substring;
			}
		}
		return null;
	}
}
