package de.svws_nrw.service.crypto.secret;

import javax.crypto.SecretKey;

/**
 * Liefert den symmetrischen Schlüssel für die Ver- und Entschlüsselung von Secrets.
 */
public interface SecretKeyProvider {

	/**
	 * Gibt den symmetrischen Schlüssel zurück. Ist noch kein Schlüssel vorhanden, so wird er erzeugt.
	 *
	 * @return der symmetrische Schlüssel
	 *
	 * @throws de.svws_nrw.db.utils.ApiOperationException   (500) falls der Schlüssel nicht geladen, erzeugt oder gespeichert werden kann
	 */
	SecretKey getKey();

}
