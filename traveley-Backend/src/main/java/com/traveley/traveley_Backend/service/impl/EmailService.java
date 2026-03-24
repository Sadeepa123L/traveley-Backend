package com.traveley.traveley_Backend.service.impl;

import com.traveley.traveley_Backend.entity.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendBookingConfirmation(String toEmail, String travelerName, String packageName, Long bookingId) {
        String referenceNumber = String.format("BKG-%04d", bookingId);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Booking Confirmed! [Ref: " + referenceNumber + "]");

        String mailBody = "Dear " + travelerName + ",\n\n"
                + "Great news! Your booking for the '" + packageName + "' tour package has been successfully confirmed by the agency.\n\n"
                + "Booking Reference Number: " + referenceNumber + "\n\n"
                + "Get ready for your amazing journey!\n\n"
                + "Best Regards,\n"
                + "Traveley Team";

        message.setText(mailBody);
        mailSender.send(message);
    }
}
