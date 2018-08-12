public class MethodPut implements IMethodRequest{
    private Future<Boolean> future;
    private Hangar hangar;
    private int quantity;

    public MethodPut(Future<Boolean> future, Hangar hangar, int quantity){
        this.future = future;
        this.hangar = hangar;
        this.quantity = quantity;
    }

    @Override
    public boolean guard() { return hangar.getFreeSpace() >= quantity; }

    @Override
    public void call() {
        hangar.put(quantity);
        future.setDone(true);
    }
}
