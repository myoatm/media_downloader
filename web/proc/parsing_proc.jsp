<%@ page import="Interface.IndexInterface" %>
<%@ page import="org.json.simple.JSONObject" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%

    request.setCharacterEncoding("UTF-8");
    String data = request.getParameter("data");
    String media = request.getParameter("media");


    IndexInterface ii = new IndexInterface();

    ServletContext sc = this.getServletConfig().getServletContext();
    String local_path = sc.getRealPath("/");
    JSONObject dataJson = ii.integratedParse(media, data, local_path);

    //JSONObject dataJson = ii.parsingFromFacebook(data);

    // https://www.youtube.com/watch?v=Xr-zs6HbQFQ
    // https://www.facebook.com/541583102878284/videos/582715658765028/

    response.getWriter().print(dataJson.toJSONString());

%>
