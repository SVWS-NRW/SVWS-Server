import { ref } from "vue";
import { BaseSelectManager, type BaseSelectManagerConfig } from "./BaseSelectManager";
import { DeveloperNotificationException } from "../../../../../../core/src/core/exceptions/DeveloperNotificationException";

export interface BaseSelectManagerSingleConfig<T> extends BaseSelectManagerConfig<T> {
	selected?: T | null
}

export abstract class BaseSelectManagerSingle<T> extends BaseSelectManager<T> {

	protected _selected = ref<T | null>(null);

	protected readonly _multi: boolean = false;

	public constructor (config?: BaseSelectManagerSingleConfig<T>) {
		super(config);
		this.selected = config?.selected ?? null;
	}

	public set selected(value: T | null | undefined) {
		this._selected.value = value ?? null;
	}

	public get selected() : T | null {
		return this._selected.value;
	}

	public isSelected(option: T): boolean {
		if (this.selected === null)
			return false;
		return this.selected === option;
	}

	public select(option: T): void {
		if (this.isSelected(option))
			throw new DeveloperNotificationException(`Die Option ${this.getOptionText(option)} ist bereits selektiert.`);

		this.selected = option;
	}

	public deselect(option: T): void {
		if (!this.removable)
			throw new DeveloperNotificationException("Das Select ist auf removable=false gesetzt, daher kann der Eintrag nicht deselektiert werden");
		this.selected = null;
	}

	public clearSelection(): void {
		this.selected = null;
	}

	public hasSelection(): boolean {
		return this.selected !== null;
	}

	public syncSelectionAndFilteredOptions() {
		if (this.selected === null)
			return;
		const filteredSet = new Set<T>(this.filteredOptions);
		if (!filteredSet.has(this.selected))
			this.selected = null;
	}

}