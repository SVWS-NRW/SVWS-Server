import type { Fach, Fachgruppe } from "../../../../../../core/src";
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
	 * @param fachgruppe    Liste der Fachgruppen, nach denen gefiltert wird.
	 * @param schuljahr     Das genutzte Schuljahr
	 */
	constructor(key: string, fachgruppe: List<Fachgruppe>, schuljahr: number ) {
		this.key = key;
		this._fachgruppen = fachgruppe;
		this._schuljahr = schuljahr;
	}

	/**
	 * Wendet den Filter auf die übergebenen Optionen an. Falls nach mehreren Fachgruppen gefiltert wird, werden diese per union kombiniert.
	 *
	 * @param options
	 *
	 * @returns Liste der gefilterten Optionen
	 */
	apply(options: Iterable<Fach>): List<Fach> {
		const filteredItems: List<Fach> = new ArrayList<Fach>();

		for (const item of options) {
			if (this._fachgruppen.isEmpty())
				filteredItems.add(item);
			for (const fachgruppe of this._fachgruppen)
				if (item.getFachgruppe(this._schuljahr) === toRaw(fachgruppe))
					filteredItems.add(item);
		}

		return filteredItems;
	}
}
