import org.junit.jupiter.api.*;
import org.myhal.Data;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.myhal.Data.*;


public class WebDTests {


    private final By loginBtn = By.xpath("//div[@class='login_container']//input[@id='login-button']");
    private final By inputUserNameField = By.id("user-name");
    private final By inputUserPassField = By.id("password");
    private final By getErrorMesg = By.xpath("//div[@class='error-message-container error']//h3[@data-test='error']");
    private final By getMesg = By.xpath("//div[@class='primary_header']//div[@class='app_logo']");
    private final By getMenu = By.xpath("//div[@class='bm-burger-button']//button[@id='react-burger-menu-btn']");
    private final By getLogout = By.xpath("//div[@class='bm-menu']//a[@id='logout_sidebar_link']");


    WebDriver driver;

    @BeforeAll
    public static void beforeAll() {
        String path = String.format("%s/drivers/chromedriver.exe", System.getProperty("user.dir"));
        System.setProperty("webdriver.chrome.driver", path);
    }

    @BeforeEach
    public void beforeEach() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(Data.testUrl);
    }

    @Test
    public void testUC1() {

        /* UC -1 - Test Login form with empty credentials
            1.Type any credentials
            2.Clear the inputs.
            3.Check the error messages:
              3.1 Username is required. */

        driver.findElement(inputUserNameField).sendKeys(anyName);
        driver.findElement(inputUserPassField).sendKeys(anyPass);

        driver.findElement(inputUserNameField).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        driver.findElement(inputUserPassField).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));

        driver.findElement(loginBtn).click();

        String currentErrorMessage = driver.findElement(getErrorMesg).getText();

        Assertions.assertEquals("Epic sadface: Username is required", currentErrorMessage);

    }

    @Test
    public void testUC2() {

        /* UC-2 - Test Login form with credentials by passing Username.
        1.Type any credentials in user name
        2.Enter password and Clear the input.
        3.Check the error messages:
          3.1 Password is required.*/

        driver.findElement(inputUserNameField).sendKeys(anyName);

        driver.findElement(inputUserPassField).sendKeys(userPassword);
        driver.findElement(inputUserPassField).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));

        driver.findElement(loginBtn).click();

        String currentErrorMessage = driver.findElement(getErrorMesg).getText();

        Assertions.assertEquals("Epic sadface: Password is required", currentErrorMessage);
    }

    @Test
    public void testUC3() {
        /* UC -3 - Test Login form with credentials by passing Username & Password
        1. Type credentials in user name which are under Accepted username are sections
        2. Enter password as secret sauce.
        3. Click on Login and validate the title “Swag Labs” in the dashboard.*/

        driver.findElement(inputUserNameField).sendKeys(userName);
        driver.findElement(inputUserPassField).sendKeys(userPassword);

        driver.findElement(loginBtn).click();

        String currentMessage = driver.findElement(getMesg).getText();

        Assertions.assertEquals("Swag Labs", currentMessage);


        driver.findElement(getMenu).click();

        new WebDriverWait(driver, Duration.ofSeconds(1)).
                until(ExpectedConditions.elementToBeClickable(getLogout));
        driver.findElement(getLogout).click();

    }

    @AfterEach
    void tearDown() {
        driver.close();
    }
}
