import type { BaseSelectManager } from "./BaseSelectManager";

export interface UiSelectProps<T, V> {
	label?: string;
	manager?: BaseSelectManager<T>;
	searchable?: boolean;
	deepSearchAttributes?: string[];
	required?: boolean;
	removable?: boolean;
	nullable?: boolean;
	disabled?: boolean;
	readonly?: boolean;
	statistics?: boolean;
	headless?: boolean;
	validator?: () => V;
}
export interface UiSelectSingleProps<T, V> extends UiSelectProps<T, V> {
	doValidate?: (validator: V, value: T | null) => boolean;
}

export interface UiSelectMultiProps<T, V> extends UiSelectProps<T, V> {
	minOptions?: number;
	maxOptions?: number;
	doValidate?: (validator: V, value: Iterable<T> | null) => boolean;
}