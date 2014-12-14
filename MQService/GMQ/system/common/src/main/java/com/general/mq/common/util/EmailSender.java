package com.general.mq.common.util;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.general.mq.common.error.SystemCode;
import com.general.mq.common.exception.SystemException;
import com.general.mq.common.logger.MQLogger;
import com.general.mq.common.util.conf.MQConfig;

public class EmailSender {
	

	public static void sendMail(String subject,String emailText,String attachFileName,String from){
		MQLogger.l.info("EmailSender sending email");
			
		final String username = MQConfig.MAIL_USERNAME;
		final String password = MQConfig.MAIL_PASSWORD;

		Properties props = new Properties();
		//Properties needs to be configuration driven.
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", MQConfig.MAIL_HOST);
		props.put("mail.smtp.port", MQConfig.MAIL_PORT);

		Session session = Session.getInstance(props,  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from!=null?from:MQConfig.MAIL_USERNAME));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(MQConfig.MAIL_TO_NOTIFY));
			message.setSubject(subject);
			message.setText("Dear Admin,\n\n"+emailText);
			

			// Set the email attachment file
			if(attachFileName!=null){
				MimeBodyPart attachmentPart = new MimeBodyPart();
				FileDataSource fileDataSource = new FileDataSource(attachFileName);
				attachmentPart.setDataHandler(new DataHandler(fileDataSource));
				attachmentPart.setFileName(fileDataSource.getName());
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(attachmentPart);
				message.setContent(multipart);
			}

			Transport.send(message);
			MQLogger.l.info("EmailSender email sent successfully");

		} catch (MessagingException e) {
			MQLogger.l.error("EmailSender email fail with error: "+e);
			throw new SystemException(e,SystemCode.MAIL_SEND_FAIL);
		}
	}


}
