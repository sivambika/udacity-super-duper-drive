package com.udacity.jwdnd.course1.cloudstorage;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    @FindBy(id = "logout-button")
    private WebElement logoutButton;
    @FindBy(id = "nav-notes-tab")
    private WebElement noteTab;
    @FindBy(id = "add-new-note-button")
    private WebElement addNoteButton;
    @FindBy(id = "note-title")
    private WebElement noteTitle;
    @FindBy(id = "note-description")
    private WebElement noteDescription;
    @FindBy(id = "note-submit-button")
    private WebElement submitNoteButton;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialTab;
    @FindBy(id = "add-new-cred-button")
    private WebElement addCredentialButton;
    @FindBy(id = "credential-url")
    private WebElement credentialURL;
    @FindBy(id = "credential-username")
    private WebElement credentialUsername;
    @FindBy(id = "credential-password")
    private WebElement credentialPassword;
    @FindBy(id = "cred-submit")
    private WebElement submitCredentialButton;

    private WebDriverWait wait;
    private WebDriver webDriver;

    public HomePage(WebDriver webDriver, WebDriverWait wait) {
        this.webDriver = webDriver;
        this.wait = wait;
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        logoutButton.click();
    }

    public void clickAddANewNoteButton() {
        // click the note tab
        noteTab.click();
        //wait for 'Add a new note' button
        wait.until(ExpectedConditions.elementToBeClickable(addNoteButton));
        //click the Add a new note button
        addNoteButton.click();
    }

    public void fillOutNoteModalAndSave(String noteTitle, String noteDescription) {
        wait.until(ExpectedConditions.visibilityOf(this.noteTitle));
        this.noteTitle.clear();
        this.noteTitle.sendKeys(noteTitle);

        wait.until(ExpectedConditions.visibilityOf(this.noteDescription));
        this.noteDescription.clear();
        this.noteDescription.sendKeys(noteDescription);

        wait.until(ExpectedConditions.elementToBeClickable(submitNoteButton));
        submitNoteButton.click();
    }

    public void clickEditNote() {
        // click the note tab
        noteTab.click();
        //get the WebElement for 'Edit' button
        WebElement editButton = webDriver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/td[1]/button"));
        //wait for 'Edit' button
        wait.until(ExpectedConditions.elementToBeClickable(editButton));
        //click the 'Edit button'
        editButton.click();
    }

    public void clickDeleteNote() {
        // click the note tab
        noteTab.click();
        //get the WebElement for 'Delete' button
        WebElement deleteButton = this.webDriver.findElement(By.xpath("//*[@id='userTable']/tbody/tr/td[1]/a"));
        //wait for 'Delete' button
        this.wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        //click the 'Edit button'
        deleteButton.click();
    }

    public void clickAddANewCredButton() {
        // click the credential tab
        credentialTab.click();
        //wait for 'Add a new credential' button
        wait.until(ExpectedConditions.elementToBeClickable(addCredentialButton));
        //click the 'Add a new credential' button
        addCredentialButton.click();
    }

    public void fillOutCredModalAndSave(String url, String userName, String password) {
        wait.until(ExpectedConditions.visibilityOf(this.credentialURL));
        this.credentialURL.clear();
        this.credentialURL.sendKeys(url);

        wait.until(ExpectedConditions.visibilityOf(this.credentialUsername));
        this.credentialUsername.clear();
        this.credentialUsername.sendKeys(userName);

        wait.until(ExpectedConditions.visibilityOf(this.credentialPassword));

        if (StringUtils.isNotBlank(this.credentialPassword.getText())) {
            Assertions.assertEquals("loft123", this.credentialPassword.getText());
        }

        this.credentialPassword.clear();
        this.credentialPassword.sendKeys(password);

        wait.until(ExpectedConditions.elementToBeClickable(submitCredentialButton));
        submitCredentialButton.click();
    }

    public void clickEditCredential() {
        // click the credential tab
        credentialTab.click();
        //get the WebElement for 'Edit' button
        WebElement editButton = webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[1]/button"));
        //wait for 'Edit' button
        wait.until(ExpectedConditions.elementToBeClickable(editButton));
        //click the 'Edit button'
        editButton.click();
    }

    public void clickDeleteCredential() {
        // click the credential tab
        credentialTab.click();
        //get the WebElement for 'Delete' button
        WebElement deleteButton = this.webDriver.findElement(By.xpath("//*[@id='credentialTable']/tbody/tr/td[1]/a"));
        //wait for 'Delete' button
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        //click the 'Edit button'
        deleteButton.click();
    }
}
