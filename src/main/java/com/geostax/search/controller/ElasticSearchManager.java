package com.geostax.search.controller;

import java.net.InetAddress;
import java.util.ArrayList;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.geostax.search.model.Docs;

public class ElasticSearchManager {

	public static long totalDocs = 0;
	public static final int PAGE_SIZE = 10;// 指定每一页显示10条记录

	TransportClient client = null;

	public ElasticSearchManager() {
		try {
			client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public ArrayList<Docs> search(String query) {
		ArrayList<Docs> docList = new ArrayList<Docs>();
		QueryBuilder qb = QueryBuilders.matchQuery("description", query);

		SearchResponse scrollResp = client.prepareSearch("guowuyuan").setTypes("page")
				.addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC).setScroll(new TimeValue(60000)).setQuery(qb)
				.setSize(100).get(); // max of 100 hits will be returned
										// for each scroll
		// Scroll until no hits are returned
		do {
			for (SearchHit searchHit : scrollResp.getHits().getHits()) {
				String tile = (String) searchHit.getSourceAsMap().get("title");
				String url = (String) searchHit.getSourceAsMap().get("url");
				String description = (String) searchHit.getSourceAsMap().get("description");
				System.out.println(tile);
				System.out.println(url);
				System.out.println(description);
				Docs docs = new Docs(tile, url, description, totalDocs);
				docList.add(docs);
			}

			scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute()
					.actionGet();
		} while (scrollResp.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while
																// loop.
		return docList;
	}

	/**
	 * 执行搜索
	 * 
	 * @param indexname
	 *            索引名称
	 * @param type
	 *            索引类型
	 * @param queryBuilder
	 *            查询条件
	 * @return
	 */
	public ArrayList<Docs> search(String indexName, String typeName, String query) {
		totalDocs = 0;
		ArrayList<Docs> docList = new ArrayList<Docs>();
		QueryBuilder queryBuilder = QueryBuilders.matchQuery("description", query);// 搜索name为kimchy的数据
		SearchResponse response = client.prepareSearch(indexName).setTypes(typeName)
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(QueryBuilders.matchQuery("description", query)) // Query
				.setFrom(0).setSize(60).setExplain(true).get();

		SearchHits hits = response.getHits();
		totalDocs = hits.getTotalHits();
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit searchHit : searchHits) {
			String tile = (String) searchHit.getSourceAsMap().get("title");
			String url = (String) searchHit.getSourceAsMap().get("url");
			String description = (String) searchHit.getSourceAsMap().get("description");
			System.out.println(tile);
			System.out.println(url);
			System.out.println(description);
			Docs docs = new Docs(tile, url, description, totalDocs);
			docList.add(docs);
		}
		return docList;
	}

	/**
	 * 执行搜索
	 * 
	 * @param indexname
	 *            索引名称
	 * @param type
	 *            索引类型
	 * @param queryBuilder
	 *            查询条件
	 * @return
	 */
	public ArrayList<Docs> multiSearch(String indexName, String typeName, String query) {
		totalDocs = 0;
		ArrayList<Docs> docList = new ArrayList<Docs>();

		SearchRequestBuilder srb1 = client.prepareSearch("guowuyuan").setTypes("page")
				.setQuery(QueryBuilders.matchQuery("title", query)).setSize(2000);
		SearchRequestBuilder srb2 = client.prepareSearch("guowuyuan").setTypes("page")
				.setQuery(QueryBuilders.matchQuery("description", query)).setSize(2000);

		// (2)执行查询
		MultiSearchResponse searchResponse = client.prepareMultiSearch().add(srb1).add(srb2).get();
		for (MultiSearchResponse.Item item : searchResponse.getResponses()) {
			SearchResponse response = item.getResponse();
			SearchHits hits = response.getHits();
			totalDocs += hits.getTotalHits();
		}
		System.out.println(totalDocs);
		// (3)解析结果
		long nbHits = 0;
		for (MultiSearchResponse.Item item : searchResponse.getResponses()) {
			SearchResponse response = item.getResponse();
			SearchHits hits = response.getHits();

			SearchHit[] searchHits = hits.getHits();
			for (SearchHit searchHit : searchHits) {
				String tile = (String) searchHit.getSourceAsMap().get("title");
				String url = (String) searchHit.getSourceAsMap().get("url");
				String description = (String) searchHit.getSourceAsMap().get("description");
				System.out.println(tile);
				System.out.println(url);
				System.out.println(description);
				Docs docs = new Docs(tile, url, description, totalDocs);
				docList.add(docs);
			}
		}

		return docList;

	}

	public ArrayList<Docs> dismaxSearch(String indexName, String typeName, String query) {
		totalDocs = 0;
		ArrayList<Docs> docList = new ArrayList<Docs>();
		QueryBuilder queryBuilder = QueryBuilders.disMaxQuery().add(QueryBuilders.matchQuery("description", query))
				.add(QueryBuilders.matchQuery("tile", query));
		SearchResponse response = client.prepareSearch(indexName).setTypes(typeName).setScroll(new TimeValue(60000))
				.setQuery(queryBuilder).setSize(100).execute().actionGet();
		// Scroll until no hits are returned
		do {
			for (SearchHit searchHit : response.getHits().getHits()) {
				String tile = (String) searchHit.getSourceAsMap().get("title");
				String url = (String) searchHit.getSourceAsMap().get("url");
				String description = (String) searchHit.getSourceAsMap().get("description");
				System.out.println(tile);
				System.out.println(url);
				System.out.println(description);
				Docs docs = new Docs(tile, url, description, totalDocs);
				docList.add(docs);
			}

			response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute()
					.actionGet();
		} while (response.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while
															// loop.
		return docList;

	}

	public ArrayList<Docs> orSearch(String indexName, String typeName, String query) {
		totalDocs = 0;
		ArrayList<Docs> docList = new ArrayList<Docs>();

		QueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("description", query))
				.should(QueryBuilders.matchQuery("tile", query));

		SearchResponse response = client.prepareSearch(indexName).setTypes(typeName).setScroll(new TimeValue(60000))
				.setQuery(queryBuilder).setSize(100).execute().actionGet();
		// Scroll until no hits are returned
		do {
			for (SearchHit searchHit : response.getHits().getHits()) {
				String tile = (String) searchHit.getSourceAsMap().get("title");
				String url = (String) searchHit.getSourceAsMap().get("url");
				String description = (String) searchHit.getSourceAsMap().get("description");
				System.out.println(tile);
				System.out.println(url);
				System.out.println(description);
				Docs docs = new Docs(tile, url, description, totalDocs);
				docList.add(docs);
			}

			response = client.prepareSearchScroll(response.getScrollId()).setScroll(new TimeValue(60000)).execute()
					.actionGet();
		} while (response.getHits().getHits().length != 0); // Zero hits mark the end of the scroll and the while
															// loop.
		return docList;

	}

	public static void main(String[] args) {
		String indexName = "guowuyuan";// 索引名称
		String typeName = "page";// 类型名称
		ElasticSearchManager em = new ElasticSearchManager();
		// 2.执行查询
		// (1)创建查询条件
		QueryBuilder queryBuilder = QueryBuilders.matchQuery("description", "一带一路");// 搜索name为kimchy的数据
		// (2)执行查询
		// ArrayList<Docs> searchResponse = em.search(indexName, typeName, "哈萨克斯坦");
		// System.out.println(searchResponse.size());
		em.search("一带一路");
	}

}
