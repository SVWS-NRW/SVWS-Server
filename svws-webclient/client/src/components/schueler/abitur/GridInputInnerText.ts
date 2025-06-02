import { shallowRef } from "vue";
import type { GridManager } from "./GridManager";
import { GridInput } from "./GridInput";

/**
 * Diese Klasse ist eine Basisklasse für Grid-Inputs, welche ihre Daten selber
 * verwalten und ihren Text einfach über den innerText in einem HtmlElement rendern.
 */
export class GridInputInnerText<KEY> extends GridInput<KEY> {

	// Der zwischengespeicherte Wert des Input-Elements
	protected _value = shallowRef<string | null>(null);

	// Der Getter zum Holen des darzustellenden Zellwertes
	protected _getter: () => string | null;

	// Der Setter zum Setzen des Zellwertes
	protected _setter: (value: string | null) => void;

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
	 * @param getter        der Getter zum Holen der Daten für das Grid-Input
	 * @param setter        der Setter zum Schreiben der Daten des Grid-Input
	 * @param autoCommit    gibt an, ob die Daten des Grid-Inputs automatisch über den Setter geschrieben werden sollen,
	 *                      sobald das Input-Element den Fokus verliert
	 */
	constructor(gridManager: GridManager<KEY>, key: KEY, col: number, row: number, elem : HTMLElement,
		getter : () => string | null, setter : (value: string | null) => void, autoCommit: boolean = true) {
		super(gridManager, key, col, row, elem);
		this._getter = getter;
		this._setter = setter;
		this._autoCommit = autoCommit;
		this.init();
	}

	/**
	 * Gibt den aktuellen internen Wert des Inputs zurück
	 */
	public get value() : string | null {
		return this._value.value;
	}

	/**
	 * Initialisiert den internen Zustand dieses Elements mithilfe des Getters
	 * und setzt den inneren Text des Input-Elements
	 */
	public init() : void {
		this._value.value = this._getter();
		this._elem.innerText = (this._value.value === null) ? "" : this._value.value;
	}

	/**
	 * Setzt den Zustand dieses Elements auf null und setzt den inneren Text des Input-Elements
	 * auf einen leeren Text
	 */
	public clear() : void {
		this._value.value = null;
		this._elem.innerText = "";
	}

	/**
	 * Schreibt die internen Daten dieses Inputs mithilfe des Setters.
	 */
	public commit() : void {
		this._setter(this._value.value);
	}

	/**
	 * Setzt den internen Zustand dieses Elements auf den übergebenen Wert und passt den inneren Text
	 * des Input-Elements entsprechend an.
	 *
	 * @param value   der zu setzende Wert
	 */
	public setValue(value : string) : void {
		this._value.value = value;
		this._elem.innerText = value;
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
		this.init();
	}

	/**
	 * Diese Methode reagiert auf Tastatur-Eingaben bei dem Input.
	 *
	 * @param event   das Tastaturereignis
	 *
	 * @returns true   es hat aufgrund des Tastaturereignisses eine Änderung am Zustand des Inputs stattgefunden
	 */
	public onKeyDown(event : KeyboardEvent) : boolean {
		if (event.key === "ArrowDown") {
			this.navigateDown();
			return false;
		}
		if (event.key === "ArrowUp") {
			this.navigateUp();
			return false;
		}
		if (event.key === "ArrowLeft") {
			this.navigateLeft();
			return false;
		}
		if (event.key === "ArrowRight") {
			this.navigateRight();
			return false;
		}
		return false;
	}

}
