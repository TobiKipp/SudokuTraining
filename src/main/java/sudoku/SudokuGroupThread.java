package sudoku;
import helper.TimeoutThread;
import sudoku.SudokuCell;
import java.lang.InterruptedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SudokuGroupThread extends TimeoutThread{
    private ArrayList<SudokuCell> sudokuCells;
    private int maxClusterSize;

    public SudokuGroupThread(ArrayList<SudokuCell> sudokuCells, int timeoutMax){
        super(timeoutMax);
        this.sudokuCells = (ArrayList<SudokuCell>) sudokuCells.clone();
        this.maxClusterSize = 4;
    }

    public HashMap<String, Integer> getStatistic(){
        HashMap<String, Integer> possibleValueCounters = new HashMap<String, Integer>();
        for (SudokuCell sudokuCell: this.sudokuCells.toArray(new SudokuCell[this.sudokuCells.size()])){
            String[] possibleValues = sudokuCell.getPossibleValues();
            for (String value: possibleValues){
                int oldCounter = 0;
                if (possibleValueCounters.containsKey(value)){
                    oldCounter = possibleValueCounters.get(value);
                }
                possibleValueCounters.put(value, oldCounter + 1);
            }
        
        }
        return possibleValueCounters;
    }

    public SudokuCell[] getSudokuCells(){
        return sudokuCells.toArray(new SudokuCell[this.sudokuCells.size()]);
    }

    public SudokuCell[] getCellsWithOneOf(String[] clusterValues){
        ArrayList<SudokuCell> matchingSudokuCells = new ArrayList<SudokuCell>();
        for (SudokuCell sudokuCell: this.sudokuCells.toArray(new SudokuCell[this.sudokuCells.size()])){
            String[] possibleValues = sudokuCell.getPossibleValues();    
            for (String possibleValue: possibleValues){
                if(Arrays.asList(clusterValues).contains(possibleValue)){
                    matchingSudokuCells.add(sudokuCell);
                    break;
                }
            }
        }
        return matchingSudokuCells.toArray(new SudokuCell[matchingSudokuCells.size()]);
    }
    
    public int getFirstSetCellIndex(){
        int cellIndex = -1;
        for(int i = 0; i < this.sudokuCells.size(); i++){
            if (this.sudokuCells.get(i).isSet()){
                cellIndex = i;
                break;
            }
        }
        return cellIndex;
    }

    public void handleSetCell(int cellIndex){
        this.resetTimeout();
        SudokuCell sudokuCell = this.sudokuCells.get(cellIndex);
        String value = sudokuCell.toString();
        //remove its value from the possibles of the others
        for(int i = 0; i < this.sudokuCells.size(); i++){
            if(i != cellIndex){
                this.sudokuCells.get(i).removePossibleValue(value);   
            }
        }
        //The cell has no more valuable information and is removed
        this.sudokuCells.remove(sudokuCell);
    }

    /*
     * allCombinations is like a return value
     */
    public void generateAllNElementCombinations(int n, ArrayList<String> combination, ArrayList<String> valuesLeft,
                                                ArrayList<String[]> allCombinations){
        if(n==1){
            for (Iterator<String> valuesLeftIterator = valuesLeft.iterator(); valuesLeftIterator.hasNext();){
                 ArrayList<String> finalCombination = (ArrayList<String>) combination.clone();
                 finalCombination.add(valuesLeftIterator.next());
                 allCombinations.add(finalCombination.toArray(new String[finalCombination.size()]));
            }
        }
        else{
            ArrayList<String> valuesLeftWithoutLastN = new ArrayList<String>(
                                                           valuesLeft.subList(0, valuesLeft.size() - n + 1));
            
            //The valuesLeftCopy will hagve the currently selected element removed in each loop, so it will
            //get smaller with each loop and prevent duplicated combinations.
            ArrayList<String> valuesLeftCopy = (ArrayList<String>) valuesLeft.clone();
            for (Iterator<String> valuesLeftIterator = valuesLeftWithoutLastN.iterator();
                    valuesLeftIterator.hasNext();){
                 ArrayList<String> interCombination = (ArrayList<String>) combination.clone();
                 String value = valuesLeftIterator.next();
                 interCombination.add(value);
                 valuesLeftCopy.remove(value);
                 this.generateAllNElementCombinations(n-1, interCombination, valuesLeftCopy, 
                                                 allCombinations);
            }
        }
    }

    public void findExactlyNCells(ArrayList<String[]> allCombinations, int n,
                                          ArrayList<SudokuCell> matchCells, ArrayList<String> combination){
        for(int i = 0 ; i < allCombinations.size(); i++){
            String[] combi = allCombinations.get(i);
            SudokuCell[] potentialCells = this.getCellsWithOneOf(combi);
            if (potentialCells.length == n){
                for(String val: combi){
                    combination.add(val);
                }
                for(SudokuCell cell: potentialCells){
                    matchCells.add(cell);
                }
                break;
            }
        }
    }

    public boolean eliminateWithCluster(int clusterSize){
        boolean eliminated = false;
        //Search the statistic for count of clusterSize. 
        ArrayList<String> matchCountValues = new ArrayList<String>();
        HashMap<String, Integer> possibleValueCounters = this.getStatistic(); 
        String[] values = possibleValueCounters.keySet().toArray(new String[possibleValueCounters.size()]);
        for (String value: values){
            if (possibleValueCounters.get(value) == clusterSize){
                matchCountValues.add(value);
            }
        }
        //If there are at least clusterSize values with this count then test all combinations of selecting 
        //clusterSize elements from them. Stop early when a match is found.
        if (matchCountValues.size() >= clusterSize){
           ArrayList<String[]> allCombinations = new ArrayList<String[]>();
           ArrayList<String> combination = new ArrayList<String>();
           this.generateAllNElementCombinations(clusterSize, combination, matchCountValues, allCombinations);
           ArrayList<SudokuCell> matchCells = new ArrayList<SudokuCell>();
           ArrayList<String> matchCombination = new ArrayList<String>();
           this.findExactlyNCells(allCombinations, clusterSize, matchCells, matchCombination);
           if(matchCells.size() != 0){
               for(SudokuCell sudokuCell: this.sudokuCells.toArray(new SudokuCell[this.sudokuCells.size()])){
                   if(matchCells.contains(sudokuCell)){
                       sudokuCell.limitPossibleValues(matchCombination);
                   }
                   else{
                       sudokuCell.removePossibleValues(matchCombination);
                   }
               }
               eliminated = true;
           }
        }
    return eliminated;
    }

    public void handlePotentialCluster(){
        for (int clusterSize = 1; clusterSize < this.maxClusterSize; clusterSize++){
            boolean foundPair = this.eliminateWithCluster(clusterSize);
            if (foundPair) break;
        }
    }



        


    public boolean execute(){
        int cellIndex = getFirstSetCellIndex();
        if (cellIndex != -1) this.handleSetCell(cellIndex);
        else this.handlePotentialCluster(); 
        boolean running = (this.sudokuCells.size() != 0);
        return running;
    }
}
