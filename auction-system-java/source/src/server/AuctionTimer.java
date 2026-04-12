package server;

import CommonClasses.AuctionEntity;
import packets.NetworkMessage;
import packets.RequestType;
import payload.response.CountdownTickEvent;

import java.util.Map;

public class AuctionTimer implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Ngủ 1 giây
                long currentTime = System.currentTimeMillis();

                for (Map.Entry<String, AuctionEntity> entry : AuctionServer.auctions.entrySet()) {
                    AuctionEntity auction = entry.getValue();

                    if (auction.isActive()) {
                        long timeLeft = auction.getEndTime() - currentTime;

                        if (timeLeft <= 0) {
                            // Hết giờ -> Chốt đơn
                            auction.closeAuction();
                            AuctionFinalizer.finalizeAuction(auction);
                        } else {
                            // CHƯA HẾT GIỜ -> GỬI THÔNG BÁO ĐẾM NGƯỢC
                            int secondsLeft = (int) (timeLeft / 1000);


                            // CHÚ Ý: Đã xóa vòng lặp if để Server gửi thông báo đi MỖI GIÂY
                            System.out.println("DEBUG: Server đang đếm " + secondsLeft + "s");

                            CountdownTickEvent tickEvent = new CountdownTickEvent(auction.getAuctionId(), secondsLeft);
                            NetworkMessage msg = new NetworkMessage(RequestType.COUNTDOWN_TICK_EVENT, tickEvent);
                            AuctionServer.broadcast(msg);
                        }
                    }
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}