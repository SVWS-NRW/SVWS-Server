import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";
import { JavaString } from "../../../core/src/java/lang/JavaString";

/**
 * Ein Validator, welcher prüft, ob ein String eine gültige Telefonnummer beinhaltet.
 */
export class ValidatorTelefon extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null | undefined;

	/** Die maximale Zeichenlänge für die Telefonnummer */
	private readonly maxLen: number;

	/** Gibt an, ob eine leere Telefonnummer als gültig angesehen wird oder nicht */
	private readonly allowEmpty: boolean;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data         die Funktion zum Zugriff auf die Daten
	 * @param maxLen       die maximale Zeichenlänge für die Telefonnummer
	 * @param allowEmpty   gibt an, ob eine leere Telefonnummer als gültig angesehen wird oder nicht
	 */
	constructor(data: () => string | null | undefined, maxLen: number, allowEmpty: boolean = true) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		this.maxLen = maxLen;
		this.allowEmpty = allowEmpty;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor die Telefonnummer
	 * bestimmt und auf Korrektheit prüft.
	 *
	 * @returns true, wenn der String eine gültige Telefonnummer beinhaltet.
	 */
	protected pruefe(): boolean {
		const data = this.data();

		// Prüfe, ob eine Email-Adresse angegeben wurde
		if ((data === undefined) || (data === null) || JavaString.isBlank(data)) {
			if (this.allowEmpty) {
				return true;
			}
			this.addFehler(0, "Eine Telefonnummer muss angegeben werden.");
			return false;
		}

		// Prüfe die Länge der Eingabe
		const len = data.toLocaleString().length;
		if (len > this.maxLen) {
			this.addFehler(0, "Die Telefonnummer darf maximal " + this.maxLen + " Zeichen lang sein.");
			return false;
		}

		// Prüfe das Format der Telefonnummer
		if (/^\+?\d+([-/]?\d+)*$/.test(data)) {
			return true;
		}
		this.addFehler(0, "Die angegebene Telefonnummer hat ein ungültiges Format.");
		return false;
	};

}
