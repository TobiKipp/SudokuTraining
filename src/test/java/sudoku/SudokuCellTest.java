package sudoku;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import sudoku.SudokuCell;
import java.util.Arrays;
import java.util.ArrayList;


public class SudokuCellTest{

    @Test
    public void testConstructor(){
        SudokuCell sudokuCell = new SudokuCell();
        assertEquals("", sudokuCell.toString());
        assertEquals(false, sudokuCell.isSet());
    }
    
    @Test
    public void testPossibleValues(){
        String[] possibleValues = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        SudokuCell sudokuCell = new SudokuCell(possibleValues);
        String[] possibleValues2 = sudokuCell.getPossibleValues();
        assertArrayEquals(possibleValues, possibleValues);
    }

    @Test
    public void testRemovePossibleValue(){
        String[] possibleValues = new String[]{"1", "2", "3"};
        SudokuCell sudokuCell = new SudokuCell(possibleValues);
        //The list has the correct size.
        assertEquals(3, sudokuCell.getPossibleValues().length);
        sudokuCell.removePossibleValue("2");
        //The size is reduced by 1 and the "2" is no longer in the list, (as there was only one "2" in there)
        String[] possibleValues2 = sudokuCell.getPossibleValues();
        assertEquals(2, possibleValues2.length);
        assertFalse(Arrays.asList(possibleValues2).contains("2"));
        //Try to remove a no longer available value should not fail or alter the list.
        sudokuCell.removePossibleValue("2");
        assertEquals(2, sudokuCell.getPossibleValues().length);
    }
    
    @Test
    public void testUpdate(){
        String[] possibleValues = new String[]{"1", "2", "3"};
        SudokuCell sudokuCell = new SudokuCell(possibleValues);
        sudokuCell.removePossibleValue("2");
        assertEquals("", sudokuCell.toString());
        sudokuCell.update();
        assertEquals("", sudokuCell.toString());
        sudokuCell.removePossibleValue("1");
        assertEquals("", sudokuCell.toString());
        //Only one value is left, which leads to setting "3" after calling update.
        sudokuCell.update();
        assertEquals("3", sudokuCell.toString());
        assertEquals(true, sudokuCell.isSet());
    }

    @Test
    public void testToString(){
        String[] possibleValues = new String[]{"1", "2", "3"};
        SudokuCell sudokuCell = new SudokuCell(possibleValues);
        assertEquals("", sudokuCell.toString());   
    }

    @Test
    public void testLimitPossibleValues(){
        String[] possibleValues = new String[]{"1", "2", "3"};
        SudokuCell sudokuCell = new SudokuCell(possibleValues);
        ArrayList<String> combination = new ArrayList();
        combination.add("4");
        combination.add("1");
        sudokuCell.limitPossibleValues(combination);
        assertEquals(1, sudokuCell.getPossibleValues().length);
        sudokuCell.update();
        assertEquals("1", sudokuCell.toString());

    }

    @Test
    public void testRemovePossibleValues(){
        String[] possibleValues = new String[]{"1", "2", "3"};
        SudokuCell sudokuCell = new SudokuCell(possibleValues);
        ArrayList<String> combination = new ArrayList();
        combination.add("2");
        combination.add("3");
        combination.add("4");
        sudokuCell.removePossibleValues(combination);
        assertEquals(1, sudokuCell.getPossibleValues().length);
        sudokuCell.update();
        assertEquals("1", sudokuCell.toString());
    }
}
