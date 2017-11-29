package com.geostax.search;

import java.io.PrintWriter;
import java.io.Writer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawler {

	public void crawl() throws Exception {
		Document doc = Jsoup.connect("http://www.gov.cn/2016public/map.htm").get();
		// String title = doc.title();
		// System.out.println(title);

		Elements links = doc.getElementsByClass("pub_border").select("div > a");
		for (Element link : links) {
			System.out.println(link.text() + ":" + link.absUrl("href"));
			Document doc2 = Jsoup.connect(link.absUrl("href")).get();

			Elements title = doc2.select("meta[name=pagetype]");
			if (!title.isEmpty())
				System.out.println(title.first().attr("content").toString());
			Elements content = doc2.getElementsByClass("zl_more");
			if (!content.isEmpty()) {
				String url2 = content.first().select("a").first().absUrl("href");
				System.out.println(url2);
			}
			System.out.println();
		}

	}

	public void crawl1() throws Exception {
		Document doc = Jsoup.connect("http://www.gov.cn/2016public/map.htm").get();
		Elements links = doc.getElementsByClass("pub_border").select("div > a");
		for (Element link : links) {
			System.out.println(link.text() + ":" + link.absUrl("href"));
			Document doc2 = Jsoup.connect(link.absUrl("href")).get();
			Elements content = doc2.getElementsByClass("content").select("li").select("a");
			for (Element link2 : content) {
				System.out.println("|--" + link2.text() + ":" + link2.absUrl("href"));
				Document doc3 = null;
				try {
					doc3 = Jsoup.connect(link2.absUrl("href")).get();
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (doc3 == null)
					continue;
				String title = doc3.title();
				Elements keywords = doc3.select("meta[name=keywords]");
				Elements description = doc3.select("meta[name=description]");
				Elements firstpublishedtime = doc3.select("meta[name=firstpublishedtime]");
				String desc = "";
				String kws = "";
				String date = "";
				if (!description.isEmpty()) {
					desc = description.first().attr("content").toString();
				}
				if (!keywords.isEmpty()) {
					kws = keywords.first().attr("content").toString();
				}
				if (!firstpublishedtime.isEmpty()) {
					date = firstpublishedtime.first().attr("content").toString();
				}
				System.out.println("|----" + title + "|" + kws + "|" + desc + "|" + date);

			}
		}
	}

	public void crawl2(String url, String prefix, PrintWriter writer, String pre_url) throws Exception {

		
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (doc == null)
			return;
		Elements links = doc.getElementsByClass("content").select("li").select("a");
		Elements pagetype = doc.select("meta[name=pagetype]");
		Elements robots = doc.select("meta[name=robots]");
		
		if (!doc.select("meta[name=firstpublishedtime]").isEmpty()) {
			String title = doc.title();
			Elements keywords = doc.select("meta[name=keywords]");
			Elements description = doc.select("meta[name=description]");
			Elements firstpublishedtime = doc.select("meta[name=firstpublishedtime]");
			String desc = "";
			String kws = "";
			String date = "";
			if (!description.isEmpty()) {
				desc = description.first().attr("content").toString();
			}
			if (!keywords.isEmpty()) {
				kws = keywords.first().attr("content").toString();
			}
			if (!firstpublishedtime.isEmpty()) {
				date = firstpublishedtime.first().attr("content").toString();
			}
			writer.println(prefix + title + "|" + kws + "|" + desc + "|" + date);
			return;
		}

		//if (!pagetype.isEmpty() && pagetype.first().attr("content").toString().equals("2")) {
		if (robots.isEmpty()) {
			for (Element link : links) {
				System.out.println(link.text() + ":" + link.absUrl("href"));
				crawl2(link.absUrl("href"), prefix + "--", writer,url);
			}
		}

	}

	public void crawl_map() throws Exception {
		PrintWriter writer = new PrintWriter("output.txt");
		Document doc = Jsoup.connect("http://www.gov.cn/2016public/map.htm").get();
		Elements links = doc.getElementsByClass("pub_border").select("div > a");
		for (Element link : links) {
			writer.println(link.text() + ":" + link.absUrl("href"));
			writer.println("-----------------------------------------------------------------------------");
			crawl2(link.absUrl("href"), "|", writer,"");
		}
		writer.close();
	}

	public static void main(String[] args) throws Exception {

		// new WebCrawler().crawl_map();
		PrintWriter writer = new PrintWriter("output.txt");
		//new WebCrawler().crawl2("http://www.gov.cn/zhengce/zhuti/shuangchuang/index.htm", "|", writer,"");
		new WebCrawler().crawl2("http://www.gov.cn/zhengce/wenjian/zhongyang.htm", "|", writer,"");
		writer.close();
	}
}
