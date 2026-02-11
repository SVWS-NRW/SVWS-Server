import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein String definiert ist (not null and not undefined)
 * und nicht leer ('') ist.
 */
export class ValidatorStringNotEmpty extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null | undefined;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data   die Funktion zum Zugriff auf die Daten
	 */
	constructor(data: () => string | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor den String
	 * ermittelt und anschließend prüft, ob dieser definiert und nicht leer ist.
	 *
	 * @returns true, wenn der String definiert und nicht leer ist.
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((data === undefined) || (data === null) || (data === '')) {
			this.addFehler(0, "Der Wert muss angeben sein und darf nicht leer sein.");
			return false;
		}
		return true;
	}

}
