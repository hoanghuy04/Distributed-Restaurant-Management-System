package util;

import com.google.zxing.WriterException;
import common.Constants;
import model.OrderEntity;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MailSenderUtil {

    private static final String QR_CODE_PATH = System.getProperty("user.dir") + "/resources/qrcode/";

    // Private constructor to prevent instantiation
    private MailSenderUtil() {
    }

    public static void sendEmail(String recipient, String subject, MimeMultipart multipart) throws MessagingException, IOException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Sender information
        String fromUser = "duonghoanghuydhi12@gmail.com";
        String fromUserPassword = "vcil nksh okpn ftgn";

        // Create session and authenticate with email server
        Session mailSession = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromUser, fromUserPassword);
            }
        });

        // Create and configure email
        MimeMessage mimeMessage = new MimeMessage(mailSession);
        mimeMessage.setFrom(new InternetAddress(fromUser));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        mimeMessage.setSubject(subject);
        mimeMessage.setContent(multipart);

        // Send email
        Transport.send(mimeMessage);
        System.out.println("Email successfully sent to " + recipient + "!");
    }

    public static void sendBookingConfirmationEmail(OrderEntity order) throws MessagingException, IOException, WriterException {
        String recipient = order.getCustomer().getEmail();
        String subject = "Thông báo: Đặt bàn thành công";
        MimeMultipart body = createBookingEmailBody(order);

        sendEmail(recipient, subject, body);
    }

    private static MimeMultipart createBookingEmailBody(OrderEntity order) throws WriterException, IOException, MessagingException {
        // Create MimeMultipart to hold email content
        MimeMultipart multipart = new MimeMultipart();

        // Create text body part
        MimeBodyPart textBodyPart = new MimeBodyPart();
        textBodyPart.setContent(generateEmailText(order), "text/html; charset=UTF-8");
        multipart.addBodyPart(textBodyPart);

        // Create MimeBodyPart for QR code image
        MimeBodyPart imageBodyPart = new MimeBodyPart();
        String qrCodeFilePath = QR_CODE_PATH + order.getOrderId() + ".jpg";
        File qrCodeFile = new File(qrCodeFilePath);
        if (!qrCodeFile.exists()) {
            Logger.getLogger(MailSenderUtil.class.getName()).log(Level.SEVERE, "QR code file not found: {0}", qrCodeFilePath);
            throw new IOException("QR code file not found: " + qrCodeFilePath);
        }
        DataSource fds = new FileDataSource(qrCodeFile);
        imageBodyPart.setDataHandler(new DataHandler(fds));
        imageBodyPart.setHeader("Content-ID", "<qrCode>");
        multipart.addBodyPart(imageBodyPart);

        return multipart;
    }

    public static void sendUpcomingReservationReminderEmails(List<OrderEntity> orders) throws MessagingException, IOException {
        for (OrderEntity order : orders) {
            String recipient = order.getCustomer().getEmail();
            String subject = "Thông báo: Sắp đến hạn nhận bàn";
            MimeMultipart body = createUpcomingReservationEmailBody(order);
            sendEmail(recipient, subject, body);
        }
    }

    private static MimeMultipart createUpcomingReservationEmailBody(OrderEntity order) throws MessagingException {
        MimeMultipart multipart = new MimeMultipart();

        // Create the text body part
        MimeBodyPart textBodyPart = new MimeBodyPart();
        String content = "<p>Kính gửi <strong>" + order.getCustomer().getName() + "</strong>,</p>\n"
                + "<p>Chúng tôi xin nhắc nhở quý khách rằng thời gian nhận bàn của quý khách tại nhà hàng <strong>Zenta restaurant</strong> đang đến gần. Dưới đây là thông tin chi tiết:</p>\n"
                + "<ul>\n"
                + "<li><strong>Tên khách hàng:</strong> " + order.getCustomer().getName() + "</li>\n"
                + "<li><strong>Số điện thoại:</strong> " + order.getCustomer().getPhone() + "</li>\n"
                + "<li><strong>Số lượng khách:</strong> " + order.getNumberOfCustomer() + "</li>\n"
                + "<li><strong>Ngày đặt:</strong> " + order.getReservationTime().toLocalDate().format(DatetimeFormatterUtil.getDateFormatter()) + "</li>\n"
                + "<li><strong>Giờ nhận bàn:</strong> " + order.getReservationTime().toLocalDate().format(DatetimeFormatterUtil.getDateFormatter()) + " "
                + order.getReservationTime().toLocalTime().format(DatetimeFormatterUtil.getTimeFormatter()) + "</li>\n"
                + "<li><strong>Tên bàn:</strong> " + order.getTable().getName() + "</li>\n"
                + "<li><strong>Tên lầu:</strong> " + order.getTable().getFloor().getName() + "</li>\n"
                + "</ul>\n"
                + "<p>Quý khách vui lòng có mặt đúng giờ để đảm bảo trải nghiệm tốt nhất. Nếu có bất kỳ thay đổi nào hoặc cần thêm thông tin, xin vui lòng liên hệ với chúng tôi qua số hotline <strong>Zenta restaurant: " + Constants.HOTLINE + " </strong>.</p>\n"
                + "<p>Rất mong được phục vụ quý khách!</p>\n"
                + "<p>Trân trọng,<br>\n"
                + order.getEmployee().getFullname() + "!</p>\n";

        textBodyPart.setContent(content, "text/html; charset=UTF-8");
        multipart.addBodyPart(textBodyPart);

        return multipart;
    }

    public static void sendOrderCancellationEmails(List<OrderEntity> cancelledOrders) throws MessagingException, IOException {
        for (OrderEntity order : cancelledOrders) {
            String recipient = order.getCustomer().getEmail();
            String subject = "Thông báo: Đơn đặt bàn đã bị hủy";
            MimeMultipart body = createOrderCancellationEmailBody(order);
            sendEmail(recipient, subject, body);
        }
    }

    private static MimeMultipart createOrderCancellationEmailBody(OrderEntity order) throws MessagingException {
        MimeMultipart multipart = new MimeMultipart();

        // Create the text body part
        MimeBodyPart textBodyPart = new MimeBodyPart();
        String content = "<p>Kính gửi <strong>" + order.getCustomer().getName() + "</strong>,</p>\n"
                + "<p>Chúng tôi rất tiếc phải thông báo rằng đơn đặt bàn của quý khách tại nhà hàng <strong>Zenta restaurant</strong> đã bị hủy do quá hạn. Dưới đây là thông tin chi tiết:</p>\n"
                + "<ul>\n"
                + "<li><strong>Tên khách hàng:</strong> " + order.getCustomer().getName() + "</li>\n"
                + "<li><strong>Số điện thoại:</strong> " + order.getCustomer().getPhone() + "</li>\n"
                + "<li><strong>Số lượng khách:</strong> " + order.getNumberOfCustomer() + "</li>\n"
                + "<li><strong>Ngày đặt:</strong> " + order.getReservationTime().toLocalDate().format(DatetimeFormatterUtil.getDateFormatter()) + "</li>\n"
                + "<li><strong>Giờ nhận bàn:</strong> " + order.getReservationTime().toLocalDate().format(DatetimeFormatterUtil.getDateFormatter()) + " "
                + order.getReservationTime().toLocalTime().format(DatetimeFormatterUtil.getTimeFormatter()) + "</li>\n"
                + "<li><strong>Tên bàn:</strong> " + order.getTable().getName() + "</li>\n"
                + "<li><strong>Tên lầu:</strong> " + order.getTable().getFloor().getName() + "</li>\n"
                + "</ul>\n"
                + "<p>Chúng tôi rất tiếc về sự bất tiện này và hy vọng sẽ có cơ hội phục vụ quý khách trong tương lai. Nếu quý khách cần thêm thông tin, xin vui lòng liên hệ với chúng tôi qua số hotline <strong>Zenta restaurant: " + Constants.HOTLINE + "</strong>.</p>\n"
                + "<p>Trân trọng,<br>\n"
                + order.getEmployee().getFullname() + "!</p>\n";

        textBodyPart.setContent(content, "text/html; charset=UTF-8");
        multipart.addBodyPart(textBodyPart);

        return multipart;
    }

    public static void sendConfirmCodeToResetPassword(String email, String code) {
        try {
            String subject = "Mã xác nhận đặt lại mật khẩu";
            MimeMultipart body = createResetPasswordEmailBody(code);
            sendEmail(email, subject, body);
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(MailSenderUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static MimeMultipart createResetPasswordEmailBody(String code) throws MessagingException {
        MimeMultipart multipart = new MimeMultipart();

        // Create text body part
        MimeBodyPart textBodyPart = new MimeBodyPart();
        String emailContent = "<p>Xin chào,</p>"
                + "<p>Bạn đã yêu cầu đặt lại mật khẩu. Dưới đây là mã xác nhận của bạn:</p>"
                + "<h2 style='color: #2E86C1;'>" + code + "</h2>"
                + "<p>Vui lòng nhập mã này trên trang đặt lại mật khẩu để tiếp tục.</p>"
                + "<p>Nếu bạn không yêu cầu, hãy bỏ qua email này.</p>"
                + "<p>Trân trọng,<br>Đội ngũ hỗ trợ</p>";
        textBodyPart.setContent(emailContent, "text/html; charset=UTF-8");
        multipart.addBodyPart(textBodyPart);

        return multipart;
    }

    private static String generateEmailText(OrderEntity order) {
        return "<p>Kính gửi <strong>" + order.getCustomer().getName() + "</strong>,</p>\n"
                + "<p>Cảm ơn quý khách đã tin tưởng lựa chọn nhà hàng <strong>Zenta restaurant</strong>. "
                + "Chúng tôi xin xác nhận thông tin đặt bàn của quý khách như sau:</p>\n"
                + "<ul>\n"
                + "<li><strong>Tên khách hàng:</strong> " + order.getCustomer().getName() + "</li>\n"
                + "<li><strong>Số điện thoại:</strong> " + order.getCustomer().getPhone() + "</li>\n"
                + "<li><strong>Số lượng khách:</strong> " + order.getNumberOfCustomer() + "</li>\n"
                + "<li><strong>Ngày đặt:</strong> " + order.getReservationTime().toLocalDate().format(DatetimeFormatterUtil.getDateFormatter()) + " "
                + order.getReservationTime().toLocalTime().format(DatetimeFormatterUtil.getTimeFormatter()) + "</li>\n"
                + "<li><strong>Giờ nhận bàn:</strong> " + order.getReservationTime().toLocalDate().format(DatetimeFormatterUtil.getDateFormatter()) + " "
                + order.getReservationTime().toLocalTime().format(DatetimeFormatterUtil.getTimeFormatter()) + "</li>\n"
                + "<li><strong>Tên lầu:</strong> " + order.getTable().getFloor().getName() + "</li>\n"
                + "<li><strong>Tên bàn:</strong> " + order.getTable().getName() + "</li>\n"
                + "</ul>\n"
                + "<p>Quý khách vui lòng có mặt đúng giờ để đảm bảo trải nghiệm tốt nhất. Nếu có bất kỳ thay đổi nào hoặc cần thêm thông tin, xin vui lòng liên hệ với chúng tôi qua số hotline <strong>Zenta restaurant: " + Constants.HOTLINE + "</strong>.</p>\n"
                + "<p>Rất mong được phục vụ quý khách!</p>\n"
                + "<p><img src='cid:qrCode' alt='Mã QR đặt bàn' width='200' height='200' /></p>\n"
                + "<p>Trân trọng,<br>\n"
                + order.getEmployee().getFullname() + "!</p>\n";
    }
}