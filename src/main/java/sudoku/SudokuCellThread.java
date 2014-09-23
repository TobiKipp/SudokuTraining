package sudoku;
import java.lang.Thread;
import helper.TimeoutThread;
import sudoku.SudokuCell;
import java.lang.InterruptedException;

class SudokuCellThread extends TimeoutThread{
    private SudokuCell sudokuCell;
    private String[] oldPossibleValues;

    public SudokuCellThread(SudokuCell sudokuCell, int timeoutMax){
        super(timeoutMax);
        this.sudokuCell = sudokuCell;
        this.oldPossibleValues = new String[0];
    }
    
    public boolean execute(){
        this.sudokuCell.update();
        String[] possibleValues = this.sudokuCell.getPossibleValues();
        if(possibleValues.length != this.oldPossibleValues.length){
            this.resetTimeout();
        }
        this.oldPossibleValues = possibleValues;
        boolean running = !this.sudokuCell.isSet();
        return running;
    }
}
