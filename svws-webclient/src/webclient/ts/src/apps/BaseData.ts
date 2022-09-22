import { reactive } from "vue";
import { List, Vector } from "@svws-nrw/svws-core-ts";

type ListElement<T> = T extends List<(infer ElementType)> ? ElementType : T;

export interface BaseDataState<T, U> {
	data: T | undefined;
	manager: U | undefined;
}

/**
 * Die Basisklasse für alle abgeleiteten Datenklassen.
 * Es gibt drei generische Parameter, zwei davon sind obligatorisch
 * @typeParam T - Das Datenobjekt, das hier verwaltet wird, z.B. SchuelerStammdaten
 * @typeParam ListItem - Das Listenelement, das eine vereinfachte Form des Datenobjekts präsentiert, z.B. SchuelerListeEintrag
 * @typeParam U - Ein Managementobjekt, das Möglichkeiten bietet, diese Daten zu verarbeiten, z.B. GostKursdatenManager
 */
export abstract class BaseData<T, ListItem, U = unknown> {
	protected _state: BaseDataState<T, U>;

	/**
	 * diese Variable enthält das ausgewählte Listenelement, das ein oder mehrere
	 * IDs enthält, die für das select notwendig sind.
	 *
	 */
	protected selected_list_item: ListItem | undefined;

	/** Constructor für das Datenobjekt
	 * @param {Boolean} isReactive Legt fest, ob `_state` reaktiv ist
	 */
	public constructor(isReactive = true) {
		this._state = isReactive
			? reactive({ data: undefined, manager: undefined })
			: { data: undefined, manager: undefined };
	}

	/** Getter für den Manager
	 * @returns {U|undefined} Das Datenobjekt
	 */
	public get manager(): U | undefined {
		if (!this._state.manager) return undefined;
		return this._state.manager;
	}

	/** Setter für dden Manager
	 * @param {U|undefined} data Der Manager
	 */
	public set manager(manager: U | undefined) {
		this._state.manager = manager;
	}

	/** Getter für die Daten
	 * @returns {T|undefined} Das Datenobjekt
	 */
	protected get _daten(): T | undefined {
		if (!this._state.data) return undefined;
		return this._state.data;
	}

	/** Setter für die Daten
	 * @param {T|undefined} data Das Datenobjekt
	 */
	protected set _daten(data: T | undefined) {
		this._state.data = data;
	}

	//** Ein Callback für Updates an den Daten */
	protected abstract on_update(daten: T|ListElement<T>|Partial<T|ListElement<T>>): void;

	/** Methode, die eine Auswahl aus der Liste initiiert
	 * @param {ListItem | undefined} selected_list_item das ausgewählte Element aus der Liste
	 * @returns {Promise<T | undefined>} Liefert das Datenobjekt zurück als Promise
	 */
	public async select(
		selected_list_item: ListItem | undefined = undefined
	): Promise<T | undefined> {
		this.selected_list_item = selected_list_item;
		return await this.on_select();
	}

	/**
	 * Diese Methode wird von den abgeleiteten Klassen definiert und ruft mit dem
	 * festzulegenden getter als Parameter die `_select`-Methode auf.
	 */
	public abstract on_select(): Promise<T | undefined>;

	/**
	 * Methode zur Standardselektierung von Getter-Daten
	 *
	 * @param {Function} getter Die Methode, die die Daten liefert, übergibt
	 * als Parameter ein Listenelement, was dem select aus der Liste entspricht
	 * @returns {Promise<T|undefined>} Die Daten als Promise
	 */
	protected async _select(
		getter: (eintrag: ListItem) => Promise<T>
	): Promise<T | undefined> {
		try {
			if (!this.selected_list_item) return;
			const result = await getter(this.selected_list_item);
			this._daten = result;
		} catch (error) {
			console.log(`Fehler: ${error}`);
			return this.unselect();
		}
		return this._daten;
	}

	/**
	 * Setzt die Daten auf die Default-Werte zurück.
	 *
	 * @returns {Promise<undefined>} Die Daten als Promise
	 */
	public async unselect(): Promise<undefined> {
		this._daten = undefined;
		return undefined;
	}

	/** Der Getter für die Daten des Datenobjekts
	 * @returns {T|undefined}
	 */
	public get daten(): T | undefined {
		return this._daten;
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {T|ListElement<T>} data Die Daten, die aktualisiert werden sollen
	 * @param {ListElement<T>|number} id1 entweder das zu patchende Element oder die id des Elements
	 * @param {number} id2 die zweite eventuell abhängige ID
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public abstract patch(
		data: T|ListElement<T>|Partial<T|ListElement<T>>,
		id1: ListElement<T>|number,
		id2?: number
	): Promise<boolean>;

	/**
	 * Methode zum Patch eine Elements innerhalb eine Datenarrays
	 *
	 * @param {T} data neuen Daten
	 * @param {() => Promise<void>} setter Die Methode, die die Daten liefert
	 * @param {number} id Die ID des zu patchenden Elements
	 * @returns {Promise<boolean>} Boolean
	 */
	protected async _patchElement(
		data: ListElement<T>|Partial<ListElement<T>>,
		setter: () => Promise<void>,
		id: number
	): Promise<boolean> {
		if (!(this._daten instanceof Vector) || id < 0) return false;
		const current_data = this._daten.get(id);
		return await this._patchCommon(data, current_data, setter);
	}

	/**
	 * Methode zum Patch eine Datenelements
	 *
	 * @param {T} data neuen Daten
	 * @param {() => Promise<void>} setter Die Methode, die die Daten liefert
	 * @returns {Promise<boolean>} Boolean
	 */
	protected async _patch(
		data: T|Partial<T>,
		setter: () => Promise<void>
	): Promise<boolean> {
		if (!this._daten) return false
		const current_data = this._daten;
		return await this._patchCommon(data, current_data, setter);
	}

	/**
	 * Methode zum Patch eine Datenelements
	 *
	 * @param {T} data neuen Daten
	 * @param {T} current_data neuen Daten
	 * @param {() => Promise<void>} setter Die Methode, die die Daten liefert
	 * @returns {Promise<boolean>} Boolean
	 */
	protected async _patchCommon(
		data: T|ListElement<T>|Partial<T|ListElement<T>>,
		current_data: T|ListElement<T>,
		setter: () => Promise<void>
	): Promise<boolean> {
		// keine Veränderungen
		if (current_data === data) return false;
		try {
			await setter();
			// Aktualisiere den Wert des Feldes und bearbeite ggf. Sonderfälle über den Callback
			const get_keys = Object.keys as <T>(o: T) => (keyof T)[];
			for (const key of get_keys(data)) {
				current_data[key] = data[key] as (T | ListElement<T>)[keyof T & keyof ListElement<T>]
			}
			this.on_update(data);
			return true;
		} catch (error) {
			console.log(`Fehler: ${error}`);
			return false;
		}
	}
}
