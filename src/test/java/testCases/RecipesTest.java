package testCases;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import Base.BaseTest;
import Base.BaseTest.WebDriverManager;
import pageObject.RecipeDetailsPage;
import pageObject.Recipe_LandingPage;
import utility.ExcelWriter;

public class RecipesTest extends BaseTest {

	Recipe_LandingPage rlp;
	RecipeDetailsPage rdp;
	ExcelWriter writer = new ExcelWriter();

	String recipeID, recipeName, currentPaginationPageUrl, recipeCategory, ingredients, preparationMethod,
			nutrientValues, preparationTime, cookingTime, recipeURL, foodCategory, currentAlphaPageUrl;

	public static ConcurrentHashMap<String, List<Object[]>> scrapedRecipes = new ConcurrentHashMap<>();

	@BeforeClass
	@Parameters({ "browser" })
	public void setup(String browserName) throws IOException {

		WebDriverManager.initializedriver(browserName);
		rlp = new Recipe_LandingPage(WebDriverManager.getDriver(), prop);
		rdp = new RecipeDetailsPage(WebDriverManager.getDriver());
	}

	@Test(priority = 1)
	public void getUrl() throws IOException {

		String url = prop.getProperty("url");
		WebDriverManager.getDriver().get(url);

	}

	@Parameters({ "string" })
	@Test(priority = 2)
	public void Pagination(String str) throws Exception {
		WebDriver driver = WebDriverManager.getDriver();
	
			rlp.clickRecipeAtoZ();

			System.out.println("thread id for   ::" + Thread.currentThread().getId());


			String alphaurl = "https://www.tarladalal.com/RecipeAtoZ.aspx?beginswith=" + str + "";
			System.out.println(alphaurl);
			
			driver.get(alphaurl);
			wait = new WebDriverWait(WebDriverManager.getDriver(), Duration.ofSeconds(20));
			String currentAlphaPageUrl = WebDriverManager.getDriver().getCurrentUrl();
			List<WebElement> paginationLinks = rlp.NumbersPagination;// Find all pagination links once
			if (!paginationLinks.isEmpty()) {
				int numberOfPages = Integer.parseInt(paginationLinks.get(paginationLinks.size() - 1).getText());
				System.out.println("Number of pages in alphabet " + str + " is: " + numberOfPages);
				for (int j = 1; j <= numberOfPages; j++) {
					
					String xpathExpression ="//div[@id='maincontent']/div[1]/div[2]/a[text()=" + j + "]";
					try {
					WebElement numPagination =wait.until(ExpectedConditions.elementToBeClickable 
							(By.xpath(xpathExpression)));
					numPagination.click();
					} catch (StaleElementReferenceException |TimeoutException e) {
						
						 System.err.println("Caught exception: " + e.getClass().getSimpleName());
				            System.err.println("Exception message: " + e.getMessage());
				            e.printStackTrace();
					}
					int RecipesInPage = rlp.RecipeNameSize();
					System.out.println("Total recipes in alphabet " + str + " number " + j + " page is: " + RecipesInPage);
					int countOfRecipe = 1;
					for (int k = 0; k < RecipesInPage; k++) {
						System.out.println("In alphabet " + str + ", page " + j + ", recipe no: " + countOfRecipe++);
						try {
							recipeID = rlp.getRecipeID(k);
							recipeName = rlp.getRecipeName(k);
							currentPaginationPageUrl = WebDriverManager.getDriver().getCurrentUrl();
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
								wait = new WebDriverWait(WebDriverManager.getDriver(), Duration.ofSeconds(10));
								wait.until(ExpectedConditions.alertIsPresent());

								Alert alert = WebDriverManager.getDriver().switchTo().alert();
								String alertText = alert.getText();
								System.out.println("Alert data: " + alertText);
								alert.accept();
							} catch (Exception a) {
								System.out.println("exception handled" + e.getMessage());
							}

						} finally {
							Object[] recipeData = { recipeID, recipeName, ingredients, preparationMethod, nutrientValues,
									preparationTime, cookingTime, recipeURL, foodCategory, recipeCategory };
						
							
						
	                            // Add data to ConcurrentHashMap
	                            List<Object[]> recipes = scrapedRecipes.computeIfAbsent(str, key -> new ArrayList<>());
	                            recipes.add(recipeData);
	                            System.out.println("number of recipes initally:"+ recipes.size());
	                        
					try {
								WebDriverManager.getDriver().get(currentPaginationPageUrl);
								

							} catch (Exception na) {
								// na.printStackTrace();
								try {

									wait = new WebDriverWait(driver, Duration.ofSeconds(10));
									wait.until(ExpectedConditions.alertIsPresent());

									Alert alert = WebDriverManager.getDriver().switchTo().alert();
									String alertText = alert.getText();
									System.out.println("Alert data: " + alertText);
									alert.accept();
								} catch (Exception ea) {
									System.out.println("exception handled" + ea.getMessage());
								}
							
							}
							System.out.println("-------------------------------------------------");	
						}
					}
				}
			} else {
				System.out.println("List is empty. No elements found.");
			}
			WebDriverManager.getDriver().get(currentAlphaPageUrl); // to go back to previous page using url instead of
																	// giving
		
			System.out.println("-------------------------------------------------");
		
		

	
		}
	

	@AfterTest
	public void EndSession() {
		int totalRecipes = 0;

		try {

			List<Object[]> allRecipes = new ArrayList<>();
			for (Map.Entry<String, List<Object[]>> entry : scrapedRecipes.entrySet()) {
				String key = entry.getKey();
				List<Object[]> recipe = entry.getValue();
				allRecipes.addAll(recipe);
				totalRecipes = recipe.size();
				System.out.println("number of recipes in:" + totalRecipes);
			}
			writer.writeToExcel(allRecipes); // Write each list of recipes to Excel
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		
		WebDriverManager.quitDriver();
	}
	}
	

}