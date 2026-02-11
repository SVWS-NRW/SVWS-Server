import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein String eine gültige Email beinhaltet.
 */
export class ValidatorEmail extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null | undefined;

	/** Die maximale Zeichenlänge für die Email-Adressangabe */
	private readonly maxLen: number;

	/** Gibt an, ob eine leere Email-Adressangabe als gültig angesehen wird oder nicht */
	private readonly allowEmpty: boolean;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data         die Funktion zum Zugriff auf die Daten
	 * @param maxLen       die maximale Zeichenlänge für die Email-Adressangabe
	 * @param allowEmpty   gibt an, ob eine leere Email-Adressangabe als gültig angesehen wird oder nicht
	 */
	constructor(data: () => string | null | undefined, maxLen: number, allowEmpty: boolean = true) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		this.maxLen = maxLen;
		this.allowEmpty = allowEmpty;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor auf eine
	 * gültige Email-Adresse prüft.
	 *
	 * @returns true, wenn der String eine gültige Email-Adresse beinhaltet.
	 */
	protected pruefe(): boolean {
		const data = this.data();

		// Prüfe, ob eine Email-Adresse angegeben wurde
		if ((data === undefined) || (data === null) || (data === '')) {
			if (this.allowEmpty) {
				return true;
			}
			this.addFehler(0, "Eine Email-Adresse muss angegeben werden.");
			return false;
		}

		// Prüfe die Länge der Eingabe
		const len = data.toLocaleString().length;
		if (len > this.maxLen) {
			this.addFehler(0, "Die Email-Adresse darf maximal " + this.maxLen + " Zeichen lang sein.");
			return false;
		}

		// Prüfe das Format der Email-Adresse
		if (/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))[^@]?$/.test(data) ||
			/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(data)) {
			return true;
		}
		this.addFehler(0, "Die angegebene Email-Adresse hat ein ungültiges Format.");
		return false;
	};

}
