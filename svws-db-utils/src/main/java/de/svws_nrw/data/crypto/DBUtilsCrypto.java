package de.svws_nrw.data.crypto;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.security.SecureRandom;
import java.util.Random;

import de.svws_nrw.base.crypto.AES;
import de.svws_nrw.base.crypto.AESException;
import de.svws_nrw.base.crypto.RSA;
import de.svws_nrw.base.crypto.RSAException;
import de.svws_nrw.db.Benutzer;
import de.svws_nrw.db.DBEntityManager;
import de.svws_nrw.db.dto.current.schild.schueler.DTOSchueler;
import de.svws_nrw.db.dto.current.svws.auth.DTOCredentials;
import de.svws_nrw.db.utils.OperationError;
import jakarta.ws.rs.WebApplicationException;

/**
 * Diese Klasse stellt Hilfsmethoden für den Zugriff auf Informationen
 * von Schülern zur Verfügung.
 */
public final class DBUtilsCrypto {

	private static final Random random = new SecureRandom();

	private DBUtilsCrypto() {
		throw new IllegalStateException("Instantiation of " + DBUtilsCrypto.class.getName() + " not allowed");
	}

	private static String generateRandomPassword(final int length) {
		// define char types and order
		final List<Integer> types = new ArrayList<>();
		types.add(0); // special char
		types.add(1); // digit
		types.add(2); // lowercase
		types.add(6); // uppercase
		for (int i = 4; i < length; i++)
			types.add(random.nextInt(10));
		Collections.shuffle(types);
		// randomly create chars by type and order
		final char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = switch (types.get(i)) {
				case 0 -> (char) random.nextInt(33, 45); // special char
				case 1 -> (char) random.nextInt(48, 58); // digit
				case 2, 3, 4, 5 -> (char) random.nextInt(97, 123); // lowercase
				default -> (char) random.nextInt(65, 91); // uppercase
			};
		}
		return new String(chars);
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
	 * @throws WebApplicationException falls ein Fehler auftritt
	 */
	public static DTOCredentials getOrCreateSchuelerCredentials(final DBEntityManager conn, final long id) throws WebApplicationException {
		conn.transactionFlush();
		final DTOSchueler schueler = conn.queryByKey(DTOSchueler.class, id);
		if (schueler == null)
			throw OperationError.NOT_FOUND.exception("Der Schüler mit der ID %d konnte in der Datenbank nicht gefunden werden.".formatted(id));
		final DTOCredentials cred;
		if (schueler.CredentialID == null) {
			final long credId = conn.transactionGetNextID(DTOCredentials.class);
			final List<DTOCredentials> allCreds = conn.queryAll(DTOCredentials.class);
			final Set<String> allUsernames = allCreds.stream().map(c -> c.Benutzername).collect(Collectors.toSet());
			final Set<String> allUserPseudonyms = allCreds.stream().map(c -> c.BenutzernamePseudonym).collect(Collectors.toSet());
 			cred = new DTOCredentials(credId, determineUsername(schueler.Vorname, schueler.Nachname, 16, allUsernames));
			cred.BenutzernamePseudonym = determinePseudonym("s", credId, allUserPseudonyms);
			cred.Initialkennwort = generateRandomPassword(12);
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
	 */
	public static void addRSAKeyPair(final DBEntityManager conn, final DTOCredentials cred) {
		if ((cred.RSAPrivateKey != null) || (cred.RSAPublicKey != null))
			throw OperationError.BAD_REQUEST.exception("Das Erstellen eines neuen RSA-Schlüsselpaares ist fehlgeschlagen, da bereits ein Schlüsselpaar vorhanden ist.");
		conn.transactionFlush();
		try {
			final var keypair = RSA.createKey();
			cred.RSAPublicKey = Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded());
			cred.RSAPrivateKey = Base64.getEncoder().encodeToString(keypair.getPrivate().getEncoded());
			conn.transactionPersist(cred);
			conn.transactionFlush();
		} catch (@SuppressWarnings("unused") final RSAException e) {
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Fehler beim erstellen des RSA-Schlüsselpaares für die Credentials mit der ID %d.".formatted(cred.ID));
		}
	}


	/**
	 * Fügt einen AES-Schlüssel zu den angebenen Credentials hinzu, sofern noch keiner vorhanden
	 * ist, und persistiert diesen mithilfe der angegebenen Verbindung in der Datenbank.
	 * Die Verbindung muss dabei eine aktive Transaktion haben.
	 *
	 * @param conn   die Datenbankverbindung
	 * @param cred   die anzupassenden Credentials
	 */
	public static void addAESKey(final DBEntityManager conn, final DTOCredentials cred) {
		if (cred.AES != null)
			throw OperationError.BAD_REQUEST.exception("Das Erstellen eines neuen AES-Schlüssel ist fehlgeschlagen, da bereits ein Schlüssel vorhanden ist.");
		conn.transactionFlush();
		try {
			cred.AES = Base64.getEncoder().encodeToString(AES.getRandomKey256().getEncoded());
			conn.transactionPersist(cred);
			conn.transactionFlush();
		} catch (@SuppressWarnings("unused") final AESException e) {
			throw OperationError.INTERNAL_SERVER_ERROR.exception("Fehler beim erstellen des AES-Schlüssels für die Credentials mit der ID %d.".formatted(cred.ID));
		}
	}

}
