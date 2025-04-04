import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import type { List } from "../../../../../../core/src/java/util/List";
import type { SelectFilter } from "./SelectFilter";

/**
 * Ein Filter für die UiSelect-Komponente. Er filtert die übergebenen Optionen nach einem Suchbegriff. Wildcards sind nicht erlaubt.
 */
export class SearchSelectFilter<T> implements SelectFilter<T> {
	public key: string;
	// Der Suchbegriff, nach dem gefiltert wird
	private _search: string;
	// Methode, die den Text der Option erzeugt, um ihn mit dem Suchbegriff vergleichen zu können.
	private _getText: (item: T) => string;

	/**
	 * Konstruktor für den SearchSelectFilter
	 *
	 * @param key       Eindeutiger Key des Filters.
	 * @param search    Suchbegriff
	 * @param getText   Methode zur Generierung des Optionentextes.
	 */
	constructor(key: string, search: string, getText: (option: T) => string) {
		this.key = key;
		this._search = search;
		this._getText = getText;
	}

	/**
	 * Wendet den Filter auf die übergebenen Optionen an.
	 *
	 * @param options   die Optionen, die gefiltert werden sollten
	 *
	 * @returns Liste der gefilterten Optionen
	 */
	apply(options: Iterable<T>): List<T> {
		const filteredItems: List<T> = new ArrayList<T>();

		for (const item of options) {
			if (this._search === "" || this._getText(item).toLocaleLowerCase("de-DE").includes(this._search.toLocaleLowerCase("de-DE"))) {
				filteredItems.add(item);
			}
		}

		return filteredItems;
	}

	/**
	 * Setter für den Suchbegriff
	 *
	 * @param value   der neue Suchbegriff
	 */
	set search(value: string) {
		this._search = value;
	}
}
