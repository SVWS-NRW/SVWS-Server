import { ref, shallowRef, triggerRef } from "vue";
import type { List } from "../../../../../../core/src/java/util/List";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import { DeveloperNotificationException } from "../../../../../../core/src";
import type { SelectFilter } from "../filter/SelectFilter";

/**
 * Abstrakte Manager Klasse zur Verwendung in einer Select-Komponente (UiSelect.vue). SelectManager übernehmen die Logik der Select-Komponente.
 * T bezeichnet dabei den Datentyp der auswählbaren Optionen.
 */
export abstract class BaseSelectManager<T> {

	// Alle Optionen des Dropdowns
	protected _options = shallowRef<List<T>>(new ArrayList<T>());

	// Die aktuelle Selektion des Dropdowns. Enthält bei Single-Selektion immer nur ein Objekt
	protected _selected = shallowRef<List<T>>(new ArrayList<T>());

	// Die gefilterte Liste der Optionen, die im Dropdwon angezeigt werden.
	protected _filtered = shallowRef<List<T>>(new ArrayList<T>());

	// Eine Map, die alle validen Optionen zu einem Filter enthält.
	protected _filterMap = new Map<string, List<T>>;

	// Alle Filter, die auf die Optionen angewendet werden
	private _filters: List<SelectFilter<T>> = new ArrayList();

	// Definiert, ob eine Multi-Selektion möglich ist
	protected _multi = ref<boolean>(false);

	// Definiert, wie die aktuelle Selektion im Inputfeld dargestellt wird
	protected abstract _selectionDisplayText: string | ((option: any) => string);

	// Definiert, wie die Optionen im Dropdown dargestellt werden
	protected abstract _optionDisplayText: string | ((option: any) => string);

	/**
	 * Konstruktor des Managers.
	 *
	 * @param multi      definiert, ob Multi-Selektionen erlaubt sind
	 * @param options    die Liste aller Optionen der Komponente (ungefiltert)
	 * @param selected   optional. Die Liste der aktuell selektierten Optionen. Bei einer Singe-Select-Komponente darf maximal ein Objekt in dieser Liste sein.
	 */
	public constructor(multi: boolean, options: Iterable<T>, selected?: Iterable<T>) {
		this.multi = multi;

		const optionsTmp = new ArrayList<T>();
		for (const i of options)
			optionsTmp.add(i);
		this.options = optionsTmp;

		const selectedTmp = new ArrayList<T>();
		if (selected !== undefined) {
			for (const i of selected)
				selectedTmp.add(i);
			if (!multi && (selectedTmp.size() > 1))
				throw new DeveloperNotificationException("In einer Single-Select-Komponente können nicht mehrere Optionen selektiert sein. "
					+ "Dem Konstruktor wurden jedoch mehrere übergeben.");
			this.selected = selectedTmp;
		}
	}

	/**
	 * Getter für alle (ungefilterten) Optionen der Komponente.
	 *
	 * @return ungefilterte Liste
	 */
	public get options(): List<T> {
		return this._options.value;
	}

	/**
	 * Setter für alle (ungefilterten) Optionen der Komponente.
	 *
	 * @param value   neue Optionenliste (ungefiltert)
	 */
	public set options(value: List<T>) {
		this._options.value = value;
	}

	/**
	 * Getter für alle selektierten Optionen der Komponente.
	 *
	 * @return Aktuelle Selektion
	 */
	public get selected(): List<T> {
		return this._selected.value;
	}

	/**
	 * Setter für alle selektierten Optionen der Komponente.
	 *
	 * @param value   neue Selektion
	 *
	 * @throws DeveloperNotificationException   wenn die neue Liste mehrere Optionen enthält, aber nur Single-Selektionen erlaubt sind
	 */
	public set selected(value: any) {
		const newSelection = new ArrayList<T>();
		if (value !== undefined && value !== null) {
			if ((typeof value[Symbol.iterator] === 'function') && (typeof value !== "string"))
				newSelection.addAll(value);
			else
				newSelection.add(value);
		}

		if (!this._multi.value && newSelection.size() > 1)
			throw new DeveloperNotificationException("In einer Single-Select-Komponente können nicht mehrere Optionen selektiert sein. "
				+ "Dem selected Setter wurden jedoch mehrere übergeben.");

		this._selected.value = newSelection;
		triggerRef(this._selected);
	}

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
	public set filters(value: List<SelectFilter<T>>) {
		this._filters = value;
	}

	/**
	 * Fügt einen Filter zu den aktiven Filtern hinzu. Der key des Filters muss eindeutig sein. Es dürfen keine zwei Filter mit demselben Key hinzugefügt werden.
	 * Die gefilterte Liste wird sofort aktualisiert.
	 *
	 * @param filter   der neue, aktive Filter
	 */
	public addFilter(filter: SelectFilter<T>) {
		for (const item of this.filters)
			if (item.key === filter.key)
				throw new DeveloperNotificationException(`Der SelectManager des UiSelects beinhaltet bereits einen Filter mit dem Key ${filter.key}`);
		this.filters.add(filter);
		const filteredList = filter.apply(this.options)
		this._filterMap.set(filter.key, filteredList);
		this.updateFiltered();
	}

	/**
	 * Aktualisiert einen bereits aktiven Filter z.B. wegen Zustandsänderungen innerhalb des Filters
	 *
	 * @param filter   der Filter, der aktualisiert werden soll-
	 */
	public updateFilter(filter: SelectFilter<T>) {
		this.removeFilter(filter);
		this.addFilter(filter);
	}

	/**
	 * Entfernt einen aktiven Filter. Die gefilterte Liste wird sofort aktualisiert.
	 *
	 * @param removeFilter   Filter, der entfernt werden soll.
	 */
	public removeFilter(removeFilter: SelectFilter<T>) {
		const filter = this.findFilter(removeFilter);
		if (filter === undefined)
			return;
		this.filters.remove(filter);
		this._filterMap.delete(filter.key);
		this.updateFiltered();
	}

	/**
	 * Sucht einen Filter in der aktiven Filterliste anhand seines Keys.
	 *
	 * @param findFilter   der Filter, der gefunden werden soll
	 *
	 * @returns den passenden Filter. Undefined, wenn keiner gefunden werden konnte.
	 */
	private findFilter(findFilter: SelectFilter<T>): SelectFilter<T> | undefined {
		for (const filter of this.filters)
			if (filter.key === findFilter.key)
				return filter;
		return undefined;
	}

	/**
	 * Aktualisiert die Liste der gefilterten Optionen basierend auf den aktuell aktiven Filtern.
	 */
	private updateFiltered() {
		let result: List<T> = new ArrayList();

		for (const filteredOptions of this._filterMap.values())
			if (result.isEmpty())
				result = filteredOptions;
			else
				result = this.intersect(result, filteredOptions);

		this.filtered = result;
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
	public get filtered(): List<T> {
		return this._filtered.value;
	}

	/**
	 * Setter für die gefilterte Liste an Optionen des Dropdowns.
	 *
	 * @param value   neue Liste der gefilterten Optionen
	 */
	public set filtered(value: List<T>) {
		this._filtered.value = value;
		triggerRef(this._filtered);
	}

	/**
	 * Getter, der angibt, ob die Multi-Selektion erlaubt ist.
	 *
	 * @return true, wenn Multi-Selektionen erlaubt sind
	 */
	public get multi(): boolean {
		return this._multi.value;
	}

	/**
	 * Setter zum Festlegen, ob die Multi-Selektion erlaubt ist.
	 *
	 * @param value   true, wenn Mult-Selektionen erlaubt sind
	 */
	public set multi(value: boolean) {
		this._multi.value = value;
	}

	/**
	 * Getter für die Konfiguration der Darstellung der Selektion.
	 *
	 * @return Darstellungskonfiguration für die Selektion
	 */
	public get selectionDisplayText(): string | ((option: any) => string) {
		return this._selectionDisplayText;
	}

	/**
		 * Setter für die Konfiguration der Darstellung der Selektion.
		 *
		 * @param value neue Darstellungskonfiguration für die Selektion.
		 */
	public set seletcionDisplayText(value: string | ((option: any) => string)) {
		this._selectionDisplayText = value;
	}

	/**
	 * Getter für die Konfiguration der Darstellung der Optionen.
	 *
	 * @return Darstellungskonfiguration für Optionen
	 */
	public get optionDisplayText(): string | ((option: any) => string) {
		return this._optionDisplayText;
	}

	/**
	 * Setter für die Konfiguration der Darstellung der Optionen.
	 *
	 * @param value neue Darstellungskonfiguration für Optionen.
	 */
	public set optionDisplayText(value: string | ((option: any) => string)) {
		this._optionDisplayText = value;
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
	 * @returns true, wenn die Option seketiert ist
	 */
	public isSelected(option: T): boolean {
		return this._selected.value.contains(option);
	}

	/**
	 * Selektiert die übergebene Option. Falls `_multi === false`, dann wird die aktuelle Selektion zunächst geleert. Andernfalls wird die neue Option der
	 * bestehenden Selektion angefügt.
	 *
	 * @param option   die Option, die selektiert werden soll
	 */
	public select(option: T): void {
		if (this.isSelected(option))
			throw new DeveloperNotificationException(`Die Option ${this.getOptionText(option)} ist bereits selektiert.`);
		if (this._multi.value === false)
			this.clearSelection();
		this._selected.value.add(option);
		triggerRef(this._selected);
	}

	/**
	 * Deselektiert die übergebene Option.
	 *
	 * @param option   die Option, die deselektiert werden soll
	 */
	public deselect(option: T): void {
		const index = this._selected.value.indexOf(option);
		if (index !== -1) {
			this._selected.value.removeElementAt(index);
			triggerRef(this._selected);
		}
	}

	/**
	 * Leert die aktuelle Selektion
	 */
	public clearSelection(): void {
		this._selected.value.clear();
		triggerRef(this._selected);
	}

	/**
	 * Getter für den Text eines selektierten Objektes.
	 *
	 * @param option   Option, dessen Text abgefragt wird
	 *
	 * @returns den Text der Option.
	 */
	public abstract getSelectionText(option: T): string;

	/**
	 * Getter für den Text der übergebenen Option.
	 *
	 * @param option   Option, dessen Text abgefragt wird
	 *
	 * @returns den Text der Option.
	 */
	public abstract getOptionText(option: T): string;

}