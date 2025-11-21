import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein String nicht in der übergebenen Menge ist.
 */
export class ValidatorStringNotIn extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null | undefined;

	/** Eine Menge von Strings, welche nicht mit den Daten übereinstimmen dürfen. */
	private readonly menge: Set<string>;


	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data    die Funktion zum Zugriff auf die Daten
	 * @param menge   die Strings, welche nicht mit den Daten übereinstimmen dürfen
	 */
	constructor(data: () => string | null | undefined, menge: Iterable<string> = new Array<string>) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		this.menge = new Set<string>(menge);
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor den String
	 * ermittelt und anschließend prüft, ob dieser nicht in der Menge aus dem Konstruktor
	 * enthalten ist.
	 *
	 * @returns true, wenn der String definiert und nicht leer ist.
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((data === undefined) || (data === null))
			return true;
		if (this.menge.has(data)) {
			this.addFehler(0, "'" + data + "' ist nicht zulässig.");
			return false;
		}
		return true;
	}

}
