package model;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String accountFilePath = "C:\\Users\\trand\\OneDrive\\Máy tính\\ac.txt"; // Đường dẫn đến file chứa thông tin tài khoản
        String subjectToSearch = "we've made it easy to get back on Instagram";
        String outputFilePath = "C:\\Users\\trand\\OneDrive\\Máy tính\\username.txt";

        // Đọc tất cả các tài khoản từ file
        AccountReader accountReader = new AccountReader(accountFilePath);
        List<String[]> accounts;

        try {
            accounts = accountReader.readAllAccounts();
            if (accounts.isEmpty()) {
                System.out.println("Không có tài khoản hợp lệ nào trong file.");
                return;
            }

            for (String[] accountInfo : accounts) {
                if (accountInfo.length != 2) {
                    System.out.println("Định dạng tài khoản không hợp lệ: " + String.join(":", accountInfo));
                    continue; // Bỏ qua tài khoản có định dạng không hợp lệ
                }

                String username = accountInfo[0];
                String password = accountInfo[1];
                String host = "secureimap.t-online.de"; // Thay thế bằng máy chủ IMAP của bạn

                // Khởi tạo kết nối email cho tài khoản hiện tại
                EmailConnection emailConnection = new EmailConnection(host, username, password);

                try {
                    // Kết nối đến máy chủ IMAP và mở hộp thư
                    emailConnection.connect();
                    emailConnection.openInbox();

                    // Tìm kiếm email và lưu username vào file
                    EmailSearcher emailSearcher = new EmailSearcher(emailConnection);
                    emailSearcher.searchAndSaveUsername(subjectToSearch, outputFilePath);

                } catch (MessagingException e) {
                    System.out.println("Lỗi khi kết nối hoặc xử lý tài khoản: " + username);
                    e.printStackTrace();
                } finally {
                    try {
                        // Đóng kết nối
                        emailConnection.close();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
