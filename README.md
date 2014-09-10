#Training project Sudoku#

##Introduction##

The tutorials are too short to really learn anything big new after the third one on the [spring page](spring.io). 

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

