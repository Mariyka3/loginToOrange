import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OrangeTests {
    private WebDriver driver;
    private WebDriverWait wait;
    private final String URL = "https://s1.demo.opensourcecms.com/s/44";
    private final String USERNAME = ".//input[@name='txtUsername']";
    private final String PASSWORD = ".//input[@name='txtPassword']";
    private final String LOGIN = ".//input[@name='Submit']";
    private final String MESSAGE = ".//span[@id='spanMessage']";


    /**
     * Set up method to initialize driver and WebDriverWait
     */
    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("disable-infobars");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 10);
        driver.manage().window().maximize();
        //open url
        driver.get(URL);
        //switch to frame
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector("iframe[name='preview-frame']"))));
        driver.switchTo().frame(driver.findElement(By.cssSelector("iframe[name='preview-frame']")));

    }

    /**
     * Try to login with incorrect login and password
     */
    @Test
    public void loginWithIncorrectData() {
        //enter username
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(USERNAME)))).sendKeys("Arnold");
        //enter password
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(PASSWORD)))).sendKeys("qwerty");
        //submit
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(LOGIN)))).click();
        //expect invalid credentials message
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(MESSAGE))));

        Assert.assertTrue(driver.findElement(By.xpath(MESSAGE)).isDisplayed());
        Assert.assertEquals("Invalid credentials", driver.findElement(By.xpath(MESSAGE)).getText());
    }

    @Test
    public void loginWithEmptyData() {
        //clear username
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(USERNAME)))).clear();
        //clear password
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(PASSWORD)))).clear();
        //submit
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(LOGIN)))).click();
        //expect Username cannot be empty message
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(MESSAGE))));

        Assert.assertTrue(driver.findElement(By.xpath(MESSAGE)).isDisplayed());
        Assert.assertEquals("Username cannot be empty", driver.findElement(By.xpath(MESSAGE)).getText());
    }

    @Test
    public void loginWithoutPassword() {
        //Enter username
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(USERNAME)))).sendKeys("Mich");
        //clear password
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(PASSWORD)))).clear();
        //submit
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(LOGIN)))).click();
        //expect Password cannot be empty message
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(MESSAGE))));

        Assert.assertTrue(driver.findElement(By.xpath(MESSAGE)).isDisplayed());
        Assert.assertEquals("Password cannot be empty", driver.findElement(By.xpath(MESSAGE)).getText());
    }

    @Test
    public void removeFrame() {
        //focus on default content
        driver.switchTo().defaultContent();
        //click on remove frame
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(".//span[text()='Remove Frame']")))).click();
        //ignore NoSuchElementException
        wait.ignoring(org.openqa.selenium.NoSuchElementException.class);
        //wait when frame disappear
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("iframe[name='preview-frame']")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(".//div[@class='preview__header']")));

        Assert.assertEquals(0, driver.findElements(By.xpath(".//div[@class='preview__header']")).size());
    }

    /**
     * Quit WebDriver
     */
    @After
    public void tearDown() {
        driver.quit();
    }
}
