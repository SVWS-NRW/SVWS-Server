import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein Wert für das Select vorhanden ist (keine leere Selektion)
 */
export class ValidatorSelectMultiRequired<T> extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => T[];

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf die aktuelle Selektion
	 *
	 * @param data   die Funktion zum Zugriff auf die Daten
	 */
	constructor(data: () => T[] | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? [];
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor die aktuelle Selektion
	 * ermittelt und anschließend prüft, ob diese definiert ist.
	 *
	 * @returns true, wenn etwas selektiert ist.
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if (data.length === 0) {
			this.addFehler(0, "Eine Option muss ausgewählt werden.");
			return false;
		}
		return true;
	}

}
