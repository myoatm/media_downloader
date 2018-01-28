<%--
  Created by IntelliJ IDEA.
  User: myoatm
  Date: 2017-12-30
  Time: 오전 12:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.io.File" %>
<%@ page import="module.BackgroundRemover" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    request.setCharacterEncoding("UTF-8");
    String thumb_file = request.getParameter("thumb");
    ServletContext sc = this.getServletConfig().getServletContext();
    String local_path = sc.getRealPath("/");

    BackgroundRemover bgr = new BackgroundRemover();
    bgr.removeLatency(local_path + thumb_file, 4000);


%>