public class MethodGet implements IMethodRequest{
    private Future<Integer> future;
    private Hangar hangar;
    private int quantity;

    public MethodGet(Future<Integer> future, Hangar hangar, int quantity){
        this.future = future;
        this.hangar = hangar;
        this.quantity = quantity;
    }

    @Override
    public boolean guard() { return hangar.getInStock() >= quantity; }

    @Override
    public void call() {
        future.setDone(hangar.get(quantity));
    }
}
