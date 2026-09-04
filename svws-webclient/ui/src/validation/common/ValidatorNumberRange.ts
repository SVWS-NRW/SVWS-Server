import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";

/**
 * Prüft, ob eine Zahl im definierten Min-/Max-Bereich liegt.
 */
export class ValidatorNumberRange extends BasicValidator {

	private readonly data: () => number | null;
	private readonly max: number | null;
	private readonly min: number | null;

	/**
	 * Prüft, ob eine Zahl im definierten Min-/Max-Bereich liegt.
	 *
	 * @param data Funktion zum Zugriff auf die Zahl
	 * @param max  optional: maximaler Wert
	 * @param min  optional: minimaler Wert
	 */
	constructor(data: () => number | null | undefined, min: number | null | undefined, max: number | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? null;
		this.min = min ?? null;
		this.max = max ?? null;
	}

	/**
	 * Prüft die Zahl auf Min/Max.
	 * @returns true, wenn gültig
	 */
	protected pruefe(): boolean {
		const data = this.data(); // wert: number | null
		if (data === null) {
			return true;
		}
		if ((this.min !== null) && (data < this.min)) {
			this.addFehler(0, `Der Wert muss mindestens ${this.min} sein.`);
			return false;
		}

		if ((this.max !== null) && (data > this.max)) {
			this.addFehler(0, `Der Wert darf höchstens ${this.max} sein.`);
			return false;
		}

		return true;
	}

}
