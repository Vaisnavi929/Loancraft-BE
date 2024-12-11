package com.narvee.serviceimpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.narvee.Service.EmailOtpService;
import com.narvee.entity.MailBody;
import com.narvee.entity.OtpVerification;
import com.narvee.repository.OtpRepository;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.util.OTPGenerator;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailOtpServiceImpl implements EmailOtpService {

	@Value("${frommail}")
	private String fromEmail;

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private JavaMailSender mailSender;

	@Override
    public String sendOtp(ApplicantDto applicant) throws MessagingException {

		// Generate OTP using OTPGenerator
		String token = OTPGenerator.generateRandomPassword(6);

		// Save OTP details
		OtpVerification otp = new OtpVerification();
		otp.setOtp(token);
		otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
		otpRepository.save(otp);

		StringBuilder emailContent = new StringBuilder();
        emailContent.append("<!DOCTYPE html>")
            .append("<html lang=\"en\">")
            .append("<head>")
            .append("<meta charset=\"UTF-8\">")
            .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
            .append("<title>OTP Email</title>")
            .append("<style>")
            .append("body, table, td, a {font-family: Arial, sans-serif; font-size: 14px; color: #333333; text-align: left;}")
            .append("body {margin: 0; padding: 0; background-color: #f4f4f4;}")
            .append("table {width: 100%; max-width: 600px; margin: 0 auto; border: 1px solid #e0e0e0; background-color: #ffffff; padding: 20px;}")
            .append("h1 {color: #00796b; font-size: 24px; margin-bottom: 10px;}")
            .append(".otp-code {font-size: 32px; font-weight: bold; color: #00796b; padding: 10px; background-color: #f1f8e9; border-radius: 5px; text-align: center; margin: 20px 0;}")
            .append(".footer {font-size: 12px; color: #888888; text-align: center; margin-top: 20px;}")
            .append(".footer a {color: #00796b; text-decoration: none;}")
            .append(".btn {display: inline-block; background-color: #00796b; color: #ffffff; padding: 10px 20px; border-radius: 5px; text-decoration: none; font-size: 14px; margin: 10px 0;}")
            .append("</style>")
            .append("</head>")
            .append("<body>")
            .append("<table>")
            .append("<tr><td>")
            .append("<h1>OTP Verification</h1>")
            .append("<p>Hello <strong>").append(applicant.getFirstName()+" "+applicant.getLastName()).append("</strong>,</p>")
            .append("<p>Thank you for registering. We have received a request to verify your email. Below is your one-time password (OTP):</p>")
            .append("<div class=\"otp-code\">").append(token).append("</div>")
            .append("<p>Please use the above OTP to complete your registration process. The OTP is valid for the next 5 minutes.</p>")
            .append("<p>If you did not request this OTP, please ignore this email or contact support.</p>")
            .append("<div class=\"footer\">")
            .append("<p>If you have any questions, feel free to <a href=\"mailto:support@example.com\">contact us</a>.</p>")
            .append("</div>")
            .append("</td></tr>")
            .append("</table>")
            .append("</body>")
            .append("</html>");

        MailBody mailBody = new MailBody(applicant.getEmail(), "OTP Verification Code", emailContent.toString());
        sendEmail(mailBody);

        return "OTP sent successfully.";
    }

    @Override
    public boolean verifyOtp(String otp) {
        OtpVerification otpVerification = otpRepository.findByOtp(otp)
                .orElseThrow(() -> new IllegalArgumentException("Invalid OTP"));

        if (otpVerification.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP has expired");
        }
        return true;
    }

    private void sendEmail(MailBody mailBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(mailBody.getTomail());
        helper.setFrom(fromEmail);
        helper.setSubject(mailBody.getSubject());
        helper.setText(mailBody.getText(), true);

        mailSender.send(message);
    }

}
