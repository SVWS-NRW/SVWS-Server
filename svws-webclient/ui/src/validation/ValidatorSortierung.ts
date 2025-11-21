import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob der Wert für das Attribut Sortierung gültig ist.
 */
export class ValidatorSortierung extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können */
	private readonly data: () => number | null | undefined;


	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den Wert für die Sortierung
	 *
	 * @param data    die Funktion zum Zugriff auf die Daten
	 */
	constructor(data: () => number | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor den Wert für die Sortierung
	 * ermittelt und anschließend prüft, ob dieser Wert gültig ist.
	 *
	 * @returns true, wenn die Strings übereinstimmen
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((data === undefined) || (data === null)) {
			this.addFehler(0, "Es muss ein Wert für die Sortierreihenfolge gesetzt werden");
			return false;
		}
		if (data < 0) {
			this.addFehler(0, "Der Wert für die Sortierreihenfolge muss größer oder gleich 0 sein.");
			return false;
		}
		return true;
	}

}
