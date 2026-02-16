import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob genügend Elemente selektiert sind.
 */
export class ValidatorSelectMultiMinOptions<T> extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => T[];

	/** Die minimale Anzahl an Elementen, die selektiert sein muss */
	private readonly minOptions: number | undefined;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf die aktuelle Selektion
	 *
	 * @param data         die Funktion zum Zugriff auf die Daten
	 * @param minOptions   die minimale Anzahl an Elementen, die selektiert sein muss
	 */
	constructor(data: () => T[] | null | undefined, minOptions: number | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? [];
		this.minOptions = minOptions;
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor die aktuelle Selektion
	 * ermittelt und anschließend prüft, ob diese ausreichend ist.
	 *
	 * @returns true, wenn die minimale Anzahl an Optionen selektiert ist.
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((this.minOptions === undefined) || (data.length >= this.minOptions)) {
			return true;
		}

		if (this.minOptions === 1) {
			this.addFehler(0, "Es muss mindestens eine Option ausgewählt sein.");
		} else {
			this.addFehler(0, `Es müssen mindestens ${this.minOptions} Optionen ausgewählt sein.`);
		}
		return false;
	}

}
