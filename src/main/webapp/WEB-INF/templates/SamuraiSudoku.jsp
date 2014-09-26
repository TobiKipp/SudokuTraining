<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.net.URL"%>
<%@ page import="java.net.URLConnection"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="org.apache.commons.io.IOUtils"%>
<!DOCTYPE HTML>
<html>
    <head>
        <link rel="stylesheet" href="<c:url value="/resources/css/${samuraisudokucss}"/>">
        <title>Samurai Sudoku Solver - Spring JSP JSTL Training Project</Title>
    </head>
    <body>
    <h1>Samurai Sudoku Solver</h1>
    <form name="sudokufield" action="/Sudoku/handle/samuraisudoku" method="get">
        <div class="sudokuField">
            <c:forEach begin="0" end="21" var="y">
                <div class="vertical">
                <c:choose>
                    <c:when test="${y%3 == 0}">
                    <div class="vertical">
                        <c:forEach begin="0" end="21" var="x">
                            <c:choose>
                                <c:when test="${
                                (x >= 9 && x <= 11 && y <= 5)||
                                (x >= 9 && x <= 11 && y >= 16)
                                }">
                                    <c:set var="hiddenclass" value="hidden"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="hiddenclass" value=""/>
                                </c:otherwise>
                            </c:choose>
                            <c:choose>
                                <c:when test="${ x%3==0 }">
                                <div class="horizontal separatorYX">
                                </div>
                                </c:when>
                                <c:otherwise>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${x != 21}">
                            <div class="horizontal separatorY ${hiddenclass}"></div>
                            </c:if>
                        </c:forEach>
                    </div>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
                <div class="horizontal">
                <c:forEach begin="0" end="21" var="x">
                  <c:set var="hiddenx" value=""/>
                  <c:if test="${y >= 9 && y <= 11 && (x <= 5 ||  x >= 16) }">
                      <c:set var="hiddenx" value="hidden"/>
                  </c:if>
                  <c:choose>
                      <c:when test="${ x%3==0 && y != 21 }">
                      <div class="horizontal separatorX ${hiddenx}">
                      </div>
                      </c:when>
                      <c:otherwise>
                      </c:otherwise>
                  </c:choose>
                  <c:if test="${x != 21 && y != 21}">
                  <c:choose>
                      <c:when test="${
                      (x >= 9 && x <= 11 && y >= 0 && y <= 5)||
                      (x >= 0 && x <= 5 && y >= 9 && y <= 11)||
                      (x >= 15 && x <= 20 && y >= 9 && y <= 11)||
                      (x >= 9 && x <= 11 && y >= 15 && y <= 20)
                      }">
                          <div class="horizontal hidden">
                               <input type="text" class="sudokucell hidden" maxlength="1" name="y${y}x${x}">
                          </div>
                      </c:when>
                      <c:otherwise>
                          <div class="horizontal">
                              <input type="text" class="sudokucell" value="<c:out value="${field[y][x]}"/>"
                                     maxlength="1" name="y${y}x${x}">
                          </div>
                      </c:otherwise>
                  </c:choose>
                  </c:if>
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
