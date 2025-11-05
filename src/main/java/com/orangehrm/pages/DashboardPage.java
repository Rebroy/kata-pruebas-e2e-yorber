package com.orangehrm.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage {

    @FindBy(xpath = "//h6[text()='Dashboard']")
    private WebElement dashboardHeader;

    @FindBy(className = "oxd-userdropdown-name")
    private WebElement userDropdown;

    @FindBy(xpath = "//a[contains(@class,'oxd-main-menu-item') and span[text()='PIM']]")
    private WebElement pimMenu;

    @FindBy(xpath = "//a[contains(@class,'oxd-main-menu-item') and span[text()='Directory']]")
    private WebElement directoryMenu;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    @Step("Verify Dashboard is displayed")
    public boolean isDashboardDisplayed() {
        return isDisplayed(dashboardHeader);
    }

    @Step("Get Dashboard title")
    public String getDashboardTitle() {
        return getText(dashboardHeader);
    }

    @Step("Get logged in username")
    public String getLoggedInUsername() {
        return getText(userDropdown);
    }

    @Step("Verify user is logged in")
    public boolean isUserLoggedIn() {
        return isDisplayed(userDropdown);
    }

    @Step("Navigate to PIM module")
    public PIMPage navigateToPIM() {
        click(pimMenu);
        return new PIMPage(driver);
    }

    @Step("Navigate to Directory module")
    public DirectoryPage navigateToDirectory() {
        click(directoryMenu);
        return new DirectoryPage(driver);
    }
}