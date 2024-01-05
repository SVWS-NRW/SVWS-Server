package de.svws_nrw.db.schema.revisionen;

import java.util.Base64;
import java.util.List;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.base.crypto.RSA;
import de.svws_nrw.base.crypto.RSAException;
import de.svws_nrw.core.logger.Logger;
import de.svws_nrw.db.DBDriver;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.schema.Schema;
import de.svws_nrw.db.schema.SchemaRevisionUpdateSQL;
import de.svws_nrw.db.schema.SchemaRevisionen;

/**
 * Diese Klasse enthält die SQL-Befehle für Revisions-Updates
 * auf Revision 2.
 */
public final class Revision2Updates extends SchemaRevisionUpdateSQL {

	/**
	 * Erzeugt eine Instanz für die Revisions-Updates
	 * für Revision 2.
	 */
	public Revision2Updates() {
		super(SchemaRevisionen.REV_2);
		add("Initialisierung Kurs_Schueler: Entfernen von Einträgen (sollte keiner vorhanden sein...)",
			"DELETE FROM Kurs_Schueler",
			Schema.tab_Kurs_Schueler
		);
		add("Entfernen fehlerhafter Kurs-Einträge in den Leistungsdaten (Zuordnung zu Lernabschnitten)",
			"""
			UPDATE SchuelerLeistungsdaten a
			SET Kurs_ID = NULL
			WHERE (SELECT Schuljahresabschnitts_ID FROM Kurse WHERE ID = a.Kurs_ID)
			    <> (SELECT Schuljahresabschnitts_ID FROM SchuelerLernabschnittsdaten WHERE ID = a.Abschnitt_ID)
			""",
			DBDriver.SQLITE,
			"""
			UPDATE SchuelerLeistungsdaten
			SET Kurs_ID = NULL
			WHERE (SELECT Schuljahresabschnitts_ID FROM Kurse WHERE Kurse.ID = SchuelerLeistungsdaten.Kurs_ID)
			    <> (SELECT Schuljahresabschnitts_ID FROM SchuelerLernabschnittsdaten WHERE SchuelerLernabschnittsdaten.ID = SchuelerLeistungsdaten.Abschnitt_ID)
			""",
			Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_SchuelerLeistungsdaten
		);
        add("Entfernen fehlerhafter Kurs-Einträge in den Leistungsdaten (Kurs mit nicht passenden Fächern)",
        	"UPDATE SchuelerLeistungsdaten JOIN Kurse ON SchuelerLeistungsdaten.Kurs_ID = Kurse.ID "
        		+ "SET SchuelerLeistungsdaten.Kurs_ID = NULL "
        		+ "WHERE SchuelerLeistungsdaten.Fach_ID != Kurse.Fach_ID;",
        	Schema.tab_SchuelerLeistungsdaten, Schema.tab_Kurse
        );
		add("Initialisierung Kurs_Schueler: Befüllen mit Daten",
			"""
			INSERT INTO Kurs_Schueler
			SELECT DISTINCT
			    Kurse.ID AS Kurs_ID,
			    Schueler.ID AS Schueler_ID,
			    SchuelerLernabschnittsdaten.WechselNr AS LernabschnittWechselNr
			FROM
			    Kurse JOIN SchuelerLeistungsdaten ON Kurse.ID = SchuelerLeistungsdaten.Kurs_ID
			        JOIN SchuelerLernabschnittsdaten ON SchuelerLeistungsdaten.Abschnitt_ID = SchuelerLernabschnittsdaten.ID
			        JOIN Schueler ON SchuelerLernabschnittsdaten.Schueler_ID = Schueler.ID
			""",
			Schema.tab_Kurs_Schueler, Schema.tab_Schueler, Schema.tab_SchuelerLernabschnittsdaten, Schema.tab_SchuelerLeistungsdaten
		);
	}


	@Override
	public boolean runLast(final DBEntityManager conn, final Logger logger) {
		if (conn.getDBDriver() != DBDriver.MARIA_DB) {
			logger.logLn("DBMS wird für dieses Datenbank Revisions-Update nicht unterstützt.");
			return false;
		}
		logger.logLn("Erzeuge eine AES-Schlüssel und ein RSA-Schlüsselpaar für die Schule.");
		// Bestimme die Schulnummer
		final List<String> rows = conn.queryNative("SELECT SchulNr FROM EigeneSchule");
		if ((rows == null) || (rows.size() != 1)) {
			logger.logLn(2, "Es konnte keine Schulnummer ermittelt werden.");
			return true;
		}
		final int schulnummer;
		try {
			schulnummer = Integer.parseInt(rows.get(0));
		} catch (@SuppressWarnings("unused") final NumberFormatException e) {
			logger.logLn(2, "Die Schulnummer ist ungültig.");
			return false;
		}
		if ((schulnummer < 100000) || (schulnummer >= 1000000)) {
			logger.logLn(2, "Die Schulnummer ist ungültig.");
			return false;
		}
		// Bestimme die Credentials für die Schule
		final List<Object[]> credentialsSchule = conn.queryNative("SELECT Schulnummer, RSAPublicKey, RSAPrivateKey, AES FROM SchuleCredentials WHERE Schulnummer = " + schulnummer);
		String keyRSAPublic = null;
		String keyRSAPrivate = null;
		String keyAES = null;
		boolean updateRSA = false;
		boolean updateAES = false;
		boolean updateCreds = false;
		if ((credentialsSchule != null) && (!credentialsSchule.isEmpty())) {
			updateCreds = true;
			final var creds = credentialsSchule.get(0);
			keyRSAPublic = (String) creds[1];
			keyRSAPrivate = (String) creds[2];
			keyAES = (String) creds[3];
			updateRSA = ((keyRSAPrivate == null) || (keyRSAPrivate.isBlank()) || (keyRSAPublic == null) || (keyRSAPublic.isBlank()));
			updateAES = ((keyAES == null) || (keyAES.isBlank()));
		}
		// Erzeuge ggf. einen AES-Schlüssel
		if (updateAES || (!updateCreds)) {
			try {
				keyAES = Base64.getEncoder().encodeToString(AES.getRandomKey256().getEncoded());
				final String sql = updateCreds
						? "UPDATE SchuleCredentials SET AES = '%s' WHERE Schulnummer = %d".formatted(keyAES, schulnummer)
						: "INSERT INTO SchuleCredentials (Schulnummer, AES) VALUE (%d, '%s')".formatted(schulnummer, keyAES);
				if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
					logger.logLn(2, "Fehler beim Schreiben des AES-Schlüssel der Schule");
					return false;
				}
			} catch (@SuppressWarnings("unused") final AESException e) {
				logger.logLn(2, "Fehler beim Erzeugen des AES-Schlüssels für die Schule.");
				return false;
			}
		}
		// Erzeuge ggf. ein RSA-Schlüsselpaar
		if (updateRSA || (!updateCreds)) {
			try {
				final var keypair = RSA.createKey();
				keyRSAPublic = Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded());
				keyRSAPrivate = Base64.getEncoder().encodeToString(keypair.getPrivate().getEncoded());
				final String sql = "UPDATE SchuleCredentials SET RSAPublicKey = '%s', RSAPrivateKey = '%s' WHERE Schulnummer = %d".formatted(keyRSAPublic, keyRSAPrivate, schulnummer);
				if (Integer.MIN_VALUE == conn.transactionNativeUpdateAndFlush(sql)) {
					logger.logLn(2, "Fehler beim Schreiben des RSA-Schlüsselpaares der Schule");
					return false;
				}
			} catch (@SuppressWarnings("unused") final RSAException e) {
				logger.logLn(2, "Fehler beim Erzeugen des RSA-Schlüsselpaares für die Schule.");
				return false;
			}
		}
		return true;
	}

}
