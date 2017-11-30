/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geostax.search.model;;

/**
 * Created by eightant on 2016/12/15.
 */
public class Docs {
    private String title;
    private String content;
    private String docURL;
    private long totalDocs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDocURL() {
        return docURL;
    }

    public void setDocURL(String dosURL) {
        this.docURL = dosURL;
    }

    public Docs() {
        super();
    }

    public Docs(String title,String docURL, String content ,long totalDocs){
        super();
        this.title = title;
        this.content = content;
        this.docURL = docURL;
        this.totalDocs = totalDocs;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public long getTotalDocs() {
        return totalDocs;
    }

    public void setTotalDocs(int totalDocs) {
        this.totalDocs = totalDocs;
    }
}