package de.svws_nrw.base.crypto;

/**
 * Gibt die von der {@link AES}-Klasse unterstützen AES-Verfahren an.
 */
public enum AESAlgo {

	/** Cipher Block Chaining (CBC) mit PKCS #5-Padding */
	CBC_PKCS5PADDING("AES/CBC/PKCS5Padding");

	/** die Bezeichnung des Verfahrens in der Java-Crypto-Bilbilthek */
	private String value;

	/**
	 * Erstellt einen neuen Eintrag für die unterstützen
	 * AES-Verfahren.
	 *
	 * @param value   die Bezeichnung des Verfahrens in der Java-Crypto-Bilbilthek
	 */
	AESAlgo(final String value) {
		this.value = value;
	}

	/**
	 * Gibt die Bezeichnung des Verfahrens in der Java-Crypto-Bilbilthek zurück.
	 *
	 * @return die Bezeichnung
	 */
	public String value() {
		return this.value;
	}

}
