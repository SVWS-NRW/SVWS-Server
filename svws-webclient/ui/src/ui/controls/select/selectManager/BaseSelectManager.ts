import { toValue, triggerRef, watch, type MaybeRef, type ShallowRef, isRef, shallowRef, toRaw } from "vue";
import type { List } from "../../../../../../core/src/java/util/List";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import type { Comparator } from "../../../../../../core/src/java/util/Comparator";
import type { SelectFilter } from "../filter/SelectFilter";

/**
 * Config des Managers.
 * Alle Werte können als Ref übergeben werden, wenn der Manager reaktiv auf Änderungen reagieren soll.
 */
export interface BaseSelectManagerConfig<T> {
	options?: MaybeRef<Iterable<T>>;
	sort?: MaybeRef<Comparator<T> | ((a: T, b: T) => number) | null>;
	filters?: MaybeRef<Iterable<SelectFilter<T>>>;
}

/**
 * Abstrakte Manager Klasse zur Verwendung in einer Select-Komponente (UiSelect.vue oder UiSelectMulti.vue). SelectManager übernehmen die Logik der
 * Select-Komponente.
 * T bezeichnet dabei den Datentyp der auswählbaren Optionen.
 */
export abstract class BaseSelectManager<T> {

	/**
	 * Alle Optionen des Selects als List<T>. Diese Liste ist ungefiltert und unsortiert und enthält alle verfügbaren Optionen. Entspricht nicht der Liste, die im
	 * Dropdown angezeigt wird
	 */
	protected _unfilteredOptions = shallowRef<List<T>>(new ArrayList<T>());

	/**
	 * Die gefilterte und sortierte Liste der Optionen, die im Dropdwon angezeigt werden.
	 */
	protected _filteredOptions = shallowRef<List<T>>(new ArrayList<T>());

	/**
	 * Die Sortierfunktion als Comparator oder null, falls keine Sortierung gewünscht ist.
	 */
	protected _sort = shallowRef<Comparator<T> | null>(null);

	/**
	 * Alle Filter des Selects als List
	 */
	protected _filters = shallowRef<List<SelectFilter<T>>>(new ArrayList<SelectFilter<T>>());


	/**
	 * Eine Map, die alle validen Optionen zu einem Filter enthält.
	 * Die Keys sind die Keys der Filter und die Listen sind die validen Optionen für den Filter.
	 */
	protected _filterMap = new Map<string, List<T>>;

	/**
	 * Definiert, wie die aktuelle Selektion im Inputfeld dargestellt wird.
	 * Der genaue Datentyp wird in den konkreten, abgeleiteten Klassen festgelegt.
	 */
	protected abstract _selectionDisplayText: ShallowRef<unknown>;

	/**
	 * Definiert, wie die Optionen im Dropdown dargestellt werden
	 * Der genaue Datentyp wird in den konkreten, abgeleiteten Klassen festgelegt.
	 */
	protected abstract _optionDisplayText: ShallowRef<unknown>;

	/**
	 * Ein Array, der alle Watcher enthält, die gerade verwendet werden. Die Speicherung der Watcher ist notwendig, um bei einer neuen Konfiguration
	 * die alten Watcher zu stoppen.
	 */
	protected _watcher: (() => void)[] = [];

	/**
	 * Konstruktor des Managers.
	 *
	 * @param config      die Konfiguration des Selects. Wenn nicht angegeben, dann wird das Select ohne Optionen generiert.
	 */
	protected constructor (config?: BaseSelectManagerConfig<T>) {
		this.initManager(config);
	}

	/**
	 * Ersetzt alle Konfigurationen durch die neuen. Es kann definiert werden, ob bei nicht gegebenen Konfigurationen die Defaultwerte gesetzt oder die
	 * alten Werte beibehalten werden sollen.
	 *
	 * @param config   		Die Config für den SelectManager
	 * @param setDefaults   wenn true werden die Defaultwerte für alle Konfigurationen gesetzt, die nicht übergeben wurden. Andernfalls werden die alten
	 * 					    beibehalten. Default ist false
	 */
	public setConfig(config?: BaseSelectManagerConfig<T>, setDefaults?: boolean) {
		// Alte Watcher beenden
		this._watcher.forEach(stop => stop());
		this._watcher = [];

		this.initManager(config, setDefaults);
		this.updateFilteredOptions();
	}

	/**
	 * Initialisierung bzw. Neukonfiguration des Selects.
	 *
	 * @param config   		die (neue) Konfiguration des Selects
	 * @param setDefaults   wenn true werden die Defaultwerte für alle Konfigurationen gesetzt, die nicht übergeben wurden. Andernfalls werden die alten
	 * 					    beibehalten. Default ist false
	 */
	protected initManager(config: BaseSelectManagerConfig<T> | undefined, setDefaults: boolean = false) {
		const newOptions = config?.options ?? (setDefaults ? new ArrayList<T>() : this.unfilteredOptions);
		this._unfilteredOptions = this.initShallowRef(newOptions, v => this.toList(v));

		const newSort = config?.sort ?? (setDefaults ? null : this.sort);
		this._sort = this.initShallowRef(newSort, v => this.toComparator(v));

		const newFilters = config?.filters ?? (setDefaults ? new ArrayList<SelectFilter<T>>() : this.filters);
		this._filters = this.initShallowRef(newFilters, v => this.toList(v));

		this.updateFilteredOptions();

		/*
		 * Die Watcher beobachten Änderungen der reaktiven Werte der Konfiguration, um diese intern weiterzuleiten.
		 */

		this._watcher.push(
			watch(() => config?.options, newOptions => {
				this.unfilteredOptions = toValue(newOptions) ?? new ArrayList();
			}, { deep: true }
			)
		);

		this._watcher.push(
			watch(() => config?.sort, sort => {
				this.sort = toValue(sort) ?? null;
			}, { deep: true }
			)
		);

		this._watcher.push(
			watch(() => config?.filters, filters => {
				this.filters = toValue(filters) ?? new ArrayList();
			}, { deep: true }
			)
		);
	}


	/**
	 * Erzeugt ein ShallowRef aus dem übergebenen Objekt. Es berücksichtigt dabei, ob das Objekt bereits ein Ref ist und wandelt es ggf. um.
	 *
	 * @param source	  Die Quelle des ShallowRef. Das muss entweder bereits ein Ref sein oder ein Wert.
	 * @param transform   eine Funktion zur Transformation des Werts der Source. Dadurch kann der Wert von source noch Mal vorher manipuliert werden.
	 * 					  Beispiel: `v => this.toList(v)` wandelt den Itereator von source vor der Speicherung als ShallowRef noch in eine Liste um
	 * @returns ein ShallowRef mit dem Wert der source
	 */
	protected initShallowRef<R, S>(source: MaybeRef<R>, transform: (v: R) => S ): ShallowRef<S> {
		return isRef(source) ? shallowRef(transform(toRaw(source.value))) : shallowRef(transform(toRaw(source)));
	}

	/**
	 * Getter für alle (ungefilterten) Optionen der Komponente.
	 *
	 * @return ungefilterte Liste
	 */
	public get unfilteredOptions(): List<T> {
		return this._unfilteredOptions.value;
	}

	/**
	 * Setter für alle (ungefilterten) Optionen der Komponente. Der setter triggert auch eine Aktualisierung der gefilterten Optionen `_filteredOptions`.
	 *
	 * @param value   neue Optionenliste (ungefiltert)
	 */
	public set unfilteredOptions(value: Iterable<T>) {
		this._unfilteredOptions.value = this.toList(value);
		triggerRef(this._unfilteredOptions);
		this.updateFilteredOptions();
	}

	/**
	 * Getter für die gefilterte und sortierte Liste an Optionen des Dropdowns.
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
		const filteredList = this.toList(value);
		if (this.sort !== null)
			filteredList.sort(this.sort);
		this._filteredOptions.value = filteredList;
		triggerRef(this._filteredOptions);
	}

	/**
	 * Aktualisiert die gefilterten Optionen. Wird ein Filter mitgegeben, dann werden nur die Optionen dieses Filters aktualisiert, andernfalls alle Optionen.
	 *
	 * @param filter   der Filter, deren Optionen aktualisiert werden sollen. Wenn nicht angegeben, dann werden alle optionen aktualisiert.
	 * @param remove   wenn true, dann wird der Filter aus der Map entfernt. Andernfalls wird der Filter angewendet.
	 */
	public updateFilteredOptions (filter?: SelectFilter<T>, remove: boolean = false): void {
		this.updateFilterMap(filter, remove);

		if (this._filterMap.size === 0) {
			this.filteredOptions = this.unfilteredOptions;
			return;
		}

		let result = this._filterMap.values().toArray()[0];
		for (const filteredOptions of this._filterMap.values().filter((_, idx) => idx > 0) )
			result = this.intersect(result, filteredOptions);

		this.filteredOptions = result;
	}

	/**
	 * Aktualisiert die Filter-Map, die alle Filter und deren gefilterte Listen enthält.
	 *
	 * @param filter   ein Filter, der aktualisiert wird. Wird ein Filter angegeben, dann werden nur seine Optionen aktualisiert. Wird keiner angegeben, dann
	 * 				   wird die gesamte Map aktualisiert.
	 * @param remove   falls true, wird der Filter nur aus der Map gelöscht und es wird sonst nichts aktualisiert
	 */
	private updateFilterMap(filter: SelectFilter<T> | undefined, remove: boolean) {
		if (filter !== undefined)
			if (remove) {
				this._filterMap.delete(filter.key);
			}
			else
				this._filterMap.set(filter.key, filter.apply(this.unfilteredOptions));
		else {
			this._filterMap.clear();
			for (const filter of this.filters) {
				const filteredList = filter.apply(this.unfilteredOptions);
				this._filterMap.set(filter.key, filteredList);
			}
		}
	}

	/**
	 * Generiert eine ArrayList aus einem Iterable
	 *
	 * @param iterable   das Itereable, das umgerechnet werden soll.
	 * @returns eine ArrayList, die die Elemente des Iterables enthält.
	 */
	public toList<U>(iterable: Iterable<U> | null | undefined): List<U> {
		const rawIterable = toRaw(iterable);
		if (rawIterable instanceof ArrayList)
			return rawIterable;
		const tmpList = new ArrayList<U>();
		if ((rawIterable === undefined) || (rawIterable === null))
			return tmpList;
		for (const item of rawIterable)
			tmpList.add(item);
		return tmpList;
	}

	/**
	 * Getter für alle Filter der Optionen.
	 *
	 * @return Aktuelle Filter
	 */
	protected get filters(): List<SelectFilter<T>> {
		return this._filters.value;
	}

	/**
	 * Setter für alle Filter der Optionen.
	 * Alle alten Filter werden vollständig ersetzt und die Optionenliste sofort aktualisiert.
	 *
	 * @param value   neue Filter
	 */
	protected set filters(value: Iterable<SelectFilter<T>>) {
		this._filters.value = this.toList(value);
		triggerRef(this._filters);
		this.updateFilteredOptions();
	}

	/**
	 * Fügt einen neuen Filter hinzu. Der key des Filters muss eindeutig sein. Wird ein Filter mit einem Key hinzugefügt, der bereits existiert, dann wird
	 * der alte Filter überschrieben.
	 * Die gefilterten Optionen des Dropdowns werden ebenfalls aktualisiert.
	 *
	 * @param newFilter   der neue Filter
	 */
	public addFilter(newFilter: SelectFilter<T>) {
		for (const filter of this.filters)
			if (filter.key === newFilter.key) {
				this.removeFilter(newFilter);
				break;
			}
		this._filters.value.add(newFilter);
		triggerRef(this._filters);
		this.updateFilteredOptions(newFilter);
	}

	/**
	 * Entfernt einen Filter. Die gefilterte Liste wird sofort aktualisiert.
	 *
	 * @param filter   Filter, der entfernt werden soll. Es kann der ganze Filter oder auch nur der key angegeben werden.
	 */
	public removeFilter(filter: SelectFilter<T> | string) {
		const filterToRemove = (typeof filter === "string") ? this.getFilterByKey(filter) : this.getFilterByKey(filter.key);
		if (filterToRemove === null)
			return;

		this._filters.value.remove(filterToRemove);
		triggerRef(this._filters);
		this.updateFilteredOptions(filterToRemove, true);
	}

	/**
	 * Sucht einen Filter in der Filterliste anhand seines Keys.
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
	 * Getter für die Konfiguration der Darstellung der Selektion.
	 *
	 * @return Darstellungskonfiguration für die Selektion
	 */
	public abstract get selectionDisplayText(): unknown;
	/**
	 * Setter für die Konfiguration der Darstellung der Selektion.
	 *
	 * @param value neue Darstellungskonfiguration für die Selektion.
	 */
	public abstract set selectionDisplayText(value: unknown);

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
	 * Getter für die Konfiguration der Darstellung der Optionen.
	 *
	 * @return Darstellungskonfiguration für Optionen
	 */
	public abstract get optionDisplayText(): unknown;

	/**
	 * Setter für die Konfiguration der Darstellung der Optionen.
	 *
	 * @param value neue Darstellungskonfiguration für Optionen.
	 */
	public abstract set optionDisplayText(value: unknown);

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

	/**
	 * Getter für den Sortier-Comparator der Optionen.
	 *
	 * @return Comparator für die Sortierung
	 */
	public get sort(): Comparator<T> | null {
		return this._sort.value;
	}

	/**
	 * Setter für den Sortier-Comparator der Optionen.
	 *
	 * @param value neuer Comparator für die Optionensortierung
	 */
	public set sort(value: Comparator<T> | ((a: T, b: T) => number) | null) {
		this._sort.value = this.toComparator(value);
		triggerRef(this._sort);
		this.updateSort();
	}

	/**
	 * Wandelt eine Sortierfunktion bzw. einen Comparator in ein Comparator-Objekt um.
	 *
	 * @param sort   die Sortierfunktion als Funktion, als Comparator oder null, falls keine Sortierung vorgenommen werden soll.
	 * @returns die Sortierung als Comparator oder null, falls keine Sortierung verwendet wird.
	 */
	public toComparator<T>(sort: Comparator<T> | ((a: T, b: T) => number) | null | undefined): Comparator<T> | null {
		if (!sort)
			return null;
		if (typeof sort === "function") {
			return {
				compare: sort,
			};
		}
		return sort
	}


	/**
	 * Aktualisiert die Sortierung der Optionen, der gefilterten Liste und der Selektion, falls eine Sortierfunktion definiert ist.
	 */
	public updateSort() {
		if (!this.filteredOptions.isEmpty() && (this.sort !== null))
			this.filteredOptions.sort(this.sort);

		triggerRef(this._filteredOptions);
	}

}
