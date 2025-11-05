package com.orangehrm.tests;

import com.orangehrm.utils.AllureManager;
import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.DriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        Allure.step("Setting up browser: " + ConfigReader.getProperty("browser"));
        DriverManager.setDriver(ConfigReader.getProperty("browser"));
        driver = DriverManager.getDriver();

        Allure.step("Navigating to URL: " + ConfigReader.getProperty("url"));
        driver.get(ConfigReader.getProperty("url"));
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            Allure.step("Test failed - Taking screenshot");
            AllureManager.takeScreenshot(driver);
            AllureManager.attachLog("Test failed: " + result.getThrowable().getMessage());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            Allure.step("Test passed successfully");
        }

        DriverManager.quitDriver();
    }
}