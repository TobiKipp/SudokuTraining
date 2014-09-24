package sudoku;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import sudoku.SudokuGroupThread;
import sudoku.SudokuCell;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SudokuGroupThreadTest{

    SudokuGroupThread sudokuGroupThread;
    SudokuCell sudokuCell1;
    SudokuCell sudokuCell2;
    SudokuCell sudokuCell3;
    @Before
    public void initialize(){
        this.sudokuCell1 = new SudokuCell(new String[]{"1", "3"});
        this.sudokuCell2 = new SudokuCell(new String[]{"1", "2", "3"});
        this.sudokuCell3 = new SudokuCell(new String[]{"3"});
        ArrayList<SudokuCell> sudokuCells = new ArrayList<SudokuCell>();
        sudokuCells.add(this.sudokuCell1);
        sudokuCells.add(this.sudokuCell2);
        sudokuCells.add(this.sudokuCell3);
        this.sudokuGroupThread = new SudokuGroupThread(sudokuCells, 40);
    
    }
    
    @Test 
    public void testGetStatistic(){

        HashMap<String, Integer> statistic = this.sudokuGroupThread.getStatistic();
        assertEquals(2, (int) statistic.get("1"));
        assertEquals(1, (int) statistic.get("2"));
        assertEquals(3, (int) statistic.get("3"));
        
    }
    @Test
    public void testGetSudokuCells(){
        SudokuCell[] sudokuCells = this.sudokuGroupThread.getSudokuCells();
        assertEquals(this.sudokuCell1, sudokuCells[0]);
        assertEquals(this.sudokuCell2, sudokuCells[1]);
        assertEquals(this.sudokuCell3, sudokuCells[2]);
    }

    @Test
    public void testGetCellsWithOneOf(){
        String[] clusterValues = new String[]{"2"};
        SudokuCell[] matchingCells = this.sudokuGroupThread.getCellsWithOneOf(clusterValues);
        //Only the sudokuCell2 should match with it.
        assertEquals(1, matchingCells.length);
        assertEquals(this.sudokuCell2, matchingCells[0]);
    }
    
    @Test 
    public void testGetFirstSetCellIndex(){
        //All cells are unset
        int firstIndex = this.sudokuGroupThread.getFirstSetCellIndex();
        int expectedIndex = -1;
        assertEquals(expectedIndex, firstIndex);
        //Due to the update of sudokuCell3 it will be a set cell, which should be at index 2.
        this.sudokuCell3.update();
        firstIndex = this.sudokuGroupThread.getFirstSetCellIndex();
        expectedIndex = 2;
        assertEquals(expectedIndex, firstIndex);
        
    }

    @Test
    public void testHandleSetCell(){
        //After update sudokuCell3 is set
        this.sudokuCell3.update();
        assertEquals("3", sudokuCell3.toString());
        SudokuCell[] sudokuCells = this.sudokuGroupThread.getSudokuCells();
        //Ensure the index is correct
        assertEquals(this.sudokuCell3, sudokuCells[2]);
        //Use the index to call the to test method
        this.sudokuGroupThread.handleSetCell(2);
        sudokuCells = this.sudokuGroupThread.getSudokuCells();
        assertEquals(2, sudokuCells.length);
        assertEquals(this.sudokuCell1, sudokuCells[0]);
        assertEquals(this.sudokuCell2, sudokuCells[1]);
        //Now handle to rest until no more elements are left
        this.sudokuCell1.update();
        this.sudokuGroupThread.handleSetCell(0);
        sudokuCells = this.sudokuGroupThread.getSudokuCells();
        assertEquals(1, sudokuCells.length);
        assertEquals(this.sudokuCell2, sudokuCells[0]);

        this.sudokuCell2.update();
        this.sudokuGroupThread.handleSetCell(0);
        sudokuCells = this.sudokuGroupThread.getSudokuCells();
        assertEquals(0, sudokuCells.length);
    }

    @Test
    public void testGenerateAllNElementCombinations(){
        int n = 3;
        ArrayList<String> combination = new ArrayList<String>();
        ArrayList<String> valuesLeft = new ArrayList<String>();
        valuesLeft.add("1");
        valuesLeft.add("2");
        valuesLeft.add("3");
        valuesLeft.add("4");
        ArrayList<String[]> allCombinations = new ArrayList<String[]>();
        this.sudokuGroupThread.generateAllNElementCombinations(n, combination, valuesLeft, allCombinations);
        //123, 124, 134, 234 are the possible values.
        assertEquals(4, allCombinations.size());
        assertArrayEquals(new String[]{"1","2","3"}, allCombinations.get(0));
        assertArrayEquals(new String[]{"1","2","4"}, allCombinations.get(1));
        assertArrayEquals(new String[]{"1","3","4"}, allCombinations.get(2));
        assertArrayEquals(new String[]{"2","3","4"}, allCombinations.get(3));
    }
}
