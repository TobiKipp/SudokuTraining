package sudoku;

import java.lang.Math;
import java.lang.Character;
import java.util.Arrays;

/*
 * A 9x9 Sudoku field object.
 */
public class Sudoku9{

    private String[][] field;
    
    public Sudoku9(){
    this.init("");
    }
    /*
     * The configuration is a one-dimensional representation of the 9x9 field. 
     * A value of 0 represents an unsolved cell and 1 to 9 represents a solved cell.
     * @param config A String representing the configuration.
     */
    public Sudoku9(String config){
        this.init(config);
    }
    public Sudoku9(String config, String operation){
        this.init(config);
        if(operation == "solve"){
            this.solve();
        }
    }

    public void init(String config){
        this.field = new String[9][9];
        this.loadConfig(config);
    
    }
    /*
     * The digits 1 to 9 are interpreted as field value is known. Any other Stringacter will be interpreted
     * as the value is unknown.
     */
    public void loadConfig(String config){
       int configlength = config.length();
       for (int i = 0; i < 81; i++){
           int[] xy = indexToXY(i);
           int x = xy[0];
           int y = xy[1];
           String cellValue = "";
           if (i < configlength){
               char configValue = config.charAt(i);
               if(Character.isDigit(configValue) && configValue != '0'){
                   cellValue += configValue;
               }
           }
           this.field[y][x] = cellValue;
       }
    }


    protected int[] indexToXY(int i){
        int x = i%9;
        int y = i/9;
        return new int[] {x, y};
    }

    public String[][] getField(){
        return this.field;
    }


    /*
     * returns the cofiguration. Undefined values are returned as 0.
     */
    public String toConfig(){
        String config = "";
        for (int y = 0; y < 9; y++){
            for(int x = 0; x < 9; x++){
                String cellValue = this.field[y][x];
                if (cellValue == ""){
                    cellValue = "0";
                }
                config += cellValue;
            }
        }
        return config;
    }

    public void solve(){
        //TODO
    }
}
