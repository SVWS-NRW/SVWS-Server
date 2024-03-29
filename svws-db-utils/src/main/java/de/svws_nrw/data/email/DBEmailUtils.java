package de.svws_nrw.data.email;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.base.email.MailSmtpSessionConfig;
import de.svws_nrw.core.data.email.SMTPServerKonfiguration;
import de.svws_nrw.data.benutzer.DataBenutzerEMailDaten;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.benutzer.DTOBenutzerMail;
import de.svws_nrw.db.utils.ApiOperationException;

/**
 * Diese Klasse stellt Hilfsmethoden zur Verfügung, um auf E-Mail-Informationen aus der Datenbank zuzugreifen.
 */
public final class DBEmailUtils {

	/**
	 * Ermittelt die EMail-Konfiguration des aktuellen Benutzer anhand der Datenbank
	 *
	 * @param conn   die Datenbank-Verbindung
	 *
	 * @return die E-Mail-Konfiguration
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static MailSmtpSessionConfig getSMTPConfig(final DBEntityManager conn) throws ApiOperationException {
		try {
			final AES aes = conn.getUser().getAES();
			final SMTPServerKonfiguration daten = DataEmailSMTPServerKonfiguration.getOrCreateSMTPServerKonfiguration(conn);
			final DTOBenutzerMail benutzerDaten = DataBenutzerEMailDaten.getOrCreateDTO(conn);
			final String decodedPassword = new String(aes.decryptBase64(benutzerDaten.SMTPPassword));
			final MailSmtpSessionConfig config = new MailSmtpSessionConfig(daten.host, benutzerDaten.SMTPUsername, decodedPassword);
			config.setPort(daten.port);
			config.setStartTLS(daten.useStartTLS);
			return config;
		} catch (@SuppressWarnings("unused") final AESException e) {
			return null;
		}
	}

}
