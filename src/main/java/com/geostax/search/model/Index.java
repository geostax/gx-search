/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geostax.search.model;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BZC
 * @create 2016-12-11 21:42
 **/
public class Index {
    
    public static void index(String dataPath, String indexPath) {
        IndexWriter indexWriter = null;
        try {
            //1.创建Directory
            Directory directory = FSDirectory.open(FileSystems.getDefault().getPath(indexPath));
            //2.创建IndexWriter
            Analyzer analyzer = new SmartChineseAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, indexWriterConfig);
            indexWriter.deleteAll();

            ArrayList<String> fileList = Utils.getFileNameUtil(dataPath);
            ArrayList<Docs> docss = new ArrayList<>();
            if (fileList != null) {
                for (String s : fileList) {
                    System.out.println(s);
                    docss = Utils.getDocs(dataPath + "/" + s);
                }
            }
            System.out.println("docss" + docss);
            for (Docs docs : docss) {
                Document document = new Document();
                //4.添加Field
                document.add(new Field("title", docs.getTitle(), TextField.TYPE_STORED));
                document.add(new Field("docURL", docs.getDocURL(), TextField.TYPE_STORED));
                document.add(new Field("content", docs.getContent(), TextField.TYPE_STORED));
                //5.添加到索引
                indexWriter.addDocument(document);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}