package com.orangehrm.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.File;

public class PIMPage extends BasePage {

    @FindBy(xpath = "//a[contains(@class,'oxd-main-menu-item') and span[text()='PIM']]")
    private WebElement pimMenu;

    @FindBy(xpath = "//button[normalize-space()='Add']")
    private WebElement addButton;

    @FindBy(xpath = "//h6[text()='Add Employee']")
    private WebElement addEmployeeHeader;

    @FindBy(name = "firstName")
    private WebElement firstNameField;

    @FindBy(name = "middleName")
    private WebElement middleNameField;

    @FindBy(name = "lastName")
    private WebElement lastNameField;

    @FindBy(xpath = "//label[normalize-space(text())='Employee Id']/following::input[1]")
    private WebElement employeeIdField;

    @FindBy(xpath = "//button[contains(@class,'employee-image-action')]")
    private WebElement addPhotoButton;

    @FindBy(xpath = "//input[@type='file' and contains(@class,'oxd-file-input')]")
    private WebElement hiddenFileInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement saveButton;

    @FindBy(xpath = "//h6[text()='Personal Details']")
    private WebElement personalDetailsHeader;

    @FindBy(xpath = "//div[contains(@class,'oxd-toast--success')]")
    private WebElement successToast;

    public PIMPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click on PIM menu")
    public void clickPIMMenu() {
        click(pimMenu);
    }

    @Step("Verify PIM page is displayed")
    public boolean isPIMPageDisplayed() {
        waitForElementClickable(addButton);
        return isDisplayed(addButton);
    }

    @Step("Click Add Employee button")
    public void clickAddButton() {
        click(addButton);
    }

    @Step("Verify Add Employee form is displayed")
    public boolean isAddEmployeeFormDisplayed() {
        waitForElementVisible(addEmployeeHeader);
        return isDisplayed(addEmployeeHeader);
    }

    @Step("Enter employee first name: {firstName}")
    public void enterFirstName(String firstName) {
        sendKeys(firstNameField, firstName);
    }

    @Step("Enter employee middle name: {middleName}")
    public void enterMiddleName(String middleName) {
        sendKeys(middleNameField, middleName);
    }

    @Step("Enter employee last name: {lastName}")
    public void enterLastName(String lastName) {
        sendKeys(lastNameField, lastName);
    }

    @Step("Get generated employee ID")
    public String getEmployeeId() {
        waitForElementVisible(employeeIdField);
        System.out.println(employeeIdField.getAttribute("value"));
        return employeeIdField.getAttribute("value");
    }

    @Step("Upload employee photo")
    public void uploadPhoto(String photoPath) {
        try {
            WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));

            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].style.display='block'; arguments[0].removeAttribute('hidden');", fileInput
            );

            fileInput.sendKeys(new File(photoPath).getAbsolutePath());
            System.out.println("📸 Foto cargada correctamente sin usar el diálogo del sistema");
Thread.sleep(5000);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading employee photo", e);
        }
    }

    @Step("Click Save button")
    public void clickSaveButton() {
        click(saveButton);
    }

    @Step("Wait for success message")
    public void waitForSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOf(successToast));
    }

    @Step("Verify Personal Details page is displayed")
    public boolean isPersonalDetailsDisplayed() {
        waitForElementVisible(personalDetailsHeader);
        return isDisplayed(personalDetailsHeader);
    }

    @Step("Add new employee with basic information")
    public String addEmployee(String firstName, String middleName, String lastName, String photoPath) {
        clickAddButton();
        isAddEmployeeFormDisplayed();

        enterFirstName(firstName);
        enterMiddleName(middleName);
        enterLastName(lastName);

        String employeeId = getEmployeeId();

        if (photoPath != null && !photoPath.isEmpty()) {
            uploadPhoto(photoPath);
        }

        clickSaveButton();
        waitForSuccessMessage();
        return employeeId;
    }
}
