public class gatekeeper {
    public long[] times1;
    public int[] tasks1;
    public long[] times2;
    public int[] tasks2;
    private int threads;


    public gatekeeper(int threads){
        this.threads = 2*threads;
        times1 = new long[threads];
        tasks1 = new int[threads];
        times2 = new long[threads];
        tasks2 = new int[threads];
    }


    public synchronized void put1(int id, long time, int task){
        times1[id] = time;
        tasks1[id] = task;
        --threads;
        if(threads == 0)
            notify();
    }

    public synchronized void put2(int id, long time, int task){
        times2[id] = time;
        tasks2[id] = task;
        --threads;
        if(threads == 0)
            notify();
    }

    public synchronized void get(){
        while(threads != 0)
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}
