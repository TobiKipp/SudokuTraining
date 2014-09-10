package sudoku;

import java.lang.Math;
import java.lang.Character;

/*
 * A 9x9 Sudoku field object.
 */
public class Sudoku9{

    private int[][] field;
    

    /*
     * The configuration is a one-dimensional representation of the 9x9 field. 
     * A value of 0 represents an unsolved cell and 1 to 9 represents a solved cell.
     * @param config A string representing the configuration.
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
        this.field = new int[9][9];
        this.loadConfig(config);
    
    }
    /*
     * The digits 1 to 9 are interpreted as field value is known. Any other character will be interpreted
     * as the value is unknown.
     */
    public void loadConfig(String config){
       int configlength = Math.min(config.length(), 81);//It may only have up to 81 characters processed
       for (int i = 0; i < configlength; i++){
           int[] xy = indexToXY(i);
           int x = xy[0];
           int y = xy[1];
           char value = config.charAt(i);
           if(Character.isDigit(value)){
               this.field[y][x] = (int) value;
           }
           else{
               this.field[y][x] = 0;//TODO: Does int always initialize as 0? If remove the block.
           }
       }
    }

    public void solve(){
        //TODO
    }

    private int[] indexToXY(int i){
        int x = i%9;
        int y = i/9;
        return new int[] {x, y};
    }

    public int[][] getField(){
        return this.field;
    }

}
