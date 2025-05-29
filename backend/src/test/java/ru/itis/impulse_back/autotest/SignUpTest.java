package ru.itis.impulse_back.autotest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SignUpTest {

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
        baseUrl = "https://www.google.com/";
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
    }

    @Test
    void testSignUp() {
        try {
            driver.get("http://localhost:5173/registration");
            driver.findElement(By.id("email")).clear();
            driver.findElement(By.id("email")).sendKeys("ilya@ilya1.com");
            driver.findElement(By.id("password")).clear();
            driver.findElement(By.id("password")).sendKeys("123123");
            driver.findElement(By.xpath("//div[@id='app']/div/main")).click();
            driver.findElement(By.id("email")).clear();
            driver.findElement(By.id("email")).sendKeys("f@f.com");
            driver.findElement(By.id("fullName")).click();
            driver.findElement(By.id("fullName")).clear();
            driver.findElement(By.id("fullName")).sendKeys("f");
            driver.findElement(By.xpath("//div[@id='app']/div/main/div")).click();
            driver.findElement(By.id("passwordRepeat")).click();
            driver.findElement(By.id("passwordRepeat")).clear();
            driver.findElement(By.id("passwordRepeat")).sendKeys("123123");
            driver.findElement(By.xpath("//div[@id='app']/div/main/div/form/div/div[5]/label")).click();
            driver.findElement(By.xpath("//input[@value='Зарегистрироваться']")).click();
        } catch (Exception e) {
            verificationErrors.append(e.toString());
            fail(e);
        }
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        String errors = verificationErrors.toString();
        if (!errors.isEmpty()) {
            fail(errors);
        }
    }

    // Вспомогательные методы

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
