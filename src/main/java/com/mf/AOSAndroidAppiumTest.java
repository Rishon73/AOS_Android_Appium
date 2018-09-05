package com.mf;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import io.appium.java_client.android.AndroidDriver;


public class AOSAndroidAppiumTest {

    private AndroidDriver driver = null;
    private static boolean noProblem = true;
    private enum LOG_LEVEL {INFO, ERROR};
    private enum LAB_TYPE {SRF, MC}

    /*
     *
     * Need to:
     *   1. Add proxy handling
     *   2. Add support for MC execution
     *
     */


    @Before
    public void setup(){

        /* Global vars for setup */
        boolean hasProxy = false;
        LAB_TYPE lab_type = LAB_TYPE.SRF;
        String MC_SERVER = "";                          // Your MC server
        String MC_SERVER_USER = "";                     // Your MC user name
        String MC_SERVER_PASSWORD = "";                 // Your MC password

        String SRF_SERVER = (System.getenv("SELENIUM_ADDRESS") != null) ? System.getenv("SELENIUM_ADDRESS") : "https://ftaas.saas.hpe.com";
        /*
        SRF client ID:
        When running Cloud execution, no need to provide it. But when running remote execution, you need
        */
        String SRF_CLIENT_ID = (System.getenv("SRF_CLIENT_ID") != null) ? System.getenv("SRF_CLIENT_ID") :
                "<REPLACE WITH SRF CLIENT ID>";
        /*
        SRF client secret:
        When running Cloud execution, no need to provide it. But when running remote execution, you need
        */
        String SRF_CLIENT_SECRET  = (System.getenv("SRF_CLIENT_SECRET") != null) ? System.getenv("SRF_CLIENT_SECRET") :
                "<REPLACE WITH SRF CLIENT SECRET>";

        String APP_PACKAGE = "com.Advantage.aShopping";
        String APP_ACTIVITY = "com.Advantage.aShopping.SplashActivity";

        SRF_SERVER = SRF_SERVER + "/wd/hub";

        try {
            // Set Capabilities instance
            DesiredCapabilities capabilities = new DesiredCapabilities();

            // Set device capabilities
            capabilities.setCapability("platformName", "Android");
            //capabilities.setCapability("deviceName", "Nexus 7");
            //capabilities.setCapability("deviceName", "Pixel");
            //capabilities.setCapability("platformVersion", ">6.0.0");

            // Application capabilities
            capabilities.setCapability("appPackage", APP_PACKAGE);
            capabilities.setCapability("appActivity", APP_ACTIVITY);

            if (lab_type == LAB_TYPE.SRF) {   // SRF execution - Cloud or remote
                logMessages("================== Using \"" + SRF_SERVER + "\" SRF server ==================", LOG_LEVEL.INFO);
                logMessages("Client Id: " + SRF_CLIENT_ID, LOG_LEVEL.INFO);
                logMessages("Client Secret: " + SRF_CLIENT_SECRET, LOG_LEVEL.INFO);

                capabilities.setCapability("SRF_CLIENT_SECRET", SRF_CLIENT_SECRET);
                capabilities.setCapability("SRF_CLIENT_ID", SRF_CLIENT_ID);
                driver = new AndroidDriver(new URL(SRF_SERVER), capabilities);
            } else {       // Remote execution against Mobile Server
                // Set MC Server credentials (could be skipped if "Anonymous access" is enabled for Appium scripts in the Administration settings).
                capabilities.setCapability("userName", MC_SERVER_USER);
                capabilities.setCapability("password", MC_SERVER_PASSWORD);

                logMessages("================== " + MC_SERVER + "/wd/hub ==================", LOG_LEVEL.INFO);

                // Create a session to the MC server
                driver = new AndroidDriver(new URL(MC_SERVER + "/wd/hub"), capabilities);
            }
            // Create a wait object instance in order to verify expected conditions.
            WebDriverWait waitController = new WebDriverWait(driver, 60);

            // Create an implicitly wait instance to define the timeout for 'findElement' commands
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

            logMessages("=== Android WebDriver object created successfully ===", LOG_LEVEL.INFO);
        } catch (MalformedURLException e) {
            logMessages("=== Malformed MC server URL ===\n" + e.getMessage(), LOG_LEVEL.ERROR);
            noProblem = false;
        } catch (org.openqa.selenium.WebDriverException wde) {
            logMessages("=== Test failed: WebDriverException ===\n" + wde.getMessage(), LOG_LEVEL.ERROR);
            wde.printStackTrace();
            noProblem = false;
        }
    }

    @Test
    public void runTest() {
        if (!noProblem) return;
        try {
            logMessages("Device in use: " + driver.getCapabilities().getCapability("deviceName").toString() +
                    ", version: " + driver.getCapabilities().getCapability("platformVersion").toString(), LOG_LEVEL.INFO);
            WebElement element;

            logMessages("Open menu", LOG_LEVEL.INFO);
            element = driver.findElementById("com.Advantage.aShopping:id/imageViewMenu");
            element.click();

            logMessages("Go to login", LOG_LEVEL.INFO);
            element = driver.findElementById("com.Advantage.aShopping:id/textViewMenuUser");
            element.click();

            logMessages("Set user name", LOG_LEVEL.INFO);
            element = driver.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/" +
                    "android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/" +
                    "android.widget.LinearLayout[2]/android.widget.RelativeLayout[3]/android.widget.EditText[1]");
            element.sendKeys("Shahar");

            logMessages("Set password", LOG_LEVEL.INFO);
            element = driver.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/" +
                    "android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/" +
                    "android.widget.LinearLayout[2]/android.widget.RelativeLayout[4]/android.widget.EditText[1]");
            element.click();
            element.sendKeys("Password1");

            logMessages("Sign in...", LOG_LEVEL.INFO);
            element = driver.findElementById("com.Advantage.aShopping:id/buttonLogin");
            element.click();

            logMessages("Select laptops category", LOG_LEVEL.INFO);
            element = driver.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/" +
                    "android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/" +
                    "android.view.ViewGroup[1]/android.widget.LinearLayout[2]/android.widget.LinearLayout[1]/" +
                    "android.support.v7.widget.RecyclerView[1]/android.widget.RelativeLayout[1]/android.widget.ImageView[1]");
            element.click();

            logMessages("Select laptop", LOG_LEVEL.INFO);
            element = driver.findElementByXPath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/" +
                    "android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/" +
                    "android.view.ViewGroup[1]/android.widget.LinearLayout[2]/android.widget.RelativeLayout[1]/" +
                    "android.widget.LinearLayout[1]/android.widget.GridView[1]/android.widget.RelativeLayout[2]/android.widget.ImageView[1]");
            element.click();

            logMessages("Add to cart", LOG_LEVEL.INFO);
            element = driver.findElementById("com.Advantage.aShopping:id/buttonProductAddToCart");
            element.click();

            logMessages("Goto cart", LOG_LEVEL.INFO);
            element = driver.findElementById("com.Advantage.aShopping:id/imageViewCart");
            element.click();

            logMessages("Checkout", LOG_LEVEL.INFO);
            element = driver.findElementById("com.Advantage.aShopping:id/buttonCheckOut");
            element.click();

            windowSync(1000);

            logMessages("Pay now", LOG_LEVEL.INFO);
            element = driver.findElementById("com.Advantage.aShopping:id/buttonPayNow");
            element.click();

            logMessages("Close dialog", LOG_LEVEL.INFO);
            element = driver.findElementById("com.Advantage.aShopping:id/imageViewCloseDialog");
            element.click();

            logMessages("Sign out - open menu", LOG_LEVEL.INFO);
            element = driver.findElementById("com.Advantage.aShopping:id/imageViewMenu");
            element.click();

            logMessages("Sign out", LOG_LEVEL.INFO);
            element = driver.findElementById("com.Advantage.aShopping:id/textViewMenuSignOut");
            // In case we need XPath...
            // //android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.FrameLayout[1]/android.support.v7.widget.RecyclerView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.TextView[2]
            element.click();

            logMessages("Sign out - accept", LOG_LEVEL.INFO);
            element = driver.findElementById("android:id/button2");
            // In case we need XPath...
            // android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Button[1]
            element.click();

            windowSync(5000);

            logMessages("================== Test completed ==================", LOG_LEVEL.INFO);
        } catch (Exception ex) {
            logMessages("error in script. " + ex.getMessage(), LOG_LEVEL.ERROR);
        }
    }

    @After
    public void tearDown() {
        // Release lock in all cases
        if (driver != null) {
            logMessages("MC session closed", LOG_LEVEL.INFO);
            driver.quit();
        }
    }

    private String getTimeStamp(String pattern) {
        return new java.text.SimpleDateFormat(pattern).format(new java.util.Date());
    }

    private void logMessages(String message, LOG_LEVEL level) {
        String prefix = (level==LOG_LEVEL.INFO) ? "[INFO] " : "[ERROR] ";
        System.out.println(prefix + " [" + getTimeStamp("dd/MM/yyyy HH:mm:ss") + "] " + message);
    }

    public void windowSync(long milliseconds) throws InterruptedException { Thread.sleep(milliseconds); }
}
