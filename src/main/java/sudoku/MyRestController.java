package sudoku;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sudoku.Sudoku9;
import sudoku.SamuraiSudoku;
 
@RestController
@RequestMapping("rest")
public class MyRestController {

        @RequestMapping("/sudoku9")
        public Sudoku9 sudoku9( @RequestParam(value="config", required=false,
                                              defaultValue ="123456789asdasdnm6sndjfnewrw0-*") String config,
                                @RequestParam(value="operation", required = false,
                                              defaultValue = "none") String operation){
            return new Sudoku9(config = config, operation = operation);
        }
 
        @RequestMapping("/samuraisudoku")
        public SamuraiSudoku samuraiSudoku( @RequestParam(value="config", required=false,
                                              defaultValue ="123456789asdasdnm6sndjfnewrw0-*") String config,
                                @RequestParam(value="operation", required = false,
                                              defaultValue = "none") String operation){
            return new SamuraiSudoku(config = config, operation = operation);
        }
}
