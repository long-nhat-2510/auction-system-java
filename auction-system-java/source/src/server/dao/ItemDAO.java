package server.dao;

import CommonClasses.AuctionItem;
import server.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItemDAO {
    public boolean addItem(AuctionItem item) throws SQLException{
        // item_id, name, description, owner_id, category, estimate_price
        String sql = "INSERT INTO items (item_id, name, description, owner_id, category, estimate_price) "
                + "VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, item.getItemId());
            ps.setString(2, item.getName());
            ps.setString(3, item.getDescription());
            ps.setString(4,item.getOwner());
            ps.setString(5, item.getCategory());
            ps.setDouble(6, item.getEstimatedPrice());

            return ps.executeUpdate() > 0;

        }


    }
    /**
     * Lấy thông tin chi tiết của một sản phẩm dựa trên ID
     */
    public AuctionItem getItemById(String itemId) throws SQLException {
        String sql = "SELECT * FROM items WHERE item_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, itemId);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuctionItem(
                            rs.getString("item_id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getString("owner_id"),
                            rs.getString("category"),
                            rs.getDouble("estimate_price")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Xóa sản phẩm (Cẩn thận: Chỉ xóa được nếu sản phẩm chưa được đưa lên sàn đấu giá)
     */
    public boolean deleteItem(String itemId) throws SQLException {
        String sql = "DELETE FROM items WHERE item_id = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemId);
            return ps.executeUpdate() > 0;
        }
    }
}
