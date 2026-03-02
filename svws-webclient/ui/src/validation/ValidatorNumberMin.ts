import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein Wert für das Input über dem angegebenen Minimum liegt.
 */
export class ValidatorNumberMin extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => number | null;

	/** Der minimale Wert des Inputs */
	private readonly min: number | undefined;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf die aktuell eingegebene Zahl.
	 *
	 * @param data   die Funktion zum Zugriff auf die Daten
	 * @param min    das Minimum
	 */
	constructor(data: () => number | null | undefined, min: number | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? null;
		this.min = min;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor die aktuell eingegebene Zahl
	 * ermittelt und anschließend prüft, ob diese über dem Minimum liegt.
	 *
	 * @returns true, wenn die Zahl über dem Minimum liegt.
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((data === null) || (this.min === undefined)) {
			return true;
		}

		if (data < this.min) {
			this.addFehler(0, `Der Wert muss mindestens ${this.min} sein.`);
			return false;
		}

		return true;
	}
}
