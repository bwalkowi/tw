import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Scheduler implements Runnable{
    private Queue<IMethodRequest> everybody = new LinkedList<>();
    private Queue<IMethodRequest> producers = new LinkedList<>();
    private Queue<IMethodRequest> consumers = new LinkedList<>();

    private Lock lock = new ReentrantLock();
    private Condition every = lock.newCondition();

    private Lock prods = new ReentrantLock();
    private Condition prod = prods.newCondition();

    private Lock cons = new ReentrantLock();
    private Condition con = cons.newCondition();

    public void enqueue_prod(IMethodRequest request){
        prods.lock();
        lock.lock();

        producers.add(request);
        everybody.add(request);

        prod.signal();
        every.signal();

        lock.unlock();
        prods.unlock();
    }

    public void enqueue_cons(IMethodRequest request){
        cons.lock();
        lock.lock();

        consumers.add(request);
        everybody.add(request);

        con.signal();
        every.signal();

        lock.unlock();
        cons.unlock();
    }

    private IMethodRequest dequeue(){
        IMethodRequest request;
        lock.lock();
        while((request = everybody.poll()) == null)
            try {
                every.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        lock.unlock();
        return request;
    }

    private void poll_prod(){
        prods.lock();
        producers.poll();
        prods.unlock();
    }

    private void poll_cons(){
        cons.lock();
        consumers.poll();
        cons.unlock();
    }

    private IMethodRequest take_cons(){
        IMethodRequest request;
        cons.lock();
        while((request = consumers.poll()) == null)
            try {
                con.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        cons.unlock();
        return request;
    }

    private IMethodRequest take_prod(){
        IMethodRequest request;
        prods.lock();
        while((request = producers.poll()) == null)
            try {
                prod.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        prods.unlock();
        return request;
    }

    @Override
    public void run() {
        IMethodRequest request;

        while(!Thread.interrupted()) {
            request = dequeue();

            if(request instanceof MethodGet) {
                if (consumers.peek() != request)
                    continue;
                else {
                    poll_cons();
                    while (!request.guard())
                        take_prod().call();
                }
            }
            else{
                if (producers.peek() != request)
                    continue;
                else{
                    poll_prod();
                    while (!request.guard())
                        take_cons().call();
                }
            }

            request.call();
        }
    }
}




/*
public class Scheduler implements Runnable {
    private Queue<IMethodRequest> actionQueue = new ConcurrentLinkedQueue<>();
    private Queue<IMethodRequest> producers = new ConcurrentLinkedQueue<>();
    private Queue<IMethodRequest> consumers = new ConcurrentLinkedQueue<>();

    private Lock lock = new ReentrantLock();
    private Condition cond = lock.newCondition();

    public void enqueue(IMethodRequest request) {
        lock.lock();

        actionQueue.add(request);
        if (request instanceof MethodGet)
            consumers.add(request);
        else if (request instanceof MethodPut)
            producers.add(request);
        else
            System.out.println("??\n");
        cond.signal();

        lock.unlock();
    }


    @Override
    public void run() {
        IMethodRequest request;
        IMethodRequest ogorkowa;
        Queue<IMethodRequest> pomidorowa = producers;

        while (!Thread.interrupted()) {
            lock.lock();

            while ((request = actionQueue.poll()) == null)
                try {
                    cond.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            lock.unlock();

            if (request instanceof MethodGet) {
                pomidorowa = producers;
                if (consumers.peek() != request)
                    continue;
                else
                    consumers.poll();
            } else if (request instanceof MethodPut) {
                pomidorowa = consumers;
                if (producers.peek() != request)
                    continue;
                else
                    producers.poll();
            }

            while (!request.guard()) {
                lock.lock();
                while ((ogorkowa = pomidorowa.poll()) == null)
                    try {
                        cond.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                lock.unlock();
                ogorkowa.call();
            }

            request.call();
        }
    }
}
*/