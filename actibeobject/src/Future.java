class Future<V> {
    private volatile boolean done = false;
    private volatile V result = null;

    public void setDone(V result){
        this.result = result;
        done = true;
    }

    public V get(){
        return result;
    }

    public boolean isDone() {
        return done;
    }
}
