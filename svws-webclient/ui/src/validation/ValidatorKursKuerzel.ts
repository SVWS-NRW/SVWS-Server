import type { KursDaten } from "../../../core/src/asd/data/kurse/KursDaten";
import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";
import { JavaString } from "../../../core/src/java/lang/JavaString";

/**
 * Ein Validator, welcher prüft, ob ein Kurskürzel gültig ist und nicht bereits bei einem anderenn Kurs vorhanden ist.
 */
export class ValidatorKursKuerzel extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null | undefined;

	/** Eine Menge der Kurskürzel im lower case, wo das Kürzel des zu validierenden Kurses nicht mehr enthalten ist. */
	private readonly menge: Set<string> = new Set<string>();


	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data    die Funktion zum Zugriff auf die Daten
	 * @param menge   die Liste der Kurse mit ihren Kürzeln
	 * @param id      die ID des zu validierenden Kurses, sofern dieser in der Liste der Kurse enthalten ist
	 */
	constructor(data: () => string | null | undefined, menge: Iterable<KursDaten>, id: number | null = null) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		for (const k of menge) {
			// Filtere den Kurs mit der eigenen ID, sofern diese angegeben wurde und alle Einträge mit leerem Kürzel
			if (((id !== null) && (k.id === id)) || (JavaString.isBlank(k.kuerzel))) {
				continue;
			}
			this.menge.add(k.kuerzel.toLocaleLowerCase('de'));
		}
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor das Kurskürzel ermittelt und anschließend prüft,
	 * ob dieses gültig ist und nicht bereits bei einem anderen Kurs vorhanden ist
	 *
	 * @returns true, wenn das Kurskürzel gültig ist
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((data === undefined) || (data === null) || JavaString.isBlank(data)) {
			this.addFehler(0, "Ein leeres Kurskürzel ist nicht zulässig.");
			return false;
		}

		if (data.trim().length > 20) {
			this.addFehler(0, "Ein Kurskürzel darf maximal 20 Zeichen lang sein.");
			return false;
		}

		if (this.menge.has(data.trim().toLocaleLowerCase('de'))) {
			this.addFehler(0, "Das Kürzel '" + data + "' wurde bereits für einen anderen Kurs verwendet.");
			return false;
		}
		return true;
	}

}
