package payload.response;

import CommonClasses.AuctionEntity;
import CommonClasses.BidRecord;

import java.io.Serializable;
import java.util.List;

public class AuctionDataResponse implements Serializable {
    private AuctionEntity auction;
    private List<BidRecord> bidHistory;

    public AuctionDataResponse(AuctionEntity auction, List<BidRecord> bidHistory) {
        this.auction = auction;
        this.bidHistory = bidHistory;
    }

    public AuctionEntity getAuction() {
        return auction;
    }

    public List<BidRecord> getBidHistory() {
        return bidHistory;
    }
}