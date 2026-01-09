import type { GridManager } from "./GridManager";
import { GridInputInnerText } from "./GridInputInnerText";
import type { List } from "../../../../../core/src/java/util/List";
import type { Collection } from "../../../../../core/src/java/util/Collection";

/**
 * Ein Grid-Input für die Schnelleingabe von Zahlen mit einer festen Anzahl an Nachkommastellen.
 */
export class GridInputNumberFixed<KEY> extends GridInputInnerText<KEY, number | null> {

	// Der Setter zum Schreiben der Daten
	protected _setter: (value: number | null) => void;

	// Der zwischengespeicherte Wert des Input-Elements, anhand der Anzahl der Nachkommastellen (dp) verschoben
	protected _zahl: number | null = null;

	// Gibt die Nachkommastelle an, bei der die nächste Eingabe stattfindet. Ein Wert von 0 gibt an, dass aktuell noch keine Eingabe der Nachkommastellen stattfindet.
	protected _nachkommastelle: number = 0;

	// Die maximale Anzahl an zulässigen Nachkommastellen (decimal places)
	protected _dp: number = 2;

	// Die maximale Zahl, die erlaubt ist
	protected _max: number | null = null;

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
	 * @param dp            die Anzahl der zulässigen Nachkommastellen (decimal places)
	 * @param setter        der Setter zum Schreiben der Daten des Grid-Input
	 */
	constructor(gridManager: GridManager<KEY, any, Collection<any> | List<any>>, key: KEY, col: number, row: number, elem: HTMLElement, max: number | null,
		dp: number, setter: (value: number | null) => void) {
		super(gridManager, key, col, row, elem);
		this._max = max;
		this._dp = dp;
		this._setter = setter;
		super.updateText(null);
	}

	/**
	 * Initialisiert das Input-Element mithilfe des übergebenen Wertes
	 *
	 * @param value   der Wert
	 */
	public update(value: number | null): void {
		if (value === null) {
			this.reset();
			return;
		}
		this._zahl = value * Math.pow(10, this._dp);
		this._nachkommastelle = this._dp + 1;
		while ((this._nachkommastelle > 1) && (this._zahl % 10 === 0)) {
			this._zahl /= 10;
			this._nachkommastelle--;
		}
		if (this._nachkommastelle === 1) {
			this._nachkommastelle = 0;
		}
		this.updateInternal();
	}


	private reset() {
		this._nachkommastelle = 0;
		this._zahl = null;
	}

	/**
	 * Initialisiert das Input-Element mithilfe des übergebenen Wertes
	 *
	 * @param value   der Wert
	 */
	private updateInternal() {
		const zahl = this._zahl;
		if (zahl === null) {
			super.updateText("");
			return;
		}
		const tmp = (this._nachkommastelle <= 1) ? zahl : zahl / Math.pow(10, this._nachkommastelle - 1);
		super.updateText(tmp.toFixed(this._dp).replace(".", ","));
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
	public commit(): void {
		if (this._zahl === null) {
			this._setter(null);
		} else if (this._nachkommastelle === 0) {
			this._setter(this._zahl);
		} else {
			this._setter(this._zahl / Math.pow(10, this._nachkommastelle - 1));
		}
	}

	/**
	 * Ergänzt die übergebene Ziffer bei den Notenpunkten, sofern diese Eingabe zu einem zulässigen Wert führt.
	 *
	 * @param ziffer   die Ziffer
	 *
	 * @returns true, falls das Anhängen der Ziffer zulässig war und sonst false
	 */
	public append(ziffer: number): boolean {
		const tmp = (this._zahl === null) ? ziffer : ((this._zahl * 10) + ziffer);
		if ((this._max !== null) && (tmp > this._max * Math.pow(10, this._nachkommastelle))) {
			return false;
		}
		if (this._nachkommastelle > this._dp) {
			return false;
		}
		if (this._nachkommastelle > 0) {
			this._nachkommastelle++;
		}
		this._zahl = tmp;
		this.updateInternal();
		return true;
	}

	/**
	 * Entfernt die letzte Stelle der Notenpunkt-Zahl. Handelt es sich um die letzte Stelle, so wird das
	 * Input geleert.
	 */
	public remove() {
		if (this._zahl === null) {
			return;
		}
		if ((this._nachkommastelle === 0) && (this._zahl < 10)) {
			this.reset();
			return;
		}
		if (this._nachkommastelle > 0) {
			this._nachkommastelle--;
		}
		this._zahl = Math.floor(this._zahl / 10);
		this.updateInternal();
	}

	/**
	 * Diese Methode reagiert auf Tastatur-Eingaben bei dem Input.
	 *
	 * @param event   das Tastaturereignis
	 *
	 * @returns true   es hat aufgrund des Tastaturereignisses eine Änderung am Zustand des Inputs stattgefunden
	 */
	public onKeyDown(event: KeyboardEvent): boolean {
		if (super.onKeyDownNavigation(event)) {
			return false;
		}
		// Lösche ggf. den aktuellen Wert
		if (event.key === "Delete") {
			this.reset();
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
			if (this.navigateOnEnter === "DOWN") {
				this.navigateDown();
			} else if (this.navigateOnEnter === "RIGHT") {
				this.navigateRight();
			}
			return true;
		}
		// Prüfe, ob ein Komma oder ein Punkt eingegeben wurde, für Nachkommastellen eingegeben wurde
		if ((event.key === ",") || (event.key === ".")) {
			if (this._nachkommastelle > 0) {
				return false;
			}
			// Für den Fall, dass die Eingabe mit einem Komma begonnen wird, ergänze automatisch eine 0 vor dem Komma
			if (this._isNewFocus.value) {
				this.reset();
				this.append(0);
			}
			this._nachkommastelle = 1;
		}
		// Prüfe, ob eine Ziffer eingegeben wurde
		const ziffer = Number.parseInt(event.key);
		if (Number.isNaN(ziffer)) {
			return false;
		} // Keine erfolgreiche Eingabe...
		// Wenn es sich um eine neue Fokussierung handelt, dann darf die Ziffer aber nicht größer als das vorgegebene Maximum sein (falls dieses einstellig ist)
		if ((this._isNewFocus.value) && ((this._max !== null) && (ziffer > this._max))) {
			return false;
		}
		// Wenn es sich um eine neue Fokussierung handelt, dann ersetze den Wert bei einer Eingabe (sonst anhängen)
		if (this._isNewFocus.value) {
			this.reset();
		}
		return this.append(ziffer);
	}

}
