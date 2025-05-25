package com.company.enroller.e2e;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.assertj.core.api.Assertions.assertThat;

public class BasicExample {

    public static void main(String[] args) throws InterruptedException, IOException {
        String url = "https://ipsych.up.krakow.pl/pracownik/lista-pracownikow/";

        WebDriver driver = WebDriverManager.chromedriver().create();
        driver.get(url);
        Thread.sleep(1 * 1000);
        // https://www.selenium.dev/selenium/docs/api/java/org/openqa/selenium/support/ui/ExpectedConditions.html
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Zamknięcie okienko ze zgodą na używanie cookies
        WebElement acceptCookiesBtn = driver.findElement(By.cssSelector("#cn-accept-cookie"));
        wait.until(ExpectedConditions.elementToBeClickable(acceptCookiesBtn)).click();

        // Wypisanie w konsoli danych, nazwa oraz mail wszystkich pracowników.
        do {
            List<WebElement> employees = driver.findElements(By.cssSelector(".workers-item"));
            employees.forEach(employee -> {
                WebElement employeeName = getElementIfExists(employee, By.cssSelector("h2"));
                WebElement employeeMail = getElementIfExists(employee, By.cssSelector(".workers-email"));

                String employeeNameText = Optional.ofNullable(employeeName).map(WebElement::getText).orElse("");
                String employeeMailText = Optional.ofNullable(employeeMail).map(WebElement::getText).orElse("");

                System.out.println(employeeNameText + ";" + employeeMailText);
            });
            try {
                WebElement nextPageBtn = wait.until(ExpectedConditions
                        .presenceOfElementLocated(By.cssSelector(".pagination .current + a")));
                wait.until(ExpectedConditions.elementToBeClickable(nextPageBtn)).click();
            } catch (TimeoutException e) {
                break;
            }
        }
        while (true);


        // Wykonaj screenshot
        TakesScreenshot ts = (TakesScreenshot) driver;
        File screenshot = ts.getScreenshotAs(OutputType.FILE);
        Path destination = Paths.get("screenshot.png");
        Files.move(screenshot.toPath(), destination, REPLACE_EXISTING);
    }

    private static WebElement getElementIfExists(WebElement parent, By by) {
        List<WebElement> elements = parent.findElements(by);
        return elements.isEmpty() ? null : elements.get(0);
    }

}



//
//package com.company.enroller.e2e;
//
//        import io.github.bonigarcia.wdm.WebDriverManager;
//        import org.junit.jupiter.api.*;
//        import org.openqa.selenium.*;
//        import org.openqa.selenium.chrome.ChromeDriver;
//        import org.openqa.selenium.support.ui.ExpectedConditions;
//        import org.openqa.selenium.support.ui.WebDriverWait;
//
//        import java.io.File;
//        import java.io.IOException;
//        import java.nio.file.Files;
//        import java.nio.file.Path;
//        import java.nio.file.Paths;
//        import java.time.Duration;
//        import java.util.ArrayList;
//        import java.util.List;
//        import java.util.Optional;
//
//        import static org.assertj.core.api.Assertions.assertThat;
//
//
//public class HelloWorld {
//
//    private WebDriver driver;
//
//    @BeforeEach
//    void setup() {
//        driver = new ChromeDriver();
//    }
//
//    @Test
//    @DisplayName("Nie wszyscy mają maile")
//    void test1() throws InterruptedException {
//        String sutUrl = "https://ipsych.up.krakow.pl/pracownik/lista-pracownikow/";
//        driver.get(sutUrl);
//
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
//
//        // Close cookies window
//        By acceptCookiesBtnCss = By.cssSelector("#cn-accept-cookie");
//        WebElement acceptCookiesBtn = wait.until(ExpectedConditions.elementToBeClickable(acceptCookiesBtnCss));
//        acceptCookiesBtn.click();
//
//        // Pagination element
//        By nextPageBtnCss = By.cssSelector("div.pagination span + a");
//
//
//        while (true) {
//
//            List<WebElement> workersItems = driver.findElements(By.cssSelector("div.workers div.workers-item"));
//            List<String> mails = new ArrayList<>();
//            for (WebElement workerItem : workersItems) {
//
//                List<WebElement> mailItem = workerItem.findElements(By.cssSelector(".workers-email"));
//
//                mailItem.stream()
//                        .findFirst()
//                        .map(WebElement::getText)
//                        .ifPresent(mails::add);
//            }
//
//            assertThat(mails.size()).isEqualTo(workersItems.size());
//            if (!driver.findElements(nextPageBtnCss).isEmpty()) {
//                WebElement nextPageBtn = driver.findElement(nextPageBtnCss);
//                nextPageBtn.click();
//            } else {
//                break;
//            }
//
//        }
//    }
//
//
//    @Test
//    @DisplayName("Nie wszyscy mają maile")
//    void test2() throws InterruptedException {
//        String sutUrl = "https://ipsych.up.krakow.pl/pracownik/lista-pracownikow/";
//        driver.get(sutUrl);
//
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(4));
//
//        // Close cookies window
//        By acceptCookiesBtnCss = By.cssSelector("#cn-accept-cookie");
//        WebElement acceptCookiesBtn = wait.until(ExpectedConditions.elementToBeClickable(acceptCookiesBtnCss));
//        acceptCookiesBtn.click();
//
//        // Pagination element
//        By nextPageBtnCss = By.cssSelector("div.pagination span + a");
//
//
//        while (true) {
//
//            List<WebElement> workersItems = driver.findElements(By.cssSelector("div.workers div.workers-item"));
//            List<String> mails = new ArrayList<>();
//            for (WebElement workerItem : workersItems) {
//
//                List<WebElement> mailItem = workerItem.findElements(By.cssSelector(".workers-email"));
//
//                mailItem.stream()
//                        .findFirst()
//                        .map(WebElement::getText)
//                        .ifPresent(mails::add);
//            }
//
//            assertThat(mails.size()).isEqualTo(workersItems.size());
//            if (!driver.findElements(nextPageBtnCss).isEmpty()) {
//                WebElement nextPageBtn = driver.findElement(nextPageBtnCss);
//                nextPageBtn.click();
//            } else {
//                break;
//            }
//
//        }
//    }
//
//    @AfterEach
//    void exit() {
//        driver.quit();
//    }
//
//
//}

