package com.narvee.serviceimpl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.narvee.Service.EmailOtpService;
import com.narvee.entity.OtpVerification;
import com.narvee.entity.UserInfo;
import com.narvee.repository.OtpRepository;
import com.narvee.repository.UserRepository;
import com.narvee.request.dto.ApplicantDto;
import com.narvee.request.dto.MailBody;
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
	
	@Autowired
   private UserRepository userRepository;


	//Bhargav	
		@Override
		public String sendOtp(ApplicantDto applicant) throws MessagingException {
		// ✅ 1. Find user info by email
		if (applicant.getEmail() != null && !applicant.getEmail().isEmpty()) {
		Optional<UserInfo> existingUserOpt = userRepository.findByEmail(applicant.getEmail().trim());

		if (existingUserOpt.isPresent()) {
		UserInfo user = existingUserOpt.get();

		// Since your UserInfo has a single "name" column, split into first/last if possible
		String fullName = user.getName() != null ? user.getName().trim() : "User";
		String[] nameParts = fullName.split(" ", 2);

		applicant.setFirstName(nameParts[0]);
		applicant.setLastName(nameParts.length > 1 ? nameParts[1] : "");
		} else {
		// fallback if email not found in DB
		applicant.setFirstName("User");
		applicant.setLastName("");
		}
		}
		// Generate OTP using OTPGenerator
		String token = OTPGenerator.generateRandomPassword(6);
		// Save OTP details
		OtpVerification otp = new OtpVerification();
		otp.setOtp(token);
		otp.setApplicantEmail(applicant.getEmail());
		otp.setExpiryTime(LocalDateTime.now().plusMinutes(2));
		otpRepository.save(otp);
		//i added this one
		StringBuilder emailContent = new StringBuilder();
		emailContent.append("<!DOCTYPE html>")
		.append("<html lang=\"en\">")
		.append("<head>")
		.append("<meta charset=\"UTF-8\">")
		.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
		.append("<title>Login Verification Code</title>")
		.append("<style>")
		.append("body {margin:0; padding:0; background-color:#f4f4f4; font-family: Arial, sans-serif;}")
		.append("table {width:100%; max-width:600px; margin:20px auto; border:1px solid #e0e0e0; background:#ffffff; padding:20px;}")
		.append(".header {background-color:#1976d2; color:#ffffff; font-size:20px; padding:15px; text-align:center; font-weight:bold;}")
		.append(".company {font-size:16px; font-weight:bold; color:#333; margin-top:20px; text-align:center;}")
		.append("p {font-size:14px; color:#333; text-align:left; line-height:1.5; margin:10px 0;}")
		.append(".otp-code {font-size:24px; font-weight:bold; color:#1976d2; margin:20px 0; text-align:center;}")
		.append(".footer {font-size:12px; color:#888; text-align:center; margin-top:20px;}")
		.append("</style>")
		.append("</head>")
		.append("<body>")
		.append("<table>")
		.append("<tr><td>")
		.append("<div class=\"header\">Verify Your Login</div>")
		.append("<div class=\"company\">Beacon Holdings Limited</div>")
		.append("<p>Hello <strong>").append(applicant.getFirstName()).append(" ").append(applicant.getLastName())
		.append("</strong>,</p>")
		.append("<p>Thank you for choosing Beacon Holdings Limited.Use the following OTP to complete your Sign-In:</p>")
		.append("<div class=\"otp-code\">").append(token).append("</div>")
		.append("<p>The OTP is valid for 2 minutes.Please do not share this code with anyone.</p>")
		.append("<p>Best Regards,<br/><strong>Beacon Holdings Limited</strong></p>")
		.append("</td></tr>")
		.append("</table>")
		.append("</body>")
		.append("</html>");




		MailBody mailBody = new MailBody(applicant.getEmail(), "Login Verification code ", emailContent.toString());// and here i also changed like in the place of Otp verfication
		sendEmail(mailBody);

		return "OTP sent successfully."+ applicant.getEmail();
		}
	//Bhargav

		
		@Override
		public boolean verifyOtpForEmail(String email, String otp) {
		    // Get the latest OTP for this email
		    OtpVerification otpVerification = otpRepository
		            .findTopByApplicantEmailOrderByExpiryTimeDesc(email)
		            .orElseThrow(() -> new IllegalArgumentException("No OTP found for this email"));

		    // Check expiry
		    if (otpVerification.getExpiryTime().isBefore(LocalDateTime.now())) {
		        throw new IllegalArgumentException("OTP has expired");
		    }

		    // Check OTP match
		    if (!otpVerification.getOtp().equals(otp)) {
		        throw new IllegalArgumentException("Invalid OTP");
		    }
		    

		    return true;
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
    @Override
    public void sendLoanApplicationNotification( String applicationReferenceId, String status, String firstName, String lastName, String email) {
        String subject = "Loan Application Update";
        String messageBody = buildEmailBody(applicationReferenceId, status, firstName, lastName, email);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(messageBody, true); 

            mailSender.send(message);
            System.out.println("Email sent successfully to " + email);
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }

    private String buildEmailBody(String applicationReferenceId, String status, String firstName, String lastName, String email) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<h1 style='color: #0056B3;'>We’ve Received Your Loan Application</h1>" +
                "<p>Dear " + firstName + " " + lastName + ",</p>" +
                "<p>We’ve Received Your Loan Application\r\n"
                + "We are writing to update you about the status of your loan application with Loan ID  <strong>" + applicationReferenceId + "</strong>.</p>" +
                "<p>The current status of your application is: <strong>" + status + "</strong>.</p>" +
                "<p>If you would like to appeal this decision or have any questions, please contact our customer support team for assistance.</p>" +
                "<br>" +
                "<p>Best regards,</p>" +
                "<p>Narvee Technologies</p>" +
                "<p style='font-size: 12px; color: #999;'>This email was sent to <a href='mailto:" + email + "'>" + email + "</a></p>" +
                "</body>" +
                "</html>";
    }

//	@Override
//	public boolean verifyOtpForApplicant(Long applicantId, String otp) {
//		// TODO Auto-generated method stub
//		return false;
//	}

}
