import { BaseSelectManagerSingle, type BaseSelectManagerSingleConfig } from "./BaseSelectManagerSingle";

export interface SelectManagerSingleConfig<T> extends BaseSelectManagerSingleConfig<T> {
	selectionDisplayText?: (option: T) => string;
	optionDisplayText?: (option: T) => string;
}

/**
 * Einfache Manager Klasse zur Verwendung in einer Single-Select-Komponente (UiSelect.vue). SelectManager übernehmen die Logik der Select-Komponente.
 * T bezeichnet dabei den Datentyp der auswählbaren Optionen.
 *
 * Diese Komponente kann für alle Datentypen verwendet werden. Zum Beispiel Strings, Numbers, Objects etc.
 */
export class SelectManagerSingle<T> extends BaseSelectManagerSingle<T> {

	// Definiert, wie die aktuelle Selektion im Inputfeld dargestellt wird
	protected _selectionDisplayText: (((option: T) => string)) = option => option?.toString() ?? "";

	// Definiert, wie die Optionen im Dropdown dargestellt werden
	protected _optionDisplayText: (((option: T) => string)) = option => option?.toString() ?? "";

	public constructor (config?: SelectManagerSingleConfig<T>) {
		super(config);
		if (config === undefined)
			return;
		if (config.optionDisplayText !== undefined)
			this.optionDisplayText = config.optionDisplayText;
		if (config.selectionDisplayText !== undefined)
			this.selectionDisplayText = config.selectionDisplayText;
	}

}
