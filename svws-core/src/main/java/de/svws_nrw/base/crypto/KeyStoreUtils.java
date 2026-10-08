package de.svws_nrw.base.crypto;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.StringUtils;


/**
 * Diese Klasse stellt Methoden für den Umgang mit dem Java-Keystore, Zertifikaten, etc. zur Verfügung
 */
public final class KeyStoreUtils {

	private KeyStoreUtils() {
		throw new IllegalStateException("Instantiation not allowed");
	}

	/**
	 * Diese Methode einen neuen leere Java-Keystore (Default Type) zurück.
	 *
	 * @return der Java-Keystore
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn das Erstellen des Keystore nicht möglich ist.
	 */
	public static KeyStore newKeystore() throws KeyStoreException {
		try {
			final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(null, null);
			return keystore;
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new KeyStoreException("Erstellen eines neuen leeren Keystore fehlgeschlagen.", e);
		}
	}

	/**
	 * Diese Methode liefert den aktuell konfigurierten Java-Keystore zurück.
	 *
	 * @param location   der Pfad zu dem Keystore
	 * @param password   das Kennwort für den Zugriff auf den Keystore
	 *
	 * @return der Java-Keystore
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Zugriff auf den Keystore nicht möglich ist.
	 */
	public static KeyStore getKeystore(final String location, final String password) throws KeyStoreException {
		try {
			try (FileInputStream is = new FileInputStream(location)) {
				final KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
				keystore.load(is, password.toCharArray());
				return keystore;
			}
		} catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
			throw new KeyStoreException("Zugriff auf den Keystore fehlgeschlagen.", e);
		}
	}


	/**
	 * Diese Methode liefert den privaten Schlüssel zu dem angegebenen Alias aus dem Keystore.
	 *
	 * @param keystore   der Keystore
	 * @param alias      der Alias des privaten Schlüssels
	 * @param password   das Kennwort für den Zugriff auf den privaten Schlüssel
	 *
	 * @return der private Schlüssel
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Zugriff auf den Keystore oder den privaten Schlüssel nicht möglich ist.
	 */
	public static PrivateKey getPrivateKey(final KeyStore keystore, final String alias, final String password) throws KeyStoreException {
		try {
			final Key key = keystore.getKey(alias, password.toCharArray());
			if (key instanceof final PrivateKey privateKey) {
				return privateKey;
			}
			throw new KeyStoreException("Der private Schlüssel konnte nicht ausgelesen werden.");
		} catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException e) {
			throw new KeyStoreException("", e);
		}
	}


	/**
	 * Liefert das Zertifikat zu dem angegebenen Alias aus dem Keystore.
	 *
	 * @param keystore   der Keystore
	 * @param alias      der Alias des Zertifikats
	 *
	 * @return das Zertifikat
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Zugriff auf den Keystore oder das Zertifikat nicht möglich ist.
	 */
	public static Certificate getCertificate(final KeyStore keystore, final String alias) throws KeyStoreException {
		try {
			return keystore.getCertificate(alias);
		} catch (final KeyStoreException e) {
			throw new KeyStoreException("Der Zugriff auf das Zertifikat ist fehlgeschlagen", e);
		}
	}


	/**
	 * Liefert das Zertifikat zu dem angegebenen Alias aus dem Keystore in Base64-Kodierung.
	 *
	 * @param keystore   der Keystore
	 * @param alias      der Alias des Zertifikats
	 *
	 * @return das Zertifikat
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Zugriff auf den Keystore oder das Zertifikat nicht möglich ist.
	 */
	public static String getCertificateBase64(final KeyStore keystore, final String alias) throws KeyStoreException {
		try {
			final Certificate cert = keystore.getCertificate(alias);
			return "-----BEGIN CERTIFICATE-----\n" + Base64.getMimeEncoder().encodeToString(cert.getEncoded()) + "\n-----END CERTIFICATE-----\n";
		} catch (KeyStoreException | CertificateEncodingException e) {
			throw new KeyStoreException("Der Zugriff auf das Zertifikat ist fehlgeschlagen", e);
		}
	}



	/**
	 * Liefert den öffentlichen Schlüssel des Zertifikats zu dem angegebenen Alias aus dem Keystore.
	 *
	 * @param keystore   der Keystore
	 * @param alias      der Alias des Zertifikats
	 *
	 * @return der öffentliche Schlüssel
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Zugriff auf den Keystore oder den öffentlichen Schlüssel nicht möglich ist.
	 */
	public static PublicKey getPublicKey(final KeyStore keystore, final String alias) throws KeyStoreException {
		return getCertificate(keystore, alias).getPublicKey();
	}


	/**
	 * Liefert den öffentlichen Schlüssel des Zertifikats zu dem angegebenen Alias aus dem Keystore in Base64-Kodierung.
	 *
	 * @param keystore   der Keystore
	 * @param alias      der Alias des Zertifikats
	 *
	 * @return der öffentliche Schlüssel
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Zugriff auf den Keystore oder den öffentlichen Schlüssel nicht möglich ist.
	 */
	public static String getPublicKeyBase64(final KeyStore keystore, final String alias) throws KeyStoreException {
		final PublicKey pubKey = KeyStoreUtils.getPublicKey(keystore, alias);
		return Base64.getMimeEncoder().encodeToString(pubKey.getEncoded());
	}


	/**
	 * Fügt den privaten Schlüssel und das Zertifikat im keystore unter dem angegebenen Alias hinzu. Sind unter dem Alias
	 * schon Schlüssel und/oder Zertifikat vorhanden, so werden sie ersetzt. Anschließend wird der Keystore unter
	 * dem angegebenen Pfad gesichert.
	 *
	 * @param keystore   der Keystore
	 * @param location   der Pfad zu dem Keystore
	 * @param password   das Kennwort für den Zugriff auf den Keystore
	 * @param alias      der Alias
	 * @param key        der private Schlüssel
	 * @param cert       das Zertifikat
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Schlüssel und das Zertifikat nicht zum Keystore hinzugefügt werden können
	 */
	public static void addPrivateKeyCertificateBase64(final KeyStore keystore, final String location, final String password,
			final String alias, final byte[] key, final byte[] cert) throws KeyStoreException {
		if ((alias == null) || alias.isBlank()) {
			throw new KeyStoreException("Für das Hinzufügen muss ein Alias angegeben werden.");
		}
		// Überprüfe den privaten Schlüssel und wandle ihn um
		if ((key == null) || (key.length == 0)) {
			throw new KeyStoreException("Für das Hinzufpgen muss ein privater Schlüssel angegeben werden.");
		}
		String[] tmp = new String(key, StandardCharsets.UTF_8).split("-----BEGIN PRIVATE KEY-----\r*\n*");
		if (tmp.length != 2) {
			throw new KeyStoreException("Der private Schlüssel kann nicht eingelesen werden. Überprüfen sie das Dateiformat.");
		}
		tmp = tmp[1].split("\r*\n*-----END PRIVATE KEY-----");
		if (tmp.length != 2) {
			throw new KeyStoreException("Der private Schlüssel kann nicht eingelesen werden. Überprüfen sie das Dateiformat.");
		}
		final String keyBase64 = tmp[0];
		final byte[] keyRaw = Base64.getMimeDecoder().decode(keyBase64);
		final PrivateKey keyDecoded;
		try {
			final KeyFactory kf = KeyFactory.getInstance("RSA");
			keyDecoded = kf.generatePrivate(new PKCS8EncodedKeySpec(keyRaw));
		} catch (final NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new KeyStoreException("Fehler beim Dekodieren des RSA-Schlüssels.", e);
		}
		// Überprüfe das Zertifikat und wandle es um
		if ((cert == null) || (cert.length == 0)) {
			throw new KeyStoreException("Ein Zertifikat muss angegeben werden.");
		}
		tmp = new String(cert, StandardCharsets.UTF_8).split("-----BEGIN CERTIFICATE-----\r*\n*");
		if (tmp.length != 2) {
			throw new KeyStoreException("Das Zertifikat kann nicht eingelesen werden. Überprüfen sie das Dateiformat.");
		}
		tmp = tmp[1].split("\r*\n*-----END CERTIFICATE-----");
		if (tmp.length != 2) {
			throw new KeyStoreException("Das Zertifikat kann nicht eingelesen werden. Überprüfen sie das Dateiformat.");
		}
		final String certBase64 = tmp[0];
		final byte[] certRaw = Base64.getMimeDecoder().decode(certBase64);
		final Certificate certDecoded;
		try {
			final CertificateFactory cf = CertificateFactory.getInstance("X.509");
			certDecoded = cf.generateCertificate(new ByteArrayInputStream(certRaw));
		} catch (final CertificateException e) {
			throw new KeyStoreException("Fehler beim Dekodieren des Zertifikats.", e);
		}
		// Schreibe den privaten Schlüssel und das Zertifikat in den keystore
		final Certificate[] certChain = new Certificate[1];
		certChain[0] = certDecoded;
		try {
			keystore.setKeyEntry(alias, keyDecoded, password.toCharArray(), certChain);
		} catch (final KeyStoreException e) {
			throw new KeyStoreException("Fehler beim Setzen des Keystore-Eintrags", e);
		}
		try (FileOutputStream os = new FileOutputStream(location, false)) {
			keystore.store(os, password.toCharArray());
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			throw new KeyStoreException("Fehler beim Schreiben des Keystores", e);
		}
	}


	/**
	 * Fügt den privaten Schlüssel und das Zertifikat im Keystore unter dem angegebenen Alias hinzu.
	 * Sind unter dem Alias schon Schlüssel und/oder Zertifikat vorhanden, so werden sie ersetzt.
	 * Anschließend wird der Keystore unter dem angegebenen Pfad gesichert.
	 *
	 * @param keystore   der Keystore
	 * @param location   der Pfad zu dem Keystore
	 * @param password   das Kennwort für den Zugriff auf den Keystore
	 * @param alias      der Alias
	 * @param privateKey der private Schlüssel
	 * @param cert       das Zertifikat
	 *
	 * @throws KeyStoreException die Exception tritt auf, wenn der Schlüssel und das Zertifikat nicht zum Keystore hinzugefügt werden können
	 */
	public static void addPrivateKeyCertificate(final KeyStore keystore, final String location, final String password, final String alias,
			final PrivateKey privateKey, final X509Certificate cert) throws KeyStoreException {
		if ((alias == null) || alias.isBlank()) {
			throw new KeyStoreException("Für das Hinzufügen muss ein Alias angegeben werden.");
		}
		if (privateKey == null) {
			throw new KeyStoreException("Für das Hinzufügen muss ein privater Schlüssel angegeben werden.");
		}
		if (cert == null) {
			throw new KeyStoreException("Für das Hinzufügen muss ein Zertifikat angegeben werden.");
		}

		try {
			// Erstelle die Zertifikatskette (hier bestehend aus dem selbst-signierten Zertifikat)
			final Certificate[] chain = new Certificate[] { cert };

			// Setze den Key-Eintrag im Keystore geschützt mit dem Keystore-Passwort
			keystore.setKeyEntry(alias, privateKey, password.toCharArray(), chain);

			// Schreibe den aktualisierten Keystore zurück in das Dateisystem
			try (FileOutputStream fos = new FileOutputStream(location)) {
				keystore.store(fos, password.toCharArray());
			}
		} catch (NoSuchAlgorithmException | CertificateException | IOException | KeyStoreException e) {
			throw new KeyStoreException("Fehler beim Hinzufügen des privaten Schlüssels und Zertifikats zum Keystore.", e);
		}
	}


	/**
	 * Fügt das angegebene Zertifikat zu dem Keystore unter dem angegebenen Alias hinzu.
	 *
	 * @param keystore   der Keystore
	 * @param alias      der Alias
	 * @param cert       das Zertifikat
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn das Zertifikat nicht zum Keystore hinzugefügt werden kann
	 */
	public static void addCertificateFileBase64(final KeyStore keystore, final String alias, final byte[] cert) throws KeyStoreException {
		try {
			final X509Certificate certDecoded = TLSUtils.decodeCertFileBase64(new String(cert, StandardCharsets.UTF_8));
			addCertificate(keystore, alias, certDecoded);
		} catch (final CertificateException e) {
			throw new KeyStoreException("Fehler beim Dekodieren des TLS-Zertifikates", e);
		}
	}


	/**
	 * Fügt das angegebene Zertifikat zu dem Keystore unter dem angegebenen Alias hinzu.
	 *
	 * @param keystore   der Keystore
	 * @param alias      der Alias
	 * @param cert       das Zertifikat
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn das Zertifikat nicht zum Keystore hinzugefügt werden kann
	 */
	public static void addCertificate(final KeyStore keystore, final String alias, final X509Certificate cert) throws KeyStoreException {
		if ((alias == null) || alias.isBlank()) {
			throw new KeyStoreException("Für das Hinzufügen muss ein Alias angegeben werden.");
		}
		// Überprüfe das Zertifikat und wandle es um
		if (cert == null) {
			throw new KeyStoreException("Ein Zertifikat muss angegeben werden.");
		}
		// Schreibe das Zertifikat unter dem angebenen alias in den Keystore
		try {
			keystore.setCertificateEntry(alias, cert);
		} catch (final KeyStoreException e) {
			throw new KeyStoreException("Fehler beim Setzen des Keystore-Eintrags", e);
		}
	}



	/**
	 * Liefert den symmetrischen Schlüssel zu dem angegebenen Alias aus dem Keystore.
	 *
	 * @param keystore   der Keystore (darf nicht null sein)
	 * @param alias      der Alias des Schlüssels (darf nicht blank sein)
	 * @param password   das Kennwort für den Zugriff auf den Schlüssel (darf nicht null sein)
	 *
	 * @return der symmetrische Schlüssel oder null, falls unter dem Alias kein Eintrag existiert
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn kein Alias angegeben ist, der Eintrag nicht gelesen
	 *                             werden kann oder kein symmetrischer Schlüssel ist
	 * @throws NullPointerException   die Exception tritt auf, wenn der Keystore oder das Kennwort null ist (keine explizite Prüfung)
	 */
	public static SecretKey getSecretKey(final KeyStore keystore, final String alias, final String password) throws KeyStoreException {
		if (StringUtils.isBlank(alias)) {
			throw new KeyStoreException("Für das Auslesen muss ein Alias angegeben werden.");
		}
		if (!keystore.containsAlias(alias)) {
			return null;
		}

		final Key key;
		try {
			key = keystore.getKey(alias, password.toCharArray());
		} catch (final UnrecoverableKeyException | NoSuchAlgorithmException e) {
			throw new KeyStoreException("Der symmetrische Schlüssel konnte nicht aus dem Keystore gelesen werden.", e);
		}
		if (key instanceof final SecretKey secretKey) {
			return secretKey;
		}
		throw new KeyStoreException("Der Eintrag im Keystore ist kein symmetrischer Schlüssel.");
	}


	/**
	 * Fügt den symmetrischen Schlüssel unter dem angegebenen Alias zum Keystore hinzu. Ein vorhandener Eintrag
	 * unter dem Alias wird ersetzt. Der Keystore wird dabei nicht gespeichert (siehe
	 * {@link #storeKeystore(KeyStore, String, String)}).
	 *
	 * @param keystore   der Keystore (darf nicht null sein)
	 * @param alias      der Alias (darf nicht blank sein)
	 * @param key        der symmetrische Schlüssel (darf nicht null sein)
	 * @param password   das Kennwort, mit welchem der Eintrag geschützt wird (darf nicht null sein)
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn Alias oder Schlüssel fehlen oder der Eintrag nicht gesetzt werden kann
	 * @throws NullPointerException   die Exception tritt auf, wenn der Keystore oder das Kennwort null ist (keine explizite Prüfung)
	 */
	public static void setSecretKey(final KeyStore keystore, final String alias, final SecretKey key, final String password) throws KeyStoreException {
		if (StringUtils.isBlank(alias)) {
			throw new KeyStoreException("Für das Hinzufügen muss ein Alias angegeben werden.");
		}
		if (key == null) {
			throw new KeyStoreException("Für das Hinzufügen muss ein symmetrischer Schlüssel angegeben werden.");
		}

		keystore.setEntry(alias, new KeyStore.SecretKeyEntry(key), new KeyStore.PasswordProtection(password.toCharArray()));
	}


	/**
	 * Speichert den Keystore unter dem angegebenen Pfad. Der Keystore wird zunächst in eine temporäre Datei im
	 * Zielverzeichnis geschrieben und diese anschließend atomar an den Zielpfad verschoben, so dass nie eine
	 * unvollständige Keystore-Datei entsteht. Das Zielverzeichnis muss existieren.
	 *
	 * @param keystore   der Keystore (darf nicht null sein)
	 * @param location   der Pfad der Keystore-Datei (darf nicht null sein)
	 * @param password   das Kennwort für den Zugriff auf den Keystore (darf nicht null sein)
	 *
	 * @throws KeyStoreException   die Exception tritt auf, wenn der Keystore nicht geschrieben werden kann
	 * @throws NullPointerException   die Exception tritt auf, wenn der Keystore, der Pfad oder das Kennwort null ist. Die Prüfung
	 *                                erfolgt vor dem Anlegen der temporären Datei, so dass keine Reste im Zielverzeichnis verbleiben.
	 */
	public static void storeKeystore(final KeyStore keystore, final String location, final String password) throws KeyStoreException {
		Objects.requireNonNull(keystore, "Der Keystore darf nicht null sein.");
		Objects.requireNonNull(location, "Der Pfad der Keystore-Datei darf nicht null sein.");
		Objects.requireNonNull(password, "Das Kennwort darf nicht null sein.");

		final Path target = Path.of(location).toAbsolutePath();
		Path tmp = null;
		try {
			tmp = Files.createTempFile(target.getParent(), "keystore", ".tmp");
			try (OutputStream os = Files.newOutputStream(tmp)) {
				keystore.store(os, password.toCharArray());
			}
			moveAtomic(tmp, target);
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			final KeyStoreException ex = new KeyStoreException("Fehler beim Schreiben des Keystores", e);
			deleteTempFile(tmp, ex);
			throw ex;
		}
	}


	private static void moveAtomic(final Path source, final Path target) throws IOException {
		try {
			Files.move(source, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
		} catch (final AtomicMoveNotSupportedException e) {
			Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
		}
	}


	private static void deleteTempFile(final Path tmp, final Exception cause) {
		if (tmp == null) {
			return;
		}
		try {
			Files.deleteIfExists(tmp);
		} catch (final IOException e) {
			cause.addSuppressed(e);
		}
	}

	// TODO ggf. Implementierung über alternativen Code zum Einlesen von PKCS12-Dateien
	//	try {
	//		final KeyStore inKeystore = KeyStore.getInstance("PKCS12");
	//		inKeystore.load(new ByteArrayInputStream(key), "geheim".toCharArray());
	//		keyDecoded = (PrivateKey) inKeystore.getKey(inKeystore.aliases().nextElement(), "geheim".toCharArray());
	//	} catch (final KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException e) {
	//		throw new SVWSKonfigurationException("Fehler beim Dekodieren des privaten Schlüssels.", e);
	//	}

}
