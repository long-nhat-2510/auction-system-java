package server;

import CommonClasses.AuctionEntity;
import packets.NetworkMessage;
import packets.RequestType;
import payload.WinnerNotification;
import payload.NoWinnerNotification;

public class AuctionFinalizer {

    public static void finalizeAuction(AuctionEntity auction) {
        System.out.println("\n🛑 [Hệ thống] Phiên đấu giá ID " + auction.getAuctionId() + " (" + auction.getItem().getName() + ") đã kết thúc!");

        String winner = auction.getHighestBidder();

        if (winner != null && !winner.isEmpty()) {

            System.out.println("🏆 Người chiến thắng: " + winner + " với mức giá " + auction.getCurrentPrice());

            WinnerNotification winnerPayload = new WinnerNotification(
                    auction.getAuctionId(),
                    winner,
                    auction.getCurrentPrice()
            );
            NetworkMessage msg = new NetworkMessage(RequestType.WINNER_NOTIFICATION, winnerPayload);
            AuctionServer.broadcast(msg);

        } else {

            System.out.println("😔 Không có ai ra giá cho vật phẩm: " + auction.getItem().getName());

            NoWinnerNotification noWinnerPayload = new NoWinnerNotification(auction.getAuctionId());
            NetworkMessage msg = new NetworkMessage(RequestType.NO_WINNER_NOTIFICATION, noWinnerPayload);
            AuctionServer.broadcast(msg);

        }

        // Xóa phiên đấu giá đã hoàn tất khỏi ServerLauncher để giải phóng bộ nhớ
        AuctionServer.auctions.remove(auction.getAuctionId());
    }
}