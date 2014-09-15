<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <body>
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
    </body>
</html>
