package de.svws_nrw.base.email;

import java.io.UnsupportedEncodingException;
import java.net.IDN;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * @param config   Die Konfiguration für den SMTP-Server.
	 */
	public MailSmtpSession(final MailSmtpSessionConfig config) {
		final Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", config.isStartTLS() ? "true" : "false");
		prop.put("mail.smtp.host", config.getHost());
		prop.put("mail.smtp.port", "" + config.getPort());
		prop.put("mail.smtp.ssl.trust", config.getHost());
		if (config.isTLS()) {
			prop.put("mail.smtp.ssl.enable", "true");
			prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
		}
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


	private static void addAttachment(final Multipart multipart, final @NotNull EmailJobAttachment attachment)
			throws MessagingException {
		final MimeBodyPart part = new MimeBodyPart();
		final ByteArrayDataSource ds = new ByteArrayDataSource(attachment.data, attachment.mimetype);
		part.setDataHandler(new DataHandler(ds));
		part.setFileName(attachment.filename);
		multipart.addBodyPart(part);
	}


	private static @NotNull InternetAddress[] convertMailAddresses(final String addresses) throws MessagingException {
		try {
			if (addresses == null)
				return new InternetAddress[0];
			final String tmp = addresses.trim().replace(" ", "");
			if (tmp.isBlank())
				return new InternetAddress[0];
			final String[] arrayAddresses = tmp.split(",");
			final Pattern p = Pattern.compile("(.*)<([^<>]*)>\\s*");
			final InternetAddress[] result = new InternetAddress[arrayAddresses.length];
			for (int i = 0; i < arrayAddresses.length; i++) {
				final String addr = arrayAddresses[i];
				final Matcher m = p.matcher(addr);
				result[i] = m.hasMatch()
						? new InternetAddress(IDN.toASCII(m.group(2)), m.group(1), StandardCharsets.UTF_8.name())
						: new InternetAddress(IDN.toASCII(addr));
			}
			return result;
		} catch (final UnsupportedEncodingException e) {
			throw new MessagingException("UTF-8 encoding not supported", e);
		}
	}


	private @NotNull Message createMimeMessage(final @NotNull String from, final @NotNull String to, final @NotNull String cc, final @NotNull String bcc, final @NotNull String subject) throws MessagingException {
		final Message message = new MimeMessage(this.session);
		message.addFrom(convertMailAddresses(from));
		message.setRecipients(Message.RecipientType.TO, convertMailAddresses(to));
		message.addRecipients(Message.RecipientType.CC, convertMailAddresses(cc));
		message.addRecipients(Message.RecipientType.BCC, convertMailAddresses(bcc));
		message.setSubject(subject);
		return message;
	}


	/**
	 * Sendet eine Text-Nachricht über diese Session, mit den angegebenen Informationen
	 *
	 * @param from      die Adresse, von der die Mail versendet wird.
	 * @param to        die Adresse, zu der die Mail gesendet wird.
	 * @param subject   der Betreff der Nachricht.
	 * @param text      der Text der Nachricht.
	 *
	 * @throws MessagingException   falls ein Fehler bei dem Versenden der Nachricht auftritt.
	 */
	public void sendTextMessage(final @NotNull String from, final @NotNull String to, final @NotNull String subject, final @NotNull String text)
			throws MessagingException {
		final @NotNull Message message = createMimeMessage(from, to, "", "", subject);

		final Multipart multipart = new MimeMultipart();
		addBody(multipart, text);
		message.setContent(multipart);

		Transport.send(message);
	}


	/**
	 * Sendet eine Text-Nachricht über diese Session, mit den angegebenen Informationen und einem Anhang
	 *
	 * @param from         die Adresse, von der die Mail versendet wird.
	 * @param to           die Adresse, zu der die Mail gesendet wird.
	 * @param subject      der Betreff der Nachricht.
	 * @param text         der Text der Nachricht.
	 * @param attachment   die Informationen zum E-Mail-Attachment
	 *
	 * @throws MessagingException   falls ein Fehler bei dem Versenden der Nachricht auftritt.
	 */
	public void sendTextMessageWithAttachment(final @NotNull String from, final @NotNull String to, final @NotNull String subject, final @NotNull String text,
			final @NotNull EmailJobAttachment attachment) throws MessagingException {
		final @NotNull Message message = createMimeMessage(from, to, "", "", subject);

		final Multipart multipart = new MimeMultipart();
		addBody(multipart, text);
		addAttachment(multipart, attachment);
		message.setContent(multipart);

		Transport.send(message);
	}

	/**
	 * Sendet eine Text-Nachricht über diese Session mit mehreren Anhängen.
	 *
	 * @param from          die Adresse, von der die Mail versendet wird.
	 * @param to            die Adresse, zu der die Mail gesendet wird.
	 * @param subject       der Betreff der Nachricht.
	 * @param text          der Text der Nachricht.
	 * @param attachments   die Liste mit den Attachments.
	 *
	 * @throws MessagingException   falls ein Fehler beim Versenden auftritt.
	 */
	public void sendTextMessageWithAttachments(final @NotNull String from, final @NotNull String to, final @NotNull String subject,
			final @NotNull String text, final @NotNull List<EmailJobAttachment> attachments) throws MessagingException {
		final @NotNull Message message = createMimeMessage(from, to, "", "", subject);

		final Multipart multipart = new MimeMultipart();
		addBody(multipart, text);

		for (int i = 0; i < attachments.size(); i++)
			addAttachment(multipart, attachments.get(i));

		message.setContent(multipart);
		Transport.send(message);
	}

}
