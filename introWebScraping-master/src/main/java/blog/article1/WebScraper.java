package blog.article1;

import java.io.File;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class WebScraper {

	public static void main(String[] args) {

		//String searchQuery = "iphone 6s" ;
		String baseUrl = "https://oscar.gatech.edu/pls/bprod/bwckctlg.p_disp_listcrse";
		String queryUrl = "?term_in=201702&subj_in=CHIN&crse_in=1002&schd_in=%";
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);

//        final File file = new File("introWebScraping-master/chromedriver");
//        System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
//
//        WebDriver driver = new ChromeDriver();
//        driver.get(baseUrl + queryUrl);


        try {
			String searchUrl = baseUrl + queryUrl;
			HtmlPage page = client.getPage(searchUrl);

			List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//table[@class='datadisplaytable']");
			if(items.isEmpty()){
				System.out.println("No items found !");
			}else{
				for (int i = 0; i < items.size(); i++) {
					HtmlElement htmlItem = items.get(i);
					//HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//span[@class='txt']/span[@class='pl']/a"));
					//HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//span[@class='txt']/span[@class='l2']/span[@class='price']")) ;

					// It is possible that an item doesn't have any price, we set the price to 0.0 in this case
					//String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText() ;
					//System.out.println(htmlItem);
					Item item = new Item();
                    //List<HtmlElement> sublist = (List<HtmlElement>) (page.getByXPath("//table[@class='datadisplaytable']/descendant::table["+i+"]//td[1]"));


                    //WebElement table_element = driver.findElement(By.id("testTable"));
                    //List<HtmlElement> tr_collection=(List<HtmlElement>) page.findByXPath("//th[@class]class('datadisplaytable')/tbody/tr");

                    System.out.println("NUMBER OF ROWS IN THIS TABLE = "+items.size());
                    int row_num,col_num;
                    row_num=1;
                    for (int j = 0; j < items.size(); j++) {
                        HtmlElement trElement = items.get(j);
                        List<HtmlElement> td_collection = trElement.getElementsByTagName("td");
                        System.out.println("NUMBER OF COLUMNS=" + td_collection.size());
                        col_num = 1;
                        for (HtmlElement tdElement : td_collection) {
                            System.out.println("row # " + row_num + ", col # " + col_num + "text=" + tdElement.asText());
                            col_num++;
                        }
                        row_num++;
                    }
                    //item.setDays(htmlItem.asText());
                        //item.setLocation(htmlItem.asText());
                        //item.setTitle(itemAnchor.asText());
                        //item.setUrl( baseUrl + itemAnchor.getHrefAttribute());

                        //item.setPrice(new BigDecimal(itemPrice.replace("$", "")));


                        ObjectMapper mapper = new ObjectMapper();
                        String jsonString = mapper.writeValueAsString(item);

                        System.out.println(jsonString);

				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}

	}

}
