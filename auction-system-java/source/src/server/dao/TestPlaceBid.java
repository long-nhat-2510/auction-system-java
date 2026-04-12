package server.dao;

import CommonClasses.BidRecord;
import java.time.LocalDateTime;

public class TestPlaceBid {
    public static void main(String[] args) {
        AuctionDAO auctionDAO = new AuctionDAO();

        // 1. Giả lập một bản ghi đặt giá mới
        // BID-ID: Tự sinh theo thời gian để không trùng
        String bidId = "BID-" + System.currentTimeMillis();
        String auctionId = "AUC001"; // ID phiên đấu giá trong DB của bạn
        String bidderId = "78bbebeb"; // ID user bạn đã tạo thành công
        double bidAmount = 2500.0;     // Chắc chắn số này > currentPrice trong DB

        BidRecord newBid = new BidRecord(
                bidId,
                auctionId,
                bidderId,
                bidAmount,
                LocalDateTime.now()
        );

        System.out.println("--- Đang thử nghiệm Transaction Đặt giá ---");

        // 2. Gọi hàm Double Update
        boolean success = auctionDAO.placeBidTransaction(newBid);

        if (success) {
            System.out.println("✅ THÀNH CÔNG rực rỡ!");
            System.out.println("1. Bảng auctions đã cập nhật giá mới: " + bidAmount);
            System.out.println("2. Bảng bids đã lưu lại lịch sử với mã: " + bidId);
        } else {
            System.out.println("❌ THẤT BẠI!");
            System.out.println("Lý do có thể: ");
            System.out.println("- Giá bạn đặt (" + bidAmount + ") không cao hơn giá hiện tại.");
            System.out.println("- Phiên đấu giá không ở trạng thái 'RUNNING'.");
            System.out.println("- Lỗi kết nối hoặc ID không tồn tại (Foreign Key fail).");
        }
    }
}