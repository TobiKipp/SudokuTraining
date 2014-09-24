
package sudoku;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import sudoku.SudokuCellThread;
import sudoku.SudokuCell;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

public class SudokuCellThreadTest{

    SudokuCellThread sudokuCellThread;
    
    @Test 
    public void testUpdate(){
        SudokuCell sudokuCell = new SudokuCell(new String[]{"3"});
        //The value is not set yet
        assertEquals("", sudokuCell.toString());
        this.sudokuCellThread = new SudokuCellThread(sudokuCell, 20);
        this.sudokuCellThread.update();
        assertEquals("3", sudokuCell.toString());
    }
}
