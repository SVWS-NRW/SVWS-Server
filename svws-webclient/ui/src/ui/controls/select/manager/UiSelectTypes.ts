import type { Ref } from "vue";
import type { BasicValidator } from "../../../../../../core/src/asd/validate/BasicValidator";
import type { BaseSelectManager } from "./BaseSelectManager";
import type { List } from "../../../../../../core/src/java/util/List";

export interface UiSelectProps<T, V extends BasicValidator> {
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
export interface UiSelectSingleProps<T, V extends BasicValidator> extends UiSelectProps<T, V> {
	doValidate?: (validator: V, value: T | null) => boolean;
}

export interface UiSelectMultiProps<T, V extends BasicValidator> extends UiSelectProps<T, V> {
	minOptions?: number;
	maxOptions?: number;
	doValidate?: (validator: V, value: Iterable<T> | null) => boolean;
}

export interface UiSelectState<T, V extends BasicValidator> {
	instanceId: string;
	multi: boolean;
	label: string;
	manager?: BaseSelectManager<T>;
	searchable: boolean;
	search: string;
	deepSearchAttributes: string[];
	required: boolean;
	removable: boolean;
	disabled: boolean;
	readonly: boolean;
	headless: boolean;
	validator?: () => V;
	isValid: boolean,
	isValidatorValid: boolean,
}

// Interface für alle HTML Elemente, deren Styling und Events gesetzt werden müssen
export interface UiSelectHTMLElements {
	uiSelect: Ref<HTMLElement | null>,
	uiSelectCombobox: Ref<HTMLElement | null>,
	uiSelectSearch: Ref<HTMLElement | null>,
	uiSelectDropdown: Ref<HTMLDivElement | null>,
}

// Methoden, die die Selektion im Select betreffen
export interface UiSelectSelectionMethods<T> {
	isSelected: (option: T) => boolean,
	hasSelection: () => boolean,
	deselectAllowed: () => boolean,
	deselectOption: (option: T) => void,
	selectOption: (option: T) => void
}

// Status und Methoden des Dropdowns
export interface UiSelectDropdown<T> {
	dropdownIsOpen: Ref<boolean>,
	highlightedIndex: Ref<number>,
	selectHighlightedOption: (filteredOptions: List<T>) => void,
	hasHighlightedOption: () => boolean,
	closeDropdown: () => void,
	openDropdown: () => void,
	toggleDropdown: () => void,
	highlightFirstOption: () => void,
	highlightLastOption: (optionsSize: number) => void,
	highlightOptionThatStartsWith: (filteredOptions: List<T>, search: string) => void,
	removeOptionHighlighting: () => void
}