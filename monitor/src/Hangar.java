import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Hangar{
    private ReentrantLock lock = new ReentrantLock();
    private Condition firstProducer = lock.newCondition();
    private Condition firstConsumer = lock.newCondition();

    private ReentrantLock prod = new ReentrantLock();
    private ReentrantLock cons = new ReentrantLock();

    private int inStock = 0;
    private int capacity;

    public Hangar(int size) {
        this.capacity = 2 * size;
    }

    public void put(int quantity){
        prod.lock();
        lock.lock();

        while (capacity - inStock < quantity){
            // System.out.println("Producer: " + Thread.currentThread().getId() + " waiting as first.");
            try {
                firstProducer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // System.out.println("Producer: " + Thread.currentThread().getId() + " putting: " + quantity);

        inStock += quantity;
        firstConsumer.signal();

        lock.unlock();
        prod.unlock();
    }

    public void get(int quantity){
        cons.lock();
        lock.lock();

        while (inStock < quantity){
            // System.out.println("Consumer: " + Thread.currentThread().getId() + " waiting as first.");
            try {
                firstConsumer.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // System.out.println("Consumer: " + Thread.currentThread().getId() + " getting: " + quantity);

        inStock -= quantity;
        firstProducer.signal();

        lock.unlock();
        cons.unlock();
    }
}

