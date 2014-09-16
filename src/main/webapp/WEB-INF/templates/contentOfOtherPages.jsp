<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.net.URL"%>
<%@ page import="java.net.URLConnection"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="org.apache.commons.io.IOUtils"%>
<html>
    <body>
    <%
        String sudoku9url = (String)request.getAttribute("sudoku9Url"); 
        out.println(sudoku9url);
        URL url = new URL(sudoku9url);
        URLConnection connection = url.openConnection();
        InputStream sudoku9Stream = connection.getInputStream();
        String sudoku9data = IOUtils.toString(sudoku9Stream, "UTF-8");
        out.println(sudoku9data);
    %>
        Hello, world!
    <%
      // beliebiger Java-Code
      out.println( "Hello from java!" );
    %>
    <c:forEach begin="0" end="8" var="y">
        <c:forEach begin="0" end="8" var="x">
            <c:out value="${field[y][x]}" />
        </c:forEach>
    </c:forEach>
    <c:out value="${selfurl}" />
    <c:out value="${config}" />
    <c:import var="data" url="${sudoku9Url}"/>
    <c:out value="${data}"/>

    <c:set var="anurl" value="${selfurl}"/>
    <%
        String selfurl = (String)pageContext.getAttribute("anurl");
        out.println(selfurl);
    %>
    </body>
</html>
