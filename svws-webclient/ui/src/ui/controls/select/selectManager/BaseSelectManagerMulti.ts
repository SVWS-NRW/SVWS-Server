import { ref } from "vue";
import { BaseSelectManager, type BaseSelectManagerConfig } from "./BaseSelectManager";
import type { List } from "../../../../../../core/src/java/util/List";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import { DeveloperNotificationException } from "../../../../../../core/src/core/exceptions/DeveloperNotificationException";

export interface BaseSelectManagerMultiConfig<T> extends BaseSelectManagerConfig<T> {
	selected?: Iterable<T>
}

export abstract class BaseSelectManagerMulti<T> extends BaseSelectManager<T> {

	protected _selected = ref<List<T>>(new ArrayList<T>);

	protected readonly _multi: boolean = true;

	public constructor (config?: BaseSelectManagerMultiConfig<T>) {
		super(config);
		this.selected = config?.selected ?? null;
	}

	public set selected(value: Iterable<T> | null | undefined) {
		let newSelection = new ArrayList<T>();
		if (value === undefined || value === null)
			this._selected.value = newSelection;
		else
			newSelection = this.getListFromIterable(value);
		this._selected.value = newSelection;
	}

	public get selected() : List<T> {
		return this._selected.value;
	}

	public isSelected(option: T): boolean {
		return this.selected.contains(option);
	}

	public select(option: T): void {
		if (this.isSelected(option))
			throw new DeveloperNotificationException(`Die Option ${this.getOptionText(option)} ist bereits selektiert.`);
		this.selected.add(option);
	}

	public deselect(option: T): void {
		if (!this.removable && (this.selected.size() === 1))
			throw new DeveloperNotificationException("Das Select ist auf removable=false gesetzt, daher kann der Eintrag nicht deselektiert werden");

		const index = this.selected.indexOf(option);
		if (index !== -1)
			this.selected.removeElementAt(index);
	}

	public clearSelection(): void {
		this.selected.clear();
	}

	public hasSelection(): boolean {
		return !this.selected.isEmpty();
	}

	public syncSelectionAndFilteredOptions() {
		const filteredSet = new Set<T>(this.filteredOptions);
		const newSelected = new ArrayList<T>;

		for (const selection of this.selected) {
			if (filteredSet.has(selection))
				newSelected.add(selection);
		}

		this.selected = newSelected;
	}

}