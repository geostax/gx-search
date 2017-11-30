<%-- 
    Document   : index
    Created on : 2017-11-30, 17:14:27
    Author     : Phil
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="assets/css/main.css">
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <link rel="shortcut icon" href="assets/images/favicon.ico" type="image/x-icon" />
        <title>Search</title>
    </head>

    <body>
        <div class="main">
            <div class="logo">
                <img alt="logo" src="assets/images/favicon.png">
            </div>
            <div class="searchbox">
                <div class="searchform">
                    <form action="search?p=1" method="get">
                        <input type="text" name="query" id="query" value="">
                        <input type="submit" value=" 搜  索 ">
                    </form>
                </div>
            </div>
        </div>

    </body>
</html>
