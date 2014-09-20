package sudoku;
import java.lang.Thread;
import sudoku.SudokuCell;
import java.lang.InterruptedException;
import java.util.ArrayList;

class SudokuGroupThread extends Thread{
    private ArrayList<SudokuCell> sudokuCells;
    private int timeoutMax;

    public SudokuGroupThread(ArrayList<SudokuCell> sudokuCells, int timeoutMax){
        this.sudokuCells = sudokuCells;
        this.timeoutMax = timeoutMax;
    }
    
    public void run(){
        try{
            int timeout = this.timeoutMax;
            while (sudokuCells.size() != 0){
                timeout -= 1;
                if(timeout < 0){
                    break;
                }
                //find the first set SudokuCell 
                int j = -1;
                for(int i = 0; i < this.sudokuCells.size(); i++){
                    if (this.sudokuCells.get(i).isSet()){
                        j = i;
                        break;
                    }
                }
                if (j != -1){
                    timeout = this.timeoutMax;
                    String value = this.sudokuCells.get(j).toString();
                    //remove its value from the possibles of the others
                    for(int i = 0; i < this.sudokuCells.size(); i++){
                        //as it is set the case (i == j) does not have to be handled
                        this.sudokuCells.get(i).removePossibleValue(value);   
                    }
                    //The cell has no more valuable information and is removed
                    this.sudokuCells.remove(j);
                }
                this.sleep(200);
            }
        }
        catch (InterruptedException e){
            System.out.println(e.toString());
        }
    }
}
