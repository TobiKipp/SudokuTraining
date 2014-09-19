package sudoku;
import java.lang.Thread;
import sudoku.SudokuCell;
import java.lang.InterruptedException;
import java.util.ArrayList;

class SudokuGroupThread extends Thread{
    private ArrayList<SudokuCell> sudokuCells;

    public SudokuGroupThread(ArrayList<SudokuCell> sudokuCells){
        this.sudokuCells = sudokuCells;
    }
    
    public void run(){
        try{
            while (sudokuCells.size() != 0){
                //find the first set SudokuCell 
                int j = -1;
                for(int i = 0; i < this.sudokuCells.size(); i++){
                    if (this.sudokuCells.get(i).isSet()){
                        j = i;
                        break;
                    }
                }
                if (j != -1){
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
