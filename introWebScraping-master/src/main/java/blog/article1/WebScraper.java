package blog.article1;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebScraper {

	public static void main(String[] args) {

		//String searchQuery = "iphone 6s" ;
		String baseUrl = "https://oscar.gatech.edu/pls/bprod/bwckctlg.p_disp_listcrse" ;
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		try {
			String searchUrl = baseUrl + "?term_in=201702&subj_in=CHIN&crse_in=1002&schd_in=%";
			HtmlPage page = client.getPage(searchUrl);

			List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//th[@class='ddtitle']");
			if(items.isEmpty()){
				System.out.println("No items found !");
			}else{
				for (int i = 0; i < items.size() - 1; i++) {
					HtmlElement htmlItem = items.get(i);
					//HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(".//span[@class='txt']/span[@class='pl']/a"));
					//HtmlElement spanPrice = ((HtmlElement) htmlItem.getFirstByXPath(".//span[@class='txt']/span[@class='l2']/span[@class='price']")) ;

					// It is possible that an item doesn't have any price, we set the price to 0.0 in this case
					//String itemPrice = spanPrice == null ? "0.0" : spanPrice.asText() ;
					//System.out.println(htmlItem);
					Item item = new Item();
					item.setTime(htmlItem.asText());
					item.setDays(htmlItem.asText());
					item.setLocation(htmlItem.asText());
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
