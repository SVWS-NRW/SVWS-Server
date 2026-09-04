import { BasicValidator } from "@core/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "@core/asd/validate/ValidatorFehlerart";
import { DeveloperNotificationException } from "@core/core/exceptions/DeveloperNotificationException";

/**
 * Ein Validator, welcher prüft, ob genügend Elemente selektiert sind.
 */
export class ValidatorSelectMultiOptionsRange<T> extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => T[];

	/** Die minimale Anzahl an Elementen, die selektiert sein muss */
	private readonly minOptions: number | null;

	/** Die maximale Anzahl an Elementen, die selektiert sein darf */
	private readonly maxOptions: number | null;

	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf die aktuelle Selektion
	 *
	 * @param data         die Funktion zum Zugriff auf die Daten
	 * @param minOptions   die minimale Anzahl an Elementen, die selektiert sein muss
	 * @param maxOptions   die maximale Anzahl an Elementen, die selektiert sein darf
	 */
	constructor(data: () => T[] | null | undefined, minOptions: number | null | undefined, maxOptions: number | null | undefined) {
		super(ValidatorFehlerart.MUSS);
		this.data = () => data() ?? [];

		if ((minOptions !== undefined) && (minOptions !== null) && (minOptions <= 0)) {
			throw new DeveloperNotificationException("Der Parameter mimOptions muss größer als 0 sein");
		}
		if ((maxOptions !== undefined) && (maxOptions !== null) && (maxOptions <= 0)) {
			throw new DeveloperNotificationException("Der Parameter maxOptions muss größer als 0 sein");
		}

		this.minOptions = minOptions ?? null;
		this.maxOptions = maxOptions ?? null;
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor die aktuelle Selektion
	 * ermittelt und anschließend prüft, ob diese plausibel ist.
	 *
	 * @returns true, wenn die Anzahl an selektierten Optionen im angegebenen Bereich liegt
	 */
	protected pruefe(): boolean {
		const data = this.data();

		if (data.length === 0) {
			return true;
		}

		if ((this.minOptions === null) && (this.maxOptions === null)) {
			return true;
		}

		if ((this.minOptions !== null) && (data.length < this.minOptions)) {
			const oneOption = this.minOptions === 1;
			this.addFehler(0,
				`Es ${oneOption ? 'muss mindestens eine Option' : 'müssen mindestens ' + this.minOptions + ' Optionen'} ausgewählt sein.`);
			return false;
		}

		if ((this.maxOptions !== null) && (data.length > this.maxOptions)) {
			const oneOption = this.maxOptions === 1;
			this.addFehler(0,
				`Es ${oneOption ? 'darf maximal eine Option' : 'dürfen maximal ' + this.maxOptions + ' Optionen'} ausgewählt sein.`);
			return false;
		}
		return false;
	}

}
