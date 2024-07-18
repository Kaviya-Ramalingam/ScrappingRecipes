package pageObject;

import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Base.BaseTest;

public class Recipe_LandingPage extends BaseTest {
	WebDriver driver;
	Properties prop;

	public Recipe_LandingPage(WebDriver driver, Properties prop) {
		this.driver = WebDriverManager.getDriver();
		this.prop = prop;
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//a[@title='Recipea A to Z']")
	public WebElement RecipeAtoZ;
	@FindBy(xpath = "//table[@id='ctl00_cntleftpanel_mnuAlphabets']//td[1]/a")
	public List<WebElement> AtoZPagination;
	@FindBy(xpath = "//div[@id='maincontent']/div[1]/div[2]/a")
	public List<WebElement> NumbersPagination;
	@FindBy(xpath = "//span[@class='rcc_recipename']/a")
	public List<WebElement> RecipeName;
	@FindBy(xpath = "//div[@class='rcc_recipecard']")
	public List<WebElement> RecipeID;

	public void clickRecipeAtoZ() {
		RecipeAtoZ.click();
	}

	public int AtoZPaginationSize() {
		int sizeOfAtoZPagination = AtoZPagination.size();
		return sizeOfAtoZPagination;
	}

	public int RecipeIDSize() {
		return RecipeID.size();
	}

	public String getRecipeName(int k) {
		String recipeNameText = RecipeName.get(k).getText();
		System.out.println("RecipeName : " + recipeNameText);
		return recipeNameText;
	}

	public String getRecipeID(int k) throws InterruptedException {
		
		String recipeIdNum = RecipeID.get(k).getAttribute("id");
	
		String[] RecipeID = recipeIdNum.split("p");// rcp12345 = ["rc","12345"]
		System.out.println("RecipeID: " + RecipeID[1]);
		return RecipeID[1];
	}

	public int RecipeNameSize() {
		return RecipeName.size();
	}
}
