package testCases;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Base.BaseTest;
import pageObject.RecipeDetailsPage;
import pageObject.Recipe_LandingPage;
import utility.AllergiesRecipes;
import utility.EliminateRecipes;
import utility.ExcelWriter;
import utility.ExcelWriter2;
import utility.ToAddRecipes;

public class RecipesTest extends BaseTest {

	Recipe_LandingPage rlp;
	RecipeDetailsPage rdp;
	ExcelWriter writer = new ExcelWriter();
	ExcelWriter2 writer2 = new ExcelWriter2();
	EliminateRecipes Eliminate = new EliminateRecipes();
	ToAddRecipes Added = new ToAddRecipes();
	AllergiesRecipes Allergies = new AllergiesRecipes();
	String recipeID, recipeName, currentPaginationPageUrl, recipeCategory, ingredients, preparationMethod,
			nutrientValues, preparationTime, cookingTime, recipeURL, foodCategory, currentAlphaPageUrl;

	@BeforeClass

	public void setup() throws IOException {
		initializedriver();
		rlp = new Recipe_LandingPage(driver, prop);
		rdp = new RecipeDetailsPage(driver);
	}

	@Test(priority = 1)
	public void getUrl() throws IOException {

		String url = prop.getProperty("url");
		driver.get(url);

	}

	@Parameters({ "string" })
	@Test(priority = 2)
	public void Pagination(String str) throws IOException, InterruptedException {
		List<Object[]> scrapedRecipes = new ArrayList<>();
		rlp.clickRecipeAtoZ();
		
		System.out.println("thread id for " + str + " ::" + Thread.currentThread().getId());
		/*
		 * int alphaPagination = rlp.AtoZPaginationSize();
		 * System.out.println("AtoZpage size:" + alphaPagination); for (int i = 3; i <
		 * alphaPagination; i++) { String str = rlp.AtoZPagination.get(i).getText();
		 * String strText = "\"" + str + "\""; System.out.println(strText); WebElement
		 * alphaPagination1 = driver.findElement(
		 * By.xpath("//table[@id='ctl00_cntleftpanel_mnuAlphabets']//td[1]//a[text()=" +
		 * strText + "]")); //alphaPagination1.click();
		 */
		String alphaurl = "https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=" + str + "";
		System.out.println(alphaurl);
		//driver.navigate().to(alphaurl);
		driver.get(alphaurl);
		// Thread.sleep(300);
		String currentAlphaPageUrl = driver.getCurrentUrl();
		List<WebElement> paginationLinks = rlp.NumbersPagination;// Find all pagination links once
		if (!paginationLinks.isEmpty()) {
			int numberOfPages = Integer.parseInt(paginationLinks.get(paginationLinks.size() - 1).getText());
			System.out.println("Number of pages in alphabet " + str + " is: " + numberOfPages);
			for (int j = 1; j <= numberOfPages; j++) {
				WebElement numPagination = driver
						.findElement(By.xpath("//div[@id='maincontent']/div[1]/div[2]/a[text()=" + j + "]"));
				numPagination.click();
				int RecipesInPage = rlp.RecipeNameSize();
				System.out.println("Total recipes in alphabet " + str + " number " + j + " page is: " + RecipesInPage);
				int countOfRecipe = 1;
				for (int k = 0; k < RecipesInPage; k++) {
					System.out.println("In alphabet " + str + ", page " + j + ", recipe no: " + countOfRecipe++);
					try {
						recipeID = rlp.getRecipeID(k);
						recipeName = rlp.getRecipeName(k);
						currentPaginationPageUrl = driver.getCurrentUrl();
						rlp.RecipeName.get(k).click();
						recipeCategory = rdp.getRecipeCategory();
						foodCategory = rdp.getFoodCategory();
						ingredients = rdp.getIngredients();
						preparationMethod = rdp.getPreparationMethod();
						recipeURL = rdp.getRecipeURL();
						preparationTime = rdp.getPreparationTime();
						cookingTime = rdp.getCookingTime();
						nutrientValues = rdp.getNutrientValues();
					} catch (Exception e) {
						// e.printStackTrace();

						try {
							wait = new WebDriverWait(driver, Duration.ofSeconds(10));
							wait.until(ExpectedConditions.alertIsPresent());

							Alert alert = driver.switchTo().alert();
							String alertText = alert.getText();
							System.out.println("Alert data: " + alertText);
							alert.accept();
						} catch (Exception a) {
							System.out.println("exception handled" + e.getMessage());
						}

					} finally {
						Object[] recipeData = { recipeID, recipeName, ingredients, preparationMethod, nutrientValues,
								preparationTime, cookingTime, recipeURL, foodCategory, recipeCategory };
						scrapedRecipes.add(recipeData);
						
						// writer.writeToExcel(scrapedRecipes);
						writer2.writeToExcel(scrapedRecipes);
						scrapedRecipes.clear();
						try {
							driver.get(currentPaginationPageUrl);

						} catch (Exception na) {
							// na.printStackTrace();
							try {

								wait = new WebDriverWait(driver, Duration.ofSeconds(10));
								wait.until(ExpectedConditions.alertIsPresent());

								Alert alert = driver.switchTo().alert();
								String alertText = alert.getText();
								System.out.println("Alert data: " + alertText);
								alert.accept();
							} catch (Exception ea) {
								System.out.println("exception handled" + ea.getMessage());
							}
							// Thread.sleep(300);
						}
						System.out.println("-------------------------------------------------");

					}
				}
			}

		} else {
			System.out.println("List is empty. No elements found.");
		}
		driver.get(currentAlphaPageUrl); // to go back to previous page using url instead of giving
											// ->driver.navigate().back();
		System.out.println("-------------------------------------------------");
		driver.quit();
	}

	/*
	 * @Test(priority = 3)
	 * 
	 * //driver.quit();
	 * 
	 * /* * public void eliminatingRecipes() throws Exception {
	 * 
	 * Eliminate.segregateData(); Added.AddedRecipesData(); Allergies.Allergies();
	 * 
	 * LoggerLoad.info("elimination completed"); }
	 */

	// https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=A

}
