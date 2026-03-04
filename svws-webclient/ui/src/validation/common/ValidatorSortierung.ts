import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Prüft, ob der Wert für die Sortierung gültig ist.
 */
export class ValidatorSortierung extends BasicValidator {

	private readonly data: () => number | null;


	/**
	 * Prüft, ob der Wert für die Sortierung gültig ist.
	 *
	 * @param data Funktion zum Zugriff auf die Sortierung
	 */
	constructor(data: () => number | null) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		this.run();
	}

	/**
	 * Prüft den Sortierwert.
	 * @returns true, wenn gültig
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if (data === null) {
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
