package CommonClasses;

import java.io.Serializable;

public class AuctionItem implements Serializable {
    private String itemId;
    private String name;
    private String description;
    private String owner;
    private String category;
    private double estimatedPrice;

    public AuctionItem() {}

    public AuctionItem(String itemId, String name, String description,
                       String owner, String category, double estimatedPrice) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.category = category;
        this.estimatedPrice = estimatedPrice;
    }

    // Getter & Setter
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getEstimatedPrice() { return estimatedPrice; }
    public void setEstimatedPrice(double estimatedPrice) { this.estimatedPrice = estimatedPrice; }

    @Override
    public String toString() {
        return name + " (" + category + ") - owner: " + owner;
    }
}