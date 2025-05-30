package ru.itis.impulse_back.autotest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EditProfileTest {

    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private JavascriptExecutor js;

    @BeforeAll
    public void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        baseUrl = "http://localhost:5173/";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
    }

    @Test
    public void testEditProfile() throws Exception {
        driver.get("http://localhost:5173/");
        driver.findElement(By.linkText("Вход")).click();
        driver.findElement(By.id("email")).click();
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys("ilya@ilya1.com");
        driver.findElement(By.xpath("//input[@value='Войти']")).click();
        driver.findElement(By.linkText("Профиль")).click();
        driver.findElement(By.xpath("//div[@id='app']/div/main/div/div[3]/button")).click();
        driver.findElement(By.id("bio")).click();
        driver.findElement(By.id("bio")).clear();
        driver.findElement(By.id("bio")).sendKeys("Я крут");
        driver.findElement(By.id("price")).click();
        driver.findElement(By.id("price")).clear();
        driver.findElement(By.id("price")).sendKeys("15000");
        driver.findElement(By.xpath("//div[@id='app']/div/main/div/div[3]/div[2]/div/div/button")).click();
    }
    @AfterEach
    public void tearDown() {
        driver.quit();
        String errors = verificationErrors.toString();
        if (!errors.isEmpty()) {
            fail(errors);
        }
    }



    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
