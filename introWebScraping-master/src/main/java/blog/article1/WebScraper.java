package blog.article1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.stage.Stage;

import java.util.List;
import java.util.Scanner;

public class WebScraper {

    public static void main(String[] args) {

        //String searchQuery = "iphone 6s" ;

        String baseUrl = "https://oscar.gatech.edu/pls/bprod/bwckctlg.p_disp_listcrse";

        String[] answers = askClassInfo();

        String queryUrl = "?term_in=201702&subj_in=" + answers[0]
                + "&crse_in=" + answers[1] + "&schd_in=%";

        String sectionCode = answers[2];
        String matchingSectionFound = sectionCode;

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        try {
            String searchUrl = baseUrl + queryUrl;
            HtmlPage page = client.getPage(searchUrl);

            List<HtmlElement> items = (List<HtmlElement>) page.getByXPath("//table[@class='datadisplaytable']");
            if (items.isEmpty()) {
                System.out.println("No items found !");
            } else {

                Item class1 = new Item();
                Item class2 = new Item();

                //System.out.println("NUMBER OF ROWS IN THIS TABLE = "+items.size());
                //int row_num,col_num;
                //row_num=1;
                List<HtmlElement> sections = (List<HtmlElement>) page.getByXPath("//th[@class='ddtitle']");

                int sectionIndex = 0;
                boolean foundMatch = false;

                for (int k = 0; k < sections.size(); k++) {
                    String sectionTitle = sections.get(k).asText();
                    int lastSpace = sectionTitle.lastIndexOf("- ");
                    if (sectionCode.equals(sectionTitle.substring(lastSpace + 2))) {
                        sectionIndex = k;
                        matchingSectionFound = sectionTitle.substring(lastSpace + 2);
                        foundMatch = true;
                    }
                }

                if (!foundMatch) {
                    matchingSectionFound = sections.get(0).asText().substring(sections.get(0).asText().length() - 3).trim();
                }

                HtmlElement trElement = items.get(sectionIndex + 1);
                List<HtmlElement> td_collection = trElement.getElementsByTagName("td");
//                System.out.println("NUMBER OF COLUMNS=" + td_collection.size());
//                col_num = 1;

                ObjectMapper mapper = new ObjectMapper();


                if (td_collection.size() > 0) {
                    class1.setSection(matchingSectionFound);
                    class1.setTime(td_collection.get(1).asText());
                    class1.setDays(td_collection.get(2).asText());
                    String location1 = td_collection.get(3).asText();
                    class1.setBuilding(location1.substring(0, location1.lastIndexOf(" ")));
                    class1.setRoom(location1.substring(location1.lastIndexOf(" ") + 1));
                    String jsonString1 = mapper.writeValueAsString(class1);
                    System.out.println(jsonString1);
                }

                if (td_collection.size() > 7) {
                    class2.setSection(matchingSectionFound);
                    class2.setTime(td_collection.get(8).asText());
                    class2.setDays(td_collection.get(9).asText());
                    String location2 = td_collection.get(3).asText();
                    class2.setBuilding(location2.substring(0, location2.lastIndexOf(" ")));
                    class2.setRoom(location2.substring(location2.lastIndexOf(" ") + 1));
                    String jsonString2 = mapper.writeValueAsString(class2);
                    System.out.println(jsonString2);
                }


//                for (HtmlElement tdElement : td_collection) {
//                    System.out.println("row # " + row_num + ", col # " + col_num + "text=" + tdElement.asText());
//                    col_num++;
//                }
//                row_num++;




            }
        } catch(Exception e){
            e.printStackTrace();
        }

    }

    public void start(Stage primaryStage) {
        primaryStage.show();
    }

    private static String[] askClassInfo() {

        String[] result = new String[3];

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter department code, followed by Course #, followed by Section letter(s):");
        result[0] = scan.nextLine().toUpperCase();
        result[1] = scan.nextLine();
        result[2] = scan.nextLine();

        return result;
    }

}
