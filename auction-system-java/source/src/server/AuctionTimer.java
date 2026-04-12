package server;

import CommonClasses.AuctionEntity;
import CommonClasses.AuctionStatus;
import packets.NetworkMessage;
import packets.RequestType;
import payload.response.CountdownTickEvent;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit; // Thêm import này
import java.util.Map;

public class AuctionTimer implements Runnable {

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000); // Ngủ 1 giây

                // Lấy 1 mốc thời gian duy nhất cho toàn bộ vòng lặp hiện tại
                LocalDateTime now = LocalDateTime.now();

                for (Map.Entry<String, AuctionEntity> entry : AuctionServer.auctions.entrySet()) {
                    AuctionEntity auction = entry.getValue();

                    if (auction.getStatus() == AuctionStatus.RUNNING) {

                        // 🔥 Cách chuẩn để tính số giây còn lại giữa 2 mốc LocalDateTime
                        long secondsLeft = ChronoUnit.SECONDS.between(now, auction.getEndTime());

                        if (secondsLeft <= 0) {
                            // Hết giờ -> Chốt đơn
                            auction.closeAuction();
                            AuctionFinalizer.finalizeAuction(auction);
                        } else {
                            // CHƯA HẾT GIỜ -> GỬI THÔNG BÁO ĐẾM NGƯỢC

                            // Ép kiểu về int vì CountdownTickEvent có vẻ đang nhận tham số int
                            System.out.println("DEBUG: Server đang đếm " + secondsLeft + "s");

                            CountdownTickEvent tickEvent = new CountdownTickEvent(auction.getAuctionId(), (int) secondsLeft);
                            NetworkMessage msg = new NetworkMessage(RequestType.COUNTDOWN_TICK_EVENT, tickEvent);
                            AuctionServer.broadcast(msg);
                        }
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("AuctionTimer thread interrupted.");
                break;
            }
        }
    }
}