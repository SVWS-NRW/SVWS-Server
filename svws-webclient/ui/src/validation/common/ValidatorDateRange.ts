import { BasicValidator } from "../../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein Datum sich im vorgegebenen Zeitraum befindet.
 */
export class ValidatorDateRange extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null;

	/** Das minimale Datum */
	private readonly minDate: string | undefined;

	/** Das maximale Datum */
	private readonly maxDate: string | undefined;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data      die Funktion zum Zugriff auf die Daten
	 * @param minDate   das minimale Datum
	 * @param maxDate   das maximale Datum
	 */
	constructor(data: () => string | null | undefined, minDate: string | undefined, maxDate: string | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? null;
		this.minDate = minDate;
		this.maxDate = maxDate;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor auf ein gültiges Datum prüft.
	 *
	 * @returns true, wenn das angegebene Datum im angegebenen Zeitraum liegt
	 */
	protected pruefe(): boolean {
		const data = this.data();
		const noDateRange = (this.minDate === undefined) && (this.maxDate === undefined);

		if ((data === null) || (data === "") || noDateRange) {
			return true;
		}

		if ((this.minDate !== undefined) && data < this.minDate) {
			this.addFehler(0, `Das Datum muss mindestens ${this.minDate} entsprechen.`);
			return false;
		}

		if ((this.maxDate !== undefined) && data > this.maxDate) {
			this.addFehler(0, `Das Datum darf maximal ${this.maxDate} entsprechen.`);
			return false;
		}
		return true;
	};

}
