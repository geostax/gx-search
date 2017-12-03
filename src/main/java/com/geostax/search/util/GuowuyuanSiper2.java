package com.geostax.search.util;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GuowuyuanSiper2 {

	TransportClient client = null;

	public GuowuyuanSiper2() {
		try {
			client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void crawl(String url, List<String> urllist) throws Exception {

		Document doc = Jsoup.connect(url).get();
		Elements pagetype = doc.select("meta[name=pagetype]");
		if (!pagetype.isEmpty()) {
			Elements links = doc.getElementsByClass("content").select("li").select("a");
			for (Element link : links) {
				String url2 = link.absUrl("href");
				if (urllist.contains(url2))
					continue;
				else {
					urllist.add(url2);
					crawl(url2, urllist);
				}

			}
		}

		if (!doc.select("meta[name=firstpublishedtime]").isEmpty()) {
			String title = doc.title();

			Elements keywords = doc.select("meta[name=keywords]");
			Elements description = doc.select("meta[name=description]");
			Elements firstpublishedtime = doc.select("meta[name=firstpublishedtime]");
			Elements lanmu = doc.select("meta[name=lanmu]");
			String desc = "";
			String kws = "";
			String date = "";
			String lanm = "";
			if (!description.isEmpty()) {
				desc = description.first().attr("content").toString();
			}
			if (!keywords.isEmpty()) {
				kws = keywords.first().attr("content").toString();
			}
			if (!firstpublishedtime.isEmpty()) {
				date = firstpublishedtime.first().attr("content").toString();
			}
			if (!lanmu.isEmpty()) {
				lanm = lanmu.first().attr("content").toString();
			}

			Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put("url", url);
			infoMap.put("title", title);
			infoMap.put("description", desc);
			infoMap.put("createTime", date);
			infoMap.put("lanmu", lanm);
			// System.out.println(infoMap);
			IndexResponse response = client.prepareIndex("guowuyuan", "page").setSource(infoMap).execute().actionGet();
			System.out.println("id:" + response.getId());
			// writer.println(infoMap);
		}

	}

	public void cwhy() throws Exception {
		String url = "http://www.gov.cn/guowuyuan/gwy_cwh.htm";
		PrintWriter writer = new PrintWriter("result/常务会议.txt");
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements links = doc.getElementsByClass("content").select("li").select("a");
			for (Element link : links) {

				System.out.println(link.text() + ":" + link.absUrl("href"));
				url = link.absUrl("href");
				doc = Jsoup.connect(url).get();
				if (!doc.select("meta[name=firstpublishedtime]").isEmpty()) {
					String title = doc.title();

					Elements keywords = doc.select("meta[name=keywords]");
					Elements description = doc.select("meta[name=description]");
					Elements firstpublishedtime = doc.select("meta[name=firstpublishedtime]");
					Elements lanmu = doc.select("meta[name=lanmu]");
					String desc = "";
					String kws = "";
					String date = "";
					String lanm = "";
					if (!description.isEmpty()) {
						desc = description.first().attr("content").toString();
					}
					if (!keywords.isEmpty()) {
						kws = keywords.first().attr("content").toString();
					}
					if (!firstpublishedtime.isEmpty()) {
						date = firstpublishedtime.first().attr("content").toString();
					}
					if (!lanmu.isEmpty()) {
						lanm = lanmu.first().attr("content").toString();
					}

					Map<String, Object> infoMap = new HashMap<String, Object>();
					infoMap.put("url", url);
					infoMap.put("title", title);
					infoMap.put("description", desc);
					infoMap.put("createTime", date);
					infoMap.put("lanmu", lanm);
					// System.out.println(infoMap);
					// IndexResponse response = client.prepareIndex("guowuyuan",
					// "page").setSource(infoMap).execute().actionGet();
					// System.out.println("id:" + response.getId());

					writer.println(infoMap);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		writer.close();
	}

	public void crawl2(String url, PrintWriter writer, String pre_url) throws Exception {
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
			Elements lanmu = doc.select("meta[name=lanmu]");
			String desc = "";
			String kws = "";
			String date = "";
			String lanm = "";
			if (!description.isEmpty()) {
				desc = description.first().attr("content").toString();
			}
			if (!keywords.isEmpty()) {
				kws = keywords.first().attr("content").toString();
			}
			if (!firstpublishedtime.isEmpty()) {
				date = firstpublishedtime.first().attr("content").toString();
			}
			if (!lanmu.isEmpty()) {
				lanm = lanmu.first().attr("content").toString();
			}

			Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put("url", url);
			infoMap.put("title", title);
			infoMap.put("description", desc);
			infoMap.put("createTime", date);
			infoMap.put("lanmu", lanm);

			// IndexResponse response = client.prepareIndex("guowuyuan",
			// "page").setSource(infoMap).execute().actionGet();
			// System.out.println("id:" + response.getId());
			writer.println(infoMap);
			return;
		}

		// if (!pagetype.isEmpty() &&
		// pagetype.first().attr("content").toString().equals("2")) {
		if (robots.isEmpty()) {
			for (Element link : links) {
				// System.out.println(link.text() + ":" + link.absUrl("href"));
				crawl2(link.absUrl("href"), writer, url);
			}
		}

	}

	public void crawl3(String url, PrintWriter writer, String pre_url) throws Exception {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			Elements links = doc.getElementsByClass("content").select("li").select("a");
			for (Element link : links) {
				url = link.absUrl("href");
				doc = Jsoup.connect(url).get();
				if (!doc.select("meta[name=firstpublishedtime]").isEmpty()) {
					String title = doc.title();

					Elements keywords = doc.select("meta[name=keywords]");
					Elements description = doc.select("meta[name=description]");
					Elements firstpublishedtime = doc.select("meta[name=firstpublishedtime]");
					Elements lanmu = doc.select("meta[name=lanmu]");
					String desc = "";
					String kws = "";
					String date = "";
					String lanm = "";
					if (!description.isEmpty()) {
						desc = description.first().attr("content").toString();
					}
					if (!keywords.isEmpty()) {
						kws = keywords.first().attr("content").toString();
					}
					if (!firstpublishedtime.isEmpty()) {
						date = firstpublishedtime.first().attr("content").toString();
					}
					if (!lanmu.isEmpty()) {
						lanm = lanmu.first().attr("content").toString();
					}

					Map<String, Object> infoMap = new HashMap<String, Object>();
					infoMap.put("url", url);
					infoMap.put("title", title);
					infoMap.put("description", desc);
					infoMap.put("createTime", date);
					infoMap.put("lanmu", lanm);
					// System.out.println(infoMap);
					// IndexResponse response = client.prepareIndex("guowuyuan",
					// "page").setSource(infoMap).execute().actionGet();
					// System.out.println("id:" + response.getId());

					writer.println(infoMap);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void crawl4(String url, PrintWriter writer) {

		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
			System.out.println(doc);

			Elements links = doc.getElementsByClass("list01").select("li").select("a");
			for (Element link : links) {

				System.out.println(link.text() + ":" + link.absUrl("href"));
				url = link.absUrl("href");
				doc = Jsoup.connect(url).get();
				if (!doc.select("meta[name=firstpublishedtime]").isEmpty()) {
					String title = doc.title();

					Elements keywords = doc.select("meta[name=keywords]");
					Elements description = doc.select("meta[name=description]");
					Elements firstpublishedtime = doc.select("meta[name=firstpublishedtime]");
					Elements lanmu = doc.select("meta[name=lanmu]");
					String desc = "";
					String kws = "";
					String date = "";
					String lanm = "";
					if (!description.isEmpty()) {
						desc = description.first().attr("content").toString();
					}
					if (!keywords.isEmpty()) {
						kws = keywords.first().attr("content").toString();
					}
					if (!firstpublishedtime.isEmpty()) {
						date = firstpublishedtime.first().attr("content").toString();
					}
					if (!lanmu.isEmpty()) {
						lanm = lanmu.first().attr("content").toString();
					}

					Map<String, Object> infoMap = new HashMap<String, Object>();
					infoMap.put("url", url);
					infoMap.put("title", title);
					infoMap.put("description", desc);
					infoMap.put("createTime", date);
					infoMap.put("lanmu", lanm);
					// System.out.println(infoMap);
					// IndexResponse response = client.prepareIndex("guowuyuan",
					// "page").setSource(infoMap).execute().actionGet();
					// System.out.println("id:" + response.getId());

					writer.println(infoMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		List<String> list = new ArrayList<>();
		GuowuyuanSiper2 spider=new GuowuyuanSiper2();
		
		spider.crawl("http://www.gov.cn/guowuyuan/xinwen.htm", list);// 领导活动
		spider.crawl("http://www.gov.cn/guowuyuan/gwy_cwh.htm", list);// 常务会议
		spider.crawl("http://www.gov.cn/guowuyuan/quantihui.htm", list);// 全体会议
		spider.crawl("http://www.gov.cn/premier/zuixin.htm", list);// 最新
		spider.crawl("http://www.gov.cn/premier/meitibaodao.htm", list);// 媒体报道
		spider.crawl("http://www.gov.cn/xinwen/yaowen.htm", list);// 要闻
		spider.crawl("http://www.gov.cn/xinwen/lianbo/index.htm", list);// 政务联播
		spider.crawl("http://www.gov.cn/xinwen/fabu/index.htm", list);// 新闻发布
		spider.crawl("http://www.gov.cn/xinwen/renmian/index.htm", list);// 人事
		spider.crawl("http://www.gov.cn/xinwen/gundong.htm", list);// 滚动
		spider.crawl("http://www.gov.cn/zhengce/jiedu/index.htm", list);// 解读
		spider.crawl("http://www.gov.cn/2016public/map.htm", list);// 中央有关文件
		spider.crawl("http://www.gov.cn/shuju/zhishu/qushi.htm", list);// 公报
		spider.crawl("http://www.gov.cn/shuju/yaowen.htm", list);// 数据要闻
		spider.crawl("http://www.gov.cn/shuju/zhishu/qushi.htm", list);// 指数趋势
		spider.crawl("http://www.gov.cn/shuju/chaxun/kuaisu.htm", list);// 快速查询

	}

}
