import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";
import { JavaString } from "../../../../core/src/java/lang/JavaString";


/**
 * Prüft, ob ein String definiert und nicht leer ist.
 */
export class ValidatorStringNotBlank extends BasicValidator {

	private readonly data: () => string | null;

	/**
	 * Prüft, ob ein String definiert und nicht leer ist.
	 *
	 * @param data Funktion zum Zugriff auf den String
	 */
	constructor(data: () => string | null) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		this.run();
	}

	/**
	 * Prüft den String auf Definition und Inhalt.
	 * @returns true, wenn der String definiert und nicht leer ist
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((data === null) || JavaString.isEmpty(data) || JavaString.isBlank(data)) {
			this.addFehler(0, "Der Wert muss angeben sein und darf nicht leer sein.");
			return false;
		}
		return true;
	}

}
