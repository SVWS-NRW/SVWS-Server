import { shallowRef } from "vue";
import type { GridManager } from "./GridManager";
import { GridInput } from "./GridInput";

/**
 * Diese Klasse ist eine Basisklasse für Grid-Inputs, welche einfache boolean-Toggles
 * darstellen und wo die Darstellung im inneren des HTML-Elements gehandhabt wird,
 * aber nicht von dieser Klasse.
 */
export class GridInputToggle<KEY> extends GridInput<KEY, boolean> {

	// Der zwischengespeicherte Wert des Input-Elements
	protected _value = shallowRef<boolean>(false);

	// Der Setter zum Setzen des Zellwertes
	protected _setter: (value: boolean) => void;

	/**
	 * Erzeugt ein neues Grid-Input für ein HTMLElement, welches die Tastatur-Steuerung für diesen Toggle übernimmt.
	 *
	 * @param gridManager   der Grid-Manager
	 * @param key           der eindeutige Schlüssel zur Identifikation des Input
	 * @param col           die Spalte, in welcher sich das Input befindet
	 * @param row           die Zeile, in welcher sich das Input befindet
	 * @param elem          das HTML-Element, welches dem Grid-Input und damit der Zelle des Grid zugeordnet ist
	 * @param setter        der Setter zum Schreiben der Daten des Grid-Input
	 */
	constructor(gridManager: GridManager<KEY>, key: KEY, col: number, row: number, elem : HTMLElement, setter : (value: boolean) => void) {
		super(gridManager, key, col, row, elem);
		this._setter = setter;
	}

	/**
	 * Gibt den aktuellen internen Wert des Inputs zurück
	 */
	public get value() : boolean {
		return this._value.value;
	}

	/**
	 * Initialisiert den internen Zustand dieses Elements mithilfe des Getters
	 */
	public update(value: boolean) : void {
		this._value.value = value;
	}

	/**
	 * Setzt den Zustand dieses Elements auf false
	 */
	public clear() : void {
		this._value.value = false;
		this.commit();
	}

	/**
	 * Schreibt die internen Daten dieses Inputs mithilfe des Setters.
	 */
	public commit() : void {
		this._setter(this._value.value);
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
			event.preventDefault();
			return false;
		}
		if (event.key === "ArrowUp") {
			this.navigateUp();
			event.preventDefault();
			return false;
		}
		if (event.key === "ArrowLeft") {
			this.navigateLeft();
			event.preventDefault();
			return false;
		}
		if (event.key === "ArrowRight") {
			this.navigateRight();
			event.preventDefault();
			return false;
		}
		if ((event.key === "Delete") || (event.key === "Backspace")) {
			this.clear();
			return true;
		}
		if (event.key === " ") {
			this._value.value = !this._value.value;
			this.commit();
			return true;
		}
		if (event.key === "Enter") {
			this._value.value = !this._value.value;
			this.commit();
			this.navigateDown();
			return true;
		}
		return false;
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
		this._value.value = !this._value.value;
		this.commit();
		return true;
	}

}
