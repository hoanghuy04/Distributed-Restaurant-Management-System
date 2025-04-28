/*
 * @ (#) MailBUSImpl.java      1.0      4/28/2025
 *
 * Copyright (c) 2025 IUH. ALL rights reserved.
 */
package bus.impl;

import bus.MailBUS;
import lombok.NoArgsConstructor;
import model.OrderEntity;
import util.MailSenderUtil;
import util.QrCodeGenerationUtil;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/*
 * @description:
 * @author: Hoang Huy
 * @date: 4/28/2025
 * @version: 1.0
 */
public class MailBUSImpl extends UnicastRemoteObject implements MailBUS {

    public MailBUSImpl() throws RemoteException {
        super();
    }

    @Override
    public void sendBookingConfirmation(OrderEntity order) throws RemoteException {
        try {
            // Generate QR code for the order
            QrCodeGenerationUtil.generateQrCode(order.getOrderId());

            // Send booking confirmation email
            MailSenderUtil.sendBookingConfirmationEmail(order);
        } catch (Exception e) {
            throw new RemoteException("Error sending booking confirmation: " + e.getMessage(), e);
        }
    }
}