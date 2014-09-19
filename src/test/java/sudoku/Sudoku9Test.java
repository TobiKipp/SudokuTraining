package sudoku;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.apache.commons.lang3.StringUtils;
import sudoku.Sudoku9;

public class Sudoku9Test{

    @Test
    public void testConstructor(){
        Sudoku9 sudoku9 = new Sudoku9();
        String[][] field = sudoku9.getField();
        //The field should be 9x9 so check if the length of all arrays is correct.
        assertEquals(9, field.length);
        for (int i = 0; i < 9; i++){
            assertEquals(9, field[i].length);
        }
        //Take the first cell and a randomly choosen cell to check if the empty string is in them for undefined value
        String cell0 = field[0][0];
        assertEquals("", cell0);
        String cellr = field[4][7];
        assertEquals("", cellr);
    }
    
    @Test
    public void testLoadConfigConstructor(){
        Sudoku9 sudoku9 = new Sudoku9("123456789abcdefghi1--   2");
        String[][] field = sudoku9.getField();
        for (int i = 0; i < 9; i++){
            assertEquals(String.valueOf(i+1), field[0][i]);
            assertEquals("", field[1][i]);
        }
        assertEquals("1", field[2][0]);
        assertEquals("", field[2][1]);
        assertEquals("", field[2][5]);
        assertEquals("2", field[2][6]);
        assertEquals("", field[2][7]);
        //Each value not covered by the configuration should be the empty string
        for (int y = 3 ; y < 9; y++){
            for(int x = 0; x < 9; x++){
                assertEquals("", field[y][x]);
            }
        }
    }
    
    @Test
    public void testSolveConstructor(){
        String config = "796003052002080137010050640000000085000591000560000000049010020378060900120400768";
        
        Sudoku9 sudoku9 = new Sudoku9(config, "solve");

        String[][] field = sudoku9.getField();
        assertEquals("6", field[6][0]);
        String solution = "796143852452986137813752649931674285284591376567328491649817523378265914125439768";
        assertEquals(solution, sudoku9.toConfig());
    }

    @Test 
    public void testLoadConfig(){
        Sudoku9 sudoku9 = new Sudoku9();
        sudoku9.loadConfig("987");
        String[][] field = sudoku9.getField();
        assertEquals("9", field[0][0]);
        assertEquals("8", field[0][1]);
        assertEquals("7", field[0][2]);
        assertEquals("", field[8][8]);
        //Load a longer than number of cells configuration.
        String longConfig = StringUtils.repeat("123456789",20);
        sudoku9.loadConfig(longConfig);
        field = sudoku9.getField();
        assertEquals("1", field[0][0]);
        assertEquals("2", field[5][1]);
        assertEquals("7", field[3][6]);
        assertEquals("9", field[8][8]);
    }

    @Test
    public void testIndexToXY(){
        Sudoku9 sudoku9 = new Sudoku9();
        int[] xy = sudoku9.indexToXY(0);
        int[] expectedxy = new int[] {0,0};
        assertArrayEquals(expectedxy, xy);
        xy = sudoku9.indexToXY(9);
        expectedxy = new int[] {0,1};
        assertArrayEquals(expectedxy, xy);
        xy = sudoku9.indexToXY(80);
        expectedxy = new int[] {8,8};
        assertArrayEquals(expectedxy, xy);
        //the method works outside of the array limits
        xy = sudoku9.indexToXY(90);
        expectedxy = new int[] {0,10};
        assertArrayEquals(expectedxy, xy);

    }

    @Test
    public void testToConfig(){
       String initialConfig = "123456789uffkjsn-sf-sfs-dbdfjh!!$fsdjfskdnf";
       //only the first 9 characters are defined, so everything after that should be 0 when toConfig() is used.
       String expConfig = "123456789" + StringUtils.repeat("0", 72);
       Sudoku9 sudoku9 = new Sudoku9(initialConfig);
       assertEquals(expConfig, sudoku9.toConfig());
    }
}
