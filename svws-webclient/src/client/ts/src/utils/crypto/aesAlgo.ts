
/**
 * Gibt die von der {@link AES}-Klasse unterstützen AES-Verfahren an.
 */
export class AESAlgo {

	/** Cipher Block Chaining (CBC) */
	public static readonly CBC = new AESAlgo("AES-CBC");

	/** die Bezeichnung des Verfahrens in der JavaScript-Web Crypto-Bibliothek */
	private _value: string;

	/**
	 * Erstellt einen neuen Eintrag für die unterstützen
	 * AES-Verfahren.
	 *
	 * @param value   die Bezeichnung des Verfahrens in der Java-Crypto-Bilbilthek
	 */
	private constructor(value: string) {
		this._value = value;
	}

	/**
	 * Gibt die Bezeichnung des Verfahrens in der Java-Crypto-Bilbilthek zurück.
	 *
	 * @return die Bezeichnung
	 */
	get value(): string {
		return this._value;
	}

}
