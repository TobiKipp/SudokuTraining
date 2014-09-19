package sudoku;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import sudoku.SudokuRestTemplate;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("")
public class BaseController {
 
        /*
        @RequestMapping(value="/welcome", method = RequestMethod.GET)
        public String welcome(ModelMap model) {
 
                model.addAttribute("message", "Maven Web Project + Spring 3 MVC - welcome()");
                return "index-example";
 
        }
 
        @RequestMapping(value="/welcome/{name}", method = RequestMethod.GET)
        public String welcomeName(@PathVariable("name") String name, ModelMap model) {
 
                model.addAttribute("message", "Maven Web Project + Spring 3 MVC - " + name);
                return "index-example";
 
        }
        */
        
        /*
         * Return any string representation example.
         * Here could be a complete html document (from <HTML> to </HTML> with them included)
         */
        @RequestMapping("hi")
        @ResponseBody
        public String hi(){
            return "Hello World";
        }
        
        //Access page with and without trailing slash
        @RequestMapping(value = "/contentOfOtherPages", method = RequestMethod.GET)
        public String contentOfOtherPages(ModelMap model, HttpServletRequest request,
                @RequestParam(value = "config", defaultValue = "0" ) String config ){
            RestTemplate restTemplate = new RestTemplate();
            String selfurl = request.getRequestURL().toString();
            //The selfurl contains contentOfOtherPages, which has to be removed
            model.addAttribute("selfurl", selfurl);
            String rootUrl =  selfurl.substring(0, selfurl.length() - "contentOfOtherPages".length() - 1);
            if (rootUrl.charAt(rootUrl.length() - 1) != '/'){ 
                rootUrl = rootUrl + "/";
            }
            String sudoku9Url = rootUrl + "rest/sudoku9?config=" + config;
            model.addAttribute("sudoku9Url", sudoku9Url);
            Sudoku9 sudoku = restTemplate.getForObject(sudoku9Url, Sudoku9.class);
            model.addAttribute("field", sudoku.getField());
            model.addAttribute("config", config);
            return "contentOfOtherPages";
        }

        @RequestMapping(value="/", method = RequestMethod.GET)
        public String home(ModelMap model, HttpServletRequest request,
                @RequestParam(value = "config", defaultValue = "456" ) String config,
                @RequestParam(value = "operation", defaultValue = "none") String operation){
            RestTemplate restTemplate = new RestTemplate();
            String selfurl = request.getRequestURL().toString();
            String sudokuRestUrl = selfurl + "rest/sudoku9?config=" + config + "&operation=" + operation;
            SudokuRestTemplate sudokuRT = restTemplate.getForObject(sudokuRestUrl, SudokuRestTemplate.class);
            String[][] field =  sudokuRT.getField();
            model.addAttribute("field", field);
            return "index";
        }

 
}
