package sudoku;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sudoku.Sudoku9;
 
@RestController
@RequestMapping("rest")
public class MyRestController {

        @RequestMapping("/sudoku9")
        public Sudoku9 sudoku9( @RequestParam(value="config", required=false,
                                              defaultValue ="123456789asdasdnm6sndjfnewrw0-*") String config){
            return new Sudoku9(config=config);
        }
 
}
