import { shallowRef } from "vue";
import type { GridManager } from "./GridManager";
import { GridInputInnerText } from "./GridInputInnerText";
import { Note } from "../../../../../core/src/asd/types/Note";
import type { List } from "../../../../../core/src/java/util/List";
import type { Collection } from "../../../../../core/src/java/util/Collection";

/**
 * Ein Grid-Input für die Schnelleingabe einer Note
 */
export class GridInputNote<KEY> extends GridInputInnerText<KEY, string | null> {

	// Der Setter zum Schreiben der Daten
	protected _setter : (value: string | null) => void;

	// Der zwischengespeicherte Wert des Input-Elements als String
	protected _noteTemp = shallowRef<string>("");

	// Der zwischengespeicherte Wert des Input-Elements als Note
	protected _note = shallowRef<Note>(Note.KEINE);

	// Das Schuljahr für welches die Noteneingabe erfolgt
	protected _schuljahr: number;

	// Ein Set mit den ersten Zeichen der Notenkürzel
	protected _firstChars: Set<string>;

	/**
	 * Erzeugt ein neues Grid-Input für ein HTMLElement, welches die Note in dem innerText
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
	constructor(gridManager: GridManager<KEY, any, Collection<any> | List<any>>, key: KEY, col: number, row: number, elem: HTMLElement,
		setter: (value: string | null) => void, schuljahr: number) {
		super(gridManager, key, col, row, elem);
		this._setter = setter;
		this._schuljahr = schuljahr;
		this._firstChars = new Set<string>();
		for (const note of Note.data().getEintraegeBySchuljahr(schuljahr))
			if (note.kuerzel.length > 0)
				this._firstChars.add(note.kuerzel.charAt(0));
		super.updateText(null);
	}

	private getNoteFromKuerzel(value: string | null) : Note {
		return Note.fromKuerzel(value);
	}

	private getNotenKuerzelFromNote(value: Note) : string | null {
		const eintrag = value.daten(this._schuljahr);
		return (eintrag === null) ? null : eintrag.kuerzel;
	}

	/**
	 * Initialisiert das Input-Element mithilfe des übergebenen Wertes
	 *
	 * @param value   der Wert
	 */
	public update(value: string | null) {
		if (value === null)
			this._noteTemp.value = "";
		const note = this.getNoteFromKuerzel(value);
		super.updateText(value);
		this._note.value = note;
	}

	/**
	 * Schreibt die internen Daten dieses Inputs mithilfe des Setters.
	 */
	public commit() : void {
		if (this._note.value === Note.KEINE) {
			super.updateText("");
			this._setter(null);
		} else {
			this._setter(this.getNotenKuerzelFromNote(this._note.value));
		}
	}

	/**
	 * Ergänzt das übergebene Zeichen bei der Note, sofern diese Eingabe zu einem zulässigen Wert führt.
	 *
	 * @param character   das Zeichen
	 *
	 * @returns true, falls das Anhängen des Zeichens zulässig war und sonst false
	 */
	public append(ziffer : string): boolean {
		const len = this._noteTemp.value.length;
		const tmp = this._noteTemp.value + ziffer;
		const note = Note.fromKuerzel(tmp);
		if (((len === 0) && !this._firstChars.has(ziffer)) || ((len > 0) && (note === Note.KEINE)))
			return false;
		this._noteTemp.value = tmp;
		super.updateText(tmp);
		if (note !== Note.KEINE)
			this.update(tmp);
		return true;
	}

	/**
	 * Entfernt die letzte Stelle der Notenpunkt-Zahl. Handelt es sich um die letzte Stelle, so wird das
	 * Input geleert.
	 */
	public remove() {
		if (this._noteTemp.value.length === 0)
			return;
		const tmp = this._noteTemp.value.substring(0, this._noteTemp.value.length - 1);
		this._noteTemp.value = tmp;
		this.update(tmp);
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
		// Entferne die letzte Stelle des Notenkürzels
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
		// Prüfe, ob eine Zeichen eingegeben wurde
		if (event.key.length !== 1)
			return false; // Keine erfolgreiche Eingabe...
		// Wenn es sich um eine neue Fokussierung handelt, dann ersetze den Wert bei einer Eingabe (sonst anhängen)
		if (this._isNewFocus.value)
			this.update(null);
		// Konvertiere automatisch in Großbuchstaben, um z.B. E3 zu erfassen, auch bei Eigabe von e3
		return this.append(event.key.toLocaleUpperCase());
	}

}
