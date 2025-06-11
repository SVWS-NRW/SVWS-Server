import { shallowRef } from "vue";
import type { GridManager } from "./GridManager";
import { GridInputInnerText } from "./GridInputInnerText";
import type { NoteKatalogEintrag } from "../../../../../core/src/asd/data/NoteKatalogEintrag";
import { Note } from "../../../../../core/src/asd/types/Note";

/**
 * Ein Grid-Input für die Schnelleingabe der Notenpunkte im Abiturbereich.
 */
export class GridInputAbiturNotenpunkte<KEY> extends GridInputInnerText<KEY, string | null> {

	// Der Setter zum Schreiben der Daten
	protected _setter : (value: string | null) => void;

	// Der zwischengespeicherte Wert des Input-Elements als Notepunkte oder null
	protected _notenpunkte = shallowRef<number | null>(null);

	// Das Schuljahr für welches die Noteneingabe erfolgt
	protected _schuljahr: number;

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
	 * @param schuljahr     das Schuljahr, in dem das Abitur stattfindet
	 */
	constructor(gridManager: GridManager<KEY>, key: KEY, col: number, row: number, elem: HTMLElement,
		setter: (value: string | null) => void, schuljahr: number) {
		super(gridManager, key, col, row, elem);
		this._setter = setter;
		this._schuljahr = schuljahr;
		super.updateText(null);
	}

	private getNotenpunkteFromKuerzel(value: string | null) : number | null {
		const notenkuerzel = value;
		if (notenkuerzel === null)
			return null;
		const nke : NoteKatalogEintrag | null = Note.fromKuerzel(notenkuerzel).daten(this._schuljahr);
		if ((nke === null) || (nke.notenpunkte === null))
			return null;
		return nke.notenpunkte;
	}

	private getNotenKuerzelFromNotenpunkte(value: number | null) : string | null {
		const note = Note.fromNotenpunkte(value);
		const eintrag = note.daten(this._schuljahr);
		return (eintrag === null) ? null : eintrag.kuerzel;
	}

	/**
	 * Initialisiert das Input-Element mithilfe des übergebenen Wertes
	 *
	 * @param value   der Wert
	 */
	public update(value: string | null) {
		const np = this.getNotenpunkteFromKuerzel(value);
		super.updateText((np === null) ? null : ((np < 10) ? "0" : "") + np);
		this._notenpunkte.value = np;
	}

	/**
	 * Schreibt die internen Daten dieses Inputs mithilfe des Setters.
	 */
	public commit() : void {
		this._setter(this.getNotenKuerzelFromNotenpunkte(this._notenpunkte.value));
	}

	/**
	 * Ergänzt die übergebene Ziffer bei den Notenpunkten, sofern diese Eingabe zu einem zulässigen Wert führt.
	 *
	 * @param ziffer   die Ziffer
	 *
	 * @returns true, falls das Anhängen der Ziffer zulässig war und sonst false
	 */
	public append(ziffer : number): boolean {
		if ((this._notenpunkte.value !== null) && (this._notenpunkte.value !== 0) && (((this._notenpunkte.value === 1) && (ziffer > 5)) || (this._notenpunkte.value > 1)))
			return false;
		const npNeu = (this._notenpunkte.value === null) ? ziffer : (this._notenpunkte.value * 10) + ziffer;
		this.update(this.getNotenKuerzelFromNotenpunkte(npNeu));
		return true;
	}

	/**
	 * Entfernt die letzte Stelle der Notenpunkt-Zahl. Handelt es sich um die letzte Stelle, so wird das
	 * Input geleert.
	 */
	public remove() {
		if (this._notenpunkte.value === null)
			return;
		if (this._notenpunkte.value < 10) {
			this.update(null);
			return;
		}
		const npNeu = Math.floor(this._notenpunkte.value / 10);
		this.update(this.getNotenKuerzelFromNotenpunkte(npNeu));
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
			this.navigateDown();
			return true;
		}
		// Prüfe, ob eine Ziffer eingegeben wurde
		const ziffer = parseInt(event.key);
		if (isNaN(ziffer))
			return false; // Keine erfolgreiche Eingabe...
		// Wenn es sich um eine neue Fokussierung handelt, dann ersetze den Wert bei einer Eingabe (sonst anhängen)
		if (this._isNewFocus.value)
			this.update(null);
		return this.append(ziffer);
	}

}
