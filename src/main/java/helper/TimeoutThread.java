package helper;
import java.lang.Thread;
import java.lang.InterruptedException;

public class TimeoutThread extends Thread{
    private int timeoutMax;
    private int timeout;

    public TimeoutThread(int timeoutMax){
        this.timeoutMax = timeoutMax;
        this.timeout = timeoutMax;
    }
    
    public void run(){
        try{
            boolean running = true;
            while (running){
                this.timeout -= 1;
                if(this.timeout < 0){
                    break;
                }
                running = this.execute();
                this.sleep(100);
            }
        }
        catch (InterruptedException e){
            System.out.println(e.toString());
        }
    }

    /*
     * A single loop cycle inside the thread run method. On returning false 
     * the thread will stop.
     */
    public boolean execute(){
        return false;
    }

    /*
     *   The timeout can be reset to make the thread only run out of time, when
     *   no more change occurs for the timeout.
     */
    public void resetTimeout(){
        this.timeout = this.timeoutMax;
    }
}
