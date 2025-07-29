package com.narvee.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.narvee.Service.InvitationsService;
import com.narvee.entity.Invitations;
import com.narvee.repository.InvitationsRepo;
import com.narvee.request.dto.MailBody;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class InvitationsServiceImpl implements InvitationsService{
	private static final Logger logger = LoggerFactory.getLogger(InvitationsServiceImpl.class);
	
	@Value("${frommail}")
	private String fromEmail;
	
	@Value("${URL}")
	private String url;
	
	@Autowired
	InvitationsRepo invitationsRepo;
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public String saveInvititions(Invitations invitations) throws MessagingException {
	    logger.info("!!! inside class : InvitationsServiceImpl , method : saveInvititions");
	    invitationsRepo.save(invitations);

	    String registrationLink = url+"/lms/#/register?id="+invitations.getUserId() ;

	    StringBuilder emailContent = new StringBuilder();
	    emailContent.append("<!DOCTYPE html>")
        .append("<html xmlns:th=\"http://www.thymeleaf.org\">")
        .append("<head>")
        .append("<meta charset=\"UTF-8\">")
        .append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">")
        .append("<title>Complete Your Registration</title>")
        .append("</head>")
        .append("<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; margin: 0;\">")

        // Centered container using table
        .append("<table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" bgcolor=\"#f4f4f4\">")
        .append("<tr><td align=\"center\" style=\"padding: 20px;\">")
        .append("<table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"600\" bgcolor=\"#ffffff\" style=\"max-width: 600px; width: 100%;\">")

        // Content
        .append("<tr><td style=\"padding: 30px;\">")
        .append("<p style=\"font-size: 18px; font-weight: bold; color: #333; margin: 0;\">Hi ").append(invitations.getName()).append(",</p>")
        .append("<p style=\"font-size: 16px; color: #555;\">Thank you for providing your details. You're just one step away from completing your Loan application. Please click the link below to proceed:</p>")

        // Centered Button
        .append("<table role=\"presentation\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" style=\"margin: 20px 0;\">")
        .append("<tr><td align=\"center\">")
        .append("<a href=\"").append(registrationLink).append("\" style=\"font-size: 16px; font-weight: bold; color: #007bff !important; text-decoration: none; display: inline-block; padding: 12px 24px; background: #007bff; color: #ffffff !important; border-radius: 4px;\">Register Now</a>")
        .append("</td></tr>")
        .append("</table>")

        .append("<p style=\"font-size: 14px; color: #777; text-align: left;\">If you did not request this, you can safely ignore this email.</p>")
        .append("<p style=\"font-size: 14px; font-weight: bold; color: #555; text-align: left;\">Best regards,</p>")
        .append("<p style=\"margin: 0; font-size: 14px; color: #333; text-align: left;\">Beacon Holdings</p>")

        .append("</td></tr>")
        .append("</table>")
        .append("</td></tr>")
        .append("</table>")

        .append("</body>")
        .append("</html>");


	    MailBody mailBody = new MailBody(invitations.getEmail(), "Invitation for Loan Application", emailContent.toString());
	    sendEmail(mailBody);

	    return "Invitation sent successfully.";
	}
	

  private void sendEmail(MailBody mailBody) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(mailBody.getTomail());
    helper.setFrom(fromEmail);
    helper.setSubject(mailBody.getSubject());
    helper.setText(mailBody.getText(), true);

    mailSender.send(message);
    
    logger.info("!!! inside class : InvitationsServiceImpl ,---> Email sent");
   }
}
