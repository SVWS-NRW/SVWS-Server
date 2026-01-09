import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein String definiert und nicht leer ist
 * sowie keinen Whitespace Character (\s) enthält.
 */
export class ValidatorStringHasNoWhitespaces extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null | undefined;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data         die Funktion zum Zugriff auf die Daten
	 */
	constructor(data: () => string | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor die Daten
	 * ermittelt und prüft, ob der String keinen Whitespace Character (\s) enthält
	 *
	 * @returns true, wenn der String keinen Whitespace Character (\s) enthält
	 */
	protected pruefe(): boolean {
		const data = this.data();

		// Ist der String nicht definiert
		if ((data === undefined) || (data === null)) {
			return false;
		}

		// Prüft auf Whitespace Character
		if (/\s/.exec(data) !== null) {
			this.addFehler(0, "Der Wert darf keine Leerzeichen, Tabs, etc. enthalten.");
			return false;
		}
		return true;
	};

}
