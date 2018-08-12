class Proxy {
    private Hangar hangar;
    private Scheduler scheduler;

    Proxy(Hangar hangar, Scheduler scheduler){
        this.hangar = hangar;
        this.scheduler = scheduler;
    }

    Future<Boolean> put (int quantity) {
        Future<Boolean> future = new Future<>();
        scheduler.enqueue_prod(new MethodPut(future, hangar, quantity));
        return future;
    }

    Future<Integer> get (int quantity){
        Future<Integer> future = new Future<>();
        scheduler.enqueue_cons(new MethodGet(future, hangar, quantity));
        return future;
    }
}
