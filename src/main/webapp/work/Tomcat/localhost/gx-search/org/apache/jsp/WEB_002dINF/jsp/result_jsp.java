/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/8.5.4
 * Generated at: 2017-11-30 14:41:36 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.*;
import com.geostax.search.model.Docs;

public final class result_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("java.util");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("com.geostax.search.model.Docs");
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    final java.lang.String _jspx_method = request.getMethod();
    if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSPs only permit GET POST or HEAD");
      return;
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");

    String query = (String) request.getAttribute("query");
    List<Docs> list = (List<Docs>) request.getAttribute("docList");
    int totalDoc = (int) request.getAttribute("totalDocs");
    double time = Double.parseDouble(request.getAttribute("time").toString());
    int pageNow = (int) request.getAttribute("pageNow");
    int pageCount = (int) request.getAttribute("pageCount");

      out.write("\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <link rel=\"stylesheet\" href=\"assets/css/main.css\">\n");
      out.write("        <link rel=\"stylesheet\" href=\"assets/css/bootstrap.min.css\">\n");
      out.write("        <link rel=\"shortcut icon\" href=\"assets/images/favicon.ico\" type=\"image/x-icon\" />\n");
      out.write("        <title>Search Result</title>\n");
      out.write("        <script type=\"text/javascript\">\n");
      out.write("            window.onload = function () {\n");
      out.write("                document.getElementById(\"query\").value = \"");
      out.print(query);
      out.write("\";\n");
      out.write("            }\n");
      out.write("\n");
      out.write("        </script>\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        <div class=\"nav\">\n");
      out.write("            <div class=\"nav_left\">\n");
      out.write("                <a href=\"index.jsp\"><img alt=\"logo\" src=\"assets/images/favicon.png\"></a>\n");
      out.write("            </div>\n");
      out.write("            <div class=\"nav_right\">\n");
      out.write("                <div class=\"nav_form\">\n");
      out.write("                    <form action=\"search\" method=\"get\">\n");
      out.write("                        <input id=\"query\" type=\"text\" name=\"query\" value=\"");
      out.print(query);
      out.write("\">\n");
      out.write("                        <input type=\"submit\" value=\" 搜  索 \"><br />\n");
      out.write("                    </form>\n");
      out.write("                </div>\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <div class=\"docsMain\">\n");
      out.write("            <h4>\n");
      out.write("                共搜到<span class=\"docsNum\">");
      out.print(totalDoc);
      out.write("</span>个结果|用时<span\n");
      out.write("                    class=\"newsnum\">");
      out.print(time);
      out.write("</span>秒\n");
      out.write("            </h4>\n");
      out.write("            ");

                if (list.size() > 0) {
                    Iterator<Docs> iter = list.iterator();
                    Docs docs;
                    while (iter.hasNext()) {
                        docs = iter.next();
            
      out.write("\n");
      out.write("\n");
      out.write("            <div class=\"item\">\n");
      out.write("                <h4>\n");
      out.write("                    <a href=\"");
      out.print(docs.getDocURL());
      out.write(" \" target=\"_blank\">");
      out.print(docs.getTitle());
      out.write("</a>\n");
      out.write("                </h4>\n");
      out.write("                <p>\n");
      out.write("                    ");
      out.print(docs.getContent().length() > 300 ? docs.getContent().substring(0, 300) : docs.getContent());
      out.write("\n");
      out.write("                </p>\n");
      out.write("                <p><a href=\"");
      out.print(docs.getDocURL());
      out.write("\" target=\"_blank\">");
      out.print(docs.getDocURL());
      out.write("</a></p>\n");
      out.write("                <p>----------------------------------------------------------</p>\n");
      out.write("            </div>\n");
      out.write("            ");

                    }
                }
            
      out.write("\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <div class=\"paging\">\n");
      out.write("            <ul>\n");
      out.write("                <li><a href=\"search?query=");
      out.print(query);
      out.write("&&pageNow=1\">首页</a></li>\n");
      out.write("\n");
      out.write("                ");

                    if (pageNow != 1) {
                
      out.write("\n");
      out.write("                <li><a href=\"search?query=");
      out.print(query);
      out.write("&&pageNow=");
      out.print(pageNow - 1);
      out.write("\">上一页</a></li>\n");
      out.write("                    ");

                        }
                    
      out.write("\n");
      out.write("\n");
      out.write("                ");

                    for (int i = 1; i <= pageCount; i++) {
                
      out.write("\n");
      out.write("                <li><a href=\"search?query=");
      out.print(query);
      out.write("&&pageNow=");
      out.print(i);
      out.write('"');
      out.write('>');
      out.print(i);
      out.write("</a></li>\n");
      out.write("                    ");

                        }
                    
      out.write("\n");
      out.write("                    ");

                        if (pageNow != pageCount) {
                    
      out.write("\n");
      out.write("                <li><a href=\"search?query=");
      out.print(query);
      out.write("&&pageNow=");
      out.print(pageNow + 1);
      out.write("\">下一页</a></li>\n");
      out.write("                <li><a href=\"search?query=");
      out.print(query);
      out.write("&&pageNow=");
      out.print(pageCount);
      out.write("\">末页</a></li>\n");
      out.write("                    ");

                        }
                    
      out.write("\n");
      out.write("            </ul>\n");
      out.write("            <hr>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("        <div class=\"footerinfo\">\n");
      out.write("            <p>     </p>\n");
      out.write("        </div>\n");
      out.write("\n");
      out.write("    </body>\n");
      out.write("</html>\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}