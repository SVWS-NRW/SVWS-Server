import type { Ref } from "vue";
import type { BaseSelectManager } from "./BaseSelectManager";
import type { List } from "../../../../../../core/src/java/util/List";
import type { ValidatorFehler } from "../../../../../../core/src/asd/validate/ValidatorFehler";
import type { ValidationResult } from "../../../../validation/ValidationResult";

export interface UiSelectProps<T> {
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
	validation?: () => List<ValidatorFehler>;
}
export interface UiSelectSingleProps<T> extends UiSelectProps<T> {
}

export interface UiSelectMultiProps<T> extends UiSelectProps<T> {
	minOptions?: number;
	maxOptions?: number;
}

export interface UiSelectState<T> {
	instanceId: string;
	multi: boolean;
	label: string;
	manager: BaseSelectManager<T>;
	searchable: boolean;
	search: string;
	deepSearchAttributes: string[];
	required: boolean;
	removable: boolean;
	disabled: boolean;
	readonly: boolean;
	headless: boolean;
	isValid: boolean;
	validationResult: ValidationResult;
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
