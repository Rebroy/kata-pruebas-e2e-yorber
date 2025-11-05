package com.orangehrm.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    @FindBy(name = "username")
    private WebElement usernameField;

    @FindBy(name = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement loginButton;

    @FindBy(xpath = "//p[@class='oxd-text oxd-text--p oxd-alert-content-text']")
    private WebElement errorMessage;

    @FindBy(xpath = "//h5[text()='Login']")
    private WebElement loginHeader;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Verify Login page is displayed")
    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginHeader);
    }

    @Step("Enter username: {username}")
    public void enterUsername(String username) {
        sendKeys(usernameField, username);
    }

    @Step("Enter password")
    public void enterPassword(String password) {
        sendKeys(passwordField, password);
    }

    @Step("Click login button")
    public void clickLoginButton() {
        click(loginButton);
    }

    @Step("Login with username: {username}")
    public DashboardPage login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return new DashboardPage(driver);
    }

    @Step("Get error message")
    public String getErrorMessage() {
        waitForElementVisible(errorMessage);
        return getText(errorMessage);
    }
}