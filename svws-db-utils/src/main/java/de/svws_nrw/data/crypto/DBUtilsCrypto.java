package de.svws_nrw.data.crypto;

import java.util.Base64;

import de.svws_nrw.base.crypto.RSA;
import de.svws_nrw.base.crypto.RSAException;
import de.svws_nrw.db.DBEntityManager;
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
		if ((cred.RSAPrivateKey != null) || (cred.RSAPublicKey != null)) {
			throw new ApiOperationException(Status.BAD_REQUEST,
					"Das Erstellen eines neuen RSA-Schlüsselpaares ist fehlgeschlagen, da bereits ein Schlüsselpaar vorhanden ist.");
		}
		conn.transactionFlush();
		try {
			final var keypair = RSA.createKey();
			cred.RSAPublicKey = Base64.getEncoder().encodeToString(keypair.getPublic().getEncoded());
			cred.RSAPrivateKey = Base64.getEncoder().encodeToString(keypair.getPrivate().getEncoded());
			conn.transactionPersist(cred);
			conn.transactionFlush();
		} catch (@SuppressWarnings("unused") final RSAException e) {
			throw new ApiOperationException(Status.INTERNAL_SERVER_ERROR,
					"Fehler beim erstellen des RSA-Schlüsselpaares für die Credentials mit der ID %d.".formatted(cred.ID));
		}
	}

}
