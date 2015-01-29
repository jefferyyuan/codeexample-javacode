import java.util.List;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.uci.ics.crawler4j.url.WebURL;

public class Controller {

	public static class Crawler extends WebCrawler {
		Pattern filters = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
				+ "|png|tiff?|mid|mp2|mp3|mp4"
				+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
				+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

		@Override
		public boolean shouldVisit(WebURL url) {
			String href = url.getURL().toLowerCase();
			return !filters.matcher(href).matches()
					&& href.startsWith("http://www.lankadeepa.lk/");
		}

		@Override
		public void visit(Page page) {
			String url = page.getWebURL().getURL();
			System.out.println("Visited: " + url);

			if (page.getParseData() instanceof HtmlParseData) {
				HtmlParseData htmlParseData = (HtmlParseData) page
						.getParseData();
				String text = htmlParseData.getText();
				String html = htmlParseData.getHtml();
				List<WebURL> links = htmlParseData.getOutgoingUrls();
				System.out.println("Text length: " + text.length());
				System.out.println("Html length: " + html.length());
				System.out.println("Number of outgoing links: " + links.size());
			}

		}
	}

	public static void main(String[] args) throws Exception {
		String rootFolder = "data/crowler";
		int numberOfCrawlers = 1;

		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(rootFolder);
		config.setMaxPagesToFetch(4);
		config.setPolitenessDelay(1000);
		config.setMaxDepthOfCrawling(10);
//		config.setProxyHost("cache.mrt.ac.lk");
//		config.setProxyPort(3128);

		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher);
		CrawlController controller = new CrawlController(config, pageFetcher,
				robotstxtServer);

		controller.addSeed("http://www.lankadeepa.lk/");
		controller.start(Crawler.class, numberOfCrawlers);

	}

}