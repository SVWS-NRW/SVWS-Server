import type { KlassenDaten } from "../../../core/src/asd/data/klassen/KlassenDaten";
import { BasicValidator } from "../../../core/src/asd/validate/BasicValidator";
import { ValidatorFehlerart } from "../../../core/src/asd/validate/ValidatorFehlerart";
import { JavaString } from "../../../core/src/java/lang/JavaString";

/**
 * Ein Validator, welcher prüft, ob ein Klassenkürzel gültig ist und nicht bereits bei einer anderen Klasse vorhanden ist.
 */
export class ValidatorKlassenKuerzel extends BasicValidator {

	/** Eine Funktion, um auf die zu validierenden Daten zugreifen zu können. */
	private readonly data: () => string | null | undefined;

	/** Eine Menge der Klassenkürzel, wo das Kürzel der zu validierenden Klasse nicht mehr enthalten ist. */
	private readonly menge: Set<string> = new Set<string>();


	/**
	 * Erzeugt einen neuen Validator mit der Funktion zum Zugriff auf den String
	 *
	 * @param data    die Funktion zum Zugriff auf die Daten
	 * @param menge   die Liste der Klassen mit ihren Kürzeln
	 * @param id      die ID der zu validierenden Klasse, sofern diese in der Liste der Klassen enthalten ist
	 */
	constructor(data: () => string | null | undefined, menge: Iterable<KlassenDaten>, id: number | null = null) {
		super(ValidatorFehlerart.MUSS);
		this.data = data;
		for (const kl of menge) {
			// Filtere die Klasse mit der eigenen ID, sofern diese angegeben wurde und alle Einträge mit leerem Kürzel
			if (((id !== null) && (kl.id === id)) || (kl.kuerzel === null) || (JavaString.isBlank(kl.kuerzel))) {
				continue;
			}
			this.menge.add(kl.kuerzel);
		}
		this.run();
	}

	/**
	 * Die Prüfroutine, welche über die Funktion aus dem Konstruktor das Klassenkürzel ermittelt und anschließend prüft,
	 * ob dieses gültig ist und nicht bereits bei einer anderen Klasse vorhanden ist
	 *
	 * @returns true, wenn das Klassenkürzel gültig ist
	 */
	protected pruefe(): boolean {
		const data = this.data();
		if ((data === undefined) || (data === null) || JavaString.isBlank(data)) {
			this.addFehler(0, "Ein leeres Klassenkürzel ist nicht zulässig.");
			return false;
		}

		if (data.trim().length > 15) {
			this.addFehler(0, "Ein Klassenkürzel darf maximal 15 Zeichen lang sein.");
			return false;
		}

		if (this.menge.has(data.trim())) {
			this.addFehler(0, "Das Kürzel '" + data + "' wurde bereits für eine andere Klasse verwendet.");
			return false;
		}
		return true;
	}

}
