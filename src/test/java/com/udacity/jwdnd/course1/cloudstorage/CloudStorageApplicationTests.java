package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {
    @LocalServerPort
    private int port;
    private WebDriver driver;
    WebDriverWait webDriverWait;
    private SignUpPage signUpPage;
    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        driver = new ChromeDriver();
        webDriverWait = new WebDriverWait(driver, 5);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    private void doMockSignUp(String firstName, String lastName, String userName, String password) {
        signUpPage = new SignUpPage(driver);
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
        // Fill out credentials
        signUpPage.fillOutCredentials(webDriverWait, firstName, lastName, userName, password);
        // Attempt to sign up.
        signUpPage.clickSignUp(webDriverWait);
    }

    private void doLogIn(String userName, String password) {
        loginPage = new LoginPage(driver);
        // Fill out credentials
        loginPage.fillOutCredentials(webDriverWait, userName, password);
        // Attempt to log in
        loginPage.clickLogin(webDriverWait);
        webDriverWait.until(ExpectedConditions.titleContains("Home"));
    }

    private void signupAndLogin() {
        // Visit the signup page.
        driver.get("http://localhost:" + this.port + "/signup");
        //sign up with a dummy account
        doMockSignUp("Samplefirstname", "SamplelastName", "test", "password123");

        // Visit the login page
        driver.get("http://localhost:" + this.port + "/login");
        // Log in to our dummy account.
        doLogIn("test", "password123");
    }

    @Test
    public void testHomePageUnAuthorizedAccess() {

        /**
         * Signup, login and then make sure the home page is accessible
         */
        signupAndLogin();
        Assertions.assertEquals("Home", driver.getTitle());

        /**
         * Logout, attempt to access the home page, it should be inaccessible
         */
        homePage = new HomePage(driver, webDriverWait);
        homePage.logout();
        driver.get("http://localhost:" + this.port + "/home");

        Assertions.assertEquals("Login", driver.getTitle());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     * <p>
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */

    @Test
    public void testBadUrl() {
        // Create a mock account and login
        signupAndLogin();
        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Visit the signup page.
        driver.get("http://localhost:" + this.port + "/signup");
        // Create a test account
        doMockSignUp("Redirection", "Test", "RT", "123");

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     * <p>
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     * <p>
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Visit the signup page.
        driver.get("http://localhost:" + this.port + "/signup");
        // Create a test account
        doMockSignUp("Large File", "Test", "LFT", "123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
    }

    private void verifyNoteSavedOrUpdated(String expectedTitle, String expectedDes) {
        webDriverWait.until(ExpectedConditions.visibilityOf
                (driver.findElement(By.xpath("//*[@id='userTable']/tbody/tr"))));

        WebElement newlyAddedNoteTitle = this.driver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/th"));
        WebElement newlyAddedNoteDesc = this.driver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/td[2]"));

        Assertions.assertEquals(expectedTitle, newlyAddedNoteTitle.getText());
        Assertions.assertEquals(expectedDes, newlyAddedNoteDesc.getText());
    }

    @Test
    public void testNoteCreation() throws InterruptedException {
        //create a mock signup and login
        signupAndLogin();
        homePage = new HomePage(driver, webDriverWait);
        //click "Add a new note button"
        homePage.clickAddANewNoteButton();

        Thread.sleep(500);
        //fill the note title and desc and click Save button
        homePage.fillOutNoteModalAndSave("To-do", "1.Shopping 2.Gardening");
        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("note-status-msg"))));
        //tests if the note was created successfully.
        Assertions.assertEquals("A note has been created successfully!", driver.findElement(By.id("note-status-msg")).getText());
        // verifies if the newly added note is displayed
        verifyNoteSavedOrUpdated("To-do", "1.Shopping 2.Gardening");

        homePage.clickDeleteNote();
    }

    @Test
    public void testEditNote() throws InterruptedException {
        //create a mock signup and login
        signupAndLogin();
        homePage = new HomePage(driver, webDriverWait);
        //click "Add a new note button"
        homePage.clickAddANewNoteButton();
        //fill the note title and desc and click Save button
        homePage.fillOutNoteModalAndSave("To-do", "1.Shopping 2.Gardening");
        //webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("note-status-msg"))));
        Thread.sleep(500);
        //click the edit button and
        homePage.clickEditNote();
        //edit the note title and desc and click Save button
        homePage.fillOutNoteModalAndSave("To-do-updated", "1.Shopping 2.Gardening 3.Knitting");
        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("note-status-msg"))));
        //tests if the note was updated successfully.
        Assertions.assertEquals("A note has been updated successfully!", driver.findElement(By.id("note-status-msg")).getText());
        // verifies if the newly edited note is displayed
        verifyNoteSavedOrUpdated("To-do-updated", "1.Shopping 2.Gardening 3.Knitting");

        homePage.clickDeleteNote();
    }

    @Test
    public void testNoteDeletion() throws InterruptedException {
        //create a mock signup and login
        signupAndLogin();
        homePage = new HomePage(driver, webDriverWait);
        //click "Add a new note button"
        homePage.clickAddANewNoteButton();
        //fill the note title and desc and click Save button
        homePage.fillOutNoteModalAndSave("To-do", "1.Shopping 2.Gardening");
        //webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("note-status-msg"))));
        Thread.sleep(500);

        homePage.clickDeleteNote();
        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("note-delete-success-msg"))));
        //tests if the note was deleted successfully.
        Assertions.assertEquals("The note has been deleted successfully!!", driver.findElement(By.id("note-delete-success-msg")).getText());
        List<WebElement> rows = driver.findElements(By.xpath("//*[@id='userTable']/tbody/tr"));
        Assertions.assertEquals(0, rows.size());
    }

    private void verifyCredentialSavedOrUpdated(String expectedURL, String expectedUserName, String expectedPassword) {
        webDriverWait.until(ExpectedConditions.visibilityOf
                (driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr"))));

        WebElement newlyAddedURL = this.driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/th"));
        WebElement newlyAddedUserName = this.driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[2]"));
        WebElement newlyAddedPassword = this.driver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[3]"));

        Assertions.assertEquals(expectedURL, newlyAddedURL.getText());
        Assertions.assertEquals(expectedUserName, newlyAddedUserName.getText());
        Assertions.assertNotEquals(expectedPassword, newlyAddedPassword.getText());
    }


    @Test
    public void testCredentialCreation() throws InterruptedException {
        //create a mock signup and login
        signupAndLogin();
        homePage = new HomePage(driver, webDriverWait);
        //click "Add a new credential button"
        homePage.clickAddANewCredButton();

        Thread.sleep(500);
        //fill the credential details and click Save button
        homePage.fillOutCredModalAndSave("https://loft.com", "loft", "loft123");
        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("cred-status-msg"))));
        //tests if the credential was created successfully.
        Assertions.assertEquals("The credential details has been created successfully!", driver.findElement(By.id("cred-status-msg")).getText());
        // verifies if the newly added credential is displayed
        verifyCredentialSavedOrUpdated("https://loft.com", "loft", "loft123");

        homePage.clickDeleteCredential();
    }

    @Test
    public void testCredentialEdit() throws InterruptedException {
        //create a mock signup and login
        signupAndLogin();
        homePage = new HomePage(driver, webDriverWait);
        //click "Add a new credential button"
        homePage.clickAddANewCredButton();
        //fill the note title and desc and click Save button
        homePage.fillOutCredModalAndSave("https://loft.com", "loft", "loft123");
        Thread.sleep(500);
        //click the edit button
        homePage.clickEditCredential();
        //edit the credential details and click Save button
        homePage.fillOutCredModalAndSave("https://loft.com", "testloft", "loft1234");
        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("cred-status-msg"))));
        //tests if the credential details was updated successfully.
        Assertions.assertEquals("The credential details has been updated successfully!", driver.findElement(By.id("cred-status-msg")).getText());
        // verifies if the updated credential details are displayed
        verifyCredentialSavedOrUpdated("https://loft.com", "testloft", "loft1234");

        homePage.clickDeleteCredential();
    }

    @Test
    public void testCredDeletion() throws InterruptedException {
        //create a mock signup and login
        signupAndLogin();
        homePage = new HomePage(driver, webDriverWait);
        //click "Add a new credential button"
        homePage.clickAddANewCredButton();
        //fill the note title and desc and click Save button
        homePage.fillOutCredModalAndSave("https://loft.com", "loft", "loft123");

        Thread.sleep(500);

        homePage.clickDeleteCredential();
        webDriverWait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("cred-delete-success-msg"))));
        //tests if the credential was deleted successfully.
        Assertions.assertEquals("The credential has been deleted successfully!", driver.findElement(By.id("cred-delete-success-msg")).getText());
        List<WebElement> rows = driver.findElements(By.xpath("//*[@id='credentialTable']/tbody/tr"));
        Assertions.assertEquals(0, rows.size());
    }
}
