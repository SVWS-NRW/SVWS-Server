import type { GridManager } from "./GridManager";
import { GridInput } from "./GridInput";

/**
 * Diese Klasse ist eine Basisklasse für Grid-Inputs, welche ihre Daten selber
 * verwalten und ihren Text einfach über den innerText in einem HtmlElement rendern.
 */
export class GridInputInnerText<KEY, DATA> extends GridInput<KEY, DATA> {

	// Gibt an, ob Änderungen an dem Zellwert automatisch beim Verlieren des Focus gepeichert werden sollen
	protected _autoCommit: boolean;

	/**
	 * Erzeugt ein neues Grid-Input für ein HTMLElement, welches seine Daten in dem innerText des Elements rendert.
	 *
	 * @param gridManager   der Grid-Manager
	 * @param key           der eindeutige Schlüssel zur Identifikation des Input
	 * @param col           die Spalte, in welcher sich das Input befindet
	 * @param row           die Zeile, in welcher sich das Input befindet
	 * @param elem          das HTML-Element, welches dem Grid-Input und damit der Zelle des Grid zugeordnet ist
	 * @param autoCommit    gibt an, ob die Daten des Grid-Inputs automatisch über den Setter geschrieben werden sollen,
	 *                      sobald das Input-Element den Fokus verliert
	 */
	constructor(gridManager: GridManager<KEY>, key: KEY, col: number, row: number, elem : HTMLElement, autoCommit: boolean = true) {
		super(gridManager, key, col, row, elem);
		this._autoCommit = autoCommit;
	}

	/**
	 * Initialisiert den internen Zustand dieses Elements mithilfe des Getters
	 * und setzt den inneren Text des Input-Elements
	 */
	public updateText(value: string | null) : void {
		this._elem.innerText = (value === null) ? "" : value;
	}

	/**
	 * Schreibt die internen Daten dieses Inputs mithilfe des Setters.
	 */
	public commit() : void {
		// Sollte in der abgeleiteten Klasse überschrieben werden, um den Setter aufzurufen
	}

	/**
	 * Eine Methode, welche überschrieben werden kann, um auf das Verlieren des Fokus zu reagieren.
	 * Diese Methode sollte als super.onBlur() dabei aufgerufen werden.
	 */
	public onBlur() : void {
		if (this._autoCommit) {
			if (!this._isNewFocus.value)
				this.commit();
			return;
		}
	}

}
