import { shallowRef } from "vue";
import type { GridManager } from "./GridManager";
import { GridInputInnerText } from "./GridInputInnerText";
import { Note, NoteKatalogEintrag } from "@core";

/**
 * Ein Grid-Input für die Schnelleingabe der Notenpunkte im Abiturbereich.
 */
export class GridInputAbiturNotenpunkte<KEY> extends GridInputInnerText<KEY> {

	// Der zwischengespeicherte Wert des Input-Elements als Notepunkte oder null
	protected notenpunkte = shallowRef<number | null>(null);

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
	 * @param schuljahr     das Schuljahr, in dem das Abitur stattfindet
	 */
	constructor(gridManager: GridManager<KEY>, key: KEY, col: number, row: number, elem: HTMLElement,
		getter: () => string | null, setter: (value: string | null) => void, schuljahr: number) {
		super(gridManager, key, col, row, elem, (): string | null => {
			const notenkuerzel = getter();
			if (notenkuerzel === null)
				return null;
			const nke : NoteKatalogEintrag | null = Note.fromKuerzel(notenkuerzel).daten(schuljahr);
			if ((nke === null) || (nke.notenpunkte === null))
				return null;
			return ((nke.notenpunkte < 10) ? "0" : "") + nke.notenpunkte;
		}, (value: string | null) => {
			const note = Note.fromNotenpunkteString(value);
			const eintrag = note.daten(schuljahr);
			setter((eintrag === null) ? null : eintrag.kuerzel);
		});
		elem.innerText = this._value.value ?? "";
	}

	/**
	 * Initialisiert das Input-Element mithilfe des Getters
	 */
	public init() {
		super.init();
		const np = (this._value.value === null) ? NaN : parseInt(this._value.value);
		this.notenpunkte = shallowRef(isNaN(np) ? null : np);
	}

	/**
	 * Setzt das Input-Element auf leer (null)
	 */
	public clear() {
		super.clear();
		this.notenpunkte.value = null;
	}

	/**
	 * Setzt den Wert des Input-Element auf die Notenpunkte, die aktuell lokal gespeichert sind.
	 */
	public setValue() {
		if (this.notenpunkte.value === null)
			super.clear();
		else
			super.setValue(((this.notenpunkte.value >= 10) ? "" : "0") + this.notenpunkte.value);
	}

	/**
	 * Ergänzt die übergebene Ziffer bei den Notenpunkten, sofern diese Eingabe zu einem zulässigen Wert führt.
	 *
	 * @param ziffer   die Ziffer
	 *
	 * @returns true, falls das Anhängen der Ziffer zulässig war und sonst false
	 */
	public append(ziffer : number): boolean {
		if ((this.notenpunkte.value !== null) && (this.notenpunkte.value !== 0) && (((this.notenpunkte.value === 1) && (ziffer > 5)) || (this.notenpunkte.value > 1)))
			return false;
		this.notenpunkte.value = (this.notenpunkte.value === null) ? ziffer : (this.notenpunkte.value * 10) + ziffer;
		this.setValue();
		return true;
	}

	/**
	 * Entfernt die letzte Stelle der Notenpunkt-Zahl. Handelt es sich um die letzte Stelle, so wird das
	 * Input geleert.
	 */
	public remove() {
		if (this.notenpunkte.value === null)
			return;
		if (this.notenpunkte.value < 10) {
			this.clear();
			return;
		}
		this.notenpunkte.value = Math.floor(this.notenpunkte.value / 10);
		this.setValue();
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
			this.clear();
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
			return true;
		}
		// Prüfe, ob eine Ziffer eingegeben wurde
		const ziffer = parseInt(event.key);
		if (isNaN(ziffer))
			return false; // Keine erfolgreiche Eingabe...
		// Wenn es sich um eine neue Fokussierung handelt, dann ersetze den Wert bei einer Eingabe (sonst anhängen)
		if (this._isNewFocus.value)
			this.clear();
		return this.append(ziffer);
	}

}
