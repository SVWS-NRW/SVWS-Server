package de.svws_nrw.service.crypto.secret;

import java.nio.file.Path;

import de.svws_nrw.config.SVWSKonfiguration;

/**
 * Factory für den serverweiten {@link SecretKeyProvider}. Der Keystore liegt im Verzeichnis des TLS-Keystores
 * und wird mit dessen Kennwort geschützt. Die SVWS-Konfiguration wird erst beim ersten Zugriff auf den
 * Schlüssel gelesen; der Schlüssel wird anschließend für die Laufzeit der JVM im Speicher gehalten.
 */
public final class SecretKeyProviderFactory {

	private static final String KEYSTORE_DATEINAME = "secrets.keystore";
	private static final String ALIAS_OAUTH_CREDENTIALS = "oauth-credentials";

	/** Der serverweite Provider, der erst beim ersten Aufruf von getKey() auf den Keystore zugreift */
	private static final SecretKeyProvider INSTANCE = Holder.INSTANCE;


	private SecretKeyProviderFactory() {
		// Instanziierung nicht erlaubt
	}


	/**
	 * Gibt den serverweiten {@link SecretKeyProvider} zurück.
	 *
	 * @return der serverweite Provider
	 */
	public static SecretKeyProvider getInstance() {
		return INSTANCE;
	}


	/**
	 * Ermittelt den Pfad der Keystore-Datei für Secrets im angegebenen Verzeichnis.
	 *
	 * @param tlsKeystorePath   das Verzeichnis des TLS-Keystores
	 *
	 * @return der Pfad der Keystore-Datei für Secrets
	 */
	static Path resolveKeystoreFile(final String tlsKeystorePath) {
		return Path.of(tlsKeystorePath, KEYSTORE_DATEINAME);
	}


	/**
	 * Hält den serverweiten Provider. Die Klasse wird erst beim ersten Zugriff initialisiert, so dass die
	 * SVWS-Konfiguration erst dann gelesen wird.
	 */
	private static final class Holder {

		/** Der serverweite Provider */
		private static final KeyStoreSecretKeyProvider INSTANCE = create();

		private static KeyStoreSecretKeyProvider create() {
			final SVWSKonfiguration config = SVWSKonfiguration.get();
			return new KeyStoreSecretKeyProvider(resolveKeystoreFile(config.getTLSKeystorePath()), config.getTLSKeystorePassword(), ALIAS_OAUTH_CREDENTIALS);
		}

	}

}
