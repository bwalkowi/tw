import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;


public class Main {

    public static void main(String[] args) {
        int max_tasks = 100;
        int capacity = 100;
        Random rand;

        int max_threads = 100;
        Thread[] producers;
        Thread[] consumers;
        gatekeeper gk;
        Thread sched;
        Scheduler scheduler;
        Proxy proxy;

        for(int threads = 1; threads <= max_threads; threads *= 5) {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter("Thread" + threads, "UTF-8");
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            for (int tasks = 1; tasks <= max_tasks; ++tasks) {
                rand = new Random(50);
                producers = new Thread[threads];
                consumers = new Thread[threads];
                gk = new gatekeeper(threads);
                scheduler = new Scheduler();
                sched = new Thread(scheduler);
                sched.start();
                proxy = new Proxy(new Hangar(capacity), scheduler);

                for (int i = 0; i < threads; ++i) {
                    producers[i] = new Thread(new Producer(i, proxy, tasks, rand.nextInt(capacity - 1) + 1, gk));
                    consumers[i] = new Thread(new Consumer(i, proxy, tasks, rand.nextInt(capacity - 1) + 1, gk));
                    producers[i].start();
                    consumers[i].start();
                }
                gk.get();
                for (int i = 0; i < threads; ++i) {
                    producers[i].stop();
                    consumers[i].stop();
                }
                sched.stop();
                long time1 = 0;
                long time2 = 0;
                long task1 = 0;
                long task2 = 0;
                for(int i = 0; i < threads; ++i){
                    time1 += gk.times1[i];
                    time2 += gk.times2[i];
                    task1 += gk.tasks1[i];
                    task2 += gk.tasks2[i];
                }
                writer.println(tasks + ";" + time1 / threads + ";" + task1/threads + ";" + time2 / threads + ";" + task2/threads);
            }
            writer.close();
        }
    }
}
