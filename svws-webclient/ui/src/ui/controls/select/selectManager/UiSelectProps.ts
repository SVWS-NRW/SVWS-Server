import type { BaseSelectManager } from "./BaseSelectManager";
import type { BaseSelectManagerSingle } from "./BaseSelectManagerSingle";
import type { BaseSelectManagerMulti } from "./BaseSelectManagerMulti";

export interface UiSelectProps<T, V> {
	label?: string;
	manager?: BaseSelectManager<T>;
	searchable?: boolean;
	required?: boolean;
	disabled?: boolean;
	statistics?: boolean;
	headless?: boolean;
	validator?: () => V;
}
export interface UiSelectSingleProps<T, V> extends UiSelectProps<T, V> {
	manager?: BaseSelectManagerSingle<T>;
	doValidate?: (validator: V, value: T | null) => boolean;
}

export interface UiSelectMultiProps<T, V> extends UiSelectProps<T, V> {
	manager?: BaseSelectManagerMulti<T>;
	minOptions?: number | undefined;
	maxOptions?: number | undefined;
	doValidate?: (validator: V, value: Iterable<T> | null) => boolean;
}