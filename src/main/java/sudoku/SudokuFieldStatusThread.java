package sudoku;
import java.lang.Thread;
import sudoku.SudokuCell;
import java.lang.InterruptedException;

class SudokuFieldStatusThread extends Thread{
    private SudokuCell[][] field;

    public SudokuFieldStatusThread(SudokuCell[][] field){
        this.field = field;
    }
    
    public void run(){
        try{
            while(true){
                boolean done = true;
                for(int y = 0 ; y < 9; y++){
                    System.out.println("");
                    for(int x = 0; x < 9; x++){ 
                        String value = this.field[y][x].toString();
                        if (value.equals("")){
                            value = " ";
                            done = false;
                        }
                        System.out.print(value);
                    }
                    
                }
                System.out.println();
                if(done) break;
                this.sleep(1000);
            }
            
        }
        catch (InterruptedException e){
            System.out.println(e.toString());
        }
    }
}
