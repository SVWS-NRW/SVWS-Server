import { shallowRef } from "vue";
import type { GridManager } from "./GridManager";
import { GridInputInnerText } from "./GridInputInnerText";
import { DeveloperNotificationException } from "@core";

/**
 * Ein Grid-Input für die Schnelleingabe der Reihenfolge der mündlichen Prüfungen im Abiturbereich.
 */
export class GridInputAbiturPruefungsreihenfolge<KEY> extends GridInputInnerText<KEY> {

	// Der zwischengespeicherte Wert des Input-Elements als Reihenfolge von 1 bis 3 oder null
	protected reihenfolge = shallowRef<number | null>(null);

	/**
	 * Erzeugt ein neues Grid-Input für ein HTMLElement, welches die Notepunkte im Abitur in dem innerText
	 * des Elements rendert.
	 *
	 * @param gridManager   der Grid-Manager
	 * @param key           der eindeutige Schlüssel zur Identifikation des Input
	 * @param col           die Spalte, in welcher sich das Input befindet
	 * @param row           die Zeile, in welcher sich das Input befindet
	 * @param elem          das HTML-Element, welches dem Grid-Input und damit der Zelle des Grid zugeordnet ist
	 * @param getter        der Getter zum Holen der Daten für das Grid-Input
	 * @param setter        der Setter zum Schreiben der Daten des Grid-Input
	 */
	constructor(gridManager: GridManager<KEY>, key: KEY, col: number, row: number, elem: HTMLElement,
		getter : () => number | null, setter : (value: number | null) => void) {
		super(gridManager, key, col, row, elem, () => {
			const value = getter();
			return (value === null) ? null : "" + value;
		}, (value: string | null) => {
			if (value === null) {
				setter(null);
				return;
			}
			const tmp = parseInt(value);
			if (isNaN(tmp))
				throw new DeveloperNotificationException("Der Wert für die Reihenfolge muss null oder ein gültiger Zahlenwert sein.");
			setter(tmp);
		});
		elem.innerText = (this._value.value === null) ? "" : this._value.value + ".";
	}

	/**
	 * Initialisiert das Input-Element mithilfe des Getters
	 */
	public init() {
		super.init();
		const rf = (this._value.value === null) ? NaN : parseInt(this._value.value);
		this.reihenfolge = shallowRef(isNaN(rf) ? null : rf);
	}

	/**
	 * Setzt das Input-Element auf leer (null)
	 */
	public clear() {
		super.clear();
		this.reihenfolge.value = null;
	}

	/**
	 * Schreibt die internen Daten dieses Inputs mithilfe des Setters.
	 */
	public commit() : void {
		this._setter("" + this.reihenfolge.value);
	}

	/**
	 * Setzt den Wert des Input-Element auf die Reihenfolge, die aktuell lokal gespeichert ist.
	 */
	public setValue() {
		if (this.reihenfolge.value === null)
			super.clear();
		else
			super.setValue("" + this.reihenfolge.value + ".");
	}

	/**
	 * Prüft, ob die übergebene Ziffer ein sinnvoller Wert für die Reihenfolge ist (zwischen 1 und 3)
	 *
	 * @param ziffer   die Ziffer
	 *
	 * @returns true, falls die Ziffer zulässig war und sonst false
	 */
	public check(ziffer : number): boolean {
		if ((this.reihenfolge.value !== null) || (ziffer < 0) || (ziffer > 3))
			return false;
		this.reihenfolge.value = ziffer;
		this.setValue();
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
		super.onKeyDown(event);
		// Lösche ggf. den aktuellen Wert
		if ((event.key === "Delete") || (event.key === "Backspace")) {
			this.clear();
			return true;
		}
		// Speicher den aktuellen Wert im Input
		if (event.key === "Enter") {
			this.commit();
			this.navigateDown();
			return true;
		}
		// Prüfe, ob eine Ziffer eingegeben wurde
		const ziffer = parseInt(event.key);
		if (isNaN(ziffer))
			return false; // Keine erfolgreiche Eingabe...
		// Wenn es sich um eine neue Fokussierung handelt, dann ersetze den Wert bei einer Eingabe (sonst anhängen)
		if (this._isNewFocus.value)
			this.clear();
		return this.check(ziffer);
	}

}
