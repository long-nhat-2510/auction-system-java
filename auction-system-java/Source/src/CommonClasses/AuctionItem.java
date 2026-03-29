package CommonClasses;

public class AuctionItem {
    private int id;
    private String name;
    private double currentPrice;

    public AuctionItem(int id, String name, double initialPrice){
        this.id = id;
        this.name = name;
        this.currentPrice = initialPrice;
    }

    public int getId() {
        return id;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice){
        this.currentPrice = currentPrice;
    }
}
