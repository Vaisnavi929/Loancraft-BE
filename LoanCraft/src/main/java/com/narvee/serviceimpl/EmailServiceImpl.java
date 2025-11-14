
package com.narvee.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.narvee.Service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    private final String FROM_EMAIL = "no-reply@beaconhl.com";

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true,"UTF-8");

            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true); // true = enable HTML
          
            
            helper.setFrom(FROM_EMAIL);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Invalid EmailAddress. Please try again.", e);
        }
    }

    // ================= Password Reset Email =================
    @Override
    public void sendForgotPasswordEmail(String toEmail, String otp) {
        String subject = "Reset Your Password - Beacon Holdings Limited";

        String body = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; border:1px solid #ddd;'>"
                + "<div style='background: #007bff; color: white; padding: 15px; text-align: center; font-size: 18px;'>"
                + "Reset Your Password</div>"
                + "<div style='padding: 20px;'>"
                + "<p>Hello,</p>"
                + "<p>We received a request to reset your password. Please click the button below to set a new password:</p>"
                + "<p style='text-align:center;'><a href='" + otp + "' style='background:#007bff;color:#fff;"
                + "padding:12px 20px;text-decoration:none;border-radius:5px;font-size:16px;'>Reset Password</a></p>"
                + "<p>If you didn’t request this, please ignore this email.</p>"
                + "<br><p>Best Regards,<br><b>Beacon Holdings Limited</b></p>"
                + "</div></div>";

        sendEmail(toEmail, subject, body);
    }

    // ================= OTP Email =================
    public void sendOtpEmail(String toEmail, String otpCode) {
        String subject = "Verify Your Login - Beacon Holdings Limited";

        String body = "<!DOCTYPE html>"
                + "<html><head><meta charset='UTF-8'></head>"
                + "<body style='font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px;'>"
                + "<table align='center' width='600' cellpadding='0' cellspacing='0' style='background:#fff; border:1px solid #ddd; border-radius:6px;'>"

                // Header
                + "<tr>"
                + "  <td style='background:#007bff; color:#fff; padding:15px; text-align:center; font-size:20px; font-weight:bold;'>"
                + "    Verify Your Login"
                + "  </td>"
                + "</tr>"

                // Body
                + "<tr>"
                + "  <td style='padding:25px; color:#333; font-size:15px; line-height:1.6;'>"
                + "    <div style='text-align:center; font-size:18px; font-weight:bold; margin-bottom:10px;'>Beacon Holdings Limited</div>"
                + "    <p>Hello <b>" + toEmail.split("@")[0] + "</b>,</p>"
                + "    <p>Thank you for choosing <b>Beacon Holdings Limited</b>. Use the following OTP to complete your Sign-In:</p>"
                + "    <p style='text-align:center; font-size:32px; font-weight:bold; color:#007bff; margin:20px 0;'>" + otpCode + "</p>"
                + "    <p style='text-align:center; color:#555;'>The OTP is valid for 10 minutes. Please do not share this code with anyone.</p>"
                + "    <br><p>Best Regards,<br><b>Beacon Holdings Limited</b></p>"
                + "  </td>"
                + "</tr>"

                + "</table>"
                + "</body></html>";

        sendEmail(toEmail, subject, body);
    }


}
