package server.dao;

import CommonClasses.AuctionEntity;
import CommonClasses.AuctionItem;
import CommonClasses.AuctionStatus;
import CommonClasses.BidRecord;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TestAuctionDAO {
    public static void main(String[] args) {
        AuctionDAO auctionDAO = new AuctionDAO();

        try {
            // TEST 1: Lấy danh sách phiên đấu giá (getAllAuctions)
            System.out.println("--- Đang lấy danh sách đấu giá từ DB ---");
            List<AuctionEntity> list = auctionDAO.getAllAuctions();

            if (list.isEmpty()) {
                System.out.println("⚠ Database chưa có dữ liệu hoặc bạn chưa INSERT vào bảng items và auctions.");
            } else {
                for (AuctionEntity a : list) {
                    System.out.println("✅ " + a.toString());
                    System.out.println("   > Sản phẩm: " + a.getItem().getName() + " - Loại: " + a.getItem().getCategory());
                }
            }

            // TEST 2: Thử cập nhật giá thầu (updateBid)
            // Lưu ý: Thay "AUC001" và "user_id_co_that" bằng dữ liệu trong máy bạn
            System.out.println("\n--- Đang test đặt giá mới ---");
            String testAuctionId = "AUC001"; // ID mẫu
            String testBidderId = "78bbebeb";    // ID người dùng mẫu
            double testPrice = 1500.0;

            boolean isUpdated = auctionDAO.updateBid(testAuctionId, testBidderId, testPrice);

            if (isUpdated) {
                System.out.println("✅ Đặt giá thành công cho phiên: " + testAuctionId);
            } else {
                System.out.println("❌ Đặt giá thất bại (có thể do sai ID, sai trạng thái, hoặc giá mới thấp hơn giá cũ).");
            }

            //bidRecord
            BidDAO bidDAO = new BidDAO();
            String testBidID ="BID001";
            LocalDateTime time = LocalDateTime.now();
            BidRecord record = new BidRecord(testBidID,testAuctionId,testBidderId,testPrice, time);
            boolean res = bidDAO.addBid(record);
            if (res){
                System.out.println("Cập nhật lịch sử thành công");
            }
            else {
                System.out.println("LỖI");
            }



        } catch (SQLException e) {
            System.err.println("🚨 Lỗi SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}