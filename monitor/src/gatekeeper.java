public class gatekeeper {
    public long[] times1;
    public long[] times2;
    private int threads;


    public gatekeeper(int threads){
        this.threads = 2*threads;
        times1 = new long[threads];
        times2 = new long[threads];
    }

    public synchronized void put1(int id, long time){
        times1[id] = time;
        --threads;
        if(threads == 0)
            notify();
    }

    public synchronized void put2(int id, long time){
        times2[id] = time;
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
