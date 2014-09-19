package sudoku;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SudokuRestTemplate{

    private String[][] field;

    public String[][] getField(){
        return field;
    }


}
