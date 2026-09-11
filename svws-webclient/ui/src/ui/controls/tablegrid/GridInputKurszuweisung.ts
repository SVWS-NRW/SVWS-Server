import type { Collection } from "@core/java/util/Collection";

import { GridInputInnerText } from "./GridInputInnerText";
import type { GridManager } from "./GridManager";

export type ZulaessigeKursartWerte = 'E' | 'G' | null;

/**
 * Ein Grid-Input für die Schnelleingabe der Reihenfolge der mündlichen Prüfungen im Abiturbereich.
 */
export class GridInputKurszuweisung<KEY> extends GridInputInnerText<KEY, ZulaessigeKursartWerte> {

	private readonly _zulaessigeEingaben = new Set(['G', 'E', null]);

	// Der Setter zum Schreiben der Daten
	protected _setter: (value: ZulaessigeKursartWerte) => void;

	// Der zwischengespeicherte Wert des Input-Elements als Reihenfolge von E, G oder null
	protected _kursart: ZulaessigeKursartWerte = null;

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
	constructor(gridManager: GridManager<KEY, any, Collection<any> | any[]>, key: KEY, col: number, row: number, elem: HTMLElement, setter: (value: ZulaessigeKursartWerte) => void) {
		super(gridManager, key, col, row, elem);
		this._setter = setter;
		super.updateText(null);
	}

	/**
	 * Initialisiert das Input-Element mithilfe des übergebenen Wertes
	 *
	 * @param value   der Wert
	 */
	public update(value: ZulaessigeKursartWerte) {
		this._kursart = value;
		super.updateText(value);
	}

	/**
	 * Schreibt die internen Daten dieses Inputs mithilfe des Setters.
	 */
	public commit(): void {
		this._setter(this._kursart);
	}

	/**
	 * Prüft, ob die übergebene Eingabe ein sinnvoller Wert für die Reihenfolge ist
	 *
	 * @param eingabe   die Eingabe
	 *
	 * @returns true, falls die Eingabe zulässig war und sonst false
	 */
	private check(eingabe: string | null): eingabe is ZulaessigeKursartWerte {
		if (!this._zulaessigeEingaben.has(eingabe)) {
			return false;
		}
		return true;
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
		if ((event.key === "Delete") || (event.key === "Backspace")) {
			this.update(null);
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
		// Prüfe, ob eine Ziffer eingegeben wurde
		const upcased = event.key.toUpperCase();
		if (this.check(upcased)) {
			this.update(upcased);
			return true;
		}
		return false;
	}

}
