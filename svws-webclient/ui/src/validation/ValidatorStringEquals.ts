import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein String mit dem übergebenen String übereinstimmt.
 */
export class ValidatorStringEquals extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können */
	private readonly data: () => string | null | undefined;

	/** Der String für den Vergleich */
	private readonly other: () => string;


	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data    die Funktion zum Zugriff auf die Daten
	 * @param other   der String für den Vergleich
	 */
	constructor(data: () => string | null | undefined, other: () => string) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		this.other = other;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor den String
	 * ermittelt und anschließend prüft, ob dieser mit dem String aus dem Konstruktor übereinstimmt.
	 *
	 * @returns true, wenn die Strings übereinstimmen
	 */
	protected pruefe(): boolean {
		const data = this.data();
		const other = this.other();
		if ((data === undefined) || (data === null)) {
			return false;
		}
		if (data === other) {
			return true;
		}
		this.addFehler(0, "Die beiden Werte stimmen nicht überein");
		return false;
	}

}
