import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein Wert für das Input vorhanden ist (nicht null und nicht undefined)
 */
export class ValidatorNumberRequired extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => number | null;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf die aktuell eingegebene Zahl.
	 *
	 * @param data   die Funktion zum Zugriff auf die Daten
	 */
	constructor(data: () => number | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? null;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor die aktuell eingegebene Zahl
	 * ermittelt und anschließend prüft, ob diese definiert ist.
	 *
	 * @returns true, wenn eine Zahl eingegeben ist.
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if (data === null) {
			this.addFehler(0, "Ein Wert muss angegeben sein.");
			return false;
		}
		return true;
	}
}
