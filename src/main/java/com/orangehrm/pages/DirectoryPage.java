package com.orangehrm.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class DirectoryPage extends BasePage {

    @FindBy(xpath = "//a[contains(@class,'oxd-main-menu-item') and span[text()='Directory']]")
    private WebElement directoryMenu;

    @FindBy(xpath = "//h6[text()='Directory']")
    private WebElement directoryHeader;

    @FindBy(xpath = "//input[@placeholder='Type for hints...']")
    private WebElement employeeNameSearchField;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchButton;

    @FindBy(xpath = "//div[@class='oxd-autocomplete-dropdown']//span")
    private List<WebElement> autocompleteSuggestions;

    // Contenedor de las tarjetas de empleados
    @FindBy(xpath = "//div[contains(@class,'orangehrm-directory-card')]")
    private List<WebElement> employeeCards;

    // Nombre del empleado dentro de la tarjeta
    @FindBy(xpath = "//p[contains(@class,'orangehrm-directory-card-header')]")
    private WebElement employeeCardName;

    // Cargo o título del empleado (si existe)
    @FindBy(xpath = "//p[contains(@class,'orangehrm-directory-card-subtitle')]")
    private WebElement employeeCardJobTitle;

    @FindBy(xpath = "//span[text()='No Records Found']")
    private WebElement noRecordsMessage;

    public DirectoryPage(WebDriver driver) {
        super(driver);
    }

    @Step("Click on Directory menu")
    public void clickDirectoryMenu() throws InterruptedException {
        click(directoryMenu);
    }

    @Step("Verify Directory page is displayed")
    public boolean isDirectoryPageDisplayed() {
        waitForElementVisible(directoryHeader);
        return isDisplayed(directoryHeader);
    }

    @Step("Enter employee name in search field: {employeeName}")
    public void enterEmployeeName(String employeeName) {
        waitForElementClickable(employeeNameSearchField);
        employeeNameSearchField.clear();
        employeeNameSearchField.sendKeys(employeeName);
    }

    @Step("Wait for autocomplete suggestions")
    public void waitForAutocompleteSuggestions() {
        wait.until(ExpectedConditions.visibilityOfAllElements(autocompleteSuggestions));
    }

    @Step("Select first autocomplete suggestion")
    public void selectFirstSuggestion() {
        waitForAutocompleteSuggestions();
        if (!autocompleteSuggestions.isEmpty()) {
            click(autocompleteSuggestions.get(0));
        }
    }

    @Step("Search employee by name with autocomplete")
    public void searchEmployeeByName(String employeeName) {
        enterEmployeeName(employeeName);

        // Espera dinámica a que aparezcan sugerencias
        try {
            wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                    By.xpath("//div[contains(@class,'oxd-autocomplete-dropdown')]//span")
            ));

            // Envía las teclas ARROW_DOWN y ENTER
            employeeNameSearchField.sendKeys(Keys.ARROW_DOWN);
            employeeNameSearchField.sendKeys(Keys.ENTER);

        } catch (Exception e) {
            System.out.println("⚠️ No se detectaron sugerencias, continuando con la búsqueda directa.");
        }

        clickSearchButton();
    }

    @Step("Click Search button")
    public void clickSearchButton() {
        waitForElementClickable(searchButton);
        click(searchButton);
    }

    @Step("Wait for search results")
    public void waitForSearchResults() {
        By employeeCardsLocator = By.xpath("//div[contains(@class,'orangehrm-directory-card')]");
        By noRecordsLocator = By.xpath("//*[contains(text(),'No Records Found')]");

        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(employeeCardsLocator),
                ExpectedConditions.visibilityOfElementLocated(noRecordsLocator)
        ));
    }

    @Step("Get number of employee cards displayed")
    public int getEmployeeCardsCount() {
        waitForSearchResults();
        return employeeCards.size();
    }

    @Step("Verify employee is found in results")
    public boolean isEmployeeFound() {
        waitForSearchResults();
        return !employeeCards.isEmpty();
    }

    @Step("Get employee name from first card")
    public String getEmployeeNameFromCard() {
        waitForSearchResults();
        WebElement firstCard = employeeCards.get(0);
        WebElement nameElement = firstCard.findElement(By.xpath(".//p[contains(@class,'orangehrm-directory-card-header')]"));
        return nameElement.getText();
    }

    @Step("Get employee job title from first card")
    public String getEmployeeJobTitleFromCard() {
        if (isDisplayed(employeeCardJobTitle)) {
            return getText(employeeCardJobTitle);
        }
        return "";
    }

    @Step("Verify employee information in directory")
    public boolean verifyEmployeeInformation(String expectedFirstName, String expectedLastName) {
        if (!isEmployeeFound()) {
            return false;
        }

        String fullNameOnCard = getEmployeeNameFromCard();
        String expectedFullName = expectedFirstName + " " + expectedLastName;

        return fullNameOnCard.contains(expectedFirstName) &&
                fullNameOnCard.contains(expectedLastName);
    }
}