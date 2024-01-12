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

public class AllergiesRecipes {

	public void Allergies() throws Exception {
		String filename = "Hypertension.xlsx";

		String filepath = "/Users/uvaraj/eclipse-workspace/Team22_RecipeScraping/src/test/resources/DataFile/Hypertension.xlsx";
		ZipSecureFile.setMinInflateRatio(0);
		XSSFWorkbook workbook = new XSSFWorkbook(new File(filepath));

		XSSFSheet allergiesInSheet = workbook.getSheet("Allergies");

		ArrayList<String> Allergies = new ArrayList<>();

		for (int i = 0; i < allergiesInSheet.getLastRowNum(); i++) {
			Allergies.add(allergiesInSheet.getRow(i).getCell(0).getStringCellValue().toLowerCase());
		}

		LoggerLoad.info("Allergic Ingredient to be excluded" + Allergies.toString().toLowerCase());

		XSSFSheet AllergicRecipes = workbook.getSheet(" AllergicRecipes");
		if (Objects.isNull(AllergicRecipes)) {
			AllergicRecipes = workbook.createSheet(" AllergicRecipes");
		}
		XSSFSheet NonAllergicRecipes = workbook.getSheet("nonallergicrecipes");
		if (Objects.isNull(NonAllergicRecipes)) {
			NonAllergicRecipes = workbook.createSheet("nonallergicrecipes");
		}

		XSSFSheet AddedRecipesSheet = workbook.getSheet("ToAddRecipes");

		String[] headers = { "RecipeID", "Recipe Name", "ingredients", "method", "nutrientValue", "preparationTime",
				"cookingTime", "url", "foodCategory","RecipeCategory" };
		XSSFRow allergicrecipesSheetHeaderRow = AllergicRecipes.getLastRowNum() == 0 ? AllergicRecipes.createRow(0)
				: null;
		XSSFRow nonallergicrecipesSheetHeaderRow = NonAllergicRecipes.getLastRowNum() == 0
				? NonAllergicRecipes.createRow(0)
				: null;
		try {
			for (int i = 0; i < headers.length; i++) {

				if (Objects.isNull(nonallergicrecipesSheetHeaderRow))
					nonallergicrecipesSheetHeaderRow.createCell(i).setCellValue(headers[i]);
				if (Objects.nonNull(allergicrecipesSheetHeaderRow))
					allergicrecipesSheetHeaderRow.createCell(i).setCellValue(headers[i]);

			}
		} catch (Exception e) {
			System.out.print("Exception while creating headers");

		}
		int allergicrecipesRowNumber = 1;
		int nonallergicrecipesRowNumber = 1;
		int missedRowNumber = 1;

		for (int i = 1; i < AddedRecipesSheet.getLastRowNum(); i++) {
			// System.out.print(fullSheet.getRow(i).getCell(2).getStringCellValue().toLowerCase());

			String alString = getMatchingSubstring(
					AddedRecipesSheet.getRow(i).getCell(2).getStringCellValue().toLowerCase(), Allergies);

			if (alString != null) {

				System.out.println(AddedRecipesSheet.getRow(i).getCell(1).getStringCellValue() + " contains " + alString
						+ " allergic ingredients to be excluded");
				AllergicRecipes.createRow(allergicrecipesRowNumber++).copyRowFrom(AddedRecipesSheet.getRow(i),
						new CellCopyPolicy());
			} else {
				NonAllergicRecipes.createRow(missedRowNumber++).copyRowFrom(AddedRecipesSheet.getRow(i),
						new CellCopyPolicy());
			}

		}

		LoggerLoad.info("Total Records processed : " + AddedRecipesSheet.getLastRowNum());

		LoggerLoad.info("Nonallergicrecipes : " + NonAllergicRecipes.getLastRowNum());

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
