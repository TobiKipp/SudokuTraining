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
import java.util.HashMap;

public class SamuraiSudoku{

    private HashMap<Integer, HashMap<Integer, SudokuCell> > sudokuField = new HashMap<Integer, HashMap<Integer, SudokuCell> >();
    private Integer[][] skipIndex = new Integer[][]{
            {9, 10, 11},
            {9, 10, 11},
            {9, 10, 11},
            {9, 10, 11},
            {9, 10, 11},
            {9, 10, 11},
            {},
            {},
            {},
            {0, 1, 2, 3 ,4, 5, 15, 16, 17, 18, 19, 20},
            {0, 1, 2, 3 ,4, 5, 15, 16, 17, 18, 19, 20},
            {0, 1, 2, 3 ,4, 5, 15, 16, 17, 18, 19, 20},
            {},
            {},
            {},
            {9, 10, 11},
            {9, 10, 11},
            {9, 10, 11},
            {9, 10, 11},
            {9, 10, 11},
            {9, 10, 11}
            };
    private int[] rowLength;
    private static final String[] possibleValues = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    
    public SamuraiSudoku(){
        this.init("");
    }
    /*
     * The configuration is a one-dimensional representation of the 9x9 sudokuField. 
     * A value of 0 represents an unsolved cell and 1 to 9 represents a solved cell.
     * @param config A String representing the configuration.
     */
    public SamuraiSudoku(String config){
        this.init(config);
    }
    public SamuraiSudoku(String config, String operation){
        this.init(config);
        if(operation.equals("solve")){
            this.solve();
        }
    }

    public void init(String config){
        this.rowLength = new int[21];
        for(int y = 0; y < 21; y++){
            this.rowLength[y] = 21 - this.skipIndex[y].length;
            this.sudokuField.put(y, new HashMap<Integer, SudokuCell>());
        }
        this.loadConfig(config);

    
    }
    /*
     * The digits 1 to 9 are interpreted as sudokuField value is known. Any other Stringacter will be interpreted
     * as the value is unknown.
     */
    public void loadConfig(String config){
       int configlength = config.length();
       for (int i = 0; i < 369; i++){
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
          
           this.sudokuField.get(y).put(x, sudokuCell);
       }
    }

    /*
     * Turns an index to the array coordinates.
     */
    public int[] indexToXY(int i){
        if (i >= 369 ) return new int[]{-1, -1};
        int rest = i;
        int y = 0;
        while(rest >= this.rowLength[y]){
            rest -= this.rowLength[y];
            y++;
        }
        int x = -1; 
        while (rest >= 0){
            rest--;
            x++;
            while (Arrays.asList(skipIndex[y]).contains(x)){
                x++;        
            }
        } 
        return new int[] {x, y};
    }
    
    public String[][] getField(){
        String[][] field = new String[21][21];
        for (int y = 0; y < 21; y++){
            for(int x = 0; x < 21; x++){
                if(this.sudokuField.get(y).containsKey(x)){
                    field[y][x] = this.sudokuField.get(y).get(x).toString();
                }
                else{
                    field[y][x] = "_";
                }
            }
        }
        return field;
    }

    public String toConfig(){
        String config = "";
        for (int y = 0; y < 21; y++){
            for(int x = 0; x < 21; x++){
                if(this.sudokuField.get(y).containsKey(x)){
                    String cellValue = this.sudokuField.get(y).get(x).toString();
                    if (cellValue == ""){
                        cellValue = "0";
                    }
                    config += cellValue;
                }
            }
        }
        return config;
    }

    public void solve(){
        int timeoutCell = 40;
        int timeoutGroup = 20;
        ArrayList<Thread> allThreads = new ArrayList<Thread>();
        for (int i=0; i < 369; i++){
           int[] xy = indexToXY(i);
           int x = xy[0];
           int y = xy[1];
            
           SudokuCellThread thread = new SudokuCellThread(this.sudokuField.get(y).get(x), timeoutCell);
           allThreads.add(thread);
        }
        //First collect all indices.
        //41 blocks
        //45 rows
        //45 columns
        int[][][] sudokuGroups = new int[131][9][2];
        int[] offsetsX = new int[]{0, 12, 6,  0, 12};
        int[] offsetsY = new int[]{0,  0, 6, 12, 12};
        //rows and colums and the blocks for the 4 outer sudoku9x9
        int blockOffset = 0;
        for(int sudoku9x9 = 0; sudoku9x9 < 5; sudoku9x9++){
            int offsetX = offsetsX[sudoku9x9];        
            int offsetY = offsetsY[sudoku9x9];        
            for(int i = 0; i < 9; i++){
                int blockTop = i/3*3;
                int blockLeft = i%3*3;
                for(int element=0; element < 9; element++){
                    //rows
                    sudokuGroups[i+sudoku9x9*18][element] = new int[]{offsetY + i, offsetX + element};
                    //columns
                    sudokuGroups[i+9+sudoku9x9*18][element] = new int[]{offsetY + element, offsetX + i};
                    //rows and colums take indices 0 to 89
                    //blocks starting at 90
                    if(sudoku9x9 != 2){
                        int y = element/3 + offsetY + blockTop;
                        int x = element%3 + offsetX + blockLeft;
                        sudokuGroups[i + blockOffset * 9 + 90][element] = new int[]{y, x};
                    }
                }
            }
            if(sudoku9x9 != 2) blockOffset++;
        }
        //As check for the index to be at the correct poisition. The last used blockOffset is 3 and i is 9.
        // The last used index is: 8 + 3*9 + 90 = 125. This leaves 126 to 130 for the last 5 groups
        int[] boxStartX = new int[]{9, 6, 9 , 12,  9};
        int[] boxStartY = new int[]{6, 9, 9 ,  9, 12};
        for(int box = 0; box < 5; box++){
            for(int element=0; element < 9; element++){
                int y = element/3 + boxStartY[box];
                int x = element%3 + boxStartX[box];
                sudokuGroups[126+box][element] = new int[]{y, x};
            }
        }

        //Then get the groups of SudokuCells according to the indices
        SudokuCell[][] sudokuCellGroups = new SudokuCell[131][9];
        for (int group=0; group < 131; group++){
            for(int element = 0; element < 9; element++){
                int[] address = sudokuGroups[group][element];
                int y = address[0];
                int x = address[1];
                sudokuCellGroups[group][element] = this.sudokuField.get(y).get(x);
            }
        }
         
        for (SudokuCell[] sudokuCellGroup: sudokuCellGroups){
            ArrayList<SudokuCell> sudokuCellGroupList = new ArrayList<SudokuCell>(Arrays.asList(sudokuCellGroup));
            SudokuGroupThread thread = new SudokuGroupThread(sudokuCellGroupList, timeoutGroup);
            allThreads.add(thread);
        }

        //StatusThread for debugging in test mode. Shows up the so far solved field when running mvn package.
        //Running as daemon
        //Thread statusThread = new SudokuFieldStatusThread(this.sudokuField);
        //statusThread.start();

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
