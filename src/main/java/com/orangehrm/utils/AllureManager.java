package com.orangehrm.utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class AllureManager {

    /**
     * Adjunta una captura de pantalla en el reporte de Allure.
     * Este método debe retornar el byte[] de la imagen para que Allure la procese correctamente.
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.err.println("Error taking screenshot: " + e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Adjunta texto en el reporte de Allure.
     */
    @Attachment(value = "{0}", type = "text/plain")
    public static String attachLog(String message) {
        return message;
    }
}

