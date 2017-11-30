/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geostax.search.controller;

import com.geostax.search.model.SearchManager;
import com.geostax.search.model.Docs;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import static jdk.nashorn.internal.objects.NativeString.search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Phil
 */
@Controller
@RequestMapping(value = "/", method = RequestMethod.GET)
public class IndexController {

    @Autowired
    SearchManager searchmanager;

    private int pageCount = 1;// 总共的页数
    private int rowCount = 1;// 总共有多少条记录
    private int pageNow = 1;// 当前要显示的页数，初始化为1
    
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(ModelMap model) throws Exception {
        System.out.println("Go to index");
        ModelAndView modelAndView = new ModelAndView("index");
        return modelAndView;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView search(HttpServletRequest request, ModelMap model) throws Exception {
         System.out.println("Go to search");
        ModelAndView modelAndView = new ModelAndView("result");
        
        String query = request.getParameter("query");
        System.out.println("查询： " + query);

        // 计算查询时间
        long starTime = System.currentTimeMillis();// start time

/*
        //创建索引
        indexPathStr = request.getServletContext().getRealPath("/index");
        System.out.println("indexPathStr="+indexPathStr);
        String dataPath = request.getServletContext().getRealPath("/data");
        System.out.println("dataPath="+dataPath);
        Index.index(dataPath,indexPathStr);
*/

        if (!"".equals(query) && query != null) {
            ArrayList<Docs> docList = searchmanager.getSearch(query);

            //分页
            String temp_pageNow = request.getParameter("pageNow");
            if (temp_pageNow != null) {
                pageNow = Integer.parseInt(temp_pageNow);
            }
            rowCount = (int)SearchManager.totalDocs;//条数
            pageCount = (rowCount - 1) /  SearchManager.PAGE_SIZE + 1;//页数

            List<Docs> pagelist = docList.subList( SearchManager.PAGE_SIZE * (pageNow - 1),
            		 SearchManager.PAGE_SIZE * pageNow < rowCount ?  SearchManager.PAGE_SIZE * pageNow : rowCount);

            if (docList.size() != 0) {
                //System.out.println("文档长度servlet docList length:" + docList.size());
            	modelAndView.addObject("query", query);
            	modelAndView.addObject("docList", pagelist);
            	modelAndView.addObject("totalDocs",  SearchManager.totalDocs);
                long endTime = System.currentTimeMillis();// end time
                long Time = endTime - starTime;
                modelAndView.addObject("time", (double) Time / 1000);

                modelAndView.addObject("pageNow", pageNow);
                modelAndView.addObject("pageCount", pageCount);
            }
            return modelAndView;
        } else {
            return  new ModelAndView("error");
        }
    }
    
    
}
