package model;

import javax.mail.*;
import javax.mail.search.SubjectTerm;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class EmailSearcher {
    private EmailConnection emailConnection;

    // Constructor để khởi tạo EmailSearcher với EmailConnection
    public EmailSearcher() {
    }

    ;

    public EmailSearcher(EmailConnection emailConnection) {
        this.emailConnection = emailConnection;
    }

    // Phương thức để tìm kiếm email dựa trên tiêu đề
    public void searchAndSaveUsername(String subjectToSearch, String outputFilePath) throws MessagingException, IOException {
        Folder inbox = emailConnection.getInbox();

        // Tạo điều kiện tìm kiếm dựa trên tiêu đề email
        SubjectTerm subjectTerm = new SubjectTerm(subjectToSearch);
        Message[] foundMessages = inbox.search(subjectTerm);

        System.out.println("Số lượng email tìm thấy của tài khoản: "+emailConnection.getUsername()+":"+emailConnection.getPassword()+"là: " + foundMessages.length);

        // Kiểm tra số lượng email tìm thấy
        if (foundMessages.length > 0) {
            // Lấy email đầu tiên
            Message message = foundMessages[0];

            // Lấy tiêu đề email và trích xuất username
            String subject = message.getSubject();
            String username = extractUsernameFromSubject(subject);

            // Lưu thông tin vào file
            if (username != null) {
                saveAccountInfoToFile(emailConnection.getUsername(), emailConnection.getPassword(), username, outputFilePath);
            }

            // In thông tin email
            System.out.println("Subject: " + subject);
            System.out.println("From: " + message.getFrom()[0].toString());
            System.out.println("Sent Date: " + message.getSentDate());
            System.out.println("---------------------------------");
        } else {
            System.out.println("Không tìm thấy email nào với tiêu đề: " + subjectToSearch);
        }
    }

    // Phương thức để trích xuất username từ tiêu đề email
    private String extractUsernameFromSubject(String subject) {
        if (subject != null) {
            // Tìm vị trí của dấu phẩy
            int commaIndex = subject.indexOf(',');
            if (commaIndex > 0) {
                // Trích xuất phần trước dấu phẩy và loại bỏ khoảng trắng
                return subject.substring(0, commaIndex).trim();
            }
        }
        return null;
    }

    // Phương thức để lưu username vào file
    private void saveUsernameToFile(String username, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(username);
            writer.newLine();
        }
    }
    // Phương thức để lưu thông tin vào file với định dạng email:pass:username
    private void saveAccountInfoToFile(String email, String password, String username, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(email + ":" + password + ":" + username);
            writer.newLine();
        }
    }
    public static void main(String[] args) {
        String test = "Subject: bendikctastctiliv, we've made it easy to get back on Instagram" +
                "From: Instagram <security@mail.instagram.com>" +
                "Sent Date: Wed Aug 14 20:21:55 ICT 2024";
        EmailSearcher emailSearcher = new EmailSearcher();
        System.out.println(emailSearcher.extractUsernameFromSubject(test));
    }
}
