<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.net.URL"%>
<%@ page import="java.net.URLConnection"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="org.apache.commons.io.IOUtils"%>
<html>
    <head>
        <link rel="stylesheet" href="<c:url value="/resources/css/sudoku.css"/>">
    </head>
    <body>

    <c:forEach begin="0" end="8" var="y">
        <c:forEach begin="0" end="8" var="x">
            <div class="test">
                <c:out value="${field[y][x]}" />
            </div>
        </c:forEach>
    </c:forEach>
    <c:out value="${selfurl}" />
    <c:out value="${config}" />
    <c:import var="data" url="${selfurl}rest/sudoku9"/>
    <c:out value="${data}"/>

   
    </body>
</html>
