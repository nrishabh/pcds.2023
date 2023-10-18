
// The Consumer of Producer/Consumer
//   a thread that the will take items from the queue

public class Consumer implements Runnable{

    protected BlockingQueue queue = null;
    private int count;

    public Consumer(BlockingQueue queue, int count) {
        this.queue = queue;
        this.count = count;
    }

    public void run() {
        try {
    
            // Consume count items from the queue
            for (int i=0; i<this.count; i++) {

                String item = queue.take();

                // output if you're debugging 
                System.out.println("take " + item);

                // sleep can slow things down to make output understandable
//                 Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
