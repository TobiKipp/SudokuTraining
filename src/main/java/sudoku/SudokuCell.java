package sudoku;
import java.util.Iterator;
import java.util.ArrayList;
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
        //System.out.print("UPDATE" + this.value);
        //for (Object val: this.possibleValues.toArray()){
        //    System.out.print(val);
        //}
        //System.out.println("");
        if (this.possibleValues.size() == 1 && this.value.equals("")){
            this.value = this.possibleValues.peek();

        }
    }

    public String toString(){
        return this.value;
    }

    public void limitPossibleValues(ArrayList<String> combination){
        Iterator<String> possibleValuesIterator = this.possibleValues.iterator();
        while(possibleValuesIterator.hasNext()){
            String value = possibleValuesIterator.next();
            if(!combination.contains(value)){
                possibleValuesIterator.remove(); 
            }
        }
    }

    public void removePossibleValues(ArrayList<String> combination){
        for(int i=0; i < combination.size(); i++){
            String value = combination.get(i);
            this.removePossibleValue(value);
        }
    }
}
