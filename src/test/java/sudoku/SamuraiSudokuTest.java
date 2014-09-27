package sudoku;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.apache.commons.lang3.StringUtils;
import sudoku.SamuraiSudoku;

public class SamuraiSudokuTest{


    @Test 
    public void testLoadConfig(){
        SamuraiSudoku samuraisudoku = new SamuraiSudoku();
        samuraisudoku.loadConfig("987");
        String[][] field = samuraisudoku.getField();
        assertEquals("9", field[0][0]);
        assertEquals("8", field[0][1]);
        assertEquals("7", field[0][2]);
        assertEquals("", field[8][8]);
        //Load a longer than number of cells configuration.
        String longConfig = StringUtils.repeat("123456789",100);
        samuraisudoku.loadConfig(longConfig);
        field = samuraisudoku.getField();
        assertEquals("1", field[0][0]);
        assertEquals("2", field[5][1]);
        assertEquals("7", field[3][6]);
        assertEquals("9", field[20][20]);
    }

    @Test
    public void testIndexToXY(){
        SamuraiSudoku samuraisudoku = new SamuraiSudoku();
        int[] xy = samuraisudoku.indexToXY(0);
        int[] expectedxy = new int[] {0, 0};
        assertArrayEquals(expectedxy, xy);
        int[] indices = new int[]{8, 9, 17, 18, 368};
        int[][] results = new int[][]{{8,0}, {12, 0}, {20,0}, {0,1}, {20,20}};

        for(int i = 0; i < 5; i++){
            xy = samuraisudoku.indexToXY(indices[i]);
            assertArrayEquals(results[i], xy);
        }
    }

    @Test
    public void testToConfig(){
       String initialConfig = "123456789uffkjsn-sf-sfs-dbdfjh!!$fsdjfskdnf";
       //only the first 9 characters are defined, so everything after that should be 0 when toConfig() is used.
       String expConfig = "123456789" + StringUtils.repeat("0", 360);
       SamuraiSudoku samuraisudoku = new SamuraiSudoku(initialConfig);
       assertEquals(expConfig, samuraisudoku.toConfig());
    }
}
