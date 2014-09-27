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
import java.util.Map;
import java.util.Set;
import java.lang.Integer;

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
                @RequestParam(value = "config", defaultValue = "0" ) String config,
                @RequestParam(value = "operation", defaultValue = "none") String operation){
            RestTemplate restTemplate = new RestTemplate();
            String selfurl = request.getRequestURL().toString();
            String sudokuRestUrl = selfurl + "rest/sudoku9?config=" + config + "&operation=" + operation;
            SudokuRestTemplate sudokuRT = restTemplate.getForObject(sudokuRestUrl, SudokuRestTemplate.class);
            String[][] field =  sudokuRT.getField();
            model.addAttribute("field", field);
            return "index";
        }

        @RequestMapping(value="/handle/sudoku9", params="store", method = RequestMethod.GET)
        public String storeSudoku9(@RequestParam Map<String,String> allRequestParams, ModelMap model){
            String config = extractConfigSudoku9(allRequestParams);
            return "redirect:/?config="+config;
        } 

        @RequestMapping(value="/handle/sudoku9", params="solve", method = RequestMethod.GET)
        public String solveSudoku9(@RequestParam Map<String,String> allRequestParams, ModelMap model){
            String config = extractConfigSudoku9(allRequestParams);
            return "redirect:/?config="+config+"&operation=solve";
        } 
        @RequestMapping(value="/handle/sudoku9", params="clear", method = RequestMethod.GET)
        public String clearSudoku9(@RequestParam Map<String,String> allRequestParams, ModelMap model){
            String config = extractConfigSudoku9(allRequestParams);
            return "redirect:/?config=";
        } 
 
        public String extractConfigSudoku9(Map<String, String> allRequestParams){
            Set<String> keys = allRequestParams.keySet();
            String[][] orderedValues = new String[9][9];
            for (String key: keys){
                String value = allRequestParams.get(key);
                //The strings are expected to be in y<number>x<number> form
                //In this case we are lucky that the number has only one digit
                if(key.substring(0,1).equals("y") && key.substring(2,3).equals("x"))
                {
                    int x = Integer.parseInt(key.substring(3,4));
                    int y = Integer.parseInt(key.substring(1,2));
                    orderedValues[y][x] = value; 
                }
            }

            String config = "";
            for (int y = 0; y < 9; y++){
                for(int x = 0; x < 9; x++){
                    String value = orderedValues[y][x];
                    if(value == null) value = "0";
                    if(value.equals("")) value = "0";
                    config += value;
                }
            }
            return config;
        }

        @RequestMapping(value="/SamuraiSudoku", method = RequestMethod.GET)
        public String samuraiSudoku(ModelMap model, HttpServletRequest request,
                @RequestParam(value = "config", defaultValue = "0" ) String config,
                @RequestParam(value = "operation", defaultValue = "none") String operation){
            RestTemplate restTemplate = new RestTemplate();
            String selfurl = request.getRequestURL().toString();
            String baseurl = selfurl.replace("/Sudoku/SamuraiSudoku", "/Sudoku/");
            String sudokuRestUrl = baseurl + "rest/samuraisudoku?config=" + config + "&operation=" + operation;
            SudokuRestTemplate sudokuRT = restTemplate.getForObject(sudokuRestUrl, SudokuRestTemplate.class);
            
            String css = "samuraisudoku.css";
            String userAgent = request.getHeader("user-agent");
            if(userAgent.contains("Chrome")) css = "samuraisudoku-chrome.css";

            String[][] field =  sudokuRT.getField();
            model.addAttribute("field", field);
            model.addAttribute("samuraisudokucss", css);
            return "SamuraiSudoku";
        }

        @RequestMapping(value="/handle/samuraisudoku", params="store", method = RequestMethod.GET)
        public String storeSamuraiSudoku(@RequestParam Map<String,String> allRequestParams, ModelMap model){
            String config = extractConfigSamuraiSudoku(allRequestParams);
            return "redirect:/SamuraiSudoku?config="+config;
        } 

        @RequestMapping(value="/handle/samuraisudoku", params="solve", method = RequestMethod.GET)
        public String solveSamuraiSudoku(@RequestParam Map<String,String> allRequestParams, ModelMap model){
            String config = extractConfigSamuraiSudoku(allRequestParams);
            return "redirect:/SamuraiSudoku?config="+config+"&operation=solve";
        } 
        @RequestMapping(value="/handle/samuraisudoku", params="clear", method = RequestMethod.GET)
        public String clearSamuraiSudoku(@RequestParam Map<String,String> allRequestParams, ModelMap model){
            String config = extractConfigSamuraiSudoku(allRequestParams);
            return "redirect:/SamuraiSudoku?config=";
        } 

        public String extractConfigSamuraiSudoku(Map<String, String> allRequestParams){
            Set<String> keys = allRequestParams.keySet();
            String[][] orderedValues = new String[21][21];
            for (String key: keys){
                String value = allRequestParams.get(key);
                //The strings are expected to be in y<number>x<number> form
                //It can be either one or two digit numbers.
                //It is assumed that no other parameter starts with y.
                if(key.substring(0,1).equals("y")){
                    String[] splitKeyX = key.split("x");
                    int x = Integer.parseInt(splitKeyX[1]);
                    int y = Integer.parseInt(splitKeyX[0].split("y")[1]);
                    if(value.equals("")) value = "0";
                    orderedValues[y][x] = value; 
                }
            }

            String config = "";
            for (int y = 0; y < 21; y++){
                for(int x = 0; x < 21; x++){
                   if(!(
                     (x >= 9 && x <= 11 && (y <= 5 || y >=15))||
                     (y >= 9 && y <= 11 && (x <= 5 || x >=15))
                     )){ 
                        String value = orderedValues[y][x];
                        if(!value.equals("")){
                            config += value;
                        }
                    }
                }
            }
            return config;
        }
}
