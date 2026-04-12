package server.dao;

import CommonClasses.BidRecord;
import server.database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BidDAO {

    /**
     * Lưu lịch sử đặt giá vào bảng bids
     */
    public boolean addBid(BidRecord bid) throws SQLException {
        String sql = "INSERT INTO bids (bid_id, auction_id, bidder_id, bid_amount, bid_time) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bid.getBidId());
            ps.setString(2, bid.getAuctionId());
            ps.setString(3, bid.getBidder()); // Đây là bidder_id
            ps.setDouble(4, bid.getAmount()); // Đây là bid_amount
            ps.setTimestamp(5, Timestamp.valueOf(bid.getTimestamp()));

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lấy toàn bộ lịch sử giá của một món đồ để hiển thị lên giao diện
     */
    public List<BidRecord> getBidsByAuction(String auctionId) throws SQLException {
        List<BidRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM bids WHERE auction_id = ? ORDER BY bid_time DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, auctionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BidRecord record = new BidRecord(
                            rs.getString("bid_id"),
                            rs.getString("auction_id"),
                            rs.getString("bidder_id"),
                            rs.getDouble("bid_amount"),
                            rs.getTimestamp("bid_time").toLocalDateTime()
                    );
                    list.add(record);
                }
            }
        }
        return list;
    }
}