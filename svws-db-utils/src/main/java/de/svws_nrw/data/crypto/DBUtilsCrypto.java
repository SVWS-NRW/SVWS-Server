package de.svws_nrw.data.crypto;

import java.text.Normalizer;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.base.crypto.Passwords;
import de.svws_nrw.base.crypto.RSA;
import de.svws_nrw.base.crypto.RSAException;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentials;
import de.svws_nrw.db.utils.ApiOperationException;
import jakarta.ws.rs.core.Response.Status;

/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf Informationen
 * von Schülern zur Verfügung.
 */
public final class DBUtilsCrypto {

	private DBUtilsCrypto() {
		throw new IllegalStateException("Instantiation of " + DBUtilsCrypto.class.getName() + " not allowed");
	}

	private static String nameToAscii(final String name) {
		final String keineUmlaute = name.trim().replaceAll("Ä", "Ae").replaceAll("Ö", "Oe").replaceAll("Ü", "Ue").replaceAll("ä", "ae").replaceAll("ö", "oe").replaceAll("ü", "ue").replaceAll("ß", "ss");
		final String normalized = Normalizer.normalize(keineUmlaute, Normalizer.Form.NFD).replace('\u0141', 'L').replace('\u0142', 'l');
		final Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		final String noAccents = pattern.matcher(normalized).replaceAll("");
		final String asciiOnly = noAccents.replaceAll("[^\\x20-\\x7E]", "");
		return asciiOnly.replaceAll("\\s+", "").replace("-", "").toLowerCase();
	}

	private static String determineUsername(final String nachname, final String vorname, final int maxlen, final Set<String> existingUsernames) {
		final String vn = nameToAscii(vorname);
		String nn = nameToAscii(nachname);
		if (nn.length() > maxlen - 2)
			nn = nn.substring(0, maxlen - 2);
		// Erster Versuch mit vorname.nachname
		String username = vn + "." + nn;
		if ((username.length() <= maxlen) && (!existingUsernames.contains(username)))
			return username;
		// Zweiter Versuch mit v.nachname
		final String vn1 = vn.substring(0, 1);
		username = vn1 + "." + nn;
		if ((username.length() <= maxlen) && (!existingUsernames.contains(username)))
			return username;
		// Dritter Versuch mit vo.nachname
		if (vn.length() > 1) {
			username = vn.substring(0, 2) + "." + nn;
			if ((username.length() <= maxlen) && (!existingUsernames.contains(username)))
				return username;
		}
		// Und dann Versuch mit vX.nachname, wobei X hochgezählt wird und nachname ggf. gekürzt wird.
		long value = 1;
		while (value > 0) {
			final String nummer = "" + value;
			if (nn.length() > (maxlen - (2 + nummer.length())))
				nn = nn.substring(0, (maxlen - (2 + nummer.length())));
			username = vn1 + nummer + "." + nn;
			if ((username.length() <= maxlen) && (!existingUsernames.contains(username)))
				return username;
			value++;
		}
		throw new RuntimeException("Kann keinen Benutzernamen ermitteln.");
	}


	private static String determinePseudonym(final String praefix, final long id, final Set<String> existingPseudonyms) {
		final String pseudonym = praefix + id;
		if (existingPseudonyms.contains(pseudonym))
			throw new RuntimeException("Kann kein Pseudonym für den Benutzer erstellen.");
		return pseudonym;
	}


	/**
	 * Ermittelt die Credentials des Schülers aus der Datenbank ein und gibt diese zurück.
	 * Sollten noch keine in der Datenbank vorhanden sein, so werden neue in der Datenbank angelegt.
	 * Dabei wird ein Benutzename, ein Benutzer-Pseudonym, ein Initialkennwort und der zugehörige
	 * Password-Hash erzeugt.
	 *
	 * Für die Kommunikation mit der Datenbank wird die angegebene Verbindung genutzt, welche eine
	 * aktive Transaktion haben muss.
	 *
	 * @param conn   die Datenbank-Verbindung
	 * @param id     die ID des Schülers
	 *
	 * @return die Credentials
	 *
	 * @throws ApiOperationException falls ein Fehler auftritt
	 */
	public static DTOCredentials getOrCreateSchuelerCredentials(final DBEntityManager conn, final long id) throws ApiOperationException {
		conn.transactionFlush();
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
			throw new ApiOperationException(Status.NOT_FOUND, "Der Schüler mit der ID %d konnte in der Datenbank nicht gefunden werden.".formatted(id));
		final DTOCredentials cred;
		if (schueler.CredentialID == null) {
			final long credId = conn.transactionGetNextID(DTOCredentials.class);
			final List<DTOCredentials> allCreds = conn.queryAll(DTOCredentials.class);
			final Set<String> allUsernames = allCreds.stream().map(c -> c.Benutzername).collect(Collectors.toSet());
			final Set<String> allUserPseudonyms = allCreds.stream().map(c -> c.BenutzernamePseudonym).collect(Collectors.toSet());
 			cred = new DTOCredentials(credId, determineUsername(schueler.Vorname, schueler.Nachname, 16, allUsernames));
			cred.BenutzernamePseudonym = determinePseudonym("s", credId, allUserPseudonyms);
			cred.Initialkennwort = Passwords.generateRandomPassword(12);
			cred.PasswordHash = Benutzer.erstellePasswortHash(cred.Initialkennwort);
			cred.RSAPublicKey = null;
			cred.RSAPrivateKey = null;
			cred.AES = null;
			conn.transactionPersist(cred);
			conn.transactionFlush();
			schueler.CredentialID = credId;
			conn.transactionPersist(schueler);
		} else {
			cred = conn.queryByKey(DTOCredentials.class, schueler.CredentialID);
		}
		conn.transactionFlush();
		return cred;
	}


	/**
	 * Fügt einen RSA-Schlüsselpaar zu den angebenen Credentials hinzu, sofern noch keines vorhanden
	 * ist, und persistiert dieses mithilfe der angegebenen Verbindung in der Datenbank.
	 * Die Verbindung muss dabei eine aktive Transaktion haben.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param cred   die anzupassenden Credentials
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static void addRSAKeyPair(final DBEntityManager conn, final DTOCredentials cred) throws ApiOperationException {
		if ((cred.RSAPrivateKey != null) || (cred.RSAPublicKey != null))
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Erstellen eines neuen RSA-Schlüsselpaares ist fehlgeschlagen, da bereits ein Schlüsselpaar vorhanden ist.");
		conn.transactionFlush();
		try {
			final var keypair = RSA.createKey();
			cred.RSAPublicKey = Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded());
			cred.RSAPrivateKey = Base64.getEncoder().encodeToString(keypair.getPrivate().getEncoded());
			conn.transactionPersist(cred);
			conn.transactionFlush();
		} catch (@SuppressWarnings("unused") final RSAException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim erstellen des RSA-Schlüsselpaares für die Credentials mit der ID %d.".formatted(cred.ID));
		}
	}


	/**
	 * Fügt einen AES-Schlüssel zu den angebenen Credentials hinzu, sofern noch keiner vorhanden
	 * ist, und persistiert diesen mithilfe der angegebenen Verbindung in der Datenbank.
	 * Die Verbindung muss dabei eine aktive Transaktion haben.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param cred   die anzupassenden Credentials
	 *
	 * @throws ApiOperationException   im Fehlerfall
	 */
	public static void addAESKey(final DBEntityManager conn, final DTOCredentials cred) throws ApiOperationException {
		if (cred.AES != null)
			throw new ApiOperationException(Status.BAD_REQUEST, "Das Erstellen eines neuen AES-Schlüssel ist fehlgeschlagen, da bereits ein Schlüssel vorhanden ist.");
		conn.transactionFlush();
		try {
			cred.AES = Base64.getEncoder().encodeToString(AES.getRandomKey256().getEncoded());
			conn.transactionPersist(cred);
			conn.transactionFlush();
		} catch (@SuppressWarnings("unused") final AESException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR, "Fehler beim erstellen des AES-Schlüssels für die Credentials mit der ID %d.".formatted(cred.ID));
		}
	}

}
