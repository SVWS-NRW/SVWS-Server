import { BaseSelectManager } from "./BaseSelectManager";

/**
 * Spezialisierte Manager-Klasse für die Select-Komponente (`UiSelect.vue`), die einfache Strings oder Zahlen als Optionstypen unterstützt.
 */
export class SimpleSelectManager extends BaseSelectManager<string | number> {

	// Definiert, wie die aktuelle Selektion im Inputfeld dargestellt wird
	protected _selectionDisplayText: string | ((option: string | number) => string) = "";

	// Definiert, wie die Optionen im Dropdown dargestellt werden
	protected _optionDisplayText: string | ((option: string | number) => string) = "";

	/**
	 * Konstruktor des Managers
	 *
	 * @param multi      definiert, ob Multi-Selektionen erlaubt sind.
	 * @param list       die Liste aller Optionen der Komponente (ungefiltert).
	 * @param selected   die Liste der aktuell selektierten Optionen. Bei einer Singe-Select-Komponente darf maximal ein Objekt in dieser Liste sein.
	 */
	public constructor(multi: boolean, liste: Iterable<string | number>, selected?: Iterable<string | number>) {
		super(multi, liste, selected);
		this.filtered = this._list.value;
	}

	/**
	 * Getter für den Text eines selektierten Objektes.
	 *
	 * @param option   Option, dessen Text abgefragt wird
	 *
	 * @returns den Text der Option.
	 */
	public getSelectionText(option: string | number): string {
		return option.toString();
	}

	/**
	 * Getter für den Text der übergebenen Option.
	 *
	 * @param option   Option, dessen Text abgefragt wird
	 *
	 * @returns den Text der Option.
	 */
	public getOptionText(option: string | number): string {
		return option.toString();
	}
}