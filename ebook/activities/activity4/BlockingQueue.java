// Implementation of a subset of the java.util.concurrent.BlockingQueue interface 
public class BlockingQueue {

    // queue and dequeue string data -- not objects -- makes it easier to read
    private String [] queue;

    // queue metadata  
    private int limit = 10;
    private int head = 0;
    private int qlen = 0;

    // Create an array of strings as the queue
    public BlockingQueue(int limit){
        this.limit = limit;
        this.queue = new String [limit];
    }

    public synchronized void put(String item)
    throws InterruptedException  {
    
        // variable for slot item goes in
        int slot;

        // wait and don't add if the queue is full
        while (qlen == limit) {
            wait();
        }

        // get slot and update head and length
        slot = (head + qlen) % limit;
        qlen++;
        // head was not updated here because put() is for adding items to the queue

        // notify takers if this is the first item in queue
        if (qlen == 1) {
            notifyAll();
        }

        // add the item
        this.queue[slot] = item;
    }

    public synchronized String take()
    throws InterruptedException {

        // slot to be taken and deleted
        int slot;

        // don't take from an empty queue
        while (qlen == 0) {
            wait();
        }

        //get slot 
        slot = head;
        head = (head + 1) % limit; 
        // head is updated here because take() is for removing items from the queue
        qlen--;

        // if taking from a full queue, notify putters
        if (qlen == limit - 1) {
            notifyAll();
        }

        // take the item and dereference pointer for garbage collection
        String ret_obj = this.queue[slot];
        queue[slot]=null;

        // return item
        return ret_obj;
    }
}
