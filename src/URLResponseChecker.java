import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This program take sitemap URL as input and prints http response of URLs
 * 
 * @author Arun Kumar Singh
 *
 */
public class URLResponseChecker {
	public static void main(String[] args) {
		//String sitemap = "https://www.globalservices.bt.com/en/sitemap.xml";
		if(args.length == 0) {
			System.out.println("Please enter sitemap URL as argument.");
			return;
		}
		System.out.println("************************ Sitemap URL scanning started. ****************************");
		String sitemap = args[0];
		try {
			URL url = new URL(sitemap);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String line;
			
			FileWriter  urlListFile = new FileWriter ("URLStatusReport.csv");
			BufferedWriter writer = new BufferedWriter(urlListFile);
			
			while ((line = reader.readLine()) != null) {
				int startIndex = 0;
				if (line.contains("<loc>")) {
					int counter = 0;
					while (startIndex != -1) {
						startIndex = line.indexOf("<loc>", counter);
						int endIndex = line.indexOf("</loc>", startIndex);

						String urlStr = line.substring(startIndex + 5, endIndex);
						counter = endIndex + 6;

						URL pageURL = new URL(urlStr);
						HttpURLConnection connection = (HttpURLConnection) pageURL.openConnection();
						connection.setRequestMethod("GET");
						int responseCode = connection.getResponseCode();
						if (responseCode == 200) {
							System.out.println(urlStr + " --> " + responseCode);
							writer.write(urlStr + ","+ responseCode);
							writer.write("\n");
							writer.flush();
						} else {
							System.out.println("******************* Error Page **************************");
							System.out.println(urlStr + " --> " + responseCode);
							writer.write(urlStr + ","+ responseCode);
							writer.write("\n");
							writer.flush();
							System.out.println("**********************************************************");
						}
					}
				}
			}
			writer.close();
		} catch (Exception e) {
			System.out.println("An error occurred while checking the URLs: " + e.getMessage());
		}
		System.out.println("************************ Sitemap URL scanning completed. ****************************");
	}
}
