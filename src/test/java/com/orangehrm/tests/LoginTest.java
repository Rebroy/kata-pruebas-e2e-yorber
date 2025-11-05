package com.orangehrm.tests;

import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utils.AllureManager;
import com.orangehrm.utils.ConfigReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("OrangeHRM Automation")
@Feature("Login Functionality")
public class LoginTest extends BaseTest {

    @Test(priority = 1, description = "Verify successful login with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify that a user can successfully login with valid username and password")
    @Story("User Login")
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);

        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
        AllureManager.attachLog("Login page verified successfully");

        String username = ConfigReader.getProperty("username");
        String password = ConfigReader.getProperty("password");

        DashboardPage dashboardPage = loginPage.login(username, password);
        AllureManager.takeScreenshot(driver);

        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after successful login");

        Assert.assertEquals(dashboardPage.getDashboardTitle(), "Dashboard",
                "Dashboard title should match");

        AllureManager.attachLog("Login successful - User logged in: " + username);
    }

   @Test(priority = 2, description = "Verify login fails with invalid credentials")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify that login fails when invalid credentials are provided")
    @Story("User Login")
    public void testLoginWithInvalidCredentials() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login("invalidUser", "invalidPassword");
        AllureManager.takeScreenshot(driver);

        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Invalid") || errorMsg.contains("credentials"),
                "Error message should be displayed for invalid credentials");

        AllureManager.attachLog("Login failed as expected with error: " + errorMsg);
    }

    @Test(priority = 3, description = "Verify login with empty credentials")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify validation when submitting empty login form")
    @Story("User Login")
    public void testLoginWithEmptyCredentials() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterUsername("");
        loginPage.enterPassword("");
        loginPage.clickLoginButton();

        AllureManager.takeScreenshot(driver);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(),
                "Should remain on login page when credentials are empty");

        AllureManager.attachLog("Validation working - Empty credentials not accepted");
    }

    @Test(priority = 4, description = "Verify login with valid username and invalid password")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify login fails with valid username but invalid password")
    @Story("User Login")
    public void testLoginWithValidUsernameInvalidPassword() {
        LoginPage loginPage = new LoginPage(driver);

        String username = ConfigReader.getProperty("username");
        loginPage.login(username, "wrongPassword123");

        AllureManager.takeScreenshot(driver);
        String errorMsg = loginPage.getErrorMessage();
        Assert.assertTrue(errorMsg.contains("Invalid") || errorMsg.contains("credentials"),
                "Error message should be displayed");

        AllureManager.attachLog("Login correctly failed with invalid password");
    }
}
