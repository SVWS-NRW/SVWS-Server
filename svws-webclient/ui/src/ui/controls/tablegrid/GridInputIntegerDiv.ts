import { shallowRef } from "vue";
import type { GridManager } from "./GridManager";
import { GridInputInnerText } from "./GridInputInnerText";

/**
 * Ein Grid-Input für die Schnelleingabe von Ganzzahlen einer bestimmten Länge.
 */
export class GridInputIntegerDiv<KEY> extends GridInputInnerText<KEY, string | null> {

	// Der Setter zum Schreiben der Daten
	protected _setter : (value: number | null) => void;

	// Der zwischengespeicherte Wert des Input-Elements
	protected _zahl = shallowRef<number | null>(null);

	// Die maximale Zahl, die erlaubt ist
	protected _max : number | null = null;

	/**
	 * Erzeugt ein neues Grid-Input für ein HTMLElement, welches die Notepunkte im Abitur in dem innerText
	 * des Elements rendert.
	 *
	 * @param gridManager   der Grid-Manager
	 * @param key           der eindeutige Schlüssel zur Identifikation des Input
	 * @param col           die Spalte, in welcher sich das Input befindet
	 * @param row           die Zeile, in welcher sich das Input befindet
	 * @param elem          das HTML-Element, welches dem Grid-Input und damit der Zelle des Grid zugeordnet ist
	 * @param max           die maximale Zahl, die erlaubt ist
	 * @param setter        der Setter zum Schreiben der Daten des Grid-Input
	 */
	constructor(gridManager: GridManager<KEY>, key: KEY, col: number, row: number, elem: HTMLElement, max: number | null,
		setter: (value: number | null) => void) {
		super(gridManager, key, col, row, elem);
		this._max = max;
		this._setter = setter;
		super.updateText(null);
	}

	/**
	 * Initialisiert das Input-Element mithilfe des übergebenen Wertes
	 *
	 * @param value   der Wert
	 */
	public update(value: string | null) {
		this._zahl.value = ((value === null) || (value === "")) ? null : parseInt(value);
		super.updateText(value);
	}

	/**
	 * Setzt das Maximum, welches als Grenze für die Eingabe verwendet wird.
	 *
	 * @param value   das neue Maximum
	 */
	public set max(value: number) {
		this._max = value;
	}

	/**
	 * Schreibt die internen Daten dieses Inputs mithilfe des Setters.
	 */
	public commit() : void {
		this._setter(this._zahl.value);
	}

	/**
	 * Ergänzt die übergebene Ziffer bei den Notenpunkten, sofern diese Eingabe zu einem zulässigen Wert führt.
	 *
	 * @param ziffer   die Ziffer
	 *
	 * @returns true, falls das Anhängen der Ziffer zulässig war und sonst false
	 */
	public append(ziffer : number): boolean {
		const tmp = (this._zahl.value === null) ? ziffer : ((this._zahl.value * 10) + ziffer);
		if ((this._max !== null) && (tmp > this._max))
			return false;
		this.update("" + tmp);
		return true;
	}

	/**
	 * Entfernt die letzte Stelle der Notenpunkt-Zahl. Handelt es sich um die letzte Stelle, so wird das
	 * Input geleert.
	 */
	public remove() {
		if (this._zahl.value === null)
			return;
		if (this._zahl.value < 10) {
			this.update(null);
			return;
		}
		const zahlNeu = Math.floor(this._zahl.value / 10);
		this.update("" + zahlNeu);
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
		if (event.key === "Delete") {
			this.update(null);
			return true;
		}
		// Entferne die letzte Stelle der Zahl (Division durch 10)
		if (event.key === "Backspace") {
			this.remove();
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
		// Wenn es sich um eine neue Fokussierung handelt, dann darf die Ziffer aber nicht größer als das vorgegebene Maximum sein (falls dieses einstellig ist)
		if ((this._isNewFocus.value) && ((this._max !== null) && (ziffer > this._max)))
			return false;
		// Wenn es sich um eine neue Fokussierung handelt, dann ersetze den Wert bei einer Eingabe (sonst anhängen)
		if (this._isNewFocus.value)
			this.update(null);
		return this.append(ziffer);
	}

}
