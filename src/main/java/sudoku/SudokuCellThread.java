package sudoku;
import java.lang.Thread;
import sudoku.SudokuCell;
import java.lang.InterruptedException;

class SudokuCellThread extends Thread{
    private SudokuCell sudokuCell;

    public SudokuCellThread(SudokuCell sudokuCell){
        this.sudokuCell = sudokuCell;
    }
    
    public void run(){
        try{
            boolean set = this.sudokuCell.isSet();
            while (!set){
                this.sudokuCell.update();
                set = this.sudokuCell.isSet();
                this.sleep(200);
            }
        }
        catch (InterruptedException e){
            System.out.println(e.toString());
        }
    }
}
