import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.junit.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public WebDriver driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    String pathChrome = "/Users/nursektiwaskitha/Documents/web automation sleeflow/chromedriver-mac-arm64/chromedriver"; //adjust this path if needed

    public void loginShopee() {
        driver.findElement(By.xpath("//input[@class=\"Btdmxp\" and @type=\"text\"]")).sendKeys("testsekti@gmail.com");
        driver.findElement(By.xpath("//input[@class=\"Btdmxp\" and @type=\"password\"]")).sendKeys("TestSekti");
        driver.findElement(By.xpath("//button[@class=\"DYKctS hqfBzL SYqMlu NBaRN4 CEiA6B ukVXpA\"]")).click();
    }

    public void launchBrowserTokopedia() {
        System.setProperty("webdriver.chrome.driver", pathChrome);
        driver = new ChromeDriver();
        driver.get("https://tokopedia.com");
    }

    public void launchBrowserLazada() {
        System.setProperty("webdriver.chrome.driver", pathChrome);
        driver = new ChromeDriver();
        driver.get("https://www.lazada.co.id/");
    }

    class Products
    {
        private String websiteName;
        private String productName;
        private String price;
        private String url;

        Products(String websiteName, String productName, String age, String url)
        {
            this.productName=productName;
            this.price=age;
            this.websiteName=websiteName;
            this.url=url;
        }
    }

    @Test
    public void testSortingPriceHighToLow(){
        launchBrowserLazada();
        driver.findElement(By.xpath("//input[@class=\"search-box__input--O34g\" and @type=\"search\"]")).sendKeys("iPhone 15 Pro");
        driver.findElement(By.xpath("//a[@class=\"search-box__button--1oH7\"]")).click();

        List<Products> products = new ArrayList<Products>();
        List<WebElement> lazadaItemsName = driver.findElements(By.xpath("//div[@class=\"RfADt\"]/descendant::a[@age=\"0\"]"));
        List<WebElement> lazadaItemsPrice = driver.findElements(By.xpath("//span[@class=\"ooOxS\"]"));

        String websiteName, price, url, productName;
        WebElement productTitle;
        for(int i = 0; i<lazadaItemsName.size(); i++){
            productTitle = lazadaItemsName.get(i);
            websiteName = "Lazada";
            productName = productTitle.getAttribute("title");
            url = productTitle.getAttribute("href");
            price = lazadaItemsPrice.get(i).getText();
            Products temp = new Products(websiteName, productName, price, url);
            products.add(temp);
        }

        driver.quit();
        launchBrowserTokopedia();
        driver.findElement(By.xpath("//input[@type=\"search\"]")).sendKeys("iPhone 15 Pro");
        driver.findElement(By.xpath("//input[@type=\"search\"]")).sendKeys(Keys.ENTER);

        WebDriverWait wait =new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class=\"css-jza1fo\"]")));

        List<WebElement> tokopediaItemsName = driver.findElements(By.xpath("//div[@class=\"css-1asz3by\"]/descendant::a[@class=\"pcv3__info-content css-gwkf0u\"]"));
        List<WebElement> tokopediaItemsPrice = driver.findElements(By.xpath("//div[@class=\"prd_link-product-price css-h66vau\"]"));

        for(int i = 0; i<tokopediaItemsName.size(); i++){
            productTitle = tokopediaItemsName.get(i);
            websiteName = "Tokopedia";
            productName = productTitle.getAttribute("title");
            url = productTitle.getAttribute("href");
            price = tokopediaItemsPrice.get(i).getText();
            Products temp = new Products(websiteName, productName, price, url);
            products.add(temp);
        }

        String priceBefore, priceAfter;
        Float priceBeforeRp = null, priceAfterRp = null;
        for (int i = 0; i<products.size()-1; i++){
            for (int j = 0; j<products.size()-i-1; j++){
                priceBefore = products.get(j).price;
                priceBefore = priceBefore.substring(2, priceBefore.length());
                priceBefore = priceBefore.replaceAll("[.]", "");
                if(priceBefore.contains("jt")){
                    priceBeforeRp = Float.parseFloat(priceBefore.substring(2, toString().indexOf("j")));
                    priceBeforeRp = priceBeforeRp * 1000000;
                } else{
                    priceBeforeRp = Float.parseFloat(priceBefore);
                }

                priceAfter = products.get(j+1).price;
                priceAfter = priceAfter.substring(2, priceAfter.length());
                priceAfter = priceAfter.replaceAll("[.]", "");
                if(priceAfter.contains("jt")){
                    priceAfterRp = Float.parseFloat(priceAfter.substring(2, toString().indexOf("j")));
                    priceAfterRp = priceAfterRp * 1000000;
                } else{
                    priceAfterRp = Float.parseFloat(priceAfter);
                }

                if(priceBeforeRp > priceAfterRp){
                    Collections.swap(products, j, j+1);
                }

            }
        }

        for(int i = 0; i<products.size(); i++){
            System.out.println("Name of the Website: " + products.get(i).websiteName);
            System.out.println("Name of the product: " + products.get(i).productName);
            System.out.println("Price of the product: " + products.get(i).price);
            System.out.println("URL to the product : " + products.get(i).url);
            System.out.println();
        }
        System.out.println("Total : " + products.size());
        driver.quit();
    }
}
