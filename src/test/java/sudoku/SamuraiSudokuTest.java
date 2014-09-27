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

    @Test
    public void testSolve1(){
        String initialConfig = "010064357401000020720059004670023090046000000900700600074000003709200000050047010012040070091506700000970084460910000004000600005030070000000000090002082600000800000008037600901400000000000007203008160300000006000007680900010000000000080050800005000400000036009210079000003601200070060140020750040000003602500000030001004007000000300020580063230960014080000900456300000";
        String expConfig="819264357451869723723159684678523491546783192923714658674891523749285316258347916812346579391526748365971284467918235694187632945135472869712534197862982635471835296458137682971453943568721157243968165342798156342597681937816524387619284753842795316429875136429216479835783641295573268149921753846498153672564829137651934287198475362729581463237968514384627951456312978"; 
        SamuraiSudoku samuraisudoku = new SamuraiSudoku(initialConfig);
        samuraisudoku.solve();
        assertEquals(expConfig, samuraisudoku.toConfig());
   
    }
}
