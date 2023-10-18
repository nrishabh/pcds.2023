
public class BlockingQueueExample {

    public static void main(String[] args) throws Exception {

        // Small queue for testing
        BlockingQueue queue = new BlockingQueue(4);

        // Bigger queue 
//        BlockingQueue queue = new BlockingQueue(1024);

        // Small example that creates one producer and one consumer
//          Producer producer = new Producer(queue, 4);
//          Consumer consumer = new Consumer(queue, 4);

        // medium example that wraps the queue
        Producer producer = new Producer(queue, 20);
        Consumer consumer = new Consumer(queue, 20);

        // big example 
//        Producer producer = new Producer(queue, 1000);
//        Consumer consumer = new Consumer(queue, 1000);

        // two threads is a minimal example
        new Thread(producer).start();
        new Thread(consumer).start();

        // more threads is more fun 
//        new Thread(producer).start();
//        new Thread(consumer).start();
//        new Thread(producer).start();
//        new Thread(consumer).start();
//        new Thread(producer).start();
//        new Thread(consumer).start();
//        new Thread(producer).start();
//        new Thread(consumer).start();
    }
}
