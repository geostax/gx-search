/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geostax.search.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.geostax.search.model.Docs;

import javax.xml.parsers.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eightant on 2016/12/22.
 */
public class Utils {
	public static ArrayList<Docs> getDocs(String dataPath) {
		ArrayList<Docs> list = new ArrayList<Docs>();
		File file = new File(dataPath);
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			NodeList docList = doc.getElementsByTagName("item");
			for (int i = 0; i < docList.getLength(); i++) {

				Node docNode = docList.item(i);
				if (docNode.getNodeType() == Node.ELEMENT_NODE) {
					Element itemElement = (Element) docNode;
					Docs docs = new Docs();
					Element titleElement = (Element) itemElement.getElementsByTagName("title").item(0);
					docs.setTitle(titleElement.getTextContent());
					// System.out.println("title="+titleElement.getTextContent());
					Element linkElement = (Element) itemElement.getElementsByTagName("link").item(0);
					docs.setDocURL(linkElement.getTextContent());
					// System.out.println("link"+linkElement.getTextContent());
					Element descriptionElement = (Element) itemElement.getElementsByTagName("description").item(0);
					if (descriptionElement != null && descriptionElement.getTextContent() != null) {
						docs.setContent(descriptionElement.getTextContent());
						// System.out.println("content"+descriptionElement.getTextContent());
						list.add(docs);
						// System.out.println("list"+list);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ArrayList<String> getFileNameUtil(String dataPath) {
		ArrayList<String> fileName = new ArrayList<>();
		File file = new File(dataPath);
		File[] files = file.listFiles();
		if (files != null) {
			for (File file1 : files) {
				if (file1.isDirectory()) {
					getFileNameUtil(file1.getPath());
				} else {
					fileName.add(file1.getName());
				}
			}
		}
		return fileName;
	}
}