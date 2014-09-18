package sudoku;
import java.util.concurrent.ConcurrentLinkedQueue;

class SudokuCell{

    private ConcurrentLinkedQueue<String> possibleValues;
    private String value;

    public SudokuCell(){
        this.init();
    }

    public SudokuCell(String[] possibleValues){
        this.init();
        this.setPossibleValues(possibleValues);
    }

    public void init(){
        this.possibleValues = new ConcurrentLinkedQueue<String>();
        this.value = "";
    }

    public void setPossibleValues(String[] possibleValues){
        for (String value: possibleValues){
            this.possibleValues.add(value);
        }
    }

    public void setValue(String value){
        this.value = value;
    }

    public String[] getPossibleValues(){
        return this.possibleValues.toArray(new String[this.possibleValues.size()]);
    }

    public void removePossibleValue(String value){
        this.possibleValues.remove(value);
    }

    public boolean isSet(){
        return (!this.value.equals(""));
    }

    /*
     * Check if only one possible value is left and set it in that case.
     */
    public void update(){
        if (this.possibleValues.size() == 1){
            this.value = this.possibleValues.peek();
        }
    }

    public String toString(){
        return this.value;
    }
}
