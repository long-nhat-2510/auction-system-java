package payload;

import CommonClasses.AuctionEntity;

import java.io.Serializable;
import java.util.List;

public class AuctionListResponse implements Serializable {
    private List<AuctionEntity> auctions;

    public AuctionListResponse(List<AuctionEntity> auctions) {
        this.auctions = auctions;
    }

    public List<AuctionEntity> getAuctions() {
        return auctions;
    }
}