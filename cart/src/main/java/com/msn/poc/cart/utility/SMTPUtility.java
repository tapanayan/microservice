package com.msn.poc.cart.utility;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

public class SMTPUtility {
	private static final Logger LOGGER = Logger.getLogger(SMTPUtility.class);
	private static final String SEND_GRID_KEY = "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";

	private SMTPUtility() {
		//
	}

    public static void sendSupportNotification( String to1, String body1 )
    {
        String from1 = "support@dfims.com";
        String subject = "URGENT : Please take immediate action ";
        send( to1, from1, subject, body1 );
    }

    public static void send( String to1, String from1, String subject1, String body1 )
    {
		Email from = new Email(from1);
		String subject = subject1;
		Email to = new Email(to1);
		Content content = new Content("text/html", body1);
		Mail mail = new Mail(from, subject, to, content);
		SendGrid sg = new SendGrid(SEND_GRID_KEY);
		Request request = new Request();
        try
        {
			LOGGER.info("FROM::" + from1);
			LOGGER.info("TO::" + to1);
			LOGGER.info("BODY::" + body1);
			System.out.println("TO::" + to1);
			System.out.println("BODY::" + body1);
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			LOGGER.info("Message sent"+response);
        }
        catch ( IOException ex )
        {
			ex.printStackTrace();
		}
	}
}
