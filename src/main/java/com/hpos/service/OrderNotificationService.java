package com.hpos.service;

public interface OrderNotificationService {

    void sendOrderSuccessNotification(String orderNo, String patientName, String doctorName, String workDate, String periodText);
}
