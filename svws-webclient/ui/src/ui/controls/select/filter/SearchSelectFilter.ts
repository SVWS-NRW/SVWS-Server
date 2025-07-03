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
	private _getText: (option: T) => string;
	// Attribute, die für die Tiefensuche verwendet werden sollen. Die Tiefensuche geht über den generierten Text aus getText() hinaus und durchsucht auch
	// angegebene Attribute der Optionen.
	private _deepSearchAttributes: string[] = [];

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
	apply(options: List<T>): List<T> {
		const filteredOptions: List<T> = new ArrayList<T>()

		for (const option of options) {
			// Suche im Optionentext
			if (this._search === "" || this._getText(option).toLocaleLowerCase("de-DE").includes(this._search.toLocaleLowerCase("de-DE"))) {
				filteredOptions.add(option)
				continue;
			}

			// Suche in den Attributen der Option
			const matches = this._deepSearchAttributes.some((attr) => {
				const value = option[attr as keyof T];
				const stringValue = (value !== undefined && value !== null) ? value.toString() : '';
				return stringValue.toLocaleLowerCase("de-DE").includes(this._search.toLocaleLowerCase("de-DE"));
			})

			if (matches)
				filteredOptions.add(option);
		}

		return filteredOptions;
	}

	/**
	 * Setter für den Suchbegriff
	 *
	 * @param value   der neue Suchbegriff
	 */
	public set search(value: string) {
		this._search = value;
	}

	/**
	 * Setter für die Attribute der Tiefensuche
	 *
	 * @param value   die neuen Attribute, die zusätzlich durchsucht werden sollen.
	 */
	public set deepSearchAttributes(value: string[]) {
		this._deepSearchAttributes = value;
	}
}
