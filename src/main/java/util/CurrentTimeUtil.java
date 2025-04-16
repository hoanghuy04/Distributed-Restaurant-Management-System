package util;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTimeUtil extends JPanel {
    private JLabel timeLabel;

    public CurrentTimeUtil() {
    }

    
    // Constructor nhận JLabel
    public CurrentTimeUtil(JLabel label) {
        this.timeLabel = label; // Gán JLabel truyền vào

        // Thiết lập font cho JLabel
        timeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(timeLabel);

        // Tạo Timer để cập nhật JLabel mỗi giây
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
    }

    private void updateTime() {
        // Lấy thời gian hiện tại
        Date now = new Date();

        // Định dạng thời gian theo kiểu HH:mm:ss
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = timeFormat.format(now);

        // Hiển thị thời gian lên JLabel
        timeLabel.setText(currentTime);
    }
}
