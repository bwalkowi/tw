public class Hangar {
    private int inStock = 0;
    private int capacity;

    public Hangar(int capacity) { this.capacity = 2 * capacity; }

    public int getFreeSpace(){ return capacity - inStock; }

    public int getInStock(){ return inStock; }

    public void put(int quantity){ inStock += quantity; }

    public int get(int quantity){
        inStock -= quantity;
        return  quantity;
    }
}
