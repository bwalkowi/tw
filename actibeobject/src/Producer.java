public class Producer implements Runnable{
    private int id;
    private Proxy proxy;
    private int tasks;
    private int quantity;

    private int doSth_done = 0;
    private int tasks_done = 0;

    private gatekeeper gk;

    public Producer(int id, Proxy proxy, int tasks, int quantity, gatekeeper gk){
        this.id = id;
        this.proxy = proxy;
        this.tasks = tasks;
        this.quantity = quantity;
        this.gk = gk;
    }

    private boolean doSth(int num){
        ++doSth_done;
        if(num == 2 || num == 3)
            return true;
        if(num % 2 == 0 || num % 3 == 0 || num < 5)
            return false;
        for(int i = 5; i < Math.sqrt(num); i += 6)
            if(num % i == 0 || num % (i+2) == 0)
                return false;
        return true;
    }

    @Override
    public void run() {
        long start = System.nanoTime();
        Future<Boolean> future;
        while (!Thread.interrupted()) {
            if(tasks_done == tasks){
                gk.put1(id, System.nanoTime() - start, doSth_done);
            }
            ++tasks_done;
            future = proxy.put(quantity);
            while(!future.isDone())
                doSth(32452843);

            // if(future.get())
                // System.out.println("Producer: " + id + " putted " + quantity);
        }
    }
}
