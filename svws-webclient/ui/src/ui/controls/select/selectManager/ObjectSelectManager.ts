import { BaseSelectManager } from "./BaseSelectManager";

/**
 * Allgemeine Manager-Klasse für die Select-Komponente (`UiSelect.vue`), die mit allen Arten von Objekten umgehen kann.
 */
export class ObjectSelectManager extends BaseSelectManager<any> {

	protected _selectionDisplayText: ((option: any) => string);
	protected _optionDisplayText: ((option: any) => string);

	/**
	 * Konstruktor des Managers
	 *
	 * @param multi                  definiert, ob Multi-Selektionen erlaubt sind.
	 * @param options                die Liste aller Optionen der Komponente (ungefiltert).
	 * @param selectionDisplayText   eine Funktion, die angibt, wie die Texte der Optionen in der Selektion angezeigt werden sollen.
	 * @param optionDisplayText      eine Funktion, die angibt, wie die Texte der Optionen im Dropdown angezeigt werden sollen.
	 * @param selected               die Liste der aktuell selektierten Optionen. Bei einer Singe-Select-Komponente darf maximal ein Objekt in dieser Liste sein.
	 */
	public constructor(multi: boolean, options: Iterable<any>, selectionDisplayText: (option: any) => string, optionDisplayText: (option: any) => string,
		selected?: Iterable<any>) {
		super(multi, options, selected);
		this._optionDisplayText = optionDisplayText;
		this._selectionDisplayText = selectionDisplayText;
	}

	getSelectionText(option: any): string {
		return this._selectionDisplayText(option);
	}

	getOptionText(option: any): string {
		return this._optionDisplayText(option);
	}

}