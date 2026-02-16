import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob zu viele Elemente selektiert sind.
 */
export class ValidatorSelectMultiMaxOptions<T> extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => T[];

	/** Die maximale Anzahl an Elementen, die selektiert sein können */
	private readonly maxOptions: number | undefined;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf die aktuelle Selektion
	 *
	 * @param data         die Funktion zum Zugriff auf die Daten
	 * @param maxOptions   die maximale Anzahl an Elementen, die selektiert sein darf
	 */
	constructor(data: () => T[] | null | undefined, maxOptions: number | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? [];
		this.maxOptions = maxOptions;
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
		if ((this.maxOptions === undefined) || (data.length <= this.maxOptions)) {
			return true;
		}

		if (this.maxOptions === 1) {
			this.addFehler(0, "Es darf maximal eine Option ausgewählt sein.");
		} else {
			this.addFehler(0, `Es dürfen maximal ${this.maxOptions} Optionen ausgewählt sein.`);
		}
		return false;
	}

}
