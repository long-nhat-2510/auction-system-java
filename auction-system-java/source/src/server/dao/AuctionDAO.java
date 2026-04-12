package server.dao;

import CommonClasses.BidRecord;
import server.database.DatabaseConnection;
import CommonClasses.AuctionEntity;
import CommonClasses.AuctionItem;
import CommonClasses.AuctionStatus;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuctionDAO {

    /**
     * Lấy danh sách toàn bộ các phiên đấu giá (bao gồm thông tin Item)
     */
    public List<AuctionEntity> getAllAuctions() throws SQLException {
        List<AuctionEntity> list = new ArrayList<>();
        // JOIN để lấy tên và mô tả sản phẩm từ bảng items
        String sql = "SELECT a.*, i.name, i.description, i.owner_id, i.category, i.estimate_price " +
                "FROM auctions a " + "JOIN items i ON a.item_id = i.item_id";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // 1. Tạo đối tượng AuctionItem trước
                AuctionItem item = new AuctionItem(
                        rs.getString("item_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("owner_id"),
                        rs.getString("category"),
                        rs.getDouble("estimate_price")
                );

                // 2. Tạo AuctionEntity và đổ dữ liệu từ ResultSet
                // Chú ý: Chuyển đổi Timestamp của MySQL sang LocalDateTime của Java
                AuctionEntity auction = new AuctionEntity(
                        rs.getString("auction_id"),
                        item,
                        rs.getDouble("startingPrice"),
                        rs.getTimestamp("startAt").toLocalDateTime(),
                        rs.getTimestamp("endAt").toLocalDateTime()
                );

                auction.setCurrentPrice(rs.getDouble("currentPrice"));
                auction.setHighestBidder(rs.getString("highestBidder_id"));

                // 3. Xử lý Enum: Chuyển String từ DB về Enum AuctionStatus
                // Dùng toUpperCase() để tránh lỗi lệch kiểu chữ
                String statusStr = rs.getString("status").toUpperCase();
                auction.setStatus(AuctionStatus.valueOf(statusStr));

                list.add(auction);
            }
        }
        return list;
    }

    /**
     * Cập nhật giá thầu mới và người dẫn đầu vào Database
     */
    public boolean updateBid(String auctionId, String bidderId, double newPrice) throws SQLException {
        // SQL có thêm điều kiện kiểm tra giá mới phải lớn hơn giá cũ để đảm bảo an toàn
        String sql = "UPDATE auctions SET currentPrice = ?, highestBidder_id = ? " +
                "WHERE auction_id = ? AND ? > currentPrice AND status = 'RUNNING'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newPrice);
            ps.setString(2, bidderId);
            ps.setString(3, auctionId);
            ps.setDouble(4, newPrice);

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Tạo một phiên đấu giá mới
     */
    public boolean createAuction(AuctionEntity auction) throws SQLException {
        String sql = "INSERT INTO auctions (auction_id, item_id, startingPrice, currentPrice, startAt, endAt, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, auction.getAuctionId());
            ps.setString(2, auction.getItem().getItemId());
            ps.setDouble(3, auction.getStartingPrice());
            ps.setDouble(4, auction.getCurrentPrice());

            // Chuyển LocalDateTime sang Timestamp để MySQL hiểu được
            ps.setTimestamp(5, Timestamp.valueOf(auction.getStartTime()));
            ps.setTimestamp(6, Timestamp.valueOf(auction.getEndTime()));

            ps.setString(7, auction.getStatus().toString());

            return ps.executeUpdate() > 0;
        }
    }
    public boolean placeBidTransaction(BidRecord bid) {
        Connection conn = null;
        // SQL 1: Cập nhật giá cao nhất và người giữ giá trong bảng auctions
        String updateAuctionSql = "UPDATE auctions SET currentPrice = ?, highestBidder_id = ? " +
                "WHERE auction_id = ? AND ? > currentPrice AND status = 'RUNNING'";

        // SQL 2: Lưu lịch sử vào bảng bids
        String insertBidSql = "INSERT INTO bids (bid_id, auction_id, bidder_id, bid_amount, bid_time) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // Bước 1: Tắt tự động commit để bắt đầu Transaction

            // THỰC HIỆN LỆNH 1: Cập nhật bảng Auctions
            try (PreparedStatement psAuction = conn.prepareStatement(updateAuctionSql)) {
                psAuction.setDouble(1, bid.getAmount());
                psAuction.setString(2, bid.getBidder());
                psAuction.setString(3, bid.getAuctionId());
                psAuction.setDouble(4, bid.getAmount());

                int rowsAffected = psAuction.executeUpdate();
                if (rowsAffected == 0) {
                    conn.rollback(); // Giá không hợp lệ hoặc auction đã đóng -> Hủy!
                    return false;
                }
            }

            // THỰC HIỆN LỆNH 2: Lưu vào bảng Bids
            try (PreparedStatement psBid = conn.prepareStatement(insertBidSql)) {
                psBid.setString(1, bid.getBidId());
                psBid.setString(2, bid.getAuctionId());
                psBid.setString(3, bid.getBidder());
                psBid.setDouble(4, bid.getAmount());
                psBid.setTimestamp(5, Timestamp.valueOf(bid.getTimestamp()));

                psBid.executeUpdate();
            }

            conn.commit(); // Bước 2: Nếu mọi thứ đều OK thì mới chính thức chốt dữ liệu
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Bước 3: Nếu có bất kỳ lỗi gì, "quay xe" ngay lập tức
                    System.err.println("🚨 Transaction thất bại, đã Rollback: " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Trả lại trạng thái mặc định cho Connection
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}