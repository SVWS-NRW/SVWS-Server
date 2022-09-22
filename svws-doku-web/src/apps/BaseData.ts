import { reactive } from "vue";

type BaseType<T> = T extends (infer ElementType)[] ? ElementType : T;

export interface BaseDataState {
	data: object | undefined;
	manager: object | undefined;
}

export abstract class BaseData<T extends object, ListItem, U extends object=object> {
	protected _state: BaseDataState;

	/**
	 * diese Variable enthält das ausgewählte Listenelement, das ein oder mehrere
	 * IDs enthält, die für das select notwendig sind.
	 * 
	 */
	protected selected_list_item: ListItem | undefined;

	public constructor(isReactive = true) {
		this._state = isReactive
			? reactive({ data: undefined, manager: undefined })
			: { data: undefined, manager: undefined };
	}

	public get manager(): U | undefined {
		if (!this._state.manager) return undefined;
		return this._state.manager as U;
	}

	public set manager(manager: U | undefined) {
		this._state.manager = manager;
	}

	protected get _daten(): T | undefined {
		if (!this._state.data) return undefined;
		return this._state.data as T;
	}

	protected set _daten(data: T | undefined) {
		this._state.data = data;
	}

	//** Ein Callback für Updates an den Daten */
	protected abstract on_update<T>(daten: Partial<T>): void;

	public async select(selected_list_item: ListItem | undefined = undefined) : Promise<T | undefined> {
  	this.selected_list_item = selected_list_item;
  	return this.on_select();
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
	protected async _select(getter: (eintrag : ListItem) => Promise<T>): Promise<T | undefined> {
		try {
			if (!this.selected_list_item) 
				return;
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
	 * @returns {Promise<T>} Die Daten als Promise
	 */
	public async unselect(): Promise<undefined> {
		this._daten = undefined;
		return undefined;
	}

	/** Der Getter für die Daten des Datenobjekts */
	public get daten(): T | undefined {
		return this._daten;
	}

	/**
	 * Aktualisiert die übergebenen Felder der Daten mit dem übergebenen Objekt.
	 * Ruft ggf. einen Callback bei Änderungen an den Daten auf, so dass eine
	 * Applikation auf eine tatsächliche Änderung auf diese reagieren kann (z.B.
	 * Aktualisierung von Auswahllisten zusätzlich zu den Daten, etc.).
	 *
	 * @param {Partial<DatenTyp>} data Die Daten, die aktualisiert werden sollen
	 * @returns {Promise<boolean>} True, wenn die Daten aktualisiert wurden, sonst false
	 */
	public abstract patch<T>(
		data: Partial<T>,
		id1: number,
		id2?: number
	): Promise<boolean>;
	/**
	 * Methode zur Standardselektierung von Setter-Daten
	 *
	 * @param {Function} setter Die Methode, die die Daten liefert
	 * @returns {Promise<boolean>} Die Daten als Promise
	 */
	protected async _patchElement(
		data: Partial<BaseType<T>>,
		setter: () => Promise<void>,
		id: number
	): Promise<boolean> {
		if (!(this._daten instanceof Array) || id < 0) return false;
		const current_data: BaseType<T> = this._daten[id];
		return await this._patchCommon(data, current_data, setter);
	}
	protected async _patch(
		data: Partial<BaseType<T>>,
		setter: () => Promise<void>
	): Promise<boolean> {
		const current_data = this._daten as BaseType<T>;
		return await this._patchCommon(data, current_data, setter);
	}
	protected async _patchCommon(
		data: Partial<BaseType<T>>,
		current_data: Partial<BaseType<T>>,
		setter: () => Promise<void>
	): Promise<boolean> {
		const keys = Object.keys(data) as (keyof BaseType<T>)[];
		// keine Veränderungen (min 1)
		if (!current_data || !keys.some(key => current_data[key] !== data[key]))
			return false;
		try {
			await setter();
			// Aktualisiere den Wert des Feldes und bearbeite ggf. Sonderfälle über den Callback
			keys.forEach(
				key =>
					(current_data[key] = data[
						key
					] as BaseType<T>[keyof BaseType<T>])
			);
			this.on_update(data);
			return true;
		} catch (error) {
			console.log(`Fehler: ${error}`);
			return false;
		}
	}
}
