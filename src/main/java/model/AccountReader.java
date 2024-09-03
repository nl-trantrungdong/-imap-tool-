package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountReader {

    private String filePath;

    // Constructor để khởi tạo với đường dẫn file
    public AccountReader(String filePath) {
        this.filePath = filePath;
    }

    // Phương thức để đọc thông tin tài khoản từ file
    public List<String[]> readAllAccounts() throws IOException {
        List<String[]> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(":")) {
                    accounts.add(line.split(":")); // Tách username và password
                }
            }
        }
        return accounts;
    }
}