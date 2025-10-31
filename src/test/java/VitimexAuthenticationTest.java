

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.Random;

public class VitimexAuthenticationTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @ParameterizedTest
    @CsvSource({
            "Nguyễn, Thùy Dương, yangyang"
    })
    public void registration(String lastName, String firstName, String emailPrefix) {
        driver.get("https://vitimex.com.vn/account/register");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Đợi các input hiển thị
        WebElement hoField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("customer[last_name]")));
        WebElement tenField = driver.findElement(By.name("customer[first_name]"));
        WebElement emailField = driver.findElement(By.name("customer[email]"));
        WebElement passwordField = driver.findElement(By.name("customer[password]"));
        WebElement registerButton = driver.findElement(By.xpath("//input[@type='submit' and @value='Đăng ký']"));

        String randomEmail = emailPrefix + new Random().nextInt(10000) + "@gmail.com";

        hoField.sendKeys(lastName);
        tenField.sendKeys(firstName);
        emailField.sendKeys(randomEmail);
        passwordField.sendKeys("yang@183012");

        registerButton.click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("account"),
                ExpectedConditions.urlContains("register")
        ));

        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(
                currentUrl.contains("account") || currentUrl.contains("register"),
                "Không chuyển hướng hợp lệ sau khi đăng ký!"
        );
    }

    @ParameterizedTest
    @CsvSource({
            "duongtn1806@gmail.com, yang@183012"
    })
    public void loginTest(String email, String password) {
        driver.get("https://vitimex.com.vn/account/login");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("customer[email]")));
        WebElement passwordField = driver.findElement(By.name("customer[password]"));

        WebElement submitButton = driver.findElement(By.xpath("//input[@type='submit' and @value='Đăng nhập']"));

        emailField.sendKeys(email);
        passwordField.sendKeys(password);
        submitButton.click();

        wait.until(ExpectedConditions.urlContains("account"));

        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("account"), "Đăng nhập không thành công!");
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}