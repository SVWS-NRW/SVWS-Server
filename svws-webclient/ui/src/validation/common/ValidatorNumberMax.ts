import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein Wert für das Input unter dem angegebenen Maximum liegt.
 */
export class ValidatorNumberMax extends BasicValidator {


	// TODO:remove and replace with ValidatorNumberRange
	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => number | null;

	/** Der maximale Wert des Inputs */
	private readonly max: number | undefined;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf die aktuell eingegebene Zahl.
	 *
	 * @param data   die Funktion zum Zugriff auf die Daten
	 * @param max    das Maximum
	 */
	constructor(data: () => number | null | undefined, max: number | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? null;
		this.max = max;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor die aktuell eingegebene Zahl
	 * ermittelt und anschließend prüft, ob diese unter dem Maximum liegt.
	 *
	 * @returns true, wenn die Zahl unter dem Maximum liegt.
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((data === null) || (this.max === undefined)) {
			return true;
		}

		if (data > this.max) {
			this.addFehler(0, `Der Wert darf maximal ${this.max} sein.`);
			return false;
		}

		return true;
	}
}
