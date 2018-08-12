public class Consumer implements Runnable {
    private Hangar han;
    private int tasks;
    private int quantity;
    private int id;
    private int tasks_done = 0;

    private gatekeeper gk;

    public Consumer(int id, Hangar han, int tasks, int quantity, gatekeeper gk){
        this.id = id;
        this.han = han;
        this.tasks = tasks;
        this.quantity = quantity;
        this.gk = gk;
    }


    public void run(){
        long start = System.nanoTime();
        while(!Thread.interrupted()) {
            if (tasks_done == tasks) {
                gk.put2(id, System.nanoTime() - start);
            }
            ++tasks_done;
            han.get(quantity);
        }
    }
}
