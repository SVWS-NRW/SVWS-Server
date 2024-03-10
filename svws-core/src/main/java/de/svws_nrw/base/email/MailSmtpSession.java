package de.svws_nrw.base.email;

import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.validation.constraints.NotNull;

/**
 * Dieses Objekt repräsentiert eine Server-Session für den Versand von E-Mails über SMTP
 */
public class MailSmtpSession {

	// Die Session
	private final Session session;

	/**
	 * Erstellt eine neue Session mit der übergebenen Konfiguration
	 *
	 * @param config   die Konfiguration für den SMTP-Server
	 */
	public MailSmtpSession(final MailSmtpSessionConfig config) {
		final Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", config.isStartTLS() ? "true" : "false");
		prop.put("mail.smtp.host",  config.getHost());
		prop.put("mail.smtp.port", "" + config.getPort());
		prop.put("mail.smtp.ssl.trust", config.getHost());
		this.session = Session.getInstance(prop, new Authenticator() {
		    @Override
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(config.getUsername(), config.getPassword());
		    }
		});
	}


	private static void addBody(final Multipart multipart, final @NotNull String text) throws MessagingException {
		final MimeBodyPart body = new MimeBodyPart();
		body.setContent(text, "text/html; charset=utf-8");
		multipart.addBodyPart(body);
	}


	private static void addAttachment(final Multipart multipart, final @NotNull byte[] data, final @NotNull String mimeType, final @NotNull String filename) throws MessagingException {
		final MimeBodyPart attachment = new MimeBodyPart();
		final ByteArrayDataSource ds = new ByteArrayDataSource(data, mimeType);
		attachment.setDataHandler(new DataHandler(ds));
		attachment.setFileName(filename);
		multipart.addBodyPart(attachment);
	}


	/**
	 * Sendet eine Text-Nachricht über diese Session, mit den angegebenen Informationen
	 *
	 * @param from      die Adresse, von der die Mail versendet wird
	 * @param to        die Adresse, zu der die Mail gesendet wird
	 * @param subject   der Betreff der Nachricht
	 * @param text      der Text der Nachricht
	 *
	 * @throws MessagingException   falls ein Fehler bei dem Versenden der Nachricht auftritt
	 */
	public void sendTextMessage(final @NotNull String from, final @NotNull String to, final @NotNull String subject, final @NotNull String text) throws MessagingException {
		final Message message = new MimeMessage(this.session);
		message.setFrom(new InternetAddress(from));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		message.setSubject(subject);

		final Multipart multipart = new MimeMultipart();
		addBody(multipart, text);
		message.setContent(multipart);

		Transport.send(message);
	}


	/**
	 * Sendet eine Text-Nachricht über diese Session, mit den angegebenen Informationen und einem Anhang
	 *
	 * @param from      die Adresse, von der die Mail versendet wird
	 * @param to        die Adresse, zu der die Mail gesendet wird
	 * @param subject   der Betreff der Nachricht
	 * @param text      der Text der Nachricht
	 * @param data      die Binärdaten für das Attachment
	 * @param mimeType  der Mime-Type des Attachments
	 * @param filename  der Datei-Name des Attachments
	 *
	 * @throws MessagingException   falls ein Fehler bei dem Versenden der Nachricht auftritt
	 */
	public void sendTextMessageWithAttachment(final @NotNull String from, final @NotNull String to, final @NotNull String subject, final @NotNull String text,
			final @NotNull byte[] data, final @NotNull String mimeType, final @NotNull String filename) throws MessagingException {
		final Message message = new MimeMessage(this.session);
		message.setFrom(new InternetAddress(from));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		message.setSubject(subject);

		final Multipart multipart = new MimeMultipart();
		addBody(multipart, text);
		addAttachment(multipart, data, mimeType, filename);
		message.setContent(multipart);

		Transport.send(message);
	}

}
