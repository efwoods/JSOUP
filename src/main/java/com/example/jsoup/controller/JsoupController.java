package com.example.jsoup.controller;

import com.example.jsoup.json.Comment;
import com.example.jsoup.json.Headlines;
import com.example.jsoup.json.Response;
import com.example.jsoup.repository.CommentRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@RestController
public class JsoupController {

    private Jsoup jsoup;
//    @Value("${user.email}")
//    private String USER_EMAIL;
//    @Value("${user.password}")
//    private String USER_PASSWORD;

    private final String filename = "/Users/home/code/learning/java/JSOUP/filename.txt";

    @Autowired
    CommentRepository commentRepository;

    Response webDataContent = new Response();

    private boolean existsElement(String id, WebDriver driver) {
        try {
            driver.findElement(By.cssSelector(id));
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/jsoup")
    public Response getWebPage(
            @RequestParam(value = "url", defaultValue = "https://en.wikipedia.org") String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc.title());
        webDataContent.setTitle(doc.title());
        Elements newsHeadlines = doc.select("#mp-itn b a");
        List<Headlines> headlines = new ArrayList<Headlines>();
        int index = 0;
        for (Element headline : newsHeadlines) {

            headlines.add(new Headlines(index, headline.attr("title"), headline.absUrl("href")));
            index = index + 1;
        }

        webDataContent.setHeadLines(headlines);
        return webDataContent;
    }


// the endpoint below will scrape links as they come in
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @GetMapping("/scrape")
    public void scrape(
            @RequestParam(
                    value="url",
                    defaultValue="https://www.linkedin.com/video/event/urn:li:ugcPost:6947278127282675712/"
            ) String url,

            @RequestParam(
                    value="username",
                    defaultValue = ""
            ) String username,

            @RequestParam(
                    value="password",
                    defaultValue = ""
            ) String password,

            @RequestParam(
                    value="xpath",
                    defaultValue="/html/body/div[4]/div[3]/div[2]/div/div/aside/div/div/div[2]/div/div/div[1]/div[2]/div[2]/div"
            ) String xpath
    ) throws MalformedURLException, InterruptedException {
        // setup webdriver
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver"); //home/ec2-user/chromedriver /usr/local/bin/chromedriver
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless");
        options.addArguments("window-size=1400,1500");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("start-maximized");
        options.addArguments("enable-automation");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(url);

        // login
        driver.findElement(By.className("main__sign-in-link")).click();
        driver.findElement(By.id("username")).click();
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).click();
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector(".btn__primary--large")).click();


        // verify 2fa with mobile (if first time from new location)

        //new WebDriverWait(driver, Duration.ofSeconds(3)); // wait for the next page to load; see if 2fa

        if ( driver.getPageSource().contains("2fa")){
            System.out.println("Text: " + "2fa" + " is present. ");


//        if (existsElement("#input__phone_verification_pin", driver)) {
            System.out.println("please enter 2fa code");
            new WebDriverWait(driver, Duration.ofSeconds(60));
            Thread.sleep(60000);
            try {
                File myObj = new File(this.filename);
                Scanner myReader = new Scanner(myObj);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    System.out.println(data);
                    driver.findElement(By.cssSelector("#input__phone_verification_pin")).sendKeys(data); // this is not detected
                    driver.findElement(By.cssSelector("#two-step-submit-button")).click(); // this is not detected
                }
                myReader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }


        // search for requested content
        System.out.println("Searching for comments to scrape");
        List<WebElement> comment_div = driver.findElements(By.xpath(xpath));
        System.out.println("assert list contents div found");
        System.out.println("assert comment list is found");
        System.out.println("comment_list element:" + comment_div.size());

        // scrape, save, & host data
        int current_size = 0;
        while (comment_div.size() <= 13) {
            comment_div = driver.findElements(By.xpath(xpath));
            if (comment_div.size() > current_size) {
                System.out.println("found new comment!");
                System.out.println("comment #: " + current_size);
                System.out.println(("comment: " + comment_div.get((current_size)).getText()));
                System.out.println("total number of comments found:" + comment_div.size());
                commentRepository.save(new Comment((current_size+1),comment_div.get(current_size).getText()));
                current_size = current_size + 1;
            }
        }

        // cleanup
        driver.close();
    }

    @GetMapping("2fa")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public void whenWriteStringUsingBufferedWritter_thenCorrect(@RequestParam(value="str", defaultValue = "This works") String str) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename));
        writer.write(str);

        writer.close();
    }

}