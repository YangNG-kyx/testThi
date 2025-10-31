import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VitimexAoNamTest {

    WebDriver driver;
    WebDriverWait wait;
    String baseUrl;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions opts = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "false"))) {
            opts.addArguments("--headless=new");
        }
        opts.addArguments("--window-size=1366,768");
        driver = new ChromeDriver(opts);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        baseUrl = System.getProperty("baseUrl", "https://vitimex.com.vn/");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    WebElement firstVisible(By... locators) {
        for (By by : locators) {
            try {
                WebElement e = wait.until(ExpectedConditions.presenceOfElementLocated(by));
                if (e.isDisplayed()) return e;
            } catch (TimeoutException ignored) {}
        }
        throw new NoSuchElementException("Không tìm thấy element phù hợp");
    }

    void safeClick(WebElement el) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (Exception ex) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'})", el);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click()", el);
        }
    }

    @Test
    @Order(1)
    void openHome_andGoToAoNam() {
        driver.get(baseUrl);

        WebElement aoNamLink = firstVisible(
                By.linkText("ÁO NAM"),
                By.partialLinkText("Áo nam"),
                By.xpath("//a[normalize-space()='ÁO NAM']")
        );
        safeClick(aoNamLink);

        wait.until(ExpectedConditions.urlContains("/collections/ao-nam"));
        assertTrue(driver.getCurrentUrl().contains("/collections/ao-nam"),
                "Không điều hướng đúng /collections/ao-nam");
    }

    @Test
    @Order(2)
    void addFirstAoNamToCart_shouldGoToCart() {
        driver.get(baseUrl + "collections/ao-nam");
        wait.until(ExpectedConditions.urlContains("/collections/ao-nam"));

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector(".product-loop a[href*='/products/']")
        ));

        WebElement firstProduct = firstVisible(
                By.cssSelector(".product-loop a[href*='/products/']"),
                By.xpath("(//a[contains(@href,'/products/')])[1]")
        );

        String productHref = firstProduct.getAttribute("href");
        safeClick(firstProduct);

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/products/"),
                productHref != null ? ExpectedConditions.urlToBe(productHref) : ExpectedConditions.urlContains("/products/")
        ));

        boolean picked = false;
        try {
            java.util.List<WebElement> sizeOptions = driver.findElements(By.cssSelector(
                    "[data-value]:not([disabled]), " +
                            ".swatch-element input:not([disabled]), " +
                            ".option-swatch:not(.disabled), " +
                            "select#product-select-option-0 option:not([disabled])"
            ));

            if (!sizeOptions.isEmpty()) {
                WebElement firstSize = sizeOptions.get(0);

                // Nếu là dropdown (thẻ select)
                if (firstSize.getTagName().equalsIgnoreCase("option")) {
                    WebElement select = driver.findElement(By.cssSelector("select#product-select-option-0"));
                    new org.openqa.selenium.support.ui.Select(select).selectByIndex(1);
                    System.out.println("Đã chọn size trong dropdown");
                } else {
                    safeClick(firstSize);
                    System.out.println("Đã chọn size: " + firstSize.getAttribute("data-value"));
                }
                picked = true;
            }
        } catch (NoSuchElementException ignored) {}

        if (!picked) {
            System.out.println("⚠Không có lựa chọn size, bỏ qua bước chọn size");
        }


        WebElement addBtn = firstVisible(
                By.cssSelector("button#btn-addtocart"),
                By.cssSelector("button.add-to-cart"),
                By.cssSelector("button.btn-cart"),
                By.xpath("//button[contains(.,'THÊM VÀO GIỎ')]"),
                By.xpath("//button[contains(.,'Add to cart') or contains(.,'add to cart')]")
        );

        safeClick(addBtn);

        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/cart"),
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-popup, .drawer__cart, #cart"))
            ));
        } catch (TimeoutException e) {
            System.out.println("⚠Không thấy popup/cart ngay lập tức — tiếp tục kiểm tra URL...");
        }

        assertTrue(driver.getCurrentUrl().contains("/cart") ||
                        driver.getPageSource().toLowerCase().contains("giỏ hàng"),
                "Không vào trang giỏ hàng sau khi thêm sản phẩm!");
    }
}
