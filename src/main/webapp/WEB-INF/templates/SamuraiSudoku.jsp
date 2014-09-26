<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.net.URL"%>
<%@ page import="java.net.URLConnection"%>
<%@ page import="java.io.InputStream"%>
<%@ page import="org.apache.commons.io.IOUtils"%>
<!DOCTYPE HTML>
<html>
    <head>
        <link rel="stylesheet" href="<c:url value="/resources/css/samuraisudoku.css"/>">
        <title>Samurai Sudoku Solver - Spring JSP JSTL Training Project</Title>
    </head>
    <body>
    <h1>Samurai Sudoku Solver</h1>
    <form name="sudokufield" action="/Sudoku/handle/samuraisudoku" method="get">
        <div class="sudokuField">
            <c:forEach begin="0" end="20" var="y">
                <div class="vertical">
                <%-- Outer border --%>  
                <c:choose>
                    <c:when test="${y == 0}">
                        <c:set var="yclass" value="cell-istop"/>
                    </c:when>
                    <c:when test="${y == 20}">
                        <c:set var="yclass" value="cell-isbottom"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="yclass" value=""/>
                    </c:otherwise>
                </c:choose>
                  <c:forEach begin="0" end="20" var="x">
                  <c:choose>
                      <c:when test="${x == 0}">
                          <c:set var="xclass" value="cell-isleft"/>
                      </c:when>
                      <c:when test="${x == 20}">
                          <c:set var="xclass" value="cell-isright"/>
                      </c:when>
                      <c:otherwise>
                          <c:set var="xclass" value=""/>
                      </c:otherwise>
                  </c:choose>
                  <%-- Cut out border --%>  
                  <c:choose>
                      <c:when test="${(x >= 9 && x <= 11 && y == 6) ||
                                      (x >=0 && x <= 5 && y == 12) ||
                                      (x >=15 && x <= 20 && y == 12)
                      }">
                          <c:set var="cutclass" value="cell-cuttop"/>
                      </c:when>
                      <c:when test="${(x >= 9 && x <= 11 && y == 14)||
                                      (x >=0 && x <= 5 && y == 8) ||
                                      (x >=15 && x <= 20 && y == 8)
                      }">
                          <c:set var="cutclass" value="cell-cutbottom"/>
                      </c:when>
                      <c:when test="${(y >= 9 && y <= 11 && x == 6)||
                                      (y >=0 && y <= 5 && x == 12) ||
                                      (y >=15 && y <= 20 && x == 12)
                      
                      }">
                          <c:set var="cutclass" value="cell-cutleft"/>
                      </c:when>
                      <c:when test="${(y >= 9 && y <= 11 && x == 14)||
                                      (y >=0 && y <= 5 && x == 8) ||
                                      (y >=15 && y <= 20 && x == 8)
                      }">
                          <c:set var="cutclass" value="cell-cutright"/>
                      </c:when>
                      <c:otherwise>
                          <c:set var="cutclass" value=""/>
                      </c:otherwise>
                  </c:choose>
                  <%-- In field separators
                  <c:choose>
                      <c:when test="${ x%3==2 }">
                          <c:set var="separatorclass" value="cell-separateright"/>
                      </c:when>
                      <c:when test="${ x%3==0 }">
                          <c:set var="separatorclass" value="cell-separatelet"/>
                      </c:when>
                      <c:otherwise>
                          <c:set var="separatorclass" value=""/>
                      </c:otherwise>
                  </c:choose>
                  --%>

                    <div class="horizontal">
                    <c:choose>
                        <c:when test="${
                        (x >= 9 && x <= 11 && y >= 0 && y <= 5)||
                        (x >= 0 && x <= 5 && y >= 9 && y <= 11)||
                        (x >= 15 && x <= 20 && y >= 9 && y <= 11)||
                        (x >= 9 && x <= 11 && y >= 15 && y <= 20)
                        }">
                        <div class="${xclass} ${yclass} hidden ${separatorclass}">
                        <input type="text" class="sudokucell hidden" maxlength="1" name="y${y}x${x}">
                        </div>
                        </c:when>
                        <c:otherwise>
                            <div class="${xclass} ${yclass} ${cutclass} ${separatorclass}">
                                <input type="text" class="sudokucell" value="<c:out value="${field[y][x]}"/>"
                                       maxlength="1" name="y${y}x${x}">
                            </div>
                        </c:otherwise>
                    </c:choose>
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
