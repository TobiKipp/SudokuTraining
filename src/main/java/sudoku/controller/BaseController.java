package sudoku.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
 
@Controller
@RequestMapping("/")
public class BaseController {
 
        @RequestMapping(value="/welcome", method = RequestMethod.GET)
        public String welcome(ModelMap model) {
 
                model.addAttribute("message", "Maven Web Project + Spring 3 MVC - welcome()");
 
                //Spring uses InternalResourceViewResolver and return back index.jsp
                return "index-example";
 
        }
 
        @RequestMapping(value="/welcome/{name}", method = RequestMethod.GET)
        public String welcomeName(@PathVariable("name") String name, ModelMap model) {
 
                model.addAttribute("message", "Maven Web Project + Spring 3 MVC - " + name);
                return "index-example";
 
        }


        @RequestMapping(value="/")
        public String home(ModelMap model){
            return "index";
        }
 
}
