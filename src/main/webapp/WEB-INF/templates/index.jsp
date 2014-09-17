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
        <c:choose>
            <c:when test="${y%3 == 2}">
                <div class="vertical field-vertical cell-ymod3is2">
            </c:when>
            <c:when test="${y == 0}">
                <div class="vertical field-vertical cell-istop">
            </c:when>
            <c:otherwise>
                <div class="vertical field-vertical">
            </c:otherwise>
        </c:choose>
        <c:forEach begin="0" end="8" var="x">
            <c:choose>
                <c:when test="${x%3 == 2}">
                    <div class="horizontal cell-xmod3is2">
                </c:when>
                <c:when test="${x == 0}">
                    <div class="horizontal cell-isleft">
                </c:when>
                <c:otherwise>
                    <div class="horizontal">
                </c:otherwise>
            </c:choose>
                <input type="text" class="sudokucell" value="<c:out value="${field[y][x]}"/>" maxlength="1">
            </div>
        </c:forEach>
        </div>
    </c:forEach>
    <c:out value="${selfurl}" />
    <c:out value="${config}" />
    <c:import var="data" url="${selfurl}rest/sudoku9"/>
    <c:out value="${data}"/>

   
    </body>
</html>
