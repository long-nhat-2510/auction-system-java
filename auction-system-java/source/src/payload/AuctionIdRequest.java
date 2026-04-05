package payload;

import java.io.Serializable;

public class AuctionIdRequest implements Serializable {
    private int auctionId;
    public AuctionIdRequest() {};

    public AuctionIdRequest(int auctionId) {
        this.auctionId = auctionId;
    }

    public int getAuctionId() {
        return auctionId;
    }
}