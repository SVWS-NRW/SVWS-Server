import { isRef, type MaybeRef, type ShallowRef, shallowRef, toRaw, toValue, triggerRef, unref, watch } from "vue";

import { ArrayList } from "@core/java/util/ArrayList";
import type { Comparator } from "@core/java/util/Comparator";
import type { List } from "@core/java/util/List";

import type { SelectFilter } from "../filter/SelectFilter";

/** Konfigurationen für die Sortierung */
export type SelectSort<T> = Comparator<T> | ((a: T, b: T) => number) | 'sortierungProp' | 'idProp' | 'alphabetical' | 'unsorted' | undefined | null;

/**
 * Config des Managers.
 * Alle Werte können als Ref übergeben werden, wenn der Manager reaktiv auf Änderungen reagieren soll.
 */
export interface BaseSelectManagerConfig<T> {
	/** Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben. */
	options?: MaybeRef<Iterable<T>>;
	/** Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben. */
	sort?: MaybeRef<SelectSort<T>>;
	/** Um die Reaktivität zu gewährleisten, Refs nur ohne den Zusatz `.value` übergeben. */
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
	 * Die gefilterte und sortierte Liste der Optionen, die im Dropdown angezeigt werden.
	 */
	protected _filteredOptions = shallowRef<List<T>>(new ArrayList<T>());

	/**
	 * Die Konfiguration der Sortierung.
	 */
	protected _sort: SelectSort<T> = "sortierungProp";

	/**
	 * Der Comparator, der zur Sortierung verwendet wird.
	 */
	protected _comparator: Comparator<T> | null = null;

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
	 * Definiert, wie die Optionen im Dropdown dargestellt werden.
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
	protected constructor(config?: BaseSelectManagerConfig<T>) {
		this.initManager(config, true);
	}

	/**
	 * Ersetzt alle Konfigurationen durch die neuen. Es kann definiert werden, ob bei nicht gegebenen Konfigurationen die Defaultwerte gesetzt oder die
	 * alten Werte beibehalten werden sollen.
	 *
	 * @param config   		Die Config für den SelectManager
	 * @param setDefaults   wenn true werden die Defaultwerte für alle Konfigurationen gesetzt, die nicht übergeben wurden. Andernfalls werden die alten
	 * 					    beibehalten. Default ist false
	 */
	public setConfig(config?: BaseSelectManagerConfig<T>, setDefaults?: boolean): void {
		// Alte Watcher beenden
		for (const watcher of this._watcher) {
			watcher();
		}
		this._watcher = [];

		this.initManager(config, setDefaults);
	}

	/**
	 * Initialisierung bzw. Neukonfiguration des Selects.
	 *
	 * @param config   		die (neue) Konfiguration des Selects
	 * @param setDefaults   wenn true werden die Defaultwerte für alle Konfigurationen gesetzt, die nicht übergeben wurden. Andernfalls werden die alten
	 * 					    beibehalten. Default ist false
	 */
	private initManager(config: BaseSelectManagerConfig<T> | undefined, setDefaults: boolean = false): void {
		const newOptions = config?.options ?? (setDefaults ? new ArrayList<T>() : this.unfilteredOptions);
		this._unfilteredOptions = this.initShallowRef(newOptions, v => this.toList(v));

		const newSort = toRaw(unref(config?.sort)) ?? (setDefaults ? "sortierungProp" : this._sort);
		this.applySort(newSort);

		const newFilters = config?.filters ?? (setDefaults ? new ArrayList<SelectFilter<T>>() : this.filters);
		this._filters = this.initShallowRef(newFilters, v => this.toList(v));

		/*
		 * Die Watcher beobachten Änderungen der reaktiven Werte der Konfiguration, um diese intern weiterzuleiten.
		 */

		if (isRef(config?.options)) {
			this._watcher.push(
				watch(config.options, newOptions => {
					this.unfilteredOptions = this.getRawIterable(toValue(newOptions));
				}, { deep: true }
				)
			);
		}

		if (isRef(config?.sort)) {
			this._watcher.push(
				watch(config.sort, sort => {
					this.applySort(toRaw(sort));
					this.updateFilteredOptions();
				}, { deep: true })
			);
		}

		if (isRef(config?.filters)) {
			this._watcher.push(
				watch(config.filters, filters => {
					this.filters = filters;
				}, { deep: true })
			);
		}
	}

	/**
	 * Generiert ein Raw-Array von einem übergebenen Iterable. Löst Proxies innerhalb der Liste auf.
	 *
	 * @param list   das Iterable
	 *
	 * @return das Array/Liste ohne Proxy
	 */
	private getRawIterable(list: Iterable<T>): T[] {
		return [...toRaw(list)].map(e => toRaw(e));
	}

	/**
	 * Erzeugt ein ShallowRef aus dem übergebenen Objekt. Es berücksichtigt dabei, ob das Objekt bereits ein Ref ist und wandelt es ggf. um.
	 *
	 * @param source	  Die Quelle des ShallowRef. Das muss entweder bereits ein Ref sein oder ein Wert.
	 * @param transform   eine Funktion zur Transformation des Werts der Source. Dadurch kann der Wert von source noch mal vorher manipuliert werden.
	 * 					  Beispiel: `v => this.toList(v)` wandelt den Itereator von source vor der Speicherung als ShallowRef noch in eine Liste um
	 * @returns ein ShallowRef mit dem Wert der source
	 */
	protected initShallowRef<R, S>(source: MaybeRef<R>, transform: (v: R) => S): ShallowRef<S> {
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
	private set filteredOptions(value: Iterable<T>) {
		this._filteredOptions.value = this.toList(value);
		triggerRef(this._filteredOptions);
		this.updateSort();
	}

	/**
	 * Aktualisiert die gefilterten Optionen. Wird ein Filter mitgegeben, dann werden nur die Optionen dieses Filters aktualisiert, andernfalls alle Optionen.
	 *
	 * @param filter   der Filter, deren Optionen aktualisiert werden sollen. Wenn nicht angegeben, dann werden alle optionen aktualisiert.
	 * @param remove   wenn true, dann wird der Filter aus der Map entfernt. Andernfalls wird der Filter angewendet.
	 */
	public updateFilteredOptions(filter?: SelectFilter<T>, remove: boolean = false): void {
		this.updateFilterMap(filter, remove);

		if (this._filterMap.size === 0) {
			this.filteredOptions = [...this.unfilteredOptions];
			return;
		}

		let result = this._filterMap.values().toArray()[0];
		for (const filteredOptions of this._filterMap.values().filter((_, idx) => idx > 0)) {
			result = this.intersect(result, filteredOptions);
		}

		this.filteredOptions = result;
	}

	/**
	 * Aktualisiert die Filter-Map, die alle Filter und deren gefilterte Listen enthält.
	 *
	 * @param filter   ein Filter, der aktualisiert wird. Wird ein Filter angegeben, dann werden nur seine Optionen aktualisiert. Wird keiner angegeben, dann
	 * 				   wird die gesamte Map aktualisiert.
	 * @param remove   falls true, wird der Filter nur aus der Map gelöscht und es wird sonst nichts aktualisiert
	 */
	private updateFilterMap(filter: SelectFilter<T> | undefined, remove: boolean): void {
		if (filter === undefined) {
			this._filterMap.clear();
			for (const filter of this.filters) {
				this._filterMap.set(filter.key, filter.apply(this.unfilteredOptions));
			}

			return;
		}

		if (remove) {
			this._filterMap.delete(filter.key);
			return;
		}

		this._filterMap.set(filter.key, filter.apply(this.unfilteredOptions));
	}

	/**
	 * Generiert eine ArrayList aus einem Iterable
	 *
	 * @param iterable   das Itereable, das umgerechnet werden soll.
	 * @returns eine ArrayList, die die Elemente des Iterables enthält.
	 */
	public toList<U>(iterable: Iterable<U> | null | undefined): List<U> {
		const rawIterable = toRaw(iterable);
		if (rawIterable instanceof ArrayList) {
			return rawIterable;
		}
		const tmpList = new ArrayList<U>();
		if ((rawIterable === undefined) || (rawIterable === null)) {
			return tmpList;
		}
		for (const item of rawIterable) {
			tmpList.add(item);
		}
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
	public addFilter(newFilter: SelectFilter<T>): void {
		const existing = this.getFilterByKey(newFilter.key);
		if (existing !== null) {
			this._filters.value.remove(existing);
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
	public removeFilter(filter: SelectFilter<T> | string): void {
		const filterToRemove = (typeof filter === "string") ? this.getFilterByKey(filter) : this.getFilterByKey(filter.key);
		if (filterToRemove === null) {
			return;
		}

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
		for (const filter of this.filters) {
			if (filter.key === filterKey) {
				return filter;
			}
		}
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
		const copy = new ArrayList<T>(list1);
		copy.retainAll(list2);
		return copy;
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
		if (option === null) {
			return "";
		}
		if (typeof this.selectionDisplayText === "function") {
			return this.selectionDisplayText(option);
		}

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
		if (option === null) {
			return "";
		}
		if (typeof this.optionDisplayText === "function") {
			return this.optionDisplayText(option);
		}
		return "";
	}

	/**
	 * Comparator, der die Optionen nach optionDisplayText alphabetisch von A bis Z sortiert.
	 */
	private readonly alphabeticalComparator: Comparator<T> = {
		compare: (o1: T, o2: T): number => {
			const o1Text = this.getOptionText(o1);
			const o2Text = this.getOptionText(o2);
			return o1Text.localeCompare(o2Text, "de-DE");
		},
	};

	/**
	 * Gibt den Comparator für die Sortierung nach der Prop "id" oder "sortierung" zurück.
	 * Der Typ der Prop (number oder string) wird einmal über den ersten vorhandenen Wert (nicht null oder undefined) der ungefilterten Optionen bestimmt.
	 * Es wird angenommen, dass alle Optionen denselben Typ verwenden (abgesehen von null und undefined). Numbers werden numerisch, strings
	 * alphabetisch verglichen.
	 * Optionen ohne Wert (Prop fehlt, null oder undefined) stehen vorn und werden untereinander alphabetisch nach dem optionDisplayText sortiert.
	 * Hat keine Option einen Wert oder ist der erste vorhandene Wert weder number noch string, wird für alle Optionen alphabetisch sortiert.
	 *
	 * @param prop   der Name der Prop, nach der sortiert wird
	 *
	 * @returns der Comparator, passend zu den aktuellen Optionen
	 */
	private getPropComparator(prop: "id" | "sortierung"): Comparator<T> {
		switch (this.getPropType(prop)) {
			case "number":
				return this.createPropComparator(prop, (v1, v2) => Number(v1) - Number(v2));
			case "string":
				return this.createPropComparator(prop, (v1, v2) => String(v1).localeCompare(String(v2), "de-DE"));
			default:
				return this.alphabeticalComparator;
		}
	}

	/**
	 * Liest den Wert einer Prop aus einer Option.
	 *
	 * @param option   die Option
	 * @param prop     der Name der Prop
	 *
	 * @returns der Wert der Prop oder undefined, wenn die Option kein Objekt ist, die Prop nicht hat oder der Wert null ist
	 */
	private getPropValue(option: T, prop: "id" | "sortierung"): unknown {
		if ((option === null) || (typeof option !== "object")) {
			return undefined;
		}
		const value = (option as Record<string, unknown>)[prop];
		return (value === null) ? undefined : value;
	}

	/**
	 * Bestimmt den Datentyp einer Prop anhand des ersten vorhandenen Werts (nicht null oder undefined) in den ungefilterten Optionen.
	 *
	 * @param prop   der Name der Prop ("id" oder "sortierung")
	 *
	 * @returns der Datentyp des ersten vorhandenen Werts, wie ihn typeof liefert (z. B. "number" oder "string"), oder "undefined",
	 * 			wenn keine Option einen Wert hat
	 */
	private getPropType(prop: "id" | "sortierung"): string {
		for (const option of this.unfilteredOptions) {
			const value = this.getPropValue(option, prop);
			if (value !== undefined) {
				return typeof value;
			}
		}
		return "undefined";
	}

	/**
	 * Erzeugt einen Comparator, der nach dem Wert einer Prop sortiert. Optionen ohne Wert für diese Prop stehen vorn und werden untereinander alphabetisch
	 * sortiert.
	 *
	 * @param prop            der Name der Prop, nach der sortiert wird ("id" oder "sortierung")
	 * @param compareValues   der Vergleich zweier vorhandener Werte der Prop
	 *
	 * @returns der Comparator
	 */
	private createPropComparator(prop: "id" | "sortierung", compareValues: (v1: unknown, v2: unknown) => number): Comparator<T> {
		return {
			compare: (o1: T, o2: T): number => {
				const v1 = this.getPropValue(o1, prop);
				const v2 = this.getPropValue(o2, prop);
				if (v1 === undefined) {
					return (v2 === undefined) ? this.alphabeticalComparator.compare(o1, o2) : -1;
				}
				return (v2 === undefined) ? 1 : compareValues(v1, v2);
			},
		};
	}

	/**
	 * Gibt den Comparator zur konfigurierten Sortierung zurück.
	 * Per default wird nach der Prop "sortierung" sortiert. Mit "idProp" wird nach der Prop "id" sortiert. Haben die Optionen die jeweilige Prop nicht,
	 * wird alphabetisch nach dem optionDisplayText sortiert, ebenso bei der Konfiguration "alphabetical".
	 *
	 * @returns die Sortierung als Comparator oder null, falls keine Sortierung verwendet wird.
	 */
	public getComparator(): Comparator<T> | null {
		const sort = this._sort;
		if (typeof sort === "function") {
			return {
				compare: sort,
			};
		}

		switch (sort) {
			case "alphabetical":
				return this.alphabeticalComparator;
			case "idProp":
				return this.getPropComparator("id");
			case "unsorted":
				return null;
			case "sortierungProp":
			case null:
			case undefined:
				return this.getPropComparator("sortierung");
			default:
				return sort;
		}
	}

	/**
	 * Aktualisiert die Sortierung der Optionen, der gefilterten Liste und der Selektion, falls eine Sortierfunktion definiert ist.
	 */
	public updateSort(): void {
		if (this.filteredOptions.isEmpty()) {
			return;
		}
		const comparator = this.getComparator();
		this._comparator = comparator;
		if (comparator === null) {
			return;
		}
		this.filteredOptions.sort(comparator);

		triggerRef(this._filteredOptions);
	}

	private applySort(value: SelectSort<T>): void {
		this._sort = value;
		this._comparator = this.getComparator();

	}
}
