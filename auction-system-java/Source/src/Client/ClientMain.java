package client;

import packets.NetworkMessage;
import packets.RequestType;
import payload.AuctionIdRequest;

import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) {

        // 1. Dùng ServerConnection thay vì tự mở Socket thủ công
        ServerConnection connection = new ServerConnection();
        connection.connect("localhost", 1234);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Thêm Thread.sleep(100) ở đây một chút để menu in ra không bị đè lên tin nhắn của Server
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            System.out.println("\n-----AUCTION OPTIONS-----");
            System.out.println("1. AUCTION_ID_REQUEST (Lấy thông tin)");
            System.out.println("2. REGISTER_CLIENT_REQUEST (Đăng ký)");
            System.out.println("3. UNREGISTER_CLIENT_REQUEST");
            System.out.println("4. CREATE_AUCTION_REQUEST");
            System.out.println("5. CANCEL_AUCTION_REQUEST");
            System.out.println("6. FINISH_AUCTION_REQUEST");
            System.out.println("7. PLACE_BID_REQUEST (Đặt giá)");
            System.out.println("8. HIGHEST_BID_REQUEST");
            System.out.println("0. Close the program");
            System.out.print("👉 Lựa chọn của bạn: ");

            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                break; // Thoát vòng lặp
            }

            try {
                NetworkMessage message = null;

                switch (choice) {
                    case "1":
                        System.out.print("Nhập Auction ID cần tìm: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        AuctionIdRequest idPayload = new AuctionIdRequest(id);
                        message = new NetworkMessage(RequestType.AUCTION_ID_REQUEST, idPayload);
                        break;

                    case "2":
                        System.out.print("Enter your name: ");
                        // Code payload đăng ký ở đây...
                        break;

                    // ... các case 3, 4, 5, 6 ...

                    case "7": // THÊM CASE 7 ĐỂ ĐẶT GIÁ
                        System.out.print("Nhập Auction ID muốn đặt giá (Ví dụ: 1): ");
                        int bidAuctionId = Integer.parseInt(scanner.nextLine());

                        System.out.print("Nhập Tên của bạn: ");
                        String bidderName = scanner.nextLine();

                        System.out.print("Nhập Số tiền đặt: ");
                        double amount = Double.parseDouble(scanner.nextLine());

                        // Giả sử class PlaceBidRequest của bạn nhận 3 tham số này
                        // Lưu ý: Đảm bảo biến số tiền trong PlaceBidRequest tên là bidAmount nếu ở Server bạn dùng getBidAmount()
                        payload.PlaceBidRequest bidPayload = new payload.PlaceBidRequest(bidAuctionId, bidderName, amount);
                        message = new NetworkMessage(RequestType.PLACE_BID_REQUEST, bidPayload);
                        break;

                    default:
                        System.out.println("❌ Invalid option, please choose again!");
                        continue;
                }

                // 2. Dùng connection để gửi gói tin đi
                if (message != null) {
                    connection.sendRequest(message);
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Lỗi: Vui lòng chỉ nhập số hợp lệ!");
            }
        }

        System.out.println("👋 Client closed");
        scanner.close();
        System.exit(0); // Tắt hẳn chương trình
    }
}