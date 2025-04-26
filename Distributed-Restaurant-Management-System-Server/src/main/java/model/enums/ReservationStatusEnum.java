package model.enums;

public enum ReservationStatusEnum {
    CANCELED("Đã hủy"),
    PENDING("Đang chờ"),
    RECEIVED("Đã nhận");

    private String reservationStatus;

    private ReservationStatusEnum(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }
}
