<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.net.URL"%>
<%@ page import="java.net.URLConnection"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="org.apache.commons.io.IOUtils"%>
<!DOCTYPE HTML>
<html>
    <head>
        <link rel="stylesheet" href="<c:url value="/resources/css/sudoku.css"/>">
        <title>Sudoku Solver - Spring JSP JSTL Training Project</Title>
    </head>
    <body>
    <h1>Sudoku Solver</h1>
    <form name="sudokufield" action="handle/sudoku9" method="get">
        <div class="sudokuField">
            <c:forEach begin="0" end="8" var="y">
                <c:choose>
                    <c:when test="${y%3 == 2}">
                        <c:set var="yclass" value="cell-ymod3is2"/>
                    </c:when>
                    <c:when test="${y == 0}">
                        <c:set var="yclass" value="cell-istop"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="yclass" value=""/>
                    </c:otherwise>
                </c:choose>
                <div class="vertical">
                <c:forEach begin="0" end="8" var="x">
                    <c:choose>
                        <c:when test="${x%3 == 2}">
                            <c:set var="xclass" value="cell-xmod3is2"/>
                        </c:when>
                        <c:when test="${x == 0}">
                            <c:set var="xclass" value="cell-isleft"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="xclass" value=""/>
                        </c:otherwise>
                    </c:choose>
                     <div class="horizontal">
                        <div class="${xclass} ${yclass}">
                            <input type="text" class="sudokucell " value="<c:out value="${field[y][x]}"/>"
                            maxlength="1" name="y${y}x${x}">
                        </div>
                    </div>
                </c:forEach>
                </div>
            </c:forEach>
        <div>
    <input type="submit" name="store" value="store">
    <input type="submit" name="solve" value="solve">
    <input type="submit" name="clear" value="clear">
    </form>
    <p>
        Store will redirect to a new page that can be bookmarked.
    </p>
    <p>
        Solve will redirect to a new page that will take some time to load, due to solving the sudoku as 
        far as possible with the small set of rules. Currently only the one value left in cell rule is used.
    </p>
    </body>
</html>
