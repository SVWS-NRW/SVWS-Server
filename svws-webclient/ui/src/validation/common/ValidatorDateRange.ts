import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein Datum sich im vorgegebenen Zeitraum befindet.
 */
export class ValidatorDateRange extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null;

	/** Das minimale Datum */
	private readonly minDate: string | null;

	/** Das maximale Datum */
	private readonly maxDate: string | null;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data      die Funktion zum Zugriff auf die Daten
	 * @param minDate   das minimale Datum
	 * @param maxDate   das maximale Datum
	 */
	constructor(data: () => string | null | undefined, minDate: string | null | undefined, maxDate: string | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? null;
		this.minDate = (minDate !== undefined) && (minDate !== '') ? minDate : null;
		this.maxDate = (maxDate !== undefined) && (maxDate !== '') ? maxDate : null;
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor auf ein gültiges Datum prüft.
	 *
	 * @returns true, wenn das angegebene Datum im angegebenen Zeitraum liegt
	 */
	protected pruefe(): boolean {
		const data = this.data();

		if ((data === null) || ((this.minDate === null) && (this.maxDate === null))) {
			return true;
		}

		if ((this.minDate !== null) && (data < this.minDate)) {
			this.addFehler(0, `Das frühestmögliche Datum ist der ${this.formatDate(this.minDate)}.`);
			return false;
		}

		if ((this.maxDate !== null) && (this.maxDate !== '') && (data > this.maxDate)) {
			this.addFehler(0, `Das spätestmögliche Datum ist der ${this.formatDate(this.maxDate)}.`);
			return false;
		}
		return true;
	};

	private formatDate(date: string) {
		const [year, month, day] = date.split("-");
		return `${day}.${month}.${year}`;
	}
}
