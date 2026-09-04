import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";


/**
 * Ein Validator, welcher prüft, ob ein String - sofern definiert - die gewünschte Länge hat oder nicht.
 */
export class ValidatorStringLength extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null;

	/** Die maximale Länge für den String, sofern festgelegt (null bedeutet beliebige maximale Länge) */
	private readonly maxLen: number | null;

	/** Die minimale Länge für den String, sofern festgelegt (null bedeutet beliebige minimale Länge) */
	private readonly minLen: number | null;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data     die Funktion zum Zugriff auf die Daten
	 * @param maxLen   die maximale Länge für den String, sofern festgelegt (null oder undefined bedeutet beliebig)
	 * @param minLen   die minimale Länge für den String, sofern festgelegt (null oder undefined bedeutet beliebig)
	 */
	constructor(data: () => string | null | undefined, minLen: number | null | undefined, maxLen: number | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? null;
		if ((minLen !== undefined) && (minLen !== null) && (minLen <= 0)) {
			throw new DeveloperNotificationException("Der Parameter minLen muss größer als 0 sein");
		}
		if ((maxLen !== undefined) && (maxLen !== null) && (maxLen <= 0)) {
			throw new DeveloperNotificationException("Der Parameter maxLen muss größer als 0 sein");
		}
		this.minLen = minLen ?? null;
		this.maxLen = maxLen ?? null;
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor den String
	 * ermittelt und anschließend prüft, ob die Länge in dem angegebenen Bereich liegt.
	 *
	 * @returns true, wenn die Länge im gültigen Bereich liegt.
	 */
	protected pruefe(): boolean {
		const data = this.data();

		if ((data === null) || (data === '')) {
			return true;
		}

		// Prüfe die minimale Länge des Strings
		const len = data.toLocaleString().length;
		if ((this.minLen !== null) && (len < this.minLen)) {
			this.addFehler(0, "Der Wert muss mindestens " + this.minLen + " Zeichen lang sein.");
			return false;
		}
		// Prüfe die maximale Länge des Strings
		if ((this.maxLen !== null) && (len > this.maxLen)) {
			this.addFehler(0, "Der Wert darf maximal " + this.maxLen + " Zeichen lang sein.");
			return false;
		}
		// Die Prüfung hat keinen Fehler ergeben...
		return true;
	}

}
