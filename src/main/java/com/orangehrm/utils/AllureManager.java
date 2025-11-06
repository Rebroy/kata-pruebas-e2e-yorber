package com.orangehrm.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class AllureManager {

    /**
     * Adjunta una captura de pantalla al reporte de Allure.
     * Puede llamarse desde cualquier punto del test.
     */
    @Attachment(value = "Screenshot", type = "image/png")
    public static byte[] takeScreenshot(WebDriver driver) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            // Método alternativo para asegurar que Allure la adjunte aunque no haya contexto activo
            Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(screenshot), ".png");
            return screenshot;
        } catch (Exception e) {
            System.err.println("Error taking screenshot: " + e.getMessage());
            return new byte[0];
        }
    }

    /**
     * Adjunta texto al reporte de Allure.
     */
    @Attachment(value = "{0}", type = "text/plain")
    public static String attachLog(String message) {
        Allure.addAttachment("Log", "text/plain", new ByteArrayInputStream(message.getBytes()), ".txt");
        return message;
    }
}
