/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geostax.search.model;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author Phil
 */
public class SearchManager {

    public static long totalDocs = 0;
    public static final int PAGE_SIZE = 10;// 指定每一页显示10条记录
    String indexPathStr;
    String dataPath;

    public SearchManager() {
        String basePath = System.getProperty("gxsearch");
        System.out.println("basePath：" + basePath);
        indexPathStr = basePath+"index";
        System.out.println("indexPathStr=" + indexPathStr);
        dataPath = basePath+"assets\\data";
        System.out.println("dataPath=" + dataPath);
        Index.index(dataPath, indexPathStr);
    }

    public ArrayList<Docs> getSearch(String keyword) {
        ArrayList<Docs> docList = new ArrayList<Docs>();
        DirectoryReader directoryReader = null;
        try {
            //1.创建Directory
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(indexPathStr));
            //2.创建IndexReader
            directoryReader = DirectoryReader.open(directory);
            //3.创建IndexSearch
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
            //4.创建搜索的Query
            Analyzer analyzer = new SmartChineseAnalyzer();
            QueryParser queryParser = new QueryParser("content", analyzer);
            Query query = queryParser.parse(keyword);
            //5.searcher搜索并且返回TopDocs
            TopDocs topDocs = indexSearcher.search(query, 500);
            totalDocs = topDocs.totalHits;
            System.out.println("查找到的文档共有：" + totalDocs);
            //6.根据TopDocs获取ScoreDoc对象
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                //7.根据searcher和ScoreDoc对象获取具体的Document对象
                Document document = indexSearcher.doc(scoreDocs[i].doc);
                //8.根据Document对象获取需要的值
                System.out.println("[" + i + "]" + document.get("title"));
                System.out.println(document.get("docURL"));
                System.out.println("score:" + scoreDocs[i].score);
                Docs docs = new Docs(document.get("title"), document.get("docURL"),
                        document.get("content"), totalDocs);
                docList.add(docs);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            if (directoryReader != null) {
                try {
                    directoryReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //System.out.println("文档docList" + docList);
        return docList;
    }

}
