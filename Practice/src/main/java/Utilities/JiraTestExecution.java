package Utilities;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class JiraTestExecution
{
	WebDriver driver;

	    public void main(String Username,String Password,String EPP_Number, String Execution_Status, String Version,String Cycle, String Folder) throws InterruptedException
	    {
	        System.setProperty("webdriver.chrome.driver", "./src/main/resources/chromedriverWin.exe");
	        driver=new ChromeDriver();
	        driver.manage().window().maximize();
	        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	        driver.get("https://jira.nextgen.com/browse/EPP-"+EPP_Number);
	        driver.findElement(By.id("login-form-username" )).sendKeys(Username);
	        driver.findElement(By.id("login-form-password" )).sendKeys(Password);
	        driver.findElement(By.id("login-form-submit")).click();

	        Thread.sleep(3000);
	        WebElement ExecutionSection= driver.findElement(By.xpath("//div[@id='unfreezedGridHeader']/div[@data-columnkey='versionName']"));
	        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();",ExecutionSection);
	        ArrayList<WebElement> folder= new ArrayList<WebElement>();
	        folder.addAll(driver.findElements(By.xpath("//div[@data-columnkey='folderName']/div[@class='row']")));

	        ArrayList<WebElement> version= new ArrayList<WebElement>();
	        version.addAll(driver.findElements(By.xpath("//div[@class='row-column grid-column versionName-column']")));

	        ArrayList<WebElement> cycle= new ArrayList<WebElement>();
	        cycle.addAll(driver.findElements(By.xpath("//div[@data-columnkey='cycleName' and @class='row-column grid-column cycleName-column']")));
	        int j=0;
	        for(int i=0;i<folder.size();i++)
	        {
	            String folderName=folder.get(i).getText();
	            String VersionName= version.get(i).getText();
	            String CycleName= cycle.get(i).getText();

	            System.out.println(folderName+"  "+VersionName+"  "+CycleName);
	            if(!Folder.equalsIgnoreCase(""))
	            {
	                if(folderName.equalsIgnoreCase(Folder) & VersionName.equalsIgnoreCase(Version) & CycleName.equalsIgnoreCase(Cycle))
	                {
	                    j=i+1;
	                    break;
	                }
	            }
	            else
	            {
	                if(VersionName.equalsIgnoreCase(Version) & CycleName.equalsIgnoreCase(Cycle))
	                {
	                    j=i+1;
	                    break;
	                }
	            }
	        }
	        String xpathForAction="//div[@id='view_issue_execution_section']//div[@id='actionGridBody']/div[@class='row-column grid-column action-column']/div["+j+"]/div/div[1]";
	        driver.findElement(By.xpath(xpathForAction)).click();
	        ArrayList<WebElement> arr= new ArrayList<WebElement>();
	        WebElement TestSteps=driver.findElement(By.xpath("//div[contains(text(),'Test Step')]"));

	        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();",TestSteps);
	        arr.addAll(driver.findElements(By.xpath("//span[@class='trigger-dropDown']")));
	        if (Execution_Status.equalsIgnoreCase("Pass"))
	        {
	            for(int i=1;i<=arr.size();i++)
	            {
	                String xpath="//div[@class='row-column grid-column status-column']/div["+i+"]//span[@class='trigger-dropDown']";
	                /*Select select= new Select(arr.get(i));
	                select.selectByVisibleText("PASS");*/
	                //arr.get(i).click();
	                ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();",driver.findElement(By.xpath(xpath)));
	                driver.findElement(By.xpath(xpath)).click();
	                Thread.sleep(1000);
	                driver.findElement(By.xpath("//div[@class='dropDown-container activeElement']//li[@class='status-select-option stopBlurEvent'][contains(text(),'PASS')]")).click();
	                Thread.sleep(1000);
	            }
	            driver.findElement(By.id("attachment-delete-form-submit")).click();
	        }

	        else
	            {
	            for (int i = 0; i < arr.size(); i++) {
	                Select select = new Select(arr.get(i));
	                select.selectByVisibleText("FAIL");
	            }
	        }


	    }

	    @Test
	    void Execution() throws InterruptedException
	    {
	        this.main("sharathu","Password@3","9823","Pass","3.1.0","Defects","");
	    }

	}



