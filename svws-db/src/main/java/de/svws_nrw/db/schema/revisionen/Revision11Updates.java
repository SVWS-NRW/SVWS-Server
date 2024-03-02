package de.svws_nrw.db.schema.revisionen;

import java.util.List;

import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 11.
 */
public final class Revision11Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 11.
	 */
	public Revision11Updates() {
		super(SchemaRevisionen.REV_11);
	}


	@Override
	public boolean runLast(final DBEntityManager conn, final Logger logger) {
		if (conn.getDBDriver() != DBDriver.MARIA_DB) {
			logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
			return false;
		}

		// Ermittle ggf. eine SMTP-Server-Einstellung aus Schild2-Einstellungen und übernimmt diese in die Konfiguration des SVWS-Servers
		logger.logLn("- Ermittle eine ggf. bestehende Email-Konfiguration aus den SchildNRW 2 - Einstellungen ...");
		final List<String> tmpEinstellungen = conn.queryNative("SELECT Wert FROM Client_Konfiguration_Global WHERE AppName='Schild2' AND Schluessel='Einstellungen'");
		if (tmpEinstellungen.isEmpty())
			return true;
		final String einstellungen = tmpEinstellungen.get(0);
		final List<String> einstellungenSMTP = einstellungen.lines().filter(s -> s.contains("SMTP")).toList();
		if (einstellungenSMTP.isEmpty())
			return true;
		boolean hasSMTP = false;
		String host = "";
		int port = 25;
		boolean useTLS = false;
		boolean useStartTLS = true;
		for (final String einstellung : einstellungenSMTP) {
			final String[] keyvalue = einstellung.split("=");
			if (keyvalue.length != 2)
				continue;
			switch (keyvalue[0]) {
				case "SMTPMail" -> hasSMTP = (!"0".equals(keyvalue[1]));
				case "SMTPServer" -> host = keyvalue[1];
				case "SMTPPort" -> {
					try {
						port = Integer.parseInt(keyvalue[1]);
						if ((port < 1) || (port > 65535))
							port = 25;
					} catch (@SuppressWarnings("unused") final Exception e) {
						port = 25;
					}
				}
				case "SMTPSSL" -> useTLS = (!"0".equals(keyvalue[1]));
				case "SMTPStartTLS" -> useStartTLS = (!"0".equals(keyvalue[1]));
				default -> { /* nicht zu tun */ }
			}
		}
		if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush("INSERT INTO EigeneSchule_Email(ID, SMTPServer, SMTPPort, SMTPStartTLS, SMTPUseTLS, SMTPTrustTLSHost) VALUES (%d, '%s', %d, %d, %d, %s)".formatted(
					1,
					hasSMTP ? host : "",
					hasSMTP ? port : 25,
					hasSMTP && !useStartTLS ? 0 : 1,
					hasSMTP && useTLS ? 1 : 0,
					hasSMTP && useTLS ? "'*'" : "null"
				))) {
			logger.logLn(2, "Fehler beim Erstellen der SMTP-Server-Konfiguration.");
			return false;
		}
		return true;
	}

}
