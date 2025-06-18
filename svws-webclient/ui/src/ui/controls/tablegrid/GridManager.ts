import { type ComponentPublicInstance } from "vue";
import type { GridInput } from "./GridInput";
import { GridInputAbiturNotenpunkte } from "./GridInputAbiturNotenpunkte";
import { GridInputAbiturPruefungsreihenfolge } from "./GridInputAbiturPruefungsreihenfolge";
import { GridInputToggle } from "./GridInputToggle";
import { DeveloperNotificationException } from "../../../../../core/src/core/exceptions/DeveloperNotificationException";

export class GridManager<KEY> {

	private mapInputs: Map<KEY, GridInput<KEY, any>> = new Map();

	private gridInputsCols = new Array<{ sorted: boolean, rows: Array<GridInput<KEY, any>> }>();
	private gridInputsRows = new Array<{ sorted: boolean, cols: Array<GridInput<KEY, any>> }>();

	private register<T extends GridInput<KEY, any>>(manager: T) : T {
		// Entferne ggf. einen Manager, der zuvor dem Schlüssel zugewiesen war
		if (this.mapInputs.get(manager.key) === undefined)
			this.unregister(manager.key);
		// Aktualisiere die Datenstruktur für die Spalten
		if (!(manager.col in this.gridInputsCols))
			this.gridInputsCols[manager.col] = { sorted: true, rows: new Array<GridInput<KEY, any>>() };
		this.gridInputsCols[manager.col].rows.push(manager);
		this.gridInputsCols[manager.col].sorted = false;
		// Aktualisiere die Datenstruktur für die Zeilen
		if (!(manager.row in this.gridInputsRows))
			this.gridInputsRows[manager.row] = { sorted: true, cols: new Array<GridInput<KEY, any>>() };
		this.gridInputsRows[manager.row].cols.push(manager);
		this.gridInputsRows[manager.row].sorted = false;
		// Aktualisiere die Map mit den Managern
		this.mapInputs.set(manager.key, manager);
		return manager;
	}

	private unregister(key: KEY) : null {
		// Prüfe, ob ein Manager mit dem Key überhaupt registiert ist...
		const manager = this.mapInputs.get(key);
		if (manager === undefined)
			return null;
		// Wenn ja, dann entferne ihn aus der Map mit den Managern,
		this.mapInputs.delete(key);
		// korrigiere die Datenstruktur für die Spalten und
		if (manager.col in this.gridInputsCols) {
			const index = this.gridInputsCols[manager.col].rows.indexOf(manager);
			if (index >= 0)
				this.gridInputsCols[manager.col].rows.splice(index, 1);
		}
		// korrigiere die Datenstruktur für die Zeilen
		if (manager.row in this.gridInputsRows) {
			const index = this.gridInputsRows[manager.row].cols.indexOf(manager);
			if (index >= 0)
				this.gridInputsRows[manager.row].cols.splice(index, 1);
		}
		return null;
	}

	private sortRows(col: number) {
		if (!(col in this.gridInputsCols))
			throw new DeveloperNotificationException("Die Spalte col " + col + " eines registrierten Managers konnte nicht gefunden werden.");
		if (!this.gridInputsCols[col].sorted) {
			this.gridInputsCols[col].rows.sort((a, b) => a.row - b.row);
			this.gridInputsCols[col].sorted = true;
		}
	}

	public focusPrevRowElement(input: GridInput<KEY, any>) {
		this.sortRows(input.col);
		const index = this.gridInputsCols[input.col].rows.indexOf(input);
		if (index < 1)
			return;
		this.gridInputsCols[input.col].rows[index - 1].element.focus();
	}

	public focusNextRowElement(input: GridInput<KEY, any>) {
		this.sortRows(input.col);
		const index = this.gridInputsCols[input.col].rows.indexOf(input);
		if ((index < 0) || (index >= this.gridInputsCols[input.col].rows.length - 1))
			return;
		this.gridInputsCols[input.col].rows[index + 1].element.focus();
	}

	private getTBody(input: GridInput<KEY, any>): HTMLElement {
		let cur : HTMLElement | null = input.element;
		while (cur.parentElement !== null) {
			cur = cur.parentElement;
			const computedStyle = window.getComputedStyle(cur);
			if ((cur.tagName === 'TBODY') && ((computedStyle.overflowY === 'auto') || (computedStyle.overflowY === 'scroll')))
				return cur;
		}
		throw new DeveloperNotificationException("Konnte den Body der Tabelle nicht bestimmen.");
	}

	private isOverflowTop(input: GridInput<KEY, any>, tbody: HTMLElement) : boolean {
		const scrollBound = tbody.getBoundingClientRect();
		const elemBound = input.element.getBoundingClientRect();
		return (elemBound.top < scrollBound.top);
	}

	private getPrevOverflowTopInput(input: GridInput<KEY, any>, tbody: HTMLElement) : GridInput<KEY, any> {
		let index = this.gridInputsCols[input.col].rows.indexOf(input);
		if (index > this.gridInputsCols[input.col].rows.length - 1)
			return this.gridInputsCols[input.col].rows[this.gridInputsCols[input.col].rows.length - 1] ?? null;
		let cur = input;
		while (!this.isOverflowTop(cur, tbody)) {
			if (index <= 0)
				return this.gridInputsCols[input.col].rows[0] ?? null;
			index--;
			cur = this.gridInputsCols[input.col].rows[index];
		}
		return cur;
	}

	public focusPrevRowElementPageUp(input: GridInput<KEY, any>) {
		const tbody = this.getTBody(input);
		this.sortRows(input.col);
		this.getPrevOverflowTopInput(input, tbody).element.focus();
	}

	private isOverflowBottom(input: GridInput<KEY, any>, tbody: HTMLElement) : boolean {
		const scrollBound = tbody.getBoundingClientRect();
		const elemBound = input.element.getBoundingClientRect();
		return (elemBound.bottom > scrollBound.bottom);
	}

	private getNextOverflowBottomInput(input: GridInput<KEY, any>, tbody: HTMLElement) : GridInput<KEY, any> {
		let index = this.gridInputsCols[input.col].rows.indexOf(input);
		if (index < 0)
			return this.gridInputsCols[input.col].rows[0];
		let cur = input;
		while (!this.isOverflowBottom(cur, tbody)) {
			if (index >= this.gridInputsCols[input.col].rows.length - 1)
				return this.gridInputsCols[input.col].rows[this.gridInputsCols[input.col].rows.length - 1];
			index++;
			cur = this.gridInputsCols[input.col].rows[index];
		}
		return cur;
	}

	public focusNextRowElementPageDown(input: GridInput<KEY, any>) {
		const tbody = this.getTBody(input);
		this.sortRows(input.col);
		this.getNextOverflowBottomInput(input, tbody).element.focus();
	}


	public focusTopElement(input: GridInput<KEY, any>) {
		this.sortRows(input.col);
		this.gridInputsCols[input.col].rows[0].element.focus();
	}


	public focusBottomElement(input: GridInput<KEY, any>) {
		this.sortRows(input.col);
		this.gridInputsCols[input.col].rows[this.gridInputsCols[input.col].rows.length - 1].element.focus();
	}

	private sortCols(row: number) {
		if (!(row in this.gridInputsRows))
			throw new DeveloperNotificationException("Die Spalte row " + row + " eines registrierten Managers konnte nicht gefunden werden.");
		if (!this.gridInputsRows[row].sorted) {
			this.gridInputsRows[row].cols.sort((a, b) => a.col - b.col);
			this.gridInputsRows[row].sorted = true;
		}
	}

	public focusPrevColElement(input: GridInput<KEY, any>) {
		this.sortCols(input.row);
		const index = this.gridInputsRows[input.row].cols.indexOf(input);
		if (index < 1)
			return;
		this.gridInputsRows[input.row].cols[index - 1].element.focus();
	}

	public focusNextColElement(input: GridInput<KEY, any>) {
		this.sortCols(input.row);
		const index = this.gridInputsRows[input.row].cols.indexOf(input);
		if ((index < 0) || (index >= this.gridInputsRows[input.row].cols.length - 1))
			return;
		this.gridInputsRows[input.row].cols[index + 1].element.focus();
	}

	public update(key: KEY, data: unknown) {
		const manager = this.mapInputs.get(key);
		if (manager === undefined)
			return;
		manager.update(data);
	}

	/**
	 * Fügt oder entfernt ein HTML-Element für den übergebenen Schlüssel hinzu
	 *
	 * @param key      der Schlüssel, welcher den Input-Manager identifiziert
	 * @param col      die Nummer der Spalte im Grid
	 * @param row      die Nummer der Zeile im Grid
	 * @param elem     das HTML-Element, welches zum Manager hinzugefügt werden soll, oder null, falls es entfernt werden soll
	 * @param setter   ein Setter für das Speichern der Daten des Input-Managers
	 *
	 * @returns das Input oder null
	 */
	public applyInputToggle(key: KEY, col: number, row: number, elem: Element | ComponentPublicInstance<unknown> | null,
		setter : (value: boolean) => void) : GridInputToggle<KEY> | null {
		// Wenn elem null ist, dann entferne das Element
		if (elem === null)
			return this.unregister(key);
		// Registriere das HTMLElement, sofern nicht bereits ein Grid-Input mit dem gleichen Schlüssel registriert ist
		if (!(elem instanceof HTMLElement))
			throw new DeveloperNotificationException("Der Grid-Input für einen Toggle erfordert ein HTMLElement");
		if (this.mapInputs.has(key))
			return null;
		return this.register(new GridInputToggle(this, key, col, row, elem, setter));
	}

	/**
	 * Fügt oder entfernt ein HTML-Element für den übergebenen Schlüssel hinzu
	 *
	 * @param key         der Schlüssel, welcher den Input-Manager identifiziert
	 * @param col         die Nummer der Spalte im Grid
	 * @param row         die Nummer der Zeile im Grid
	 * @param elem        das HTML-Element, welches zum Manager hinzugefügt werden soll, oder null, falls es entfernt werden soll
	 * @param setter      ein Setter für das Speichern der Daten des Input-Managers
	 * @param schuljahr   das Schuljahr, in dem das Abitur stattfindet
	 *
	 * @returns das Input oder null
	 */
	public applyInputAbiturNotenpunkte(key: KEY, col: number, row: number, elem: Element | ComponentPublicInstance<unknown> | null,
		setter : (value: string | null) => void, schuljahr: number) : GridInputAbiturNotenpunkte<KEY> | null {
		// Wenn elem null ist, dann entferne das Element
		if (elem === null)
			return this.unregister(key);
		// Registriere das HTMLElement, sofern nicht bereits ein Grid-Input mit dem gleichen Schlüssel registriert ist
		if (!(elem instanceof HTMLElement))
			throw new DeveloperNotificationException("Der Grid-Input für Abitur-Notenpunkte erfordert ein HTMLElement");
		if (this.mapInputs.has(key))
			return null;
		return this.register(new GridInputAbiturNotenpunkte(this, key, col, row, elem, setter, schuljahr));
	}

	/**
	 * Fügt oder entfernt ein HTML-Element für den übergebenen Schlüssel hinzu
	 *
	 * @param key      der Schlüssel, welcher den Input-Manager identifiziert
	 * @param col      die Nummer der Spalte im Grid
	 * @param row      die Nummer der Zeile im Grid
	 * @param elem     das HTML-Element, welches zum Manager hinzugefügt werden soll, oder null, falls es entfernt werden soll
	 * @param setter   ein Setter für das Speichern der Daten des Input-Managers
	 *
	 * @returns das Input oder null
	 */
	public applyInputAbiturPruefungsreihenfolge(key: KEY, col: number, row: number, elem: Element | ComponentPublicInstance<unknown> | null,
		setter : (value: number | null) => void) : GridInputAbiturPruefungsreihenfolge<KEY> | null {
		// Wenn elem null ist, dann entferne das Element
		if (elem === null)
			return this.unregister(key);
		// Registriere das HTMLElement, sofern nicht bereits ein Grid-Input mit dem gleichen Schlüssel registriert ist
		if (!(elem instanceof HTMLElement))
			throw new DeveloperNotificationException("Der Grid-Input für die Prüfungsreihenfolge der mündlichen Abiturprüfungen erfordert ein HTMLElement");
		if (this.mapInputs.has(key))
			return null;
		return this.register(new GridInputAbiturPruefungsreihenfolge(this, key, col, row, elem, setter));
	}

}
