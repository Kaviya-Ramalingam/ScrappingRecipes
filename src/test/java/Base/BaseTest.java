package Base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseTest {

	//public static WebDriver driver;
	public static Properties prop;
	public static WebDriverWait wait;
	
	public static class WebDriverManager {
	    private final static ThreadLocal<WebDriver> thdriver = new ThreadLocal<>();

	    public static WebDriver getDriver() {
	        return thdriver.get();
	    }
	 

	public static void initializedriver(String browserName) throws IOException {

		prop = new Properties();

		FileInputStream fis = new FileInputStream(
				"/Users/uvaraj/eclipse-workspace/ScrappingRecipes/src/test/resources/data.properties");
		prop.load(fis);
		//String browserName = prop.getProperty("browser");

		if (browserName.equalsIgnoreCase("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--headless");
			options.addArguments("--disable-popup-blocking");
			 options.addArguments("--disable-notifications");
			 options.addArguments("--disable-extensions");
			 options.addArguments("--blink-settings=imageEnabled=false");

		thdriver.set(new ChromeDriver(options));
			

		} else if (browserName.equalsIgnoreCase("firefox")) {
			FirefoxOptions options = new FirefoxOptions();
			//options.addArguments("--headless");
			options.addArguments("--disable-popup-blocking");
			 options.addArguments("--disable-notifications");
			 options.addArguments("--disable-extensions");
			 options.addArguments("--blink-settings=imageEnabled=false");

			thdriver.set(new FirefoxDriver(options));
		} else if (browserName.equalsIgnoreCase("edge")) {
			thdriver.set(new EdgeDriver());
		}
		getDriver().manage().deleteAllCookies();
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		//getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
		getDriver().manage().window().maximize();
		
	}
	
	

	    public static void quitDriver() {
	        WebDriver driver = getDriver();
	        if (driver != null) {
	            driver.quit();
	            thdriver.remove(); // Remove the ThreadLocal variable
	        }
	    }

		
	}

	
}