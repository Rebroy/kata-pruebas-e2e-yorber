package com.orangehrm.tests;

import com.orangehrm.utils.ConfigReader;
import com.orangehrm.utils.DriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

@Listeners(com.orangehrm.listeners.TestListener.class)
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

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
