package de.svws_nrw.service.crypto.secret;

/**
 * Factory für {@link SecretCipher}-Instanzen mit dem serverweiten {@link SecretKeyProvider}.
 */
public final class SecretCipherFactory {

	/** Der Provider für den symmetrischen Schlüssel, Singleton */
	private final SecretKeyProvider keyProvider;


	private SecretCipherFactory(final SecretKeyProvider keyProvider) {
		this.keyProvider = keyProvider;
	}


	/**
	 * Erzeugt eine neue Factory mit dem serverweiten {@link SecretKeyProvider}.
	 *
	 * @return die Factory
	 */
	public static SecretCipherFactory getNewInstance() {
		return new SecretCipherFactory(SecretKeyProviderFactory.getInstance());
	}


	/**
	 * Erzeugt einen neuen {@link SecretCipher}.
	 *
	 * @return der Cipher
	 */
	public SecretCipher getSecretCipher() {
		return new SecretCipher(keyProvider);
	}

}
