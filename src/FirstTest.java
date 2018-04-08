import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

public class FirstTest
{
    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception
    {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("deviceName", "AndroidTestDevice");
        desiredCapabilities.setCapability("platformVersion", "6.0");
        desiredCapabilities.setCapability("automationName", "Appium");
        desiredCapabilities.setCapability("appPackage", "org.wikipedia");
        desiredCapabilities.setCapability("appActivity", ".main.MainActivity");
        desiredCapabilities.setCapability("app", "/Users/abatyrov/Desktop/Automation/apks/org.wikipedia.apk");

        URL remoteUrl = new URL("http://localhost:4723/wd/hub");

        driver = new AndroidDriver(remoteUrl, desiredCapabilities);

        driver.rotate(ScreenOrientation.PORTRAIT);
    }

    @After
    public void tearDown()
    {
        driver.quit();
    }

    @Test
    public void firstTest()
    {
        System.out.println("First test run");
        waitForElementAndClick(By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find search line on main page",
                5);

        waitForElementAndSendKeys(By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input line",
                5);

        waitForElementPresent(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by Java",
                15);

        try
        {
            Thread.sleep(5000);
        } catch (Exception e)
        {

        }
    }

    @Test
    public void testCancelSearch()
    {
        waitForElementAndClick(By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find search line on main page",
                5);

        waitForElementAndClick(By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find search close button",
                5);

        waitForElementNotPresent(By.id("org.wikipedia:id/search_close_btn"),
                "Search close button is still present",
                5);

    }

    @Test
    public void testCompareArticleTitle()
    {
        waitForElementAndClick(By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find search line on main page",
                5);

        waitForElementAndSendKeys(By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input line",
                5);

        waitForElementAndClick(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by Java",
                15);

        String article_title = waitForElementAndGetAttribute(By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot get article title",
                15);

        Assert.assertEquals(
                "We see unexpected title!",
                "Java (programming language)",
                article_title
        );
    }

    @Test
    public void testChangeScreenOrientationOnSearchResults()
    {
        waitForElementAndClick(By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find search line on main page",
                5);

        waitForElementAndSendKeys(By.id("org.wikipedia:id/search_src_text"),
                "Java",
                "Cannot find search input line",
                5);

        waitForElementAndClick(By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by Java",
                15);

        String article_title_before_rotation = waitForElementAndGetAttribute(By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot get article title before rotation",
                15);

        driver.rotate(ScreenOrientation.LANDSCAPE);

        String article_title_after_rotation = waitForElementAndGetAttribute(By.id("org.wikipedia:id/view_page_title_text"),
                "text",
                "Cannot get article title after rotation",
                15);

        Assert.assertEquals(
                "Article title is changed after rotation",
                article_title_before_rotation,
                article_title_after_rotation
        );
    }

    private WebElement waitForElementPresent (By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private String waitForElementAndGetAttribute(By by, String attribute, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        return element.getAttribute(attribute);
    }
}
