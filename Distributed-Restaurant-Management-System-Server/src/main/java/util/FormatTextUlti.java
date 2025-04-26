/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;


/**
 *
 * @author Trần Ngọc Huyền
 */
public class FormatTextUlti {
     // Hàm tách chuỗi với dấu cách
    public static String formatTextWithLineBreaks(String text, int maxLength, String textAlign) {
        StringBuilder formattedText = new StringBuilder("<html><body style='text-align:" + textAlign + ";'>");
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + maxLength, text.length());
            // Kiểm tra nếu không phải cuối chuỗi và không có dấu cách
            if (end < text.length() && text.charAt(end) != ' ') {
                int lastSpace = text.lastIndexOf(' ', end);
                if (lastSpace > start) {
                    end = lastSpace; // Xuống dòng tại dấu cách gần nhất
                }
            }
            formattedText.append(text, start, end).append("<br>");
            start = end + 1; // Bỏ qua dấu cách khi bắt đầu đoạn mới
        }
        formattedText.append("</body></html>");
        return formattedText.toString();
    }
}
