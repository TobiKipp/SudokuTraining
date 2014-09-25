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

Goal: Add the JSON response example without loosing the displaying of the JSP.

The first tutorial for spring is [RESTful Web Service](http://spring.io/guides/gs/rest-service/) and it
is one of the things I want to integrate into my webapp. The GUI creates a request receives the JSON 
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

and adding JSTL to maven, the page was finally loaded.

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
As kind of mixed form JSTL can be used to define the variable such that it is available in pageContext:

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

Even if some lines could be saved in the following scriptlet The c:import JSTL can not be beaten in terms of 
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

## Minor changes to the Sudoku9 class ##

The 0 in the field was not really what I wanted. To be able to extend to a 16x16 field still using only 1 character
using digits are not valid. I change the type to character using space as undefined in the class representation.
With the limit of 1 character in the input the space would have to be removed, when directly using the json
response to fill the values.

After some testing and ending with a line 

            <c:set var="space" value='${" ".charAt(0)}'/>

only to get a space character in JSTL I decided to use String instead of char or int for the cell values, even
if only one character is used. This allows to set an empty String for undefined making it much more straight forward
in the JSTL code to insert the value to the field. In Python these were things one did not have to care about
due to the duck typing. 

### Changes to Layout ###

I decided to add the DOCTYPE to the document and it totally broke the layout. This is due to some width being added
to some elements. With the limited width set for the horizontal elements the next line was used. To remove the
limit of width and remove the extra lines at the right handed side, which should not show up, I moved the 
classes that draw the border into the input. The input borders look cut off, so I added a div with the classed 
around the input. It now looks like before adding the DOCTYPE and has no more width limitation.

             <div class="horizontal">
                <div class="${xclass} ${yclass}">
                    <input type="text" class="sudokucell " value="<c:out value="${field[y][x]}"/>" maxlength="1">
                </div>
            </div>

I just tested the page in chromium and it totally fails at the column-count. So lets use an alternative for
vertical style.

Well it was so easy. I just had to use float:left with width:100% for it to work. In the Midori browser the 
lines are perfectly centered, which means I should use a library to make it look the same across all browsers, but
this is something for later. 

## Adding tests ##

As preparation I need to analyse the MVC relationship. The model is anything like the Sudoku9 class, which
handles the data fed by the controller. In this projects case one of the controllers receives the get request
with both optional parameters config and operation. The controller asks the model for the data. In this case this 
is the call of the RESTful service returning the field. The controller inserts the data into the JSP/JSTL
to render the view. This view is returned to the clients browser. As alternative the last action 
could be seen as the server controller sending to the clients browser controller, which then leads to the 
view.

The JSP/JSTL is tested manually, as it is mostly about visual information. For most parts the controller
is part of the framework and is assumed to be tested well. Anything done wrong by the controller should be
found in the visual representation.

This leaves the model to test. I will try to split up the tests, however I will not be too strict to test
only one thing per test, if testing another thing fits at the time. As example the constructor tests 
contain the test for the getField() method as well, due to having to access a private variable. 

In addition to the, yet to implement, solving method, a method to return an url to recover the current state.
It will turn any undefined field into a 0.

## Threaded solver ##

I have two things in mind. The first is group based threads that know all known values and set them 
to unavailable in all undefined cells of the group. The other is the use of cell threads, that check 
if the cell has only one solution left and then sets it. 

So how about a first implementation with these thoughts in mind? The cell needs to be altered for this.
It is no longer a string, but a Sudokucell object with a toString method that returns the value if set 
and empty string if unset. In addition it has a list of possible solutions.
The group leader can tell its cells to remove impossible values.
A list might be a very fragile component in a threaded environment. There are thread-safe list types like
java.util.concurrent.ConcurrentLinkedQueue with methods add, remove and size. 

For the SudokuCell class the value being the empty string is the equivalent to a not set flag.
As soon as the cell is set the thread handling this cell may stop.
The group leader threads may stop as soon as all cells are set. A timeout for unsolvable cases might 
be used later on.

The SudokuCell is now ready and tested, and the Sudoku9 class was updated to use it. Next are the two 
thread classes for SudokuCell and SudokuGroup. It might not be the optimal solution with threads, however
the point of doing it with threads is to train using threads in Java.

The SudokuCellThreads rely on the SudokuGroupThreads to modify the cells possible values. The only task of 
the SudokuCellThread is to call the update method in regular intervals and stop running when the value is set.
A supervisor will notice when no more SudokuCellThread is running.

### An error ###
It seems that with the change from String to SudokuCell the JSP suddenly does something wrong. The call
to the RESTful service is still fine, but the JSP leads to an, 
"The request sent by the client was syntactically incorrect."

The localization of the error shows that it has to do with the restTemplate. The following two lines lead 
to the incorrect syntax.

            String sudokuRestUrl = selfurl + "rest/sudoku9?config=" + config + "&operation=" + operation;
            Sudoku9 sudoku = restTemplate.getForObject(sudokuRestUrl, Sudoku9.class);

It can be solved by duplicating the values. With this the Sudoku9 has an array of SudokuCells and one of String, 
which is totaly not to my liking.

After some time it turned out, that it does not really need a duplication, however the code has issues with
this.sudokuField\[y\]\[x\].toString(); always returning the default value of Sudokucell,
which is the empty String in this case.

    public String[][] getField(){
        for (int y = 0; y < 9; y++){
            for(int x = 0; x < 9; x++){
                this.field[y][x] = this.sudokuField[y][x].toString();
            }}
        return this.field;
    }

Also it only affects the RestTemplate, as the rest service shows the data as it should be and the tests for the 
class run with the above method.

So lets get things clear once and for all. I added sudoku.loadConfig(config) to the controller - and after 
some patience for it to correctly reload on tomcat - it works 
with the above code. So which constructor is really used? It uses the parameterless constructor. So the 
using the 2 parameter constructor message in the log was from another call.

How to pass parameters with the RestTemplate?

I think I got something majorly wrong. What is the purpose of the RestTemplate if I can create the 
Object directly. The RestTemplate is of use if no such class is available and an JSON interpreter is of use.
To make use of it I would need another class that only contains the String array with the field values.


    @JsonIgnoreProperties(ignoreUnknown = true)
    public class SudokuRestTemplate{
    
        private String[][] field;
    
        public String[][] getField(){
            return field;
        }
    
    }

With this is all makes sense again. The RestTemplate fills this SudokuRestTemplate with the data form the 
restful service and then the getField() result is fed into the model for JSP.
I might add other variables later, but for now it only has field.

### An easy example ###

An easy sudoku has the configuration

796003052002080137010050640000000085000591000560000000049010020378060900120400768

It will be fed to the solver once it is finished.

First the SudokuCellThreads that run until they know the value of their SudokuCell is set. They do this by calling
update and isSet regularly.

The supervisor just needs to ensure that all cell threads have finished for now. This just means that all threads
have to call the join method.

The SudokuCellThread alone would run forever, as it does nothing regarding removing possible values. 
For a quick test the threads set the value to 6 if the value was not set. This also is displayed on the page.

As next step the group threads are added. Their role is to remove possible values. To keep it simple in the first
run all SudokuCells will have the possible values reduced. After that the set SudokuCells are known and do not
need to be processed in the thread. In this case SudokuGroup relates to any set of SudokuCells in 
block row and column.

The grouping of row and colum is rather easy 0 to 8 in one dimension, the other is fixed.
For groups it is a bit more complex with div and mod. After a quick sketch each block contains the elements 
[0,1,2]x[0,1,2]. Each (0,0) has to be shifted according to the box top left cell. These are [0,3,6] x [0,3,6]

Box Index i=0 top left cell is 0,0

i%3 = 0

i/3 = 0

Box Index i=1 top left cell is 0,3

i%3 = 1

i/3 = 0

Box Index i=8 top left cell is 6,6

i%3 = 2

i/3 = 2

With this follows

y = i/3\*2

x = i%3\*2

for the top left corner of the box with index i.

For some reason the whole thing seems not to solve, even though tested with GNOME Sudoku and possible value hint
activated. To find out what is missing I need a visualisation. For this another thread comes into play that displays
the field on the console while testing.

Okay the representation shows that no value is set in the progress. Lets see again what update does. 
It did not help much, so how about looking at a field that must be solved by the rules like [6,0] which has to be 
"6". The block is already full with exception to 5 and 6 and 5 is in the column. It says 2 or 8. It seems I got
something wrong with the indices.

That was some interesting debugging by printing all indices:

    GROUP 0(0,0), (0,1), (0,2), (0,3), (0,4), (0,5), (0,6), (0,7), (0,8), 
    GROUP 1(1,0), (1,1), (1,2), (1,3), (1,4), (1,5), (1,6), (1,7), (1,8), 
    GROUP 2(2,0), (2,1), (2,2), (2,3), (2,4), (2,5), (2,6), (2,7), (2,8), 
    GROUP 3(3,0), (3,1), (3,2), (3,3), (3,4), (3,5), (3,6), (3,7), (3,8), 
    GROUP 4(4,0), (4,1), (4,2), (4,3), (4,4), (4,5), (4,6), (4,7), (4,8), 
    GROUP 5(5,0), (5,1), (5,2), (5,3), (5,4), (5,5), (5,6), (5,7), (5,8), 
    GROUP 6(6,0), (6,1), (6,2), (6,3), (6,4), (6,5), (6,6), (6,7), (6,8), 
    GROUP 7(7,0), (7,1), (7,2), (7,3), (7,4), (7,5), (7,6), (7,7), (7,8), 
    GROUP 8(8,0), (8,1), (8,2), (8,3), (8,4), (8,5), (8,6), (8,7), (8,8), 
    GROUP 9(0,0), (1,0), (2,0), (3,0), (4,0), (5,0), (6,0), (7,0), (8,0), 
    GROUP 10(0,1), (1,1), (2,1), (3,1), (4,1), (5,1), (6,1), (7,1), (8,1), 
    GROUP 11(0,2), (1,2), (2,2), (3,2), (4,2), (5,2), (6,2), (7,2), (8,2), 
    GROUP 12(0,3), (1,3), (2,3), (3,3), (4,3), (5,3), (6,3), (7,3), (8,3), 
    GROUP 13(0,4), (1,4), (2,4), (3,4), (4,4), (5,4), (6,4), (7,4), (8,4), 
    GROUP 14(0,5), (1,5), (2,5), (3,5), (4,5), (5,5), (6,5), (7,5), (8,5), 
    GROUP 15(0,6), (1,6), (2,6), (3,6), (4,6), (5,6), (6,6), (7,6), (8,6), 
    GROUP 16(0,7), (1,7), (2,7), (3,7), (4,7), (5,7), (6,7), (7,7), (8,7), 
    GROUP 17(0,8), (1,8), (2,8), (3,8), (4,8), (5,8), (6,8), (7,8), (8,8), 
    GROUP 18(0,0), (0,1), (0,2), (1,0), (1,1), (1,2), (2,0), (2,1), (2,2), 
    GROUP 19(0,3), (0,4), (0,5), (1,3), (1,4), (1,5), (2,3), (2,4), (2,5), 
    GROUP 20(0,6), (0,7), (0,8), (1,6), (1,7), (1,8), (2,6), (2,7), (2,8), 
    GROUP 21(3,0), (3,1), (3,2), (4,0), (4,1), (4,2), (5,0), (5,1), (5,2), 
    GROUP 22(3,3), (3,4), (3,5), (4,3), (4,4), (4,5), (5,3), (5,4), (5,5), 
    GROUP 23(3,6), (3,7), (3,8), (4,6), (4,7), (4,8), (5,6), (5,7), (5,8), 
    GROUP 24(6,0), (6,1), (6,2), (7,0), (7,1), (7,2), (8,0), (8,1), (8,2), 
    GROUP 25(6,3), (6,4), (6,5), (7,3), (7,4), (7,5), (8,3), (8,4), (8,5), 
    GROUP 26(6,6), (6,7), (6,8), (7,6), (7,7), (7,8), (8,6), (8,7), (8,8),

Group 0-9 are rows.
Group 10-17 are colums.
Group 18 to 26 are blocks.

It was one typo and one calculation (if I even calculated anything there) error. 
The box x and y must be multiplied by 3 and not 2. 

y = i/3\*3

x = i%3\*3

I noted that Box Index i=8 top left cell is 6,6

x = i%3\*3 = 2\*3 = 6

y = i/3\*3 = 2\*3 = 6 

### Timeout ###

One of the issues is if the rules are not enough to solve the sudoku. One way would be to add a number
of rounds that a thread may have without a change. As alternative all threads would have to communicate, however
they are all running more or less independent from each other. The first one is significantly easier to implement
and might later be used to implement the more intelligent termination.
The SudokuCellThread needs to store additional information to see if the possible values changed.

As an example for and only partially solvable sudoku http://localhost:8080/Sudoku/?config=123------456------78&operation=solve can be used as url. It will find the 9 that is not in the configuration. The time to response is
around 2 to 4 seconds with the current configuration.

## Save config button ##

The save config button will create an URL from the values in the field. Since I do not plan to use Javascript 
for this the first thing that comes to my mind is to use a form.

The form was setup to use the path /handle/sudoku9 with parameters ynxm=value format.
One example output is:

    http://localhost:8080/Sudoku/handle/sudoku9?y0x0=1&y0x1=2&y0x2=3&y0x3=&y0x4=&y0x5=&y0x6=&y0x7=&y0x8=&y1x0=4&y1x1=5&y1x2=6&y1x3=&y1x4=&y1x5=&y1x6=&y1x7=&y1x8=&y2x0=7&y2x1=8&y2x2=&y2x3=&y2x4=&y2x5=&y2x6=&y2x7=&y2x8=&y3x0=&y3x1=&y3x2=&y3x3=&y3x4=&y3x5=&y3x6=&y3x7=&y3x8=&y4x0=&y4x1=&y4x2=&y4x3=&y4x4=&y4x5=&y4x6=&y4x7=&y4x8=&y5x0=&y5x1=&y5x2=&y5x3=&y5x4=&y5x5=&y5x6=&y5x7=&y5x8=&y6x0=&y6x1=&y6x2=&y6x3=&y6x4=&y6x5=&y6x6=&y6x7=&y6x8=&y7x0=&y7x1=&y7x2=&y7x3=&y7x4=&y7x5=&y7x6=&y7x7=&y7x8=&y8x0=&y8x1=&y8x2=&y8x3=&y8x4=&y8x5=&y8x6=&y8x7=&y8x8=

The method for this stores the values according to their name in an array and then turns this array to 
a configuration stream and redirects to the index page with this configuration.
As the parameters are more or less arbitrary, or at least generic, writing them all down does not make much sense.
Instead all parameters are to be stored in one Map.

        public String storeSudoku9(@RequestParam Map<String,String> allRequestParams, ModelMap model){

The @ResponseBody annotation helps to see what gets into the page a lot.

For the Sudoku9 class the index based selection was okay for the parameter to coordinate transformation.
When implementing the Samurai-Sudoku I will deal with the index extraction.

## Other buttons ##

The clear button just redirects to the start page with and empty configuration.
The solve button does almost the same as the save button. It only adds the "&operation=save" to the redirect url.


## A hard example ##

http://localhost:8080/Sudoku/?config=050702003073480005000050400040000200027090350006000010005030000400068730700109060

The link starts loads a GNOME Sudoku hard start configuration. Using solve will lead to a state, where only
at least 2 possible values per field are left.

A rule for pair exclusion might help. If two cells in a group have the same two possible values left, then
the other fields can not have this value.
### Manually solving for now ##
According to this rule the field \[8\]\[1\] is 3 and
\[8\]\[6\] is 5. This provides no further values. 
\[3\]\[8\] is 6. 
\[4\]\[8\] is 4. 
\[5\]\[8\] is 7. 

The timeouts are set to small. I had to press solve twice after setting the above.

## Code Cleanup TimeoutThread ##

I copied code, which is considered bad for changes. I added a TimeoutThread class that offers an execute method
that is one cycle in the while running. The execute method returns a boolean value, that leads to the loop stopping
when it is false. 

## Adding solver rules ##

### Collecting Ideas unstructured ###

As mentioned in the Manually solving for now section there is a rule to excluded some possible values.
There are two pairs with the same two values left in the same group then the other cells can not have
these values. This would be the simplest case of it. 
There might be cases where from the simple rules the possible values of one cell are 1, 2, 3 and the other
cell has 1, 2, while other cells do not contain the possible values 1 or 2. In that case these two must be 1 and 2.
This might lead to another rule working. When inside a group one possible value only occurs once, then it must be
that value at that cell, if the value is not already one of those that are set. Here the parallel processing must
be done carefully. 

### Creating the rules ###

The following rules will only fire, if the simplest rule did not work for the group in the given cycle. 
Create a statistic about the possible values and store which values are already used as set in the group.

1. If for a not set value it only occurs once, then it must be the value. 
2. (generic for n) There are n unset values. Find exactly n cells where the possible values contain at least one 
   of these unset values.
   If such a set of cells can be found, then the possible values of the found n cells are changed to only 
   contain values of these n unset values. 
   The other cells have the n unset values removed from the possible values.
   TODO: Is such a kind of distribution possible [[1,2],[1,2],[2],...]? One may find 3 cells with values 
   from [1, 2, 3] but there is no possible solution. I can not find an example on the fly, so I will 
   have to test if there is any failure in the solution of the fed sudokus. 

Example for rule 2 with n=2. 

    The possible values  [ [1, 2, 3], [1, 2, 4], [4, 5, 6, 7], [3, 5, 6, 7], [3, 4, 5, 7], ..] 
    count 2: 1, 2, 6
    count 3: 3, 4, 5   
    For n=2 there are pairs [ [2, 6], [1, 6], [1, 2] ]
    [2, 6]: Cells 0, 1, 2, 3 are 4 cells, which is too much.
    [1, 6]: Cells 0, 2, 3 are 3 cells, which is too much.
    [1, 2]: Only in cell 0 and cell 1. it is found. 
    The possible values are changed to [ [1, 2], [1, 2], [4, 5, 6, 7], [3, 5, 6, 7], [3, 4, 5, 7], ..] 
    Two values could be eliminated this way and another group may now find a solution with a simpler rule.

After thinking about an example I noticed that rule 1 is the special case of rule 2 for n = 1.

### Implementing the rule ###

I split up the code into more functions to be able to test them better.
After testing all so far and removing a typo, everything is working as it should. To implement the rule in
a generic way, the set of values has to be generated.

Lets assume there is the set [1, 2, 3, 4] and all 3 element combinations should be generated from it.
When writing it as a mask,  with 1 being use and 0 not use, it would be::

    1110
    1101
    1011
    0111

This can be generated using a tree or something similar. To work around the dynamic number of loops
a recursive algorithm is used.

A first try to formulate a subset generator:
    
    n is the number of elements to select, combination is the so far generated combination and
    the set contains the values to choose from.
    if n is 1
        for each element in the set add it to the combination
        store the combination in a list.
    else
        Make a subset with the last n-1 elements remove named firstElements 
        For each element in firstElements
            Add the element as part of the combination.
            Call the method for n-1 with the full set without the chosen element and the so far generated 
            combination.


The test for it has shown that there was one thing that lead to duplicate (other ordered) combinations.
To avoid duplicate combinations the already used values have to be excluded. This is done by copying the
ArrayList before the loop instead of each time inside the loop.

So now that all combinations are available for each of these check if there are exactly n SudokuCells that 
have at least of one of the values.

A matching cluster of n SudokuCells is found. For all SudokuCells in the group it is check if they are in
the cluster or not. Those in the cluster have their values limited to the values the cluster covers.
For those not in the cluster these values are removed from the possible values.


## Adding the Samurai Sudoku ##

The main functionality for the 9x9 Sudoku including the solver is finished. To make use of the already
existent code a new page shall be added that displays, stores and solves a Samurai Sudoku. The simplest
way of thinking what it is, is having 5 Sudoku fields in an x shape, where the center Sudoku shares the 
top left, top right, bottom left and bottom right block group with one of the other Sudokus. 

It will require a bit more work with the layout, due to spaces in between. The configuration will be almost
5 times as long as for the 9x9 field. The configuration parameter reduces the 2 dimensions to 1 by going from
left to right and top to bottom. 

Lets start slow. As first step we create a new JSP and just copy the old code to modify it later. The 
path /SamuraiSudoku/ will then reference to it.
A new rest service. The template can be reused to consume the restful service. But how the field values
are to be interpreted has to be thought about when it is time.

To access the restful service that is at /rest/samuraisudoku the url had to remove the /SamuraiSudoku part, which
is done by using replace. 

At this moment the view is just a copy of Sudoku9, which runs at the root of the app. Next the functionality of
the buttons is added (copied). The SamuraiSudoku.jsp had its action changed to be absolute in path, so 
"/Sudoku/" was added in front of it, to ensure the correct address will be called.

With this it is a copy of the Sudoku9, but with all addresses already changed to SamuraiSudoku.
