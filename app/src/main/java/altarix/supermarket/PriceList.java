package altarix.supermarket;

public class PriceList {

    long id;
    String name,cost;
    int type,count;

    public PriceList(long id, String name, String cost, int type, int count) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.type = type;
        this.count = count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCost() {
        return cost;
    }

    public int getType() {
        return type;
    }

    public int getCount() {
        return count;
    }
}
