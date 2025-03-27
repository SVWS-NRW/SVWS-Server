import { ref, shallowRef, triggerRef } from "vue";
import type { List } from "../../../../../core/src/java/util/List";
import { ArrayList } from "../../../../../core/src/java/util/ArrayList";
import { DeveloperNotificationException } from "../../../../../core/src";

/**
 * Abstrakte Manager Klasse zur Verwendung in einer Select-Komponente (UiSelect.vue). SelectManager übernehmen die Logik der Select-Komponente.
 * T bezeichnet dabei den Datentyp der auswählbaren Optionen.
 */
export abstract class BaseSelectManager<T> {

	// Alle Optionen des Dropdowns
	protected _list = shallowRef<List<T>>(new ArrayList<T>());

	// Die aktuelle Selektion des Dropdowns. Enthält bei Single-Selektion immer nur ein Objekt
	protected _selected = shallowRef<List<T>>(new ArrayList<T>());

	// Die gefilterte Liste der Optionen, die im Dropdwon angezeigt werden.
	protected _filtered = shallowRef<List<T>>(new ArrayList<T>());

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
	 * @param list       die Liste aller Optionen der Komponente (ungefiltert)
	 * @param selected   optional. Die Liste der aktuell selektierten Optionen. Bei einer Singe-Select-Komponente darf maximal ein Objekt in dieser Liste sein.
	 */
	public constructor(multi: boolean, list: Iterable<T>, selected?: Iterable<T>) {
		this.multi = multi;

		const listTmp = new ArrayList<T>();
		for (const i of list)
			listTmp.add(i);
		this.list = listTmp;

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
	public get list(): List<T> {
		return this._list.value;
	}

	/**
	 * Setter für alle (ungefilterten) Optionen der Komponente.
	 *
	 * @param value   neue Optionenliste (ungefiltert)
	 */
	public set list(value: List<T>) {
		this._list.value = value;
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
	public set selected(value : List<T>) {
		if (!this._multi.value && value.size() > 1)
			throw new DeveloperNotificationException("In einer Single-Select-Komponente können nicht mehrere Optionen selektiert sein. "
				+ "Dem selected Setter wurden jedoch mehrere übergeben.");
		this._selected.value = value;
		triggerRef(this._selected);
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
	 * Filtert die Optionen des Dropdowns auf Basis eines Suchbegriffs
	 *
	 * @param search   der Suchbegriff, nach dem gefiltert wird.
	 */
	public filter(search: string) {
		const result = new ArrayList<T>();

		for (const option of this.list)
			if (this.getOptionText(option).toLowerCase().includes(search.toLowerCase()))
				result.add(option);
		this._filtered.value = result;
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
		this._selected.value.remove(option);
		triggerRef(this._selected);
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