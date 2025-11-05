package com.orangehrm.tests;

import com.orangehrm.pages.DashboardPage;
import com.orangehrm.pages.DirectoryPage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.pages.PIMPage;
import com.orangehrm.utils.AllureManager;
import com.orangehrm.utils.ConfigReader;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Epic("OrangeHRM Automation")
@Feature("Employee Management")
public class EmployeeManagementTest extends BaseTest {

    private String employeeId;
    private String firstName;
    private String middleName;
    private String lastName;

    @Test(priority = 1, description = "Complete Employee Management Flow")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify complete employee creation and verification flow")
    @Story("Add Employee and Verify in Directory")
    public void testCompleteEmployeeManagementFlow() throws InterruptedException {
        // Generar datos únicos para el empleado
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        firstName = "Luis";
        middleName = "Jose";
        lastName = "Velez" + timestamp;

        // 1. Login con credenciales de administrador
        Allure.step("Step 1: Login to OrangeHRM with admin credentials");
        LoginPage loginPage = new LoginPage(driver);

        String username = ConfigReader.getProperty("username");
        String password = ConfigReader.getProperty("password");

        DashboardPage dashboardPage = loginPage.login(username, password);
        Assert.assertTrue(dashboardPage.isDashboardDisplayed(),
                "Dashboard should be displayed after login");
        AllureManager.takeScreenshot(driver);

        // 2. Navegar al módulo PIM
        Allure.step("Step 2: Navigate to PIM module");
        PIMPage pimPage = dashboardPage.navigateToPIM();
        Assert.assertTrue(pimPage.isPIMPageDisplayed(),
                "PIM page should be displayed");
        AllureManager.takeScreenshot(driver);

        // 3. Agregar nuevo empleado con información básica
        Allure.step("Step 3: Add new employee with basic information");

        // Obtener la ruta absoluta de la foto
        String projectPath = System.getProperty("user.dir");
        String photoPath = projectPath + "/src/test/resources/employee_photo.jpg";

        // Verificar si existe la foto, si no, usar null
        File photoFile = new File(photoPath);
        if (!photoFile.exists()) {
            AllureManager.attachLog("Warning: Photo file not found at: " + photoPath);
            photoPath = null; // No subir foto si no existe
        }

        employeeId = pimPage.addEmployee(firstName, middleName, lastName, photoPath);

        Assert.assertTrue(pimPage.isPersonalDetailsDisplayed(),
                "Personal Details page should be displayed after saving employee");
        AllureManager.attachLog("Employee created with ID: " + employeeId);
        AllureManager.attachLog("Employee Name: " + firstName + " " + middleName + " " + lastName);
        AllureManager.takeScreenshot(driver);

        // 4. Navegar al módulo Directory
        Allure.step("Step 4: Navigate to Directory module");
        DirectoryPage directoryPage = new DirectoryPage(driver);
        directoryPage.clickDirectoryMenu();

        Assert.assertTrue(directoryPage.isDirectoryPageDisplayed(),
                "Directory page should be displayed");
        AllureManager.takeScreenshot(driver);

        // 5. Buscar empleado por nombre
        Allure.step("Step 5: Search for employee by name");
        String searchName = firstName + " " + lastName;
        directoryPage.searchEmployeeByName(lastName);

        AllureManager.attachLog("Searching for employee: " + searchName);
        AllureManager.takeScreenshot(driver);

        // 6. Validar que la información del empleado se guardó correctamente
        Allure.step("Step 6: Validate employee information in Directory");

        Assert.assertTrue(directoryPage.isEmployeeFound(),
                "Employee should be found in directory search results");

        boolean isInformationCorrect = directoryPage.verifyEmployeeInformation(firstName, lastName);
        Assert.assertTrue(isInformationCorrect,
                "Employee information should match the entered data");

        String employeeNameFromCard = directoryPage.getEmployeeNameFromCard();
        AllureManager.attachLog("Employee found in Directory: " + employeeNameFromCard);

        Assert.assertTrue(employeeNameFromCard.contains(firstName),
                "Employee first name should be displayed in the directory card");
        Assert.assertTrue(employeeNameFromCard.contains(lastName),
                "Employee last name should be displayed in the directory card");

        AllureManager.takeScreenshot(driver);
        AllureManager.attachLog("Employee management flow completed successfully!");
    }

   /* @Test(priority = 2, description = "Add employee without photo")
    @Severity(SeverityLevel.NORMAL)
    @Description("Test to verify employee can be added without uploading a photo")
    @Story("Add Employee")
    public void testAddEmployeeWithoutPhoto() {
        // Generar datos únicos
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String empFirstName = "Maria";
        String empMiddleName = "Isabel";
        String empLastName = "Rodriguez" + timestamp;

        // Login
        LoginPage loginPage = new LoginPage(driver);
        DashboardPage dashboardPage = loginPage.login(
                ConfigReader.getProperty("username"),
                ConfigReader.getProperty("password")
        );

        // Navegar a PIM y agregar empleado sin foto
        PIMPage pimPage = dashboardPage.navigateToPIM();
        String newEmployeeId = pimPage.addEmployee(empFirstName, empMiddleName, empLastName, null);

        Assert.assertTrue(pimPage.isPersonalDetailsDisplayed(),
                "Employee should be created successfully without photo");
        AllureManager.attachLog("Employee created without photo - ID: " + newEmployeeId);
        AllureManager.takeScreenshot(driver);
    }*/
}