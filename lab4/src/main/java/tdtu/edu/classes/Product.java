package tdtu.edu.classes;

public class Product {
    private String id;
    private String name;
    private int price;

    public Product() {
        super();
    }

    public Product(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String toString() {
        return "["+id+", "+name+", "+price+"]";
    }
}