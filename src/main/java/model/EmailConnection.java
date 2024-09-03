package model;

import javax.mail.*;
import java.util.Properties;

public class EmailConnection {
    private String host;
    private String username;
    private String password;
    private Store store;
    private Folder inbox;

    // Constructor để khởi tạo các thuộc tính
    public EmailConnection(String host, String username, String password) {
        this.host = host;
        this.username = username;
        this.password = password;
    }

    // Phương thức để kết nối đến máy chủ IMAP
    public void connect() throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imap");
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");

        Session session = Session.getDefaultInstance(properties);
        store = session.getStore("imap");
        store.connect(username, password);
    }

    // Phương thức để mở hộp thư Inbox
    public void openInbox() throws MessagingException {
        inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
    }

    // Phương thức để lấy đối tượng Folder (INBOX)
    public Folder getInbox() {
        return inbox;
    }

    // Phương thức để đóng kết nối
    public void close() throws MessagingException {
        if (inbox != null && inbox.isOpen()) {
            inbox.close(false);
        }
        if (store != null) {
            store.close();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
