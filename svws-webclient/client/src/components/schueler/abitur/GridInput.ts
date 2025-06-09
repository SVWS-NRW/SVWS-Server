import { shallowRef } from "vue";
import type { GridManager } from "./GridManager";

/**
 * Diese Klasse ist eine abtrakte Basisklasse für Inputs, welche zur Verwaltung
 * zu einem GridManager hinzugefügt werden können. Die Inputs werden
 * dabei mit dem Grid über eine Referent an den Html-Komponenten verknüpft,
 * indem bei dem Grid-Manager die applyInput-Methode aufgerufen wird.
 * Diese Methode erstellt dann auch die konkrete Instanz des abgeleiteten
 * Grid-Inputs.
 */
export abstract class GridInput<KEY, DATA> {

	// Der Grid-Manager, der dieses Grid-Input verwaltet
	protected _gridManager: GridManager<KEY>;

	// Der Schlüsselwert zur eindeutigen Identifikation der Zelle, in welchem sich das Grid-input befindet
	protected _key: KEY;

	// Die eindeutige Nummer der Grid-Spalte
	protected _col: number;

	// Die eindeutige Nummer der Grid-Zeile
	protected _row: number;

	// Das verknüpfte HTML-Element
	protected _elem: HTMLElement;

	// Gibt an, ob das Input-Element neu fokussiert wurde oder ggf. schon Anpassungen seitdem stattgefunden haben
	protected _isNewFocus = shallowRef<boolean>(true);


	/**
	 * Erzeugt ein neues Grid-Input
	 *
	 * @param gridManager   der Grid-Manager
	 * @param key           der eindeutige Schlüssel zur Identifikation des Input
	 * @param col           die Spalte, in welcher sich das Input befindet
	 * @param row           die Zeile, in welcher sich das Input befindet
	 * @param elem          das HTML-Element, welches dem Grid-Input und damit der Zelle des Grid zugeordnet ist
	 */
	constructor(gridManager: GridManager<KEY>, key: KEY, col: number, row: number, elem : HTMLElement) {
		this._gridManager = gridManager;
		this._key = key;
		this._col = col;
		this._row = row;
		this._elem = elem;
		// Setze den Tab-Index ggf. auf 0, sofern dieser bereits 0 oder nicht gesetzt ist
		if (elem.tabIndex !== 0)
			elem.setAttribute("tabindex", "0");
		elem.addEventListener("focus", () => this.handleFocus());
		elem.addEventListener("blur", () => this.handleBlur());
		elem.addEventListener("keydown", (event) => this.handleKeyDown(event));
		elem.addEventListener("click", (event: MouseEvent) => this.handleClick(event));
	}

	/**
	 * Gibt den Grid-Manager zurück, welcher dieses Grid-Input verwaltet
	 */
	public get gridManager() : GridManager<KEY> {
		return this._gridManager;
	}

	/**
	 * Gibt den eindeutigen Schlüssel zur Identifikation des Input zurück
	 */
	public get key() : KEY {
		return this._key;
	}

	/**
	 * Gibt die Spalte zurück, in welcher sich das Input befindet
	 */
	public get col() : number {
		return this._col;
	}

	/**
	 * Gibt die Zeile zurück, in welcher sich das Input befindet
	 */
	public get row() : number {
		return this._row;
	}

	/**
	 * Gibt das HTML-Element des Inputs zurück.
	 */
	public get element() : HTMLElement {
		return this._elem;
	}

	/**
	 * Aktualisiert die übergebenen Daten mithilfe des übergebenen Wertes
	 *
	 * @param value   die neuen Daten
	 */
	public update(value: DATA) {
		// override method in subclass if necessary
	}

	/**
	 * Interne Methode zur Handhabung, wenn das HTML-Element den Fokus erhält
	 */
	private handleFocus() : void {
		this._isNewFocus.value = true;
		this.onFocus();
	}

	/**
	 * Eine Methode, welche überschrieben werden kann, um auf das Erhalten des Fokus zu reagieren.
	 */
	public onFocus() : void {
		// override method in subclass if necessary
	}

	/**
	 * Interne Methode zur Handhabung, wenn das HTML-Element den Fokus verliert
	 */
	private handleBlur() : void {
		this.onBlur();
		this._isNewFocus.value = true;
	}

	/**
	 * Eine Methode, welche überschrieben werden kann, um auf das Verlieren des Fokus zu reagieren.
	 */
	public onBlur() : void {
		// override method in subclass if necessary
	}

	/**
	 * Interne Methode zur Handhabung, wenn das HTML-Element eine Tastatur-Eingabe bekommt.
	 *
	 * @param event   das Tastaturereignis
	 */
	private handleKeyDown(event : KeyboardEvent) : void {
		if (this.onKeyDown(event))
			this._isNewFocus.value = false;
	}

	/**
	 * Eine Methode, welche überschrieben werden kann, um darauf zu reagieren, wenn das
	 * HTML-Element eine Tastatur-Eingabe bekommt.
	 *
	 * @param event   das Tastaturereignis
	 *
	 * @returns true   es hat aufgrund des Tastaturereignisses eine Änderung am Zustand des Inputs stattgefunden
	 */
	public onKeyDown(event : KeyboardEvent) : boolean {
		// override method in subclass if necessary
		return false;
	}

	/**
	 * Interne Methode zur Handhabung, wenn das HTML-Element angeklickt wird.
	 *
	 * @param event   das Mausereignis
	 */
	private handleClick(event : MouseEvent) : void {
		if (this.onClick(event))
			this._isNewFocus.value = false;
	}

	/**
	 * Eine Methode, welche überschrieben werden kann, um darauf zu reagieren, wenn das
	 * HTML-Element eine Maus-Eingabe bekommt.
	 *
	 * @param event   das Mausereignis
	 *
	 * @returns true   es hat aufgrund des Klickereignisses eine Änderung am Zustand des Inputs stattgefunden
	 */
	public onClick(event : MouseEvent) : boolean {
		// override method in subclass if necessary
		return false;
	}

	/**
	 * Es soll das Grid-Input oberhalb von diesem Grid-Input fokussiert werden.
	 */
	public navigateUp() : void {
		this._gridManager.focusPrevRowElement(this);
	}

	/**
	 * Es soll das Grid-Input unterhalb von diesem Grid-Input fokussiert werden.
	 */
	public navigateDown() : void {
		this._gridManager.focusNextRowElement(this);
	}

	/**
	 * Es soll das Grid-Input links von diesem Grid-Input fokussiert werden.
	 */
	public navigateLeft() : void {
		this._gridManager.focusPrevColElement(this);
	}

	/**
	 * Es soll das Grid-Input rechts von diesem Grid-Input fokussiert werden.
	 */
	public navigateRight() : void {
		this._gridManager.focusNextColElement(this);
	}

}
