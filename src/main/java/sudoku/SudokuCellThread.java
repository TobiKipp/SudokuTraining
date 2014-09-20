package sudoku;
import java.lang.Thread;
import sudoku.SudokuCell;
import java.lang.InterruptedException;

class SudokuCellThread extends Thread{
    private SudokuCell sudokuCell;
    private String[] oldPossibleValues;
    private int timeoutMax;

    public SudokuCellThread(SudokuCell sudokuCell, int timeoutMax){
        this.sudokuCell = sudokuCell;
        this.timeoutMax = timeoutMax;
        this.oldPossibleValues = new String[0];
    }
    
    public void run(){
        try{
            int timeout = this.timeoutMax;
            boolean set = this.sudokuCell.isSet();
            while (!set){
                timeout -= 1;
                if(timeout < 0){
                    break;
                }
                this.sudokuCell.update();
                set = this.sudokuCell.isSet();
                String[] possibleValues = this.sudokuCell.getPossibleValues();
                if(possibleValues.length != oldPossibleValues.length){
                    timeout = this.timeoutMax;
                }
                oldPossibleValues = possibleValues;
                this.sleep(100);
            }
        }
        catch (InterruptedException e){
            System.out.println(e.toString());
        }
    }
}
