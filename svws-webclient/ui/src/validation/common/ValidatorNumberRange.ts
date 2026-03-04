import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Prüft, ob eine Zahl im definierten Min-/Max-Bereich liegt.
 */
export class ValidatorNumberRange extends BasicValidator {

	private readonly data: () => number | null;
	private readonly max?: number;
	private readonly min?: number;

	/**
	 * Prüft, ob eine Zahl im definierten Min-/Max-Bereich liegt.
	 *
	 * @param data Funktion zum Zugriff auf die Zahl
	 * @param max  optional: maximaler Wert
	 * @param min  optional: minimaler Wert
	 */
	constructor(data: () => number | null, max?: number, min?: number) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		this.max = max;
		this.min = min;
		this.run();
	}

	/**
	 * Prüft die Zahl auf Min/Max.
	 * @returns true, wenn gültig
	 */
	protected pruefe(): boolean {
		const wert = this.data(); // wert: number | null
		if (wert === null) {
			return true;
		}
		if ((this.min !== undefined) && (wert < this.min)) {
			this.addFehler(0, `Der Wert muss mindestens ${this.min} sein.`);
			return false;
		}

		if ((this.max !== undefined) && (wert > this.max)) {
			this.addFehler(0, `Der Wert darf höchstens ${this.max} sein.`);
			return false;
		}

		return true;
	}

}
