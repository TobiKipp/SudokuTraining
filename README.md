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
