package server.dao;

import CommonClasses.BidRecord;
import server.database.DatabaseConnection;
import CommonClasses.AuctionEntity;
import CommonClasses.AuctionItem;
import CommonClasses.AuctionStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuctionDAO {

    public List<AuctionEntity> getAllAuctions() {
        List<AuctionEntity> list = new ArrayList<>();
        String sql = "SELECT a.*, i.name, i.description, i.owner_id, i.category, i.estimate_price " +
                "FROM auctions a JOIN items i ON a.item_id = i.item_id";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AuctionItem item = new AuctionItem(
                        rs.getString("item_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("owner_id"),
                        rs.getString("category"),
                        rs.getDouble("estimate_price")
                );

                AuctionEntity auction = new AuctionEntity(
                        rs.getString("auction_id"),
                        item,
                        rs.getDouble("startingPrice"),
                        rs.getTimestamp("startAt").toLocalDateTime(),
                        rs.getTimestamp("endAt").toLocalDateTime()
                );

                auction.setCurrentPrice(rs.getDouble("currentPrice"));

                // [ĐÃ FIX]: Xử lý trường hợp chưa có ai đấu giá (highestBidder_id bị null)
                String highestBidder = rs.getString("highestBidder_id");
                /*Lỗi nguy hiểm nhất (Sập giao diện - NullPointerException):
                 Khi một phiên đấu giá vừa tạo ra, chưa có ai vào đặt giá thì cột highestBidder_id trong MySQL sẽ có giá trị là NULL.
                  Code cũ lôi cái null này lên và ném thẳng vào đối tượng AuctionEntity.
                   Khi gửi lên Client, giao diện JavaFX cố gắng in cái null này ra màn hình
                    ➔ Văng app ngay lập tức. Cách khắc phục thì nếu nó là null thì gán nó thành chưa có*/
                if (highestBidder != null && !highestBidder.isEmpty()) {
                    auction.setHighestBidder(highestBidder);
                } else {
                    auction.setHighestBidder("Chưa có");
                }

                auction.setStatus(AuctionStatus.valueOf(rs.getString("status").toUpperCase()));
                list.add(auction);
            }
        } catch (SQLException e) {
            System.err.println("❌ Lỗi lấy danh sách đấu giá: " + e.getMessage());
        }
        return list;
    }

    public boolean updateBid(String auctionId, String bidderId, double newPrice) {
        String sql = "UPDATE auctions SET currentPrice = ?, highestBidder_id = ? " +
                "WHERE auction_id = ? AND ? > currentPrice AND status = 'RUNNING'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, newPrice);
            ps.setString(2, bidderId);
            ps.setString(3, auctionId);
            ps.setDouble(4, newPrice);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi update giá: " + e.getMessage());
            return false;
        }
    }

    public boolean createAuction(AuctionEntity auction) {
        String sql = "INSERT INTO auctions (auction_id, item_id, startingPrice, currentPrice, startAt, endAt, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, auction.getAuctionId());
            ps.setString(2, auction.getItem().getItemId());
            ps.setDouble(3, auction.getStartingPrice());
            ps.setDouble(4, auction.getCurrentPrice());
            ps.setTimestamp(5, Timestamp.valueOf(auction.getStartTime()));
            ps.setTimestamp(6, Timestamp.valueOf(auction.getEndTime()));
            ps.setString(7, auction.getStatus().toString());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi tạo phiên đấu giá: " + e.getMessage());
            return false;
        }
    }

    /**
     * TRANSACTION: Đặt giá và ghi lại lịch sử cùng lúc
     */
    public boolean placeBidTransaction(BidRecord bid) {
        String updateAuctionSql = "UPDATE auctions SET currentPrice = ?, highestBidder_id = ? " +
                "WHERE auction_id = ? AND ? > currentPrice AND status = 'RUNNING'";
        String insertBidSql = "INSERT INTO bids (bid_id, auction_id, bidder_id, bid_amount, bid_time) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction

            // 1. Cập nhật Auctions
            try (PreparedStatement psAuction = conn.prepareStatement(updateAuctionSql)) {
                psAuction.setDouble(1, bid.getAmount());
                psAuction.setString(2, bid.getBidder());
                psAuction.setString(3, bid.getAuctionId());
                psAuction.setDouble(4, bid.getAmount());

                if (psAuction.executeUpdate() == 0) {
                    conn.rollback();
                    return false; // Thất bại: Do giá thấp hơn hoặc đã hết giờ
                }
            }

            // 2. Ghi log Bids
            try (PreparedStatement psBid = conn.prepareStatement(insertBidSql)) {
                psBid.setString(1, bid.getBidId());
                psBid.setString(2, bid.getAuctionId());
                psBid.setString(3, bid.getBidder());
                psBid.setDouble(4, bid.getAmount());
                psBid.setTimestamp(5, Timestamp.valueOf(bid.getTimestamp()));
                psBid.executeUpdate();
            }

            conn.commit(); // Chốt sổ!
            return true;

        } catch (SQLException e) {
            System.err.println("🚨 Transaction lỗi, tiến hành Rollback: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            // Dọn dẹp trả lại trạng thái mặc định cho Connection Pool
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}