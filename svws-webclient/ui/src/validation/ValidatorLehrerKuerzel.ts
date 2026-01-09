import type { LehrerListeEintrag } from "../../../core/src";
import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";
import { JavaString } from "../../../core/src/java/lang/JavaString";

/**
 * Ein Validator, welcher prüft, ob ein Lehrerkürzel gültig ist und nicht bereits bei einem anderen Lehrer vorhanden ist.
 */
export class ValidatorLehrerKuerzel extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null | undefined;

	/** Eine Menge der Lehrerkürzel, wo das Kürzel des zu validierenden Lehrers nicht mehr enthalten ist. */
	private readonly menge: Set<string> = new Set<string>();


	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data    die Funktion zum Zugriff auf die Daten
	 * @param menge   die Liste der Lehrer mit ihren Kürzeln
	 * @param id      die ID des zu validierenden Lehrers, sofern dieser in der Liste der Lehrer enthalten ist
	 */
	constructor(data: () => string | null | undefined, menge: Iterable<LehrerListeEintrag>, id: number | null = null) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		for (const l of menge) {
			// Filtere den Lehrer mit der eigenen ID, sofern diese angegeben wurde und alle Einträge mit leerem Kürzel
			if (((id !== null) && (l.id === id)) || (JavaString.isBlank(l.kuerzel))) {
				continue;
			}
			this.menge.add(l.kuerzel);
		}
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor das Lehrerkürzel ermittelt und anschließend prüft,
	 * ob dieses gültig ist und nicht bereits bei einem anderen Lehrer vorhanden ist
	 *
	 * @returns true, wenn das Lehrerkürzel gültig ist
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((data === undefined) || (data === null) || JavaString.isBlank(data)) {
			this.addFehler(0, "Ein leeres Lehrerkürzel ist nicht zulässig.");
			return false;
		}

		if (data.trim().length > 10) {
			this.addFehler(0, "Ein Lehrerkürzel darf maximal 10 Zeichen lang sein.");
			return false;
		}

		if (this.menge.has(data.trim())) {
			this.addFehler(0, "Das Kürzel '" + data + "' wurde bereits für einen anderen Lehrer verwendet.");
			return false;
		}
		return true;
	}

}
