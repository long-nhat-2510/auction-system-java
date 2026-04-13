package server.dao;

import CommonClasses.AuctionItem;

import java.sql.SQLException;

public class TestItemDAO {
    public static void main(String[] args) throws SQLException {
        ItemDAO dao = new ItemDAO();
        AuctionItem laptop = new AuctionItem(
                "ITEM-999", "Macbook Pro M3", "Hàng mới 100%",
                "78bbebeb", "Electronics", 2500.0
        );

        if(dao.addItem(laptop)) {
            System.out.println("✅ Thêm sản phẩm thành công!");
        }
    }
}
