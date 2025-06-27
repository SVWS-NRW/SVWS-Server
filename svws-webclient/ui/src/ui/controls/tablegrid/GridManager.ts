import { shallowRef, type ComponentPublicInstance } from "vue";
import type { GridInput } from "./GridInput";
import { GridInputAbiturNotenpunkte } from "./GridInputAbiturNotenpunkte";
import { GridInputAbiturPruefungsreihenfolge } from "./GridInputAbiturPruefungsreihenfolge";
import { GridInputToggle } from "./GridInputToggle";
import { DeveloperNotificationException } from "../../../../../core/src/core/exceptions/DeveloperNotificationException";
import { GridInputNote } from "./GridInputNote";
import { GridInputIntegerDiv } from "./GridInputIntegerDiv";

export class GridManager<KEY> {

	/** Gibt an, ob die Informationen zum State aktuell sind oder nicht. */
	private _stateUpToDate: boolean = false;

	/** Eine Map mit der Zuordnung aller Inputs dieses Grids zu ihrem eindeutigen Schlüssel. */
	private mapInputs: Map<KEY, GridInput<KEY, any>> = new Map();

	/** Alle Inputs dieses Grid in Arrays, ggf. sortiert nach der Zeile. Diese Listen sind der jeweigen Spalte im äußeren Array zugeordnet. */
	private gridInputsCols = new Array<{ sorted: boolean, rows: Array<GridInput<KEY, any>> } | undefined>();
	/** Alle Inputs dieses Grid in Arrays, ggf. sortiert nach der Spalte. Diese Listen sind der jeweigen Zeile im äußeren Array zugeordnet. */
	private gridInputsRows = new Array<{ sorted: boolean, cols: Array<GridInput<KEY, any>> } | undefined>();

	/** Das aktuell fokussierte Input im Grid. */
	private _focusInput = shallowRef<GridInput<KEY, any> | null>(null);

	/** Der Schlüssel des zuletzt fokussierten Inputs im Grid. (Hier ist der Schlüssel gespeichert, da das Input evtl. nicht mehr im Grid vorhanden ist) */
	private _focusLastKey = shallowRef<KEY | null>(null);

	/** Der Spalte des zuletzt fokussierten Inputs im Grid. (berechnet und evtl. null, da das Input evtl. nicht mehr im Grid vorhanden ist) */
	private _focusLastColumn = shallowRef<number | null>(null);

	/** Der Spalte des zuletzt fokussierten Inputs im Grid. (berechnet und evtl. null, da das Input evtl. nicht mehr im Grid vorhanden ist) */
	private _focusLastRow = shallowRef<number | null>(null);

	/** Ein Handler, der jedes mal aufgerufen wird, wenn ein input ausgewählt wird */
	private _onFocusInputHandler : ((input: GridInput<any, any> | null) => void) | null = null;

	/**
	 * Gibt das Input für den übergebenen Key zurück.
	 *
	 * @param key   der Key
	 *
	 * @returns das INput für den Key oder null, falls der Key ungültig ist
	 */
	public getInputByKey(key: KEY) : GridInput<KEY, any> | null {
		return this.mapInputs.get(key) ?? null;
	}

	/**
	 * Fügt das Input mit seiner Position ein.
	 *
	 * @param input   das input, dessen Position hinzugefügt werden soll.
	 */
	private addInputPosition(input: GridInput<KEY, any>) {
		// Aktualisiere die Datenstruktur für die Spalten
		if (!(input.col in this.gridInputsCols))
			this.gridInputsCols[input.col] = { sorted: true, rows: new Array<GridInput<KEY, any>>() };
		this.gridInputsCols[input.col]!.rows.push(input);
		this.gridInputsCols[input.col]!.sorted = false;
		// Aktualisiere die Datenstruktur für die Zeilen
		if (!(input.row in this.gridInputsRows))
			this.gridInputsRows[input.row] = { sorted: true, cols: new Array<GridInput<KEY, any>>() };
		this.gridInputsRows[input.row]!.cols.push(input);
		this.gridInputsRows[input.row]!.sorted = false;
		// Der interne State des Grid-Manager wurde verändert und muss dann bei Bedarf aktualisiert werden...
		this._stateUpToDate = false;
	}

	/**
	 * Entfernt das Input aus seiner Position.
	 *
	 * @param input   das input, dessen Position entfernt werden soll.
	 */
	private removeInputPosition(input: GridInput<KEY, any>) {
		let changed = false;
		// korrigiere die Datenstruktur für die Spalten und
		if (input.col in this.gridInputsCols) {
			const index = this.gridInputsCols[input.col]!.rows.indexOf(input);
			if (index >= 0) {
				this.gridInputsCols[input.col]!.rows.splice(index, 1);
				this.gridInputsCols[input.col]!.sorted = false;
				changed = true;
			}
		}
		// korrigiere die Datenstruktur für die Zeilen
		if (input.row in this.gridInputsRows) {
			const index = this.gridInputsRows[input.row]!.cols.indexOf(input);
			if (index >= 0) {
				this.gridInputsRows[input.row]!.cols.splice(index, 1);
				this.gridInputsRows[input.row]!.sorted = false;
				changed = true;
			}
		}
		if (changed)
			this._stateUpToDate = false;
	}

	/**
	 * Registriert ein neuen Input im Grid.
	 *
	 * @param input   der Input-Manager
	 *
	 * @returns der Input-Manager
	 */
	private register<T extends GridInput<KEY, any>>(input: T) : T {
		this._stateUpToDate = false;
		// Entferne ggf. ein Input, der zuvor dem Schlüssel zugewiesen war
		if (this.mapInputs.get(input.key) !== undefined)
			this.unregister(input.key);
		// Setze die Position des Inputs
		this.addInputPosition(input);
		// Aktualisiere die Map mit den Inputs
		this.mapInputs.set(input.key, input);
		return input;
	}

	/**
	 * Aktualisiert die Registierung ggf. durch Anpassung der Position des Inputs im Grid.
	 *
	 * @param key   der Schlüssel zur eindeutigen Identifikation des Inputs
	 * @param col   die neue Spalte
	 * @param row   die neue Zeile
	 *
	 * @returns immer null
	 */
	private updateRegistration(key: KEY, col: number, row: number) : null {
		// Bestimme zunächst das Input, welches ggf. angepasst werden muss
		const input = this.mapInputs.get(key);
		if (input === undefined)
			return null;
		// Prüfe, ob die Position angepasst werden muss
		if ((col === input.col) && (row === input.row))
			return null;
		// Entferne das Input aus der alten Position
		this.removeInputPosition(input);
		// Setze die neue Position
		input.setCol(col);
		input.setRow(row);
		// Ggf. auch wenn dieses das zuletzt fokussierte Input ist...
		if (this._focusLastKey.value === input.key) {
			this._focusLastColumn.value = col;
			this._focusLastRow.value = row;
		}
		// Füge das Input in der neuen Position hinzu
		this.addInputPosition(input);
		return null;
	}

	/**
	 * Entfernt die Registrierung des Inputs mit dem angegebenen Schlüssel im Grid.
	 *
	 * @param key   der Key des Input-Managers
	 *
	 * @returns null
	 */
	private unregister(key: KEY) : null {
		// Prüfe, ob ein Input-Manager mit dem Key überhaupt registiert ist...
		const input = this.mapInputs.get(key);
		if (input === undefined)
			return null;
		this._stateUpToDate = false;
		// Wenn ja, dann entferne ihn aus der Map mit den Inputs,
		this.mapInputs.delete(key);
		// Und entferne ggf. die Information zum zuletzt fokussierten Elemen
		if (this._focusLastKey.value === key) {
			this._focusLastKey.value = null;
			this._focusLastColumn.value = null;
			this._focusLastRow.value = null;
		}
		// Entferne das Input aus seiner Position
		this.removeInputPosition(input);
		return null;
	}


	/**
	 * Aktualisiert den internen State, nachdem eine Registierung von Inputs stattgefunden hat oder
	 * diese angepasst wurden.
	 */
	private updateState() {
		// Prüfe, ob der State nicht bereits aktuell ist
		if (this._stateUpToDate)
			return;
		// Führt eine Aktualisierung der Sortierung in this.gridInputsRows und this.gridInputsCols aus.
		for (const row of this.gridInputsRows) {
			if (row === undefined)
				continue;
			if (!row.sorted) {
				row.cols.sort((a, b) => a.col - b.col);
				row.sorted = true;
			}
		}
		for (const col of this.gridInputsCols) {
			if (col === undefined)
				continue;
			if (!col.sorted) {
				col.rows.sort((a, b) => a.row - b.row);
				col.sorted = true;
			}
		}
		// Aktualisiert die Informationen zur letzen Fokussierung (Spalte und Zeile)
		const input = this.mapInputs.get(this._focusLastKey.value);
		if (input === undefined)
			this._focusLastKey.value = null;
		this._focusLastRow.value = (input === undefined) ? null : input.row;
		this._focusLastColumn.value = (input === undefined) ? null : input.col;
		// Und der State ist dann wieder aktuell
		this._stateUpToDate = true;
	}

	/**
	 * Fokussiert das in den Zeilen vorherige Input-Element ausgehend von dem übergebenen Input,
	 * sofern eines vorhanden ist. Ist keines vorhanden, so passiert nichts.
	 *
	 * @param input   das Input, von dem ausgegangen wird
	 */
	public focusPrevRowElement(input: GridInput<KEY, any>) : void {
		this.updateState();
		const index = this.gridInputsCols[input.col]!.rows.indexOf(input);
		if (index < 1)
			return;
		this.gridInputsCols[input.col]!.rows[index - 1].element.focus();
	}

	/**
	 * Fokussiert das in den Zeilen nächste Input-Element ausgehend von dem übergebenen Input,
	 * sofern eines vorhanden ist. Ist keines vorhanden, so passiert nichts.
	 *
	 * @param input   das Input, von dem ausgegangen wird
	 */
	public focusNextRowElement(input: GridInput<KEY, any>) : void {
		this.updateState();
		const index = this.gridInputsCols[input.col]!.rows.indexOf(input);
		if ((index < 0) || (index >= this.gridInputsCols[input.col]!.rows.length - 1))
			return;
		this.gridInputsCols[input.col]!.rows[index + 1].element.focus();
	}

	/**
	 * Hilfsmethode zum Bestimmen des tbody-HTMLElemnt ausgehend von dem übergebenen Input.
	 *
	 * @param input   das Grid-Input
	 *
	 * @returns das tbody-HTMLElement
	 */
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

	/**
	 * Hilfsmethode zum Bestimmen, ob das Input im Overflow-Bereich oben in Bezug auf das
	 * tbody-HTMLElement liegt oder nicht.
	 *
	 * @param input   das Grid-Input
	 * @param tbody   das tbody-HTMLElement
	 *
	 * @returns true, wenn das Input in dem Overflow-Bereich liegt, und ansonsten false
	 */
	private isOverflowTop(input: GridInput<KEY, any>, tbody: HTMLElement) : boolean {
		const scrollBound = tbody.getBoundingClientRect();
		const elemBound = input.element.getBoundingClientRect();
		return (elemBound.top < scrollBound.top);
	}

	/**
	 * Hilfsmethode zum Bestimmen, der vorigen Inputs, welches im Overflow-Bereich oben in
	 * Bezug auf das tbody-HTMLElement liegt.
	 *
	 * @param input   das Grid-Input
	 * @param tbody   das tbody-HTMLElement
	 *
	 * @returns das Input im Overflow-Bereich oder das erste Element. Der Spezialfall null kommt nicht vor.
	 */
	private getPrevOverflowTopInput(input: GridInput<KEY, any>, tbody: HTMLElement) : GridInput<KEY, any> | null {
		let index = this.gridInputsCols[input.col]!.rows.indexOf(input);
		if (index > this.gridInputsCols[input.col]!.rows.length - 1)
			return this.gridInputsCols[input.col]!.rows[this.gridInputsCols[input.col]!.rows.length - 1] ?? null;
		let cur = input;
		while (!this.isOverflowTop(cur, tbody)) {
			if (index <= 0)
				return this.gridInputsCols[input.col]!.rows[0] ?? null;
			index--;
			cur = this.gridInputsCols[input.col]!.rows[index];
		}
		return cur;
	}

	/**
	 * Fokussiert das Element eine Seite in der Darstellung oberhalb von dem übergebenen Input.
	 *
	 * @param input   das Grid-Input
	 */
	public focusPrevRowElementPageUp(input: GridInput<KEY, any>) : void {
		const tbody = this.getTBody(input);
		this.updateState();
		this.getPrevOverflowTopInput(input, tbody)?.element.focus();
	}

	/**
	 * Hilfsmethode zum Bestimmen, ob das Input im Overflow-Bereich unten in Bezug auf das
	 * tbody-HTMLElement liegt oder nicht.
	 *
	 * @param input   das Grid-Input
	 * @param tbody   das tbody-HTMLElement
	 *
	 * @returns true, wenn das Input in dem Overflow-Bereich liegt, und ansonsten false
	 */
	private isOverflowBottom(input: GridInput<KEY, any>, tbody: HTMLElement) : boolean {
		const scrollBound = tbody.getBoundingClientRect();
		const elemBound = input.element.getBoundingClientRect();
		return (elemBound.bottom > scrollBound.bottom);
	}

	/**
	 * Hilfsmethode zum Bestimmen, der nächsten Inputs, welches im Overflow-Bereich unten in
	 * Bezug auf das tbody-HTMLElement liegt.
	 *
	 * @param input   das Grid-Input
	 * @param tbody   das tbody-HTMLElement
	 *
	 * @returns das Input im Overflow-Bereich oder das letzte Element. Der Spezialfall null kommt nicht vor.
	 */
	private getNextOverflowBottomInput(input: GridInput<KEY, any>, tbody: HTMLElement) : GridInput<KEY, any> | null {
		let index = this.gridInputsCols[input.col]!.rows.indexOf(input);
		if (index < 0)
			return this.gridInputsCols[input.col]!.rows[0];
		let cur = input;
		while (!this.isOverflowBottom(cur, tbody)) {
			if (index >= this.gridInputsCols[input.col]!.rows.length - 1)
				return this.gridInputsCols[input.col]!.rows[this.gridInputsCols[input.col]!.rows.length - 1];
			index++;
			cur = this.gridInputsCols[input.col]!.rows[index];
		}
		return cur;
	}

	/**
	 * Fokussiert das Element eine Seite in der Darstellung unterhalb von dem übergebenen Input.
	 *
	 * @param input   das Grid-Input
	 */
	public focusNextRowElementPageDown(input: GridInput<KEY, any>) : void {
		const tbody = this.getTBody(input);
		this.updateState();
		this.getNextOverflowBottomInput(input, tbody)?.element.focus();
	}


	/**
	 * Fokussiert das oberste Element in der Spalte über dem übergebenen Input.
	 *
	 * @param input   das Grid-Input
	 */
	public focusTopElement(input: GridInput<KEY, any>) : void {
		this.updateState();
		this.gridInputsCols[input.col]!.rows[0].element.focus();
	}


	/**
	 * Fokussiert das unterste Element in der Spalte unter dem übergebenen Input.
	 *
	 * @param input   das Grid-Input
	 */
	public focusBottomElement(input: GridInput<KEY, any>) : void {
		this.updateState();
		this.gridInputsCols[input.col]!.rows[this.gridInputsCols[input.col]!.rows.length - 1].element.focus();
	}

	/**
	 * Fokussiert das in den Spalten vorige Input-Element ausgehend von dem übergebenen Input,
	 * sofern eines vorhanden ist. Ist keines vorhanden, so passiert nichts.
	 *
	 * @param input   das Input, von dem ausgegangen wird
	 */
	public focusPrevColElement(input: GridInput<KEY, any>) : void {
		this.updateState();
		const index = this.gridInputsRows[input.row]!.cols.indexOf(input);
		if (index < 1)
			return;
		this.gridInputsRows[input.row]!.cols[index - 1].element.focus();
	}

	/**
	 * Fokussiert das in den Spalten nächste Input-Element ausgehend von dem übergebenen Input,
	 * sofern eines vorhanden ist. Ist keines vorhanden, so passiert nichts.
	 *
	 * @param input   das Input, von dem ausgegangen wird
	 */
	public focusNextColElement(input: GridInput<KEY, any>) : void {
		this.updateState();
		const index = this.gridInputsRows[input.row]!.cols.indexOf(input);
		if ((index < 0) || (index >= this.gridInputsRows[input.row]!.cols.length - 1))
			return;
		this.gridInputsRows[input.row]!.cols[index + 1].element.focus();
	}

	/**
	 * Aktualisiert die Daten des Inputs mit dem übergeben Key.
	 *
	 * @param key    der key des Grid-Inputs
	 * @param data   die Daten, mit denen das Grid-Input aktualisiert wird
	 */
	public update(key: KEY, data: unknown) : void {
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
			return this.updateRegistration(key, col, row);
		return this.register(new GridInputToggle(this, key, col, row, elem, setter));
	}


	/**
	 * Fügt oder entfernt ein HTML-Element für den übergebenen Schlüssel hinzu
	 *
	 * @param key         der Schlüssel, welcher den Input-Manager identifiziert
	 * @param col         die Nummer der Spalte im Grid
	 * @param row         die Nummer der Zeile im Grid
	 * @param elem        das HTML-Element, welches zum Manager hinzugefügt werden soll, oder null, falls es entfernt werden soll
	 * @param max         die maximale Zahl, die erlaubt ist
	 * @param setter      ein Setter für das Speichern der Daten des Input-Managers
	 *
	 * @returns das Input oder null
	 */
	public applyInputIntegerDiv(key: KEY, col: number, row: number, elem: Element | ComponentPublicInstance<unknown> | null, max: number | null,
		setter : (value: number | null) => void) : GridInputIntegerDiv<KEY> | null {
		// Wenn elem null ist, dann entferne das Element
		if (elem === null)
			return this.unregister(key);
		// Registriere das HTMLElement, sofern nicht bereits ein Grid-Input mit dem gleichen Schlüssel registriert ist
		if (!(elem instanceof HTMLElement))
			throw new DeveloperNotificationException("Der Grid-Input für Abitur-Notenpunkte erfordert ein HTMLElement");
		if (this.mapInputs.has(key))
			return this.updateRegistration(key, col, row);
		return this.register(new GridInputIntegerDiv(this, key, col, row, elem, max, setter));
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
	public applyInputNote(key: KEY, col: number, row: number, elem: Element | ComponentPublicInstance<unknown> | null,
		setter : (value: string | null) => void, schuljahr: number) : GridInputNote<KEY> | null {
		// Wenn elem null ist, dann entferne das Element
		if (elem === null)
			return this.unregister(key);
		// Registriere das HTMLElement, sofern nicht bereits ein Grid-Input mit dem gleichen Schlüssel registriert ist
		if (!(elem instanceof HTMLElement))
			throw new DeveloperNotificationException("Der Grid-Input für Noten erfordert ein HTMLElement");
		if (this.mapInputs.has(key))
			return this.updateRegistration(key, col, row);
		return this.register(new GridInputNote(this, key, col, row, elem, setter, schuljahr));
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
			return this.updateRegistration(key, col, row);
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
			return this.updateRegistration(key, col, row);
		return this.register(new GridInputAbiturPruefungsreihenfolge(this, key, col, row, elem, setter));
	}


	/**
	 * Setzt einen Handler, der jedes mal aufegrufen wird, wenn ein Input den Fokus bekommt oder verliert.
	 */
	public set onFocusInput(handler: ((input: GridInput<any, any> | null) => void) | null) {
		this._onFocusInputHandler = handler;
	}


	/**
	 * Setzt die Information zum aktuell fokussierten Input-Element des Grids,
	 * führt aber keine Fokussierung durch.
	 *
	 * @param input   das aktuell fokussierte Input-Element oder null
	 */
	public set focusInput(input: GridInput<KEY, any> | null) {
		if (input !== null) {
			this._focusLastKey.value = input.key;
			this._stateUpToDate = false;
		}
		this._focusInput.value = input;
		if (this._onFocusInputHandler !== null)
			this._onFocusInputHandler(input);
	}


	/**
	 * Gibt das aktuell fokussierte Input-Element des Grids zurück, sofern eines
	 * fokussiert ist.
	 *
	 * @returns das aktuell fokussierte Input-Element oder null
	 */
	public get focus(): GridInput<KEY, any> | null {
		return this._focusInput.value;
	}

	/**
	 * Gibt die Spaltennummer des aktuell fokussierten Input-Elements des Grids zurück, sofern eines
	 * fokussiert ist.
	 *
	 * @returns die Spaltennummer oder null
	 */
	public get focusColumn(): number | null {
		return (this._focusInput.value === null) ? null : this._focusInput.value.col;
	}

	/**
	 * Gibt die Zeilennummer des aktuell fokussierten Input-Elements des Grids zurück, sofern eines
	 * fokussiert ist.
	 *
	 * @returns die Zeilennummer oder null
	 */
	public get focusRow(): number | null {
		return (this._focusInput.value === null) ? null : this._focusInput.value.row;
	}

	/**
	 * Gibt die Spaltennummer des zuletzt fokussierten Input-Elements des Grids zurück.
	 *
	 * @returns die Spaltennummer oder null
	 */
	public get focusColumnLast(): number | null {
		this.updateState();
		return this._focusLastColumn.value;
	}

	/**
	 * Gibt die Zeilennummer des zuletzt fokussierten Input-Elements des Grids zurück.
	 *
	 * @returns die Zeilennummer oder null
	 */
	public get focusRowLast(): number | null {
		this.updateState();
		const lastKey = this._focusLastKey.value;
		if (lastKey === null)
			return null;
		const input = this.mapInputs.get(lastKey);
		if (input === undefined)
			return null;
		return this._focusLastRow.value;
	}

	/**
	 * Fokussiert das Grid, s.h. ein Input im Grid. Ist restore auf false gesetzt,
	 * so wird einfach das erste Input im Grid fokussiert. Ist restore auf true
	 * gesetzt, so wird vorher versucht die Position der letzten Fokussierung
	 * wiederherzustellen.
	 *
	 * @param restore   gibt an, ob die letzte Fokussierung wiederhergestellt werden soll.
	 */
	public doFocus(restore: boolean) : void {
		// Stelle ggf. die Fokussierung wieder her
		if (restore && (this._focusLastKey.value !== null)) {
			const input = this.mapInputs.get(this._focusLastKey.value);
			if (input !== undefined) {
				input.element.focus();
				return;
			}
		}
		// Bestimme das erste Input in der obersten Zeile mit Inputs und fokussiere dieses
		let row = Number.POSITIVE_INFINITY;
		let col = Number.POSITIVE_INFINITY;
		let found: GridInput<KEY, any> | null = null;
		for (const input of this.mapInputs.values()) {
			if ((input.row < row) || ((input.row === row) && (input.col < col))) {
				row = input.row;
				col = input.col;
				found = input;
			}
		}
		found?.element.focus();
	}

	/**
	 * Fokussiert ein Input in der Zeile, sofern zuvor kein Input fokussiert ist.
	 * War zuvor schonmal ein Input im Grid fokussiert, so wird versucht die Spalte dieses Inputs zu verwenden.
	 * Ist dies nicht der Fall, so wird das erste Input in der Zeile markiert, sofern ein Input in der Zeile
	 * vorhanden ist.
	 * Ist auch dies nicht der Fall, so passiert nichts.
	 *
	 * @param row   die Zeile
	 */
	public doFocusRowIfNotFocussed(row: number) : void {
		// Prüfe, ob auf eine Zelle mit Input geklickt wurde, dann wurde diese zuvor automatisch fokussiert
		if (this.focus !== null)
			return;
		// Prüfe, on in der Zeile überhaupt Inputs vorhanden sind, wenn nicht, dann kann auch nichts fokussiert werden
		if (!(row in this.gridInputsRows))
			return;
		// Bestimme die Inputs in der Zeile
		this.updateState();
		const inputs = this.gridInputsRows[row]!.cols;
		if (inputs.length === 0)
			return;
		// Wenn zuvor keine Spalte fokussiert war, dann fokussiere einfach das erste Input in der Zeile
		if (this.focusColumnLast === null) {
			inputs[0].element.focus();
			return;
		}
		// Ansonsten suche das Input mit der Spaltennummer des zuletzt fokussierten Inputs...
		let input = inputs[0];
		for (let i = 1; (i < inputs.length) && (input.col !== this.focusColumnLast); i++) {
			if (input.col > this.focusColumnLast)
				break;
			input = inputs[i];
		}
		input.element.focus();
	}

	/**
	 * Setzt, on bei dem Grid-Input mit dem übergeben Key eine Navigation in Anschluss auf das Drücken
	 * der ENTER-Taste erfolgen soll und wenn ja, in welche Richtung.
	 *
	 * @param key               der Key des Grid-Input
	 * @param navigateOnEnter   null, wenn keine Navigation stattfinden soll und 'DOWN' für unten und 'RIGHT' für rechts
	 */
	public setNavigationOnEnter(key: KEY, navigateOnEnter : null | 'DOWN' | 'RIGHT') : void {
		const input = this.mapInputs.get(key);
		if (input === undefined)
			return;
		input.navigateOnEnter = navigateOnEnter;
	}

}
