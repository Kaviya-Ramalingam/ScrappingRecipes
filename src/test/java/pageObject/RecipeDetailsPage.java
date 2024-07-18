package pageObject;

import java.util.List;
import java.util.Objects;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import Base.BaseTest;
import Base.BaseTest.WebDriverManager;

public class RecipeDetailsPage extends BaseTest {
	WebDriver driver;
	public String ingredientsText;
	public String preparationTimeText;
	public String cookingTimeText;
	public String preparationMethodText;
	public String nutrientValuesText;

	public RecipeDetailsPage(WebDriver driver) {
		this.driver = WebDriverManager.getDriver();
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//div[@id='recipe_tags']/a")
	public List<WebElement> RecipeCategory;
	@FindBy(xpath = "//div[@id='rcpinglist']")
	public List<WebElement> Ingredients;
	@FindBy(xpath = "//time[@itemprop='prepTime'] | //div[@class= 'col-xs-12 col-md-12 col-sm-12 col-lg-12']/section[2]/p/text()[1]")
	public List<WebElement> PreparationTime;
	@FindBy(xpath = "//time[@itemprop='cookTime'] | //div[@class= 'col-xs-12 col-md-12 col-sm-12 col-lg-12']/section[2]/p/text()[1]")
	List<WebElement> CookingTime;
	@FindBy(xpath = "//div[@id='recipe_small_steps']")
	List<WebElement> PreparationMethod;
	@FindBy(xpath = "//div[@id='recipe_nutrients']")
	List<WebElement> NutrientValues;

	public String getRecipeCategory() throws InterruptedException {
		// Recipe Category(Breakfast/Lunch/Snack/Dinner)
		if (Objects.nonNull(RecipeCategory) && !RecipeCategory.isEmpty()) {
			String recipeCategory = RecipeCategory.get(0).getText().toLowerCase();
			if (recipeCategory.contains("breakfast")) {
				System.out.println("Recipe Category: Breakfast");
				return "Breakfast"; // This return statement to print this string on Excel
			} else if (recipeCategory.contains("lunch")) {
				System.out.println("Recipe Category: Lunch");
				return "Lunch";
			} else if (recipeCategory.contains("snack")) {
				System.out.println("Recipe Category: Snack");
				return "Snack";
			} else if (recipeCategory.contains("dinner")) {
				System.out.println("Recipe Category: Dinner");
				return "Dinner";
			} else {
				System.out.println("Recipe Category: Recipe category list is not mentioned in this recipe.");
				return "Recipe category list is not mentioned in this recipe.";
			}
		}
		// Thread.sleep(500);
		return null;
	}

	public String getFoodCategory() {
		// Food Category(Veg/non-veg/vegan/Jain)
		if (Objects.nonNull(RecipeCategory) && !RecipeCategory.isEmpty()) {
			String recipeCategory = RecipeCategory.get(0).getText().toLowerCase();
			// using RecipeCategory's xpath for food category because both xpaths are same
			if (recipeCategory.contains("veg")) {
				System.out.println("Food Category: Veg");
				return "Veg";
			} else if (recipeCategory.contains("non-veg")) {
				System.out.println("Food Category: Non Veg");
				return "Non Veg";
			} else if (recipeCategory.contains("vegan")) {
				System.out.println("Food Category: Vegan");
				return "Vegan";
			} else if (recipeCategory.contains("jain")) {
				System.out.println("Food Category: Jain");
				return "Jain";
			} else {
				System.out.println("Food Category: Food category list is not mentioned in this recipe.");
				return "Food category list is not mentioned in this recipe.";
			}
		}
		return null;
	}

	public String getIngredients() {
		if (Objects.nonNull(Ingredients) && !Ingredients.isEmpty()) {
			String ingredientsText = Ingredients.get(0).getText();
			System.out.println("Ingredients: " + ingredientsText);
			return ingredientsText;
		}
		return "";
	}

	public String getPreparationTime() {
		if (Objects.nonNull(PreparationTime) && !PreparationTime.isEmpty()) {
			String preparationTimeText = PreparationTime.get(0).getText();
			System.out.println("Preparation Time : " + preparationTimeText);
			return preparationTimeText;
		}
		return "";
	}

	public String getCookingTime() {
		if (Objects.nonNull(CookingTime) && !CookingTime.isEmpty()) {
			String cookingTimeText = CookingTime.get(0).getText();
			System.out.println("Cooking Time:  " + cookingTimeText);
			return cookingTimeText;
		}
		return "";
	}

	public String getPreparationMethod() {
		if (Objects.nonNull(PreparationMethod) && !PreparationMethod.isEmpty()) {
			String preparationMethodText = PreparationMethod.get(0).getText();
			System.out.println("Preparation Method: " + preparationMethodText);
			return preparationMethodText;
		}
		return "";
	}

	public String getNutrientValues() {
		if (Objects.nonNull(NutrientValues) && !NutrientValues.isEmpty()) {
			String nutrientValuesText = NutrientValues.get(0).getText();
			System.out.println("NutrientValue: " + nutrientValuesText);
			if (nutrientValuesText.isEmpty()) {
				System.out.println("Nutrient Value is empty.");
				return "Nutrient Value is empty.";
			}
			return nutrientValuesText;
		}
		return "";
	}

	public String getRecipeURL() {
		String recURL = driver.getCurrentUrl();
		System.out.println("RecipeURL:  " + recURL);
		return recURL;
	}
}