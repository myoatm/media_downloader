<%--
  Created by IntelliJ IDEA.
  User: myoatm
  Date: 2017-12-30
  Time: 오전 12:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.io.File" %>
<%@ page import="module.BackgroundRemover" %>
<%@ page import="org.json.simple.JSONArray" %>
<%@ page import="org.json.simple.parser.JSONParser" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    request.setCharacterEncoding("UTF-8");
    String thumbListString = request.getParameter("thumb");
    JSONArray ja;
    JSONParser jp = new JSONParser();
    ja = (JSONArray)jp.parse(thumbListString);

    String [] thumbList = new String[ja.size()];
    for(int i=0; i< ja.size(); i++){
        thumbList[i] = (String)ja.get(i);
    }

    ServletContext sc = this.getServletConfig().getServletContext();
    String local_path = sc.getRealPath("/");

    BackgroundRemover bgr = new BackgroundRemover();
    bgr.removeLatency(local_path, thumbList, 15000);


%>