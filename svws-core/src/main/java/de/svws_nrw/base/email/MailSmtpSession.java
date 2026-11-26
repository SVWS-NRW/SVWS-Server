package de.svws_nrw.base.email;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HexFormat;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import jakarta.activation.DataHandler;
import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.SendFailedException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.validation.constraints.NotNull;

import org.eclipse.angus.mail.smtp.SMTPTransport;

/**
 * Dieses Objekt repräsentiert eine Server-Session für den Versand von E-Mails über SMTP
 */
public class MailSmtpSession {

	/** Die Meldung, wenn das Zertifikat des Mailservers die Prüfung gegen den Truststore nicht besteht. */
	static final String MELDUNG_ZERTIFIKAT = "Das Zertifikat des Mailservers ist nicht vertrauenswürdig oder passt nicht zum eingetragenen Host. "
			+ "Über seinen SHA-256-Fingerabdruck in der SMTP-Konfiguration der Schule lässt sich das Zertifikat freigeben, zum Beispiel auch ein selbst "
			+ "erstelltes. Der Server vertraut dann nur noch diesem einen Zertifikat.";

	/** Die Meldung, wenn das Zertifikat des Mailservers nicht zum hinterlegten Fingerabdruck passt. */
	static final String MELDUNG_FINGERABDRUCK = "Das Zertifikat des Mailservers passt nicht zum Fingerabdruck in der SMTP-Konfiguration der Schule.";

	// Die Session
	private final Session session;

	// Gibt an, ob die Session genau einem Zertifikat über seinen Fingerabdruck vertraut
	private final boolean _hatFingerabdruck;

	/**
	 * Erstellt eine neue Session mit der übergebenen Konfiguration. Das Zertifikat des Mailservers wird gegen den Truststore
	 * von Java geprüft, außer die Konfiguration nennt einen Fingerabdruck.
	 *
	 * @param config   Die Konfiguration für den SMTP-Server.
	 */
	public MailSmtpSession(final MailSmtpSessionConfig config) {
		this(config, null);
	}


	/**
	 * Erstellt eine neue Session, die das Zertifikat des Mailservers gegen den übergebenen Truststore prüft, außer die
	 * Konfiguration nennt einen Fingerabdruck. Tests vertrauen so ihrem eigenen Testzertifikat, ohne den Truststore von Java
	 * zu verändern.
	 *
	 * @param config       Die Konfiguration für den SMTP-Server.
	 * @param truststore   die vertrauenswürdigen Zertifikate oder null für den Truststore von Java
	 */
	MailSmtpSession(final MailSmtpSessionConfig config, final KeyStore truststore) {
		final Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", config.isStartTLS() ? "true" : "false");
		// Ohne diese Einstellung sendet die Bibliothek unverschlüsselt weiter, wenn der Mailserver kein STARTTLS anbietet.
		prop.put("mail.smtp.starttls.required", config.isStartTLS() ? "true" : "false");
		prop.put("mail.smtp.host", config.getHost());
		prop.put("mail.smtp.port", "" + config.getPort());
		// Ohne Zeitgrenzen wartet ein Versand unbegrenzt auf einen hängenden Mailserver und blockiert dabei einen Systemthread.
		prop.put("mail.smtp.connectiontimeout", "" + config.getConnectionTimeout());
		prop.put("mail.smtp.timeout", "" + config.getTimeout());
		prop.put("mail.smtp.writetimeout", "" + config.getTimeout());
		this._hatFingerabdruck = (config.getZertifikatFingerabdruck() != null);
		if (_hatFingerabdruck) {
			// Ein hinterlegter Fingerabdruck bestimmt genau ein Zertifikat. Der Name darin ist dann ohne Bedeutung, und selbst
			// erstellte Zertifikate nennen oft einen internen Namen.
			prop.put("mail.smtp.ssl.socketFactory", erzeugeSocketFactory(new TrustManager[] {
					new FingerabdruckTrustManager(config.getZertifikatFingerabdruck()) }));
			prop.put("mail.smtp.ssl.checkserveridentity", "false");
		} else {
			prop.put("mail.smtp.ssl.socketFactory", erzeugeSocketFactory(erzeugeTrustManager(truststore)));
			prop.put("mail.smtp.ssl.checkserveridentity", "true");
		}
		// Sonst baut die Bibliothek nach einem abgelehnten Zertifikat still eine zweite Verbindung mit der Standardprüfung von
		// Java auf und umgeht so den Fingerabdruck.
		prop.put("mail.smtp.socketFactory.fallback", "false");
		// Gilt für TLS und STARTTLS. Ältere Versionen bleiben aus, auch wenn die Java-Installation sie zulässt.
		prop.put("mail.smtp.ssl.protocols", "TLSv1.3 TLSv1.2");
		if (config.isTLS()) {
			prop.put("mail.smtp.ssl.enable", "true");
		}
		this.session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(config.getUsername(), config.getPassword());
			}
		});
	}


	/**
	 * Gibt die Einstellungen der Mail-Session zurück. Tests prüfen damit Einstellungen, die sich mit einem lokalen Testserver
	 * nicht auslösen lassen.
	 *
	 * @return die Einstellungen der Mail-Session
	 */
	Properties getProperties() {
		return session.getProperties();
	}


	private static TrustManager[] erzeugeTrustManager(final KeyStore truststore) {
		try {
			final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(truststore);
			return tmf.getTrustManagers();
		} catch (final GeneralSecurityException e) {
			throw new IllegalStateException("Die Prüfung des Zertifikats des Mailservers lässt sich nicht einrichten.", e);
		}
	}


	private static SSLSocketFactory erzeugeSocketFactory(final TrustManager[] trustManager) {
		try {
			final SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, trustManager, null);
			return context.getSocketFactory();
		} catch (final GeneralSecurityException e) {
			throw new IllegalStateException("Die Prüfung des Zertifikats des Mailservers lässt sich nicht einrichten.", e);
		}
	}


	/**
	 * Vertraut genau dem Zertifikat mit dem hinterlegten SHA-256-Fingerabdruck. Die Gültigkeitsdauer spielt keine Rolle,
	 * weil die Schule genau dieses Zertifikat bewusst freigegeben hat.
	 */
	private static final class FingerabdruckTrustManager implements X509TrustManager {

		private final byte[] _erwartet;

		FingerabdruckTrustManager(final String fingerabdruck) {
			this._erwartet = HexFormat.ofDelimiter(":").parseHex(fingerabdruck);
		}

		@Override
		public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
			if ((chain == null) || (chain.length == 0)) {
				throw new CertificateException("Der Mailserver hat kein Zertifikat vorgelegt.");
			}
			try {
				final byte[] ist = MessageDigest.getInstance("SHA-256").digest(chain[0].getEncoded());
				if (!MessageDigest.isEqual(_erwartet, ist)) {
					throw new CertificateException("Das Zertifikat des Mailservers passt nicht zum hinterlegten Fingerabdruck.");
				}
			} catch (final NoSuchAlgorithmException e) {
				throw new CertificateException(e);
			}
		}

		@Override
		public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
			throw new CertificateException("Die Session prüft nur Zertifikate von Mailservern.");
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}

	}


	/**
	 * Eine Nachricht, deren Message-ID die Domain des Absenders nennt. Die Bibliothek setzt sonst den internen Hostnamen
	 * des SVWS-Servers ein und gibt ihn damit an jeden Empfänger weiter.
	 */
	private static final class AbsenderMimeMessage extends MimeMessage {

		private final @NotNull String _domain;

		AbsenderMimeMessage(final Session session, final @NotNull String domain) {
			super(session);
			this._domain = domain;
		}

		@Override
		protected void updateMessageID() throws MessagingException {
			setHeader("Message-ID", "<" + UUID.randomUUID() + "@" + _domain + ">");
		}

	}


	/**
	 * Ermittelt die Domain des ersten Absenders für EHLO und Message-ID.
	 *
	 * @param absender   die Absender der Nachricht
	 *
	 * @return die Domain oder ein Adressliteral, falls keine Domain angegeben ist
	 */
	private static @NotNull String ermittleDomain(final @NotNull InternetAddress[] absender) {
		if (absender.length > 0) {
			final String adresse = absender[0].getAddress();
			final int pos = (adresse == null) ? -1 : adresse.lastIndexOf('@');
			if ((pos >= 0) && (pos < (adresse.length() - 1))) {
				return adresse.substring(pos + 1);
			}
		}
		// Ein Adressliteral ist in EHLO und Message-ID zulässig und nennt keinen internen Namen.
		return "[127.0.0.1]";
	}


	/**
	 * Sendet die Nachricht. Die Begrüßung beim Mailserver nennt die Domain des Absenders statt des internen Hostnamens.
	 * Ein abgelehntes Zertifikat erhält eine verständliche Meldung, weil die Meldung der Bibliothek im Protokoll des
	 * Versandauftrags landet.
	 *
	 * @param message   die Nachricht
	 *
	 * @throws MessagingException   falls ein Fehler beim Versenden auftritt
	 */
	private void sende(final @NotNull AbsenderMimeMessage message) throws MessagingException {
		try {
			message.saveChanges();
			final Address[] empfaenger = message.getAllRecipients();
			if ((empfaenger == null) || (empfaenger.length == 0)) {
				throw new SendFailedException("No recipient addresses");
			}
			final Transport transport = session.getTransport("smtp");
			if (transport instanceof final SMTPTransport smtpTransport) {
				smtpTransport.setLocalHost(message._domain);
			}
			try {
				transport.connect();
				transport.sendMessage(message, empfaenger);
			} finally {
				transport.close();
			}
		} catch (final MessagingException e) {
			for (Throwable ursache = e; ursache != null; ursache = ursache.getCause()) {
				// Ein Handshake kann auch an der TLS-Version scheitern. Dann passt die Meldung zum Zertifikat nicht.
				if (ursache instanceof CertificateException) {
					throw new MessagingException(_hatFingerabdruck ? MELDUNG_FINGERABDRUCK : MELDUNG_ZERTIFIKAT, e);
				}
			}
			throw e;
		}
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
			if (addresses == null) {
				return new InternetAddress[0];
			}
			final String tmp = addresses.trim().replace(" ", "");
			if (tmp.isBlank()) {
				return new InternetAddress[0];
			}
			final String[] arrayAddresses = tmp.split(",");
			final Pattern p = Pattern.compile("(.*)<([^<>]*)>\\s*");
			final InternetAddress[] result = new InternetAddress[arrayAddresses.length];
			for (int i = 0; i < arrayAddresses.length; i++) {
				final String addr = arrayAddresses[i];
				final Matcher m = p.matcher(addr);
				result[i] = m.hasMatch()
						? new InternetAddress(EmailUtils.toAsciiDomain(m.group(2)), m.group(1), StandardCharsets.UTF_8.name())
						: new InternetAddress(EmailUtils.toAsciiDomain(addr));
			}
			return result;
		} catch (final UnsupportedEncodingException e) {
			throw new MessagingException("UTF-8 encoding not supported", e);
		}
	}


	private @NotNull AbsenderMimeMessage createMimeMessage(final @NotNull String from, final @NotNull String to, final @NotNull String cc, final @NotNull String bcc, final @NotNull String subject) throws MessagingException {
		final InternetAddress[] absender = convertMailAddresses(from);
		final AbsenderMimeMessage message = new AbsenderMimeMessage(this.session, ermittleDomain(absender));
		message.addFrom(absender);
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
		final @NotNull AbsenderMimeMessage message = createMimeMessage(from, to, "", "", subject);

		final Multipart multipart = new MimeMultipart();
		addBody(multipart, text);
		message.setContent(multipart);

		sende(message);
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
		final @NotNull AbsenderMimeMessage message = createMimeMessage(from, to, "", "", subject);

		final Multipart multipart = new MimeMultipart();
		addBody(multipart, text);
		addAttachment(multipart, attachment);
		message.setContent(multipart);

		sende(message);
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
		final @NotNull AbsenderMimeMessage message = createMimeMessage(from, to, "", "", subject);

		final Multipart multipart = new MimeMultipart();
		addBody(multipart, text);

		for (int i = 0; i < attachments.size(); i++) {
			addAttachment(multipart, attachments.get(i));
		}

		message.setContent(multipart);
		sende(message);
	}

}
