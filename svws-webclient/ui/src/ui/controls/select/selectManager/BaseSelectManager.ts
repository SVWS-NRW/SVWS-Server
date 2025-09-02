import { ref, shallowRef, toRaw, triggerRef } from "vue";
import type { List } from "../../../../../../core/src/java/util/List";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import type { Comparator } from "../../../../../../core/src/java/util/Comparator";
import type { SelectFilter } from "../filter/SelectFilter";
import { SearchSelectFilter } from "../filter/SearchSelectFilter";

export interface BaseSelectManagerConfig<T> {
	options?: Iterable<T>;
	removable?: boolean;
	sort?: Comparator<T> | ((a: T, b: T) => number) | null;
	filters?: Iterable<SelectFilter<T>>;
	deepSearchAttributes?: string[];
}

/**
 * Abstrakte Manager Klasse zur Verwendung in einer Select-Komponente (UiSelect.vue). SelectManager übernehmen die Logik der Select-Komponente.
 * T bezeichnet dabei den Datentyp der auswählbaren Optionen.
 */
export abstract class BaseSelectManager<T> {

	// Alle Optionen des Selects. Diese Liste ist ungefiltert und enthält alle verfügbaren Optionen. Angezeigt werden im Dropdown aber nur die gefilterten
	// Optionen aus `_filteredOptions`.
	protected _allOptions = shallowRef<List<T>>(new ArrayList<T>());

	// Die gefilterte Liste der Optionen, die im Dropdwon angezeigt werden.
	protected _filteredOptions = shallowRef<List<T>>(new ArrayList<T>());

	// Alle Filter, die auf die Optionen angewendet werden
	protected _filters: List<SelectFilter<T>> = new ArrayList();

	// Eine Map, die alle validen Optionen zu einem Filter enthält.
	protected _filterMap = new Map<string, List<T>>;

	// Die aktuelle Selektion des Dropdowns. Enthält bei Single-Selektion immer nur ein Objekt
	protected abstract _selected: unknown;

	// Definiert, ob eine Multi-Selektion möglich ist
	protected abstract readonly _multi: boolean;

	// Definiert, ob die Selektion geleert werden kann
	protected _removable = ref<boolean>(true);

	// Definiert eine Sortierfunktion für die Optionen
	protected _sort: Comparator<T> | null = null;

	// Definiert, wie die aktuelle Selektion im Inputfeld dargestellt wird
	protected abstract _selectionDisplayText: unknown;

	// Definiert, wie die Optionen im Dropdown dargestellt werden
	protected abstract _optionDisplayText: unknown;

	/**
	 * Konstruktor des Managers.
	 *
	 * @param config      die Konfiguration des Selects. Wenn nicht angegeben, dann wird das Select ohne Optionen generiert.
	 */
	public constructor (config?: BaseSelectManagerConfig<T>) {
		if (config === undefined)
			return;
		// Alle Konfigurationen, die die angezeigten Optionen beeinflussen
		let optionsUpdated = false;
		if (config.options !== undefined)
			this._allOptions.value = this.getListFromIterable(config.options);
		if (config.filters !== undefined)
			this._filters = this.getListFromIterable(config.filters);
		if (config.deepSearchAttributes !== undefined) {
			this.setDeepSearchAttributes(config.deepSearchAttributes);
			optionsUpdated = true;
		}	if (optionsUpdated === false)
			// Sollte noch keine Aktualisierung stattgefunden haben, muss dies nun passieren, bevor eine Sortierung vorgenommen wird.
			this.updateFilteredOptions();

		// Weitere Konfigurationen
		if (config.removable !== undefined)
			this._removable.value = config.removable;
		if (config.sort !== undefined)
			this.sort = config.sort;
	}

	/**
	 * Setzt alle beliebigen Konfigurationsmerkmale und aktualisiert den Manager intern.
	 *
	 * @param config   Die Config für den SelectManager
	 */
	public setConfig(config: BaseSelectManagerConfig<T>) {
		// Alle Konfigurationen, die die angezeigten Optionen beeinflussen
		let optionsUpdated = false;
		if (config.options !== undefined)
			this._allOptions.value = this.getListFromIterable(config.options);
		if (config.filters !== undefined)
			this._filters = this.getListFromIterable(config.filters);
		if (config.deepSearchAttributes !== undefined) {
			this.setDeepSearchAttributes(config.deepSearchAttributes);
			optionsUpdated = true;
		}	if (optionsUpdated === false)
			// Sollte noch keine Aktualisierung stattgefunden haben, muss dies nun passieren, bevor eine Sortierung vorgenommen wird.
			this.updateFilteredOptions();

		// Weitere Konfigurationen
		if (config.removable !== undefined)
			this._removable.value = config.removable;
		if (config.sort !== undefined)
			this.sort = config.sort;
	}

	/**
	 * Getter für alle (ungefilterten) Optionen der Komponente.
	 *
	 * @return ungefilterte Liste
	 */
	public get options(): List<T> {
		return this._allOptions.value;
	}

	/**
	 * Setter für alle (ungefilterten) Optionen der Komponente. Der setter triggert auch eine Aktualisierung der gefilterten Optionen `_filteredOptions`.
	 *
	 * @param value   neue Optionenliste (ungefiltert)
	 */
	public set options(value: Iterable<T>) {
		const optionList = this.getListFromIterable(value);
		this._allOptions.value = optionList;
		triggerRef( this._allOptions );
		this.updateFilteredOptions();
	}

	/**
	 * Generiert eine ArrayList aus einem Iterable
	 *
	 * @param iterable   das Itereable, das umgerechnet werden soll.
	 * @returns eine ArrayList, die die Elemente des Iterables enthält.
	 */
	protected getListFromIterable<U>(iterable: Iterable<U>): ArrayList<U> {
		const tmpList = new ArrayList<U>()
		for (const item of iterable)
			tmpList.add(toRaw(item))
		return tmpList
	}

	/**
	 * Getter für alle selektierten Optionen der Komponente.
	 *
	 * @return Aktuelle Selektion. Bei Single-Selektion ein einzelnes Objekt, bei Multi-Selektion eine Liste.
	 */
	public abstract get selected(): unknown;

	/**
	 * Setter für alle selektierten Optionen der Komponente.
	 *
	 * @param value   neue Selektion. Bei Single-Selektion nur ein einzelnes Element, bei Muli-Selektion eine Liste.
	 *
	 * @throws DeveloperNotificationException   wenn nur Single-Selektion erlaubt ist, aber eine Liste übergeben wird.
	 */
	public abstract set selected(value: unknown);

	public abstract clearSelection(): void;

	public abstract hasSelection(): boolean;

	/**
	 * Getter für alle aktiven Filter der Optionen.
	 *
	 * @return Aktuell aktive Filter
	 */
	public get filters(): List<SelectFilter<T>> {
		return this._filters;
	}

	/**
	 * Setter für alle aktiven Filter der Optionen.
	 *
	 * @param value   neue aktive Filter
	 */
	public set filters(value: Iterable<SelectFilter<T>>) {
		this._filters = this.getListFromIterable(value);
		this.updateFilteredOptions();
	}

	/**
	 * Fügt einen Filter zu den aktiven Filtern hinzu. Der key des Filters muss eindeutig sein. Es dürfen keine zwei Filter mit demselben Key hinzugefügt werden.
	 * Die gefilterten Optionen des Dropdowns werden ebenfalls aktualisiert.
	 *
	 * @param newFilter   der neue, aktive Filter
	 */
	public addFilter(newFilter: SelectFilter<T>) {
		for (const filter of this.filters)
			if (filter.key === newFilter.key) {
				this.removeFilter(newFilter);
				break;
			}
		this.filters.add(newFilter);
		this.updateFilteredOptions(newFilter);
	}

	/**
	 * Entfernt einen aktiven Filter. Die gefilterte Liste wird sofort aktualisiert.
	 *
	 * @param removeFilter   Filter, der entfernt werden soll.
	 */
	public removeFilter(removeFilter: SelectFilter<T>) {
		const filter = this.getFilterByKey(removeFilter.key);
		if (filter === null)
			return;
		this.updateFilteredOptions(filter, true);
	}

	/**
	 * Sucht einen Filter in der aktiven Filterliste anhand seines Keys.
	 *
	 * @param filterKey   der Key des Filters, der gefunden werden soll
	 *
	 * @returns den passenden Filter. null, wenn keiner gefunden werden konnte.
	 */
	public getFilterByKey(filterKey: string): SelectFilter<T> | null {
		for (const filter of this.filters)
			if (filter.key === filterKey)
				return filter;
		return null;
	}

	/**
	 * Fügt Attribute zum SearchSelectFilter (key = "search") hinzu, die bei der Suche nach Suchbegriffen berücksichtigt werden sollen.
	 *
	 * @param attributes   Attribute des Objekts, in denen bei einer Begriffssuche ebenfalls gesucht werden soll.
	 */
	public setDeepSearchAttributes(attributes: string[]) {
		let searchFilter = this.getFilterByKey("search");
		if (searchFilter === null) {
			searchFilter = new SearchSelectFilter<T>("search", "", (option: T) => this.getOptionText(option), attributes);
			this.addFilter(searchFilter);
		} else
			(searchFilter as SearchSelectFilter<T>).deepSearchAttributes = attributes;
		this.updateFilteredOptions(searchFilter);
	}

	/**
	 * Ermittelt die Überschneidung von zwei Listen und gibt nur die Elemente zurück, die in beiden Listen enthalten sind.
	 *
	 * @param list1   die erste Liste
	 * @param list2   die zweite Liste
	 *
	 * @returns eine Liste mit Elementen, die in beiden Listen enthalten sind.
	 */
	private intersect(list1: List<T>, list2: List<T>): List<T> {
		const result = new ArrayList<T>();
		const set1 = new Set(list1);
		const set2 = new Set(list2);

		const intersection = set1.intersection(set2);
		intersection.forEach((value) => result.add(value));

		return result;
	}

	/**
	 * Getter für die gefilterte Liste an Optionen des Dropdowns.
	 *
	 * @return gefilterte Optionen
	 */
	public get filteredOptions(): List<T> {
		return this._filteredOptions.value;
	}

	/**
	 * Setter für die gefilterte Liste an Optionen des Dropdowns. Falls eine Sortierfunktion definiert ist, wird die gefilterte Liste auch sortiert.
	 *
	 * @param value   neue sortierte Liste der gefilterten Optionen
	 */
	public set filteredOptions (value: Iterable<T>) {
		const filteredList = this.getListFromIterable(value);
		if (this.sort !== null)
			filteredList.sort(this.sort);
		this._filteredOptions.value = filteredList;
		triggerRef(this._filteredOptions);
	}

	/**
	 * Synchronisiert die aktuelle Selektion mit der Liste der gefilterten Optionen. Das bedeutet, dass Selektionen, die nicht mehr in der filteredList vorkommen,
	 * entfernt werden. Das darf jedoch nicht bei der Filterung durch den SearchSelectFilter mit dem key "search" passieren.
	 */
	public abstract syncSelectionAndFilteredOptions(): void;

	/**
	 * Aktualisiert die Filter-Map, die alle aktiven Filter und deren gefilterte Listen enthält. Wenn kein Filter übergeben wird, dann wird die komplette Map
	 * aktualisiert. Nach dem Update der Filter-Map wird auch die Liste der gefilterten Optionen aktualisiert.
	 *
	 * @param filter   der Filter, deren Optionen aktualisiert werden sollen. Wenn nicht angegeben, dann wird die komplette Map aktualisiert.
	 * @param remove   wenn true, dann wird der Filter aus der Map entfernt. Andernfalls wird der Filter angewendet.
	 */
	public updateFilteredOptions (filter?: SelectFilter<T>, remove: boolean = false): void {
		// Aktualisiert die Filter-Map, die alle aktiven Filter und deren gefilterte Listen enthält.

		if (filter !== undefined)
			if (remove === true) {
				this.filters.remove(filter);
				this._filterMap.delete(filter.key);
			}	else
				this._filterMap.set(filter.key, filter.apply(this.options));
		else {
			this._filterMap.clear();
			for (const filter of this.filters) {
				const filteredList = filter.apply(this.options);
				this._filterMap.set(filter.key, filteredList);
			}
		}

		// Aktualisiert die gefilterte Liste, die im Dropdown angezeigt wird.

		let result: List<T> = new ArrayList();
		if (this._filterMap.size === 0)
			result = this.options;

		for (const filteredOptions of this._filterMap.values())
			if (result.isEmpty())
				result = filteredOptions;
			else
				result = this.intersect(result, filteredOptions);

		this.filteredOptions = result;
		// Beim Suchfilter darf die Selektion nicht zurückgesetzt werden, sonst geht diese bei jeder erneuten Suche verloren
		if ((filter !== undefined )&& (filter.key !== "search"))
			this.syncSelectionAndFilteredOptions();
	}

	/**
	 * Setter zum Festlegen, ob eine Selektion geleert werden kann.
	 *
	 * @param value   true, wenn leeren erlaubt ist
	 */
	public set removable(value: boolean) {
		this._removable.value = value;
	}

	/**
	 * Getter, der angibt, ob eine Selektion geleert werden kann.
	 *
	 * @return true, wenn wenn leeren erlaubt ist
	 */
	public get removable(): boolean {
		return this._removable.value;
	}

	/**
	 * Getter für die Konfiguration der Darstellung der Selektion.
	 *
	 * @return Darstellungskonfiguration für die Selektion
	 */
	public get selectionDisplayText(): unknown {
		return this._selectionDisplayText;
	}

	/**
	 * Setter für die Konfiguration der Darstellung der Selektion.
	 *
	 * @param value neue Darstellungskonfiguration für die Selektion.
	 */
	public set selectionDisplayText(value: unknown) {
		this._selectionDisplayText = value;
	}

	/**
	 * Getter für die Konfiguration der Darstellung der Optionen.
	 *
	 * @return Darstellungskonfiguration für Optionen
	 */
	public get optionDisplayText(): unknown {
		return this._optionDisplayText;
	}

	/**
	 * Setter für die Konfiguration der Darstellung der Optionen.
	 *
	 * @param value neue Darstellungskonfiguration für Optionen.
	 */
	public set optionDisplayText(value: unknown) {
		this._optionDisplayText = value;
	}

	/**
	 * Getter für den Sortier-Comparator der Optionen.
	 *
	 * @return Comparator für die Sortierung
	 */
	public get sort(): Comparator<T> | null {
		return this._sort;
	}

	/**
	 * Setter für den Sortier-Comparator der Optionen.
	 *
	 * @param value neuer Comparator für die Optionensortierung
	 */
	public set sort(value: Comparator<T> | ((a: T, b: T) => number) | null) {
		let comparator: Comparator<T> | null = null;

		if (value && typeof value === 'object' && typeof value.compare === 'function') {
			comparator = value;
		} else if (typeof value === 'function') {
			comparator = {
				compare: value,
			};
		}
		this._sort = comparator;
		this.updateSort();
	}

	/**
	 * Aktualisiert die Sortierung der Optionen, der gefilterten Liste und der Selektion, falls eine Sortierfunktion definiert ist.
	 */
	public updateSort() {
		if (this.options.size() > 0 && this.sort !== null) {
			this.options.sort(this.sort);
			this.filteredOptions.sort(this.sort);
		}
		triggerRef(this._allOptions);
		triggerRef(this._filteredOptions);
	}


	/**
	 * Toggelt den Select-Zustand der übergebenen Option.
	 *
	 * @param option   die Option, dessen Select-Zustand getoggelt werden soll
	 */
	public toggleSelection(option: T) {
		if (this.isSelected(option))
			this.deselect(option);
		else
			this.select(option);
	}

	/**
	 * Prüft, ob die übergebene Option bereits selektiert ist.
	 *
	 * @param option   die Option, die geprüft werden soll
	 * @returns true, wenn die Option selektiert ist
	 */
	public abstract isSelected(option: T): boolean;

	/**
	 * Selektiert die übergebene Option. Falls `_multi === false`, dann wird die aktuelle Selektion zunächst geleert. Andernfalls wird die neue Option der
	 * bestehenden Selektion angefügt. Falls `_removable === false`, dann wird die Selektion nicht geleert.
	 *
	 * @param option   die Option, die selektiert werden soll
	 */
	public abstract select(option: T): void;

	/**
	 * Deselektiert die übergebene Option.
	 *
	 * @param option   die Option, die deselektiert werden soll
	 */
	public abstract deselect(option: T): void;

	/**
	 * Getter für den Text eines selektierten Objektes.
	 *
	 * @param option   Option, dessen Text abgefragt wird
	 *
	 * @returns den Text der Option.
	 */
	public getSelectionText(option: T | null): string {
		if (option === null)
			return "";
		if (typeof this.selectionDisplayText === "function")
			return this.selectionDisplayText(option);

		return "";
	}

	/**
	 * Getter für den Text der übergebenen Option.
	 *
	 * @param option   Option, dessen Text abgefragt wird
	 *
	 * @returns den Text der Option.
	 */
	public getOptionText(option: T | null): string {
		if (option === null)
			return "";
		if (typeof this.optionDisplayText === "function")
			return this.optionDisplayText(option);
		return "";
	}

	public get multi(): boolean {
		return this._multi;
	}

}
