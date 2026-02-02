import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";

/**
 * Ein Validator, welcher prüft, ob ein String in der übergebenen Menge ist.
 */
export class ValidatorStringExistsIn extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null | undefined;

	/** Eine Menge von Strings, wo die Daten mit einem Element übereinstimmen müssen. */
	private readonly menge: Set<string>;


	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data    die Funktion zum Zugriff auf die Daten
	 * @param menge   eine Menge von Strings, wo die Daten mit einem Element übereinstimmen müssen
	 */
	constructor(data: () => string | null | undefined, menge: Iterable<string> = new Array<string>, verwendeKleinschreibung: boolean = false) {
		super(ValidatorFehlerart.MUSS);
		if (verwendeKleinschreibung) {
			this.menge = new Set<string>();
			this.data = () => data()?.toLocaleLowerCase();
			for (const i of menge) {
				this.menge.add(i.toLocaleLowerCase());
			}
		} else {
			this.data = data;
			this.menge = new Set<string>(menge);
		}
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor den String
	 * ermittelt und anschließend prüft, ob dieser in der Menge aus dem Konstruktor
	 * enthalten ist.
	 *
	 * @returns true, wenn der String definiert und nicht leer ist.
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((data === undefined) || (data === null)) {
			return false;
		}
		if (this.menge.has(data)) {
			return true;
		}
		this.addFehler(0, "'" + data + "' ist unzulässig.");
		return false;
	}

}
