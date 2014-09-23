package sudoku;
import helper.TimeoutThread;
import sudoku.SudokuCell;
import java.lang.InterruptedException;
import java.util.ArrayList;

class SudokuGroupThread extends TimeoutThread{
    private ArrayList<SudokuCell> sudokuCells;

    public SudokuGroupThread(ArrayList<SudokuCell> sudokuCells, int timeoutMax){
        super(timeoutMax);
        this.sudokuCells = sudokuCells;
    }
    
    public boolean execute(){
        //find the first set SudokuCell 
        int j = -1;
        for(int i = 0; i < this.sudokuCells.size(); i++){
            if (this.sudokuCells.get(i).isSet()){
                j = i;
                break;
            }
        }
        if (j != -1){
            this.resetTimeout();
            String value = this.sudokuCells.get(j).toString();
            //remove its value from the possibles of the others
            for(int i = 0; i < this.sudokuCells.size(); i++){
                //as it is set the case (i == j) does not have to be handled
                this.sudokuCells.get(i).removePossibleValue(value);   
            }
            //The cell has no more valuable information and is removed
            this.sudokuCells.remove(j);
        }
        boolean running = (this.sudokuCells.size() != 0);
        return running;
    }
}
