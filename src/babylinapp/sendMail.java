package babylinapp;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class sendMail {
    String to ="";
    String from ="babylinnaturalhairfoods@gmail.com";
    String host="localhost";
    protected void sendReceipt(String email,String CustomerName, Integer orderNumber) {
        to =email;
        Properties properties = System.getProperties();
        properties.setProperty("mail.stmp.host", host);
        Session session = Session.getDefaultInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject("Purchase Order:");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("Your receipt bro");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String fileName=CustomerName+":receipt"+orderNumber+".pdf";
//            DataSource source= new FileDataSource(fileName);
//            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            Transport.send(message);
            Controller.infoBox("Sent receipt via Email!",null,"Purchase Successful!");
        } catch (MessagingException e) {
            Controller.infoBox("Receipt not sent!",null,"Error");
            jdbcController.printMessagingException(e);
        }
    }
}
