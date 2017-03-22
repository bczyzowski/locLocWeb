package service;

import model.User;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
public class EmailService {

    @Resource(name = "java:/...")
    private Session mailSession;

    public void sendEmail(User recipient, String code, User applier) {
        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(
			// email
			));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient.getEmail()));
            message.setSubject("LocLoc - Friend invitation");
            message.setText("Dear " + recipient.getFirstname() + "," +
                    "\n\n" +
                    "click the link below to become friend with " + applier.getFirstname() + " " + applier.getLastname() +
                    "\n\n" +
                    code);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }


    }
}
