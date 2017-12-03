package com.geostax.search.util;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ESTest {
	public static final String CLUSTER_NAME = "DIC";
	public static final String NODE_NAME = "dicNode";
	public static final String INDEX_NAME = "dicindex"; // should always be in lowercase
	public static String CONFIG_FILE_PATH = ""; // takes this from command line

	TransportClient client = null;

	public static void main(String[] args) throws Exception {
		//new ESTest().crawl2("http://www.gov.cn/xinwen/lianbo/index.htm", "|","");
		//new ESTest().crawl2("http://www.gov.cn/xinwen/yaowen.htm", "|","");
		new ESTest().query2();
	}

	public ESTest() {
		try {
			client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void testGet() throws Exception {
		Map<String, Object> infoMap = new HashMap<String, Object>();
		infoMap.put("name", "xiaofei");
		infoMap.put("title", "我叫肖非,毕业于香港理工大学");
		infoMap.put("createTime", new Date());
		infoMap.put("count", 1022);

		// instance a json mapper
		ObjectMapper mapper = new ObjectMapper(); // create once, reuse

		// generate json
		byte[] json = mapper.writeValueAsBytes(infoMap);

		// IndexResponse indexResponse = client.prepareIndex("test", "info",
		// "100").setSource(infoMap).execute().actionGet();
		IndexResponse response = client.prepareIndex("test", "info").setSource(infoMap).execute().actionGet();

		System.out.println("id:" + response.getId());

	}

	public void query() throws Exception {
		// term查询
		// QueryBuilder queryBuilder = QueryBuilders.termQuery("age", 50) ;
		// range查询
		QueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("count").gt(50);
		SearchResponse searchResponse = client.prepareSearch("guowuyuan").setTypes("page").setQuery(rangeQueryBuilder)
				.addSort("count", SortOrder.DESC).setSize(20).execute().actionGet();
		SearchHits hits = searchResponse.getHits();
		System.out.println("查到记录数：" + hits.getTotalHits());
		SearchHit[] searchHists = hits.getHits();
		if (searchHists.length > 0) {
			for (SearchHit hit : searchHists) {
				String name = (String) hit.getSourceAsMap().get("name");
				Integer age = (Integer) hit.getSourceAsMap().get("count");
				System.out.format("name:%s ,age :%d \n", name, age);
			}
		}
	}
	
	public void query2()  throws Exception {
		SearchResponse response = client.prepareSearch("guowuyuan")
		        .setTypes("page")
		        .setQuery(QueryBuilders.matchQuery("title", "斯洛文尼亚"))                 // Query
		        .setFrom(0).setSize(60).setExplain(true)
		        .get();
		SearchHits hits = response.getHits();
		System.out.println("查到记录数：" + hits.getTotalHits());
		SearchHit[] searchHists = hits.getHits();
		if (searchHists.length > 0) {
			for (SearchHit hit : searchHists) {
				String title = (String) hit.getSourceAsMap().get("title");
				String desc = (String) hit.getSourceAsMap().get("description");
				System.out.format("title:%s ,desc :%s \n", title, desc);
			}
		}
	}

	public void crawl2(String url, String prefix, String pre_url) throws Exception {

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
			infoMap.put("title", title);
			infoMap.put("description", desc);
			infoMap.put("createTime", date);
			infoMap.put("lanmu", lanmu);
			IndexResponse response = client.prepareIndex("guowuyuan", "page").setSource(infoMap).execute().actionGet();
			System.out.println("id:" + response.getId());
			return;
		}

		// if (!pagetype.isEmpty() &&
		// pagetype.first().attr("content").toString().equals("2")) {
		if (robots.isEmpty()) {
			for (Element link : links) {
				System.out.println(link.text() + ":" + link.absUrl("href"));
				crawl2(link.absUrl("href"), prefix + "--", url);
			}
		}

	}

}
