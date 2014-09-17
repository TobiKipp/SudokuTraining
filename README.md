#Training project Sudoku#

##Introduction##

This document keeps my thoughts during the development of the training project.

The style is more like a diary than a documentation. 

The tutorials are too short to really learn anything big new after the third one on the [spring page](spring.io).
So I decided to make a larger project with some functionality, but with focus on using spring.

After some issues with libraries I decided to wipe the first try and start from zero. Instead of using 
spring boot and starting it with the jar, I will start with a war archive build by maven and deployed to a tomcat 
server. 

The initial configuration is taken from 
[mkyong](http://www.mkyong.com/maven/how-to-create-a-web-application-project-with-maven/).

The project is more about technical solutions than about the most efficient way to handle a sudoku.
A few goals of the project:

- Use spring for a larger than tutorial size project
- Use of JSP for visualization
- Only the 9x9 version at start, but the implementation should be able to handle other designs.
  It will use HTML and CSS3 without a table desing. 
- The server will use threads for each unit to determine which values are possible in a specific field.
- Javascript should only be used for convenience features, but not for main functionality.


##Adding the startpage##

In the current version there is no index page for the root of the webapp. For this the  
index.jsp are renamed to index-example.jsp and the real index.jsp is mapped in the BaseController with the 
key "/".

##Adding RESTFul WebService Example##

Goal: Add the JSON reponse example without loosing the displaying of the JSP.

The first tutorial for spring is [RESTful Web Service](http://spring.io/guides/gs/rest-service/) and it
is one of the things I want to integrate into my webapp. The gui creates a request receives the JSON 
response and processes it into the JSP.

I added a RESTful Web Service like in the example, but without the Application.java, as I am using the WAR
archive. It now tries to open /Sudoku/WEB-INF/pages/sudoku9.jsp,
instead of running the RESTful Web Service with the path /Sudoku/sudoku9.
The handling of this is done in the mvc-dispatcher-servlet.

After some search on the web I found out how to use Java for handling this. I mixed the first tutorial
with the JSON response the Maven war tutorial and a [Stack Overflow threads](http://stackoverflow.com/questions/19068530/how-to-map-multiple-controllers-in-spring-mvc) Information for a test project. This will now be transferred 
to this project.

For this the WebInitializer.java and Application.java are added.
In addition the creation of the WAR archive is altered, which means no more XML files and the templates are
moved to src/main/resources. 

Right now the RESTfull service is returning the JSON representation of the 9x9 field.
[Example 1] (http://localhost:8080/Sudoku/rest/sudoku9) uses the default configuration.
[Example 2] (http://localhost:8080/Sudoku/rest/sudoku9?config=0) Sets the first value explicitly to 0, which is
equal to int 48. The other values are 0, as they are not defined by the config processing method.

What is not working is finding the templates. The first step is to use it the way it was provided in the example.
The example used a simple HTML file instead of JSP. With an HTML it is working with JSP it is currently not working.

After switching to spring boot following the maven war example of spring.io I got two controllers activated without
using any XML configuration. The one thing missing is to use the index.jsp instead of index.html in the resolver. 
For this a WebConfig.java is added containing a viewResolver method.

After adding a application.properties with content:

    spring.view.prefix: /WEB-INF/templates/
    spring.view.suffix: .jsp
    server.port: 8081

and adding jstl to maven, the page was finally loaded.

So it was majorly an issue with missing dependencies.

##Use the JSP to display the RESTFul WebService output##

Now both components on their own are working it is time to use one in the other. The JSP will have to
make a request and render the response into the HTML.

Dervived from the [Consuming a RESTful Web Service](http://spring.io/guides/gs/consuming-rest/) example, I added
the RestTemplate into the Controller:

        @RequestMapping(value="/", method = RequestMethod.GET)
        public String home(ModelMap model){
            RestTemplate restTemplate = new RestTemplate();
            Sudoku9 sudoku = restTemplate.getForObject("http://localhost:8080/Sudoku/rest/sudoku9", Sudoku9.class);
            model.addAttribute("field", sudoku.getField());
            return "index";
        }

It is very limited use in this case and the URL is not convincing yet. With it being the same app it there should
be an easier way.

By adding a HttpServletRequest to the method parameters the url can be reconstructed. 
To test this I copied the war archive as Sudoku2.war. Now it returns  http://localhost:8080/Sudoku2/ as url.

The spring boot jar does not seem to like the JSP too much, however on tomcat the app is running just fine.

## Set the initial configuration ##

The above method is very limited in use, as the configuration is fixed. To allow to change the configuration this 
time the a request parameter is added. It defaults to a configuration for a completely empty field.

## Request to the RESTful Service from JSP with JSTL ##

After some trying to use JSP scriptlets I found an alternative solution to make the call to the RESTful service:

    <c:import var="data" url="${selfurl}rest/sudoku9"/>
    <c:out value="${data}"/>

Still I want to solve it using plain JSP. For this I importet java.net.URL and apache commons. 
As kind of mixed form jstl can be used to define the variable such that it is available in pageContext:

    <c:set var="selfurl" value="${selfurl}"/>
    <%
        String selfurl = (String)pageContext.getAttribute("selfurl");
        out.println(selfurl);
    %>

Without using JSTL it is almost the same but the request object is used:

    <%
        String anotherurl = (String)request.getAttribute("selfurl");
        out.println(anotherurl);
    %>

Even if some lines could be saved in the following scriptlet The c:import jstl can not be beaten in terms of 
shortness. I will backup the page to have an example for the alternative solutions, for this I will also keep
the apache commons in maven. 


    <%
        String thisurl = (String)request.getAttribute("selfurl"); 
        String sudoku9url = thisurl + "rest/sudoku9";
        out.println(sudoku9url);
        URL url = new URL(sudoku9url);
        URLConnection connection = url.openConnection();
        InputStream sudoku9Stream = connection.getInputStream();
        String sudoku9data = IOUtils.toString(sudoku9Stream, "UTF-8");
        out.println(sudoku9data);
    %>

The backup needed some modifications, as it has to truncate the path and add the rest path after that. 

## The basic Layout 1 - Getting CSS to work ##

As a first step a 9x9 layout, a check and a solve button will be added. 
The app does not know how to find the stylesheets. Finding examples using xml configuration is easier and now
that I am forced to use a war archive anyhow due to bad spring boot JSP support I will use the web.xml again.

The web.xml worked basically immediately. However the missing jackson libraries caused trouble with the 
RESTful service leading to the index page using this service also malfunctioning. 

The mvc-dispatcher-servlet.xml has a line added:

        <mvc:resources mapping="/resources/**" location="/resources/" />
       
This leads to the src directory src/main/webapp/resources/ being available as /resources/ for the JSP:

        <link rel="stylesheet" href="<c:url value="/resources/css/sudoku.css"/>">

## The basic Layout 2 - Using CSS and JSTL to arrange the text inputs to a sudoku field ##

To ensure the rows stay rows a horizontal class is added. 
To enforce vertical alignment I tricked a litte using a column-count of 1.
While there is a n-th child selector in css, I decided to get more used to JSTL using the equivalent to if-then-else,
with <c:choose>.

One of the issues with my design is that the border continues to the right where no more sudoku cells are and 
the left and top have no border using only the rule x or y mod 3 is 2. I will add a rule at top and at left, such
that it will have a border as well. The choose construct immediatley paid of here.
To remove the border at the left the width of the vertical div was limited to the width of 9 cells and 4 borders.

The representation is not perfect considering the borders, however this is something that can be fixed once
everything else is working fine. One way to solve this would be to half the border width and add the x or y mod 3
is 0 rules. 


