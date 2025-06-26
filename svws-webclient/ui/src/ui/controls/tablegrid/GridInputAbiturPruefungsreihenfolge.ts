import type { GridManager } from "./GridManager";
import { GridInputInnerText } from "./GridInputInnerText";

/**
 * Ein Grid-Input für die Schnelleingabe der Reihenfolge der mündlichen Prüfungen im Abiturbereich.
 */
export class GridInputAbiturPruefungsreihenfolge<KEY> extends GridInputInnerText<KEY, number | null> {

	// Der Setter zum Schreiben der Daten
	protected _setter : (value: number | null) => void;

	// Der zwischengespeicherte Wert des Input-Elements als Reihenfolge von 1 bis 3 oder null
	protected _reihenfolge : number | null = null;

	/**
	 * Erzeugt ein neues Grid-Input für ein HTMLElement, welches die Notepunkte im Abitur in dem innerText
	 * des Elements rendert.
	 *
	 * @param gridManager   der Grid-Manager
	 * @param key           der eindeutige Schlüssel zur Identifikation des Input
	 * @param col           die Spalte, in welcher sich das Input befindet
	 * @param row           die Zeile, in welcher sich das Input befindet
	 * @param elem          das HTML-Element, welches dem Grid-Input und damit der Zelle des Grid zugeordnet ist
	 * @param setter        der Setter zum Schreiben der Daten des Grid-Input
	 */
	constructor(gridManager: GridManager<KEY>, key: KEY, col: number, row: number, elem: HTMLElement, setter : (value: number | null) => void) {
		super(gridManager, key, col, row, elem);
		this._setter = setter;
		super.updateText(null);
	}

	/**
	 * Initialisiert das Input-Element mithilfe des übergebenen Wertes
	 *
	 * @param value   der Wert
	 */
	public update(value: number | null) {
		this._reihenfolge = value;
		super.updateText((value === null) ? null : "" + value + ".");
	}

	/**
	 * Schreibt die internen Daten dieses Inputs mithilfe des Setters.
	 */
	public commit() : void {
		this._setter(this._reihenfolge);
	}

	/**
	 * Prüft, ob die übergebene Ziffer ein sinnvoller Wert für die Reihenfolge ist (zwischen 1 und 3)
	 *
	 * @param ziffer   die Ziffer
	 *
	 * @returns true, falls die Ziffer zulässig war und sonst false
	 */
	public check(ziffer : number): boolean {
		if ((this._reihenfolge !== null) || (ziffer < 0) || (ziffer > 3))
			return false;
		this.update(ziffer);
		return true;
	}

	/**
	 * Diese Methode reagiert auf Tastatur-Eingaben bei dem Input.
	 *
	 * @param event   das Tastaturereignis
	 *
	 * @returns true   es hat aufgrund des Tastaturereignisses eine Änderung am Zustand des Inputs stattgefunden
	 */
	public onKeyDown(event : KeyboardEvent) : boolean {
		if (super.onKeyDownNavigation(event))
			return false;
		// Lösche ggf. den aktuellen Wert
		if ((event.key === "Delete") || (event.key === "Backspace")) {
			this.update(null);
			return true;
		}
		// Speicher den aktuellen Wert im Input
		if (event.key === "Enter") {
			this.commit();
			if (this.navigateOnEnter === "DOWN")
				this.navigateDown();
			else if (this.navigateOnEnter === "RIGHT")
				this.navigateRight();
			return true;
		}
		// Prüfe, ob eine Ziffer eingegeben wurde
		const ziffer = parseInt(event.key);
		if (isNaN(ziffer))
			return false; // Keine erfolgreiche Eingabe...
		// Wenn es sich um eine neue Fokussierung handelt, dann ersetze den Wert bei einer Eingabe (sonst anhängen)
		if (this._isNewFocus.value)
			this.update(null);
		return this.check(ziffer);
	}

}
