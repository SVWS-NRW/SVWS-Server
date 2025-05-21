import type { Fachgruppe } from "../../../../../../core/src/asd/types/fach/Fachgruppe";
import type { Fach } from "../../../../../../core/src/asd/types/fach/Fach";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import type { List } from "../../../../../../core/src/java/util/List";
import type { SelectFilter } from "./SelectFilter";
import { toRaw } from "vue";

/**
 * Ein Filter für die UiSelect-Komponente. Er filtert Fächer auf Basis der übergebenen Fachgruppen.
 */
export class FachSelectFilter implements SelectFilter<Fach> {
	public key: string;
	// Liste der Fachgruppen, nach denen gefiltert wird.
	private _fachgruppen: List<Fachgruppe>;
	// Das genutzte Schuljahr
	private _schuljahr: number;

	/**
	 * Konstruktor für den FachSelectFilter
	 *
	 * @param key   	    eindeutiger Key für den Filter.
	 * @param fachgruppen    Liste der Fachgruppen, nach denen gefiltert wird.
	 * @param schuljahr     Das genutzte Schuljahr
	 */
	constructor(key: string, fachgruppen: Iterable<Fachgruppe>, schuljahr: number ) {
		this.key = key;

		const tmpFachgruppe = new ArrayList<Fachgruppe>();
		for (const i of fachgruppen)
			tmpFachgruppe.add(i);
		this._fachgruppen = tmpFachgruppe;
		this._schuljahr = schuljahr;
	}

	/**
	 * Wendet den Filter auf die übergebenen Optionen an. Falls nach mehreren Fachgruppen gefiltert wird, werden diese per union kombiniert.
	 *
	 * @param options
	 *
	 * @returns Liste der gefilterten Optionen
	 */
	apply(options: List<Fach>): List<Fach> {
		const filteredOptions: List<Fach> = new ArrayList<Fach>();

		for (const option of options) {
			if (this._fachgruppen.isEmpty())
				filteredOptions.add(option);
			for (const fachgruppe of this._fachgruppen)
				if (option.getFachgruppe(this._schuljahr) === toRaw(fachgruppe))
					filteredOptions.add(option);
		}

		return filteredOptions;
	}
}
