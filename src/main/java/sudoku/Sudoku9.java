package sudoku;

import java.lang.Math;
import java.lang.Character;
import java.util.Arrays;
import sudoku.SudokuCell;
import sudoku.SudokuCellThread;
import sudoku.SudokuGroupThread;
import sudoku.SudokuFieldStatusThread;
import java.lang.Thread;
import java.util.ArrayList;
import java.lang.InterruptedException;

/*
 * A 9x9 Sudoku field object.
 */


public class Sudoku9{

    private SudokuCell[][] sudokuField;
    private static final String[] possibleValues = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    
    public Sudoku9(){
        this.init("");
    }
    /*
     * The configuration is a one-dimensional representation of the 9x9 sudokuField. 
     * A value of 0 represents an unsolved cell and 1 to 9 represents a solved cell.
     * @param config A String representing the configuration.
     */
    public Sudoku9(String config){
        this.init(config);
    }
    public Sudoku9(String config, String operation){
        this.init(config);
        if(operation.equals("solve")){
            this.solve();
        }
    }

    public void init(String config){
        this.sudokuField = new SudokuCell[9][9];
        this.loadConfig(config);

    
    }
    /*
     * The digits 1 to 9 are interpreted as sudokuField value is known. Any other Stringacter will be interpreted
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
           SudokuCell sudokuCell = new SudokuCell(this.possibleValues);
           if (!cellValue.equals("")){
               sudokuCell.setValue(cellValue);
           }
           this.sudokuField[y][x] = sudokuCell;
       }
    }


    protected int[] indexToXY(int i){
        int x = i%9;
        int y = i/9;
        return new int[] {x, y};
    }
    
    public String[][] getField(){
        String[][] field = new String[9][9];
        for (int y = 0; y < 9; y++){
            for(int x = 0; x < 9; x++){
                field[y][x] = this.sudokuField[y][x].toString();
            }}
        return field;
    }

    public String toConfig(){
        String config = "";
        for (int y = 0; y < 9; y++){
            for(int x = 0; x < 9; x++){
                String cellValue = this.sudokuField[y][x].toString();
                if (cellValue == ""){
                    cellValue = "0";
                }
                config += cellValue;
            }
        }
        return config;
    }

    public void solve(){
        int timeoutCell = 20;
        int timeoutGroup = 10;
        ArrayList<Thread> allThreads = new ArrayList<Thread>();
        for (int i=0; i < 81; i++){
           int[] xy = indexToXY(i);
           int x = xy[0];
           int y = xy[1];
            
           SudokuCellThread thread = new SudokuCellThread(this.sudokuField[y][x], timeoutCell);
           allThreads.add(thread);
        }
        //First collect all indices.
        //9 blocks 9 rows 9 colums are 27 groups
        //each group has 9 elements
        //each element has 2 components x and y to access it. 
        int[][][] sudokuGroups = new int[27][9][2];
        for(int i = 0; i < 9; i++){
            int blockTop = i/3*3;
            int blockLeft = i%3*3;
            for(int element=0; element < 9; element++){
                //rows
                sudokuGroups[i][element] = new int[]{i, element};
                //columns
                sudokuGroups[i+9][element] = new int[]{element, i};
                
                //blocks
                int y = element/3;
                int x = element%3;
                sudokuGroups[i+18][element] = new int[]{blockTop+y, blockLeft+x};
            }
        }
        //Then get the groups of SudokuCells according to the indices
        SudokuCell[][] sudokuCellGroups = new SudokuCell[27][9];
        for (int group=0; group < 27; group++){
            for(int element = 0; element < 9; element++){
                int[] address = sudokuGroups[group][element];
                int y = address[0];
                int x = address[1];
                sudokuCellGroups[group][element] = this.sudokuField[y][x];
            }
        }
         
        for (SudokuCell[] sudokuCellGroup: sudokuCellGroups){
            ArrayList<SudokuCell> sudokuCellGroupList = new ArrayList<SudokuCell>(Arrays.asList(sudokuCellGroup));
            SudokuGroupThread thread = new SudokuGroupThread(sudokuCellGroupList, timeoutGroup);
            allThreads.add(thread);
        }

        //StatusThread for debugging in test mode. Shows up the so far solved field when running mvn package.
        //Thread statusThread = new SudokuFieldStatusThread(this.sudokuField);
        //allThreads.add(statusThread);

        //Start all threads
        for (Thread thread: allThreads.toArray(new Thread[allThreads.size()])){
            thread.start();
        }
        //Wait for all threads to finish
        for (Thread thread: allThreads.toArray(new Thread[allThreads.size()])){
            try{
                thread.join();
            }
            catch (InterruptedException e){
            }
        }

         
    }
}
