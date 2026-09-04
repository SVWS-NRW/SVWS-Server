import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import { AdressenUtils } from "@core/core/utils/AdressenUtils";


/**
 * Ein Validator, welcher prüft, ob eine Straße inklusive Hausnummer und Zusatz die richtige Länge hat.
 */
export class ValidatorStrasse extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null;

	/** Die maximale Zeichenlänge des Straßennamens */
	private readonly maxLenStrassenname: number;

	/** Die maximale Zeichenlänge der Hausnummer */
	private readonly maxLenHausNr: number;

	/** Die maximale Zeichenlänge des Hausnummerzusatzes */
	private readonly maxLenHausNrZusatz: number;


	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data     			   die Funktion zum Zugriff auf die Daten
	 * @param maxLenStrassenname   die maximale Zeichenlänge des Straßennamens
	 * @param maxLenHausNr		   die maximale Zeichenlänge der Hausnummer
	 * @param maxLenHausNrZusatz   die maximale Zeichenlänge des Hausnummerzusatzes
	 */
	constructor(data: () => string | null | undefined, maxLenStrassenname: number, maxLenHausNr: number, maxLenHausNrZusatz: number) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? null;
		this.maxLenStrassenname = maxLenStrassenname;
		this.maxLenHausNr = maxLenHausNr;
		this.maxLenHausNrZusatz = maxLenHausNrZusatz;
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

		const [strassenname, hausNr, hausNrZusatz] = AdressenUtils.splitStrasse(data);

		// Prüfe maximale Länge des Straßennamens
		const lenStrasse = strassenname.toLocaleString().length;
		if (lenStrasse > this.maxLenStrassenname) {
			this.addFehler(0, `Der Straßenname darf maximal ${this.maxLenStrassenname} Zeichen lang sein.`);
			return false;
		}

		// Prüfe maximale Länge der Hausnummer
		const lenHausNr = hausNr.toLocaleString().length;
		if (lenHausNr > this.maxLenHausNr) {
			this.addFehler(0, `Die Hausnummer darf maximal ${this.maxLenHausNr} Zeichen lang sein.`);
			return false;
		}

		// Prüfe maximale Länge des Hausnummerzusatzes
		const lenHausNrZusatz = hausNrZusatz.toLocaleString().length;
		if (lenHausNrZusatz > this.maxLenHausNrZusatz) {
			this.addFehler(0, `Der Hausnummerzusatz darf maximal ${this.maxLenHausNrZusatz} Zeichen lang sein.`);
			return false;
		}

		// Die Prüfung hat keinen Fehler ergeben...
		return true;
	}

}
