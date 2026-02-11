import { computed, type ComputedRef } from "vue";
import type { UiSelectDropdown, UiSelectSelectionMethods, UiSelectState } from "../manager/UiSelectTypes";
import { ValidatorFehlerart } from '../../../../../../core/src/asd/validate/ValidatorFehlerart';

export function useUiSelectStyles<T>(
	state: ComputedRef<UiSelectState<T>>,
	attrs: Record<string, any>,
	selectionMethods: UiSelectSelectionMethods<T>,
	dropdown: UiSelectDropdown<T>
): {
	filteredHtmlAttributes: ComputedRef<Record<string, any>>,
	backgroundColorClass: ComputedRef<string>,
	textColorClass: ComputedRef<string>,
	iconColorClass: ComputedRef<string>,
	borderColorClass: ComputedRef<string>,
	comboboxAriaAttrs: ComputedRef,
	searchInputAriaAttrs: ComputedRef,
	comboboxTabindex: ComputedRef,
	headlessPadding: ComputedRef<string>,
	comboboxRole: ComputedRef<string | undefined>,
	comboboxClasses: ComputedRef<string[]>,
	labelClasses: ComputedRef<string[]>,
	labelTextColorClass: ComputedRef<string>,
	labelIconClass: ComputedRef<string>,
	getOptionClasses: (option: T, optionIndex: number) => string[],
	moveLabel: ComputedRef<boolean>,
	showLabel: ComputedRef<boolean>,
	showValidatorError: ComputedRef<boolean>,
	focusClass: ComputedRef<"" | "contentFocusField" | "subNavigationFocusField">,
	getSecondaryTextColor: (color: string) => string,
} {

	/**
	 * Überprüft übergebene HTML Attribute und filtert Klassen heraus, die nicht an den Rootknoten der Komponente gesetzt werden dürfen.
	 */
	const filteredHtmlAttributes = computed(() => {
		const result = { ...attrs };
		const stringClass = result.class;
		if (typeof stringClass === 'string') {
			const excludedClasses = ['contentFocusField', 'subNavigationFocusField', 'icon-ui-', 'bg-ui-'];
			result.class = stringClass.split(' ').filter(c => !excludedClasses.some(prefix => c.startsWith(prefix))).join(' ');
		}
		return result;
	});


	/**
	 * Berechnet die Hintergrundfarbe der Combobox. Wird von außen eine gesetzt, dann wird diese verwendet. Andernfalls bg-ui.
	 */
	const backgroundColorClass = computed(() => {
		const classString = attrs.class;
		if (typeof classString !== "string") {
			return state.value.headless ? "" : "bg-ui";
		}
		const classes = classString.split(' ');

		const backgroundClass = findFirstBackgroundClass(classes);
		return backgroundClass ?? (state.value.headless ? "" : "bg-ui");
	});

	function getColorClass(prefix: 'text' | 'icon' | 'border') {
		return computed(() => {
			if (state.value.disabled) {
				return `${prefix}-ui-disabled`;
			}
			switch (state.value.validationResult.fehlerart) {
				case ValidatorFehlerart.HINWEIS:
					return `${prefix}-ui-warning`;
				case ValidatorFehlerart.KANN:
					return `${prefix}-ui-caution`;
				case ValidatorFehlerart.MUSS:
					return `${prefix}-ui-danger`;
			}

			if (!state.value.isValid) {
				return `${prefix}-ui-danger`;
			}

			const classString = attrs.class;
			if (typeof classString !== 'string') {
				return `${prefix}-ui`;
			}

			const classes = classString.split(' ');
			const match = classes.find(c => c.startsWith(`${prefix}-ui`));
			return match ?? `${prefix}-ui`;
		});
	}

	const textColorClass = getColorClass("text");
	const iconColorClass = getColorClass("icon");
	const borderColorClass = getColorClass("border");

	const comboboxAriaAttrs = computed(() => {
		const isEditable = !state.value.readonly && !state.value.disabled;
		if (state.value.searchable && isEditable) {
			// Aria Attribute werden am Input gesetzt
			return {};
		}
		return {
			'aria-labelledby': `uiSelectLabel_${state.value.instanceId}`,
			'aria-controls': isEditable ? `uiSelectDropdown_${state.value.instanceId}` : undefined,
			'aria-autocomplete': isEditable ? 'none' as const : undefined,
			'aria-expanded': isEditable ? dropdown.dropdownIsOpen.value : undefined,
			'aria-disabled': state.value.disabled ? true : undefined,
			'aria-activedescendant': (isEditable && dropdown.hasHighlightedOption()) ?
				`uiSelectOption_${dropdown.highlightedIndex.value}_${state.value.instanceId}` : undefined,
		};
	});

	const searchInputAriaAttrs = computed(() => ({
		'aria-labelledby': `uiSelectLabel_${state.value.instanceId}`,
		'aria-controls': `uiSelectDropdown_${state.value.instanceId}`,
		'aria-autocomplete': 'none' as const,
		'aria-expanded': dropdown.dropdownIsOpen.value,
		'aria-activedescendant': (dropdown.hasHighlightedOption()) ? `uiSelectOption_${dropdown.highlightedIndex.value}_${state.value.instanceId}` : undefined,
	}));

	const comboboxTabindex = computed((): number =>
		(state.value.searchable || state.value.disabled || state.value.readonly) ? -1 : 0
	);

	const headlessPadding = computed((): string =>
		state.value.headless ? 'py-0' : 'py-1'
	);

	/**
	 * Berechnet die Role der Combobox. Wird ein SearchInput generiert (searchable = true), dann erhält die Combobox keine Rolle, da diese am SearchInput
	 * gesetzt wird.
	 */
	const comboboxRole = computed((): string | undefined =>
		(!state.value.searchable || state.value.disabled || state.value.readonly) ? 'combobox' : undefined
	);

	const comboboxClasses = computed(() => {
		const headlessClasses = state.value.headless ? 'pl-1 min-h-6' : 'border mt-[0.8em] pl-3 pr-1 min-h-9';
		const pointer = state.value.disabled ? 'pointer-events-none' : '';
		let cursor: string;
		switch (true) {
			case state.value.readonly:
				cursor = 'cursor-not-allowed';
				break;
			case state.value.searchable:
				cursor = 'cursor-text';
				break;
			default:
				cursor = 'cursor-pointer';
		}
		return [headlessClasses, borderColorClass.value, pointer, cursor, backgroundColorClass.value];
	});

	const labelClasses = computed((): string[] => {
		let right = 'right-6';
		if (moveLabel.value) {
			right = 'right-2';
		} else if (state.value.removable) {
			right = 'right-11';
		}

		let left = 'left-2';
		if (state.value.headless) {
			left = state.value.removable ? 'left-10' : 'left-6';
		}

		const position = moveLabel.value ? 'absolute -top-0.5 text-xs' : 'absolute top-1/2 font-normal';

		return [position, right, left, backgroundColorClass.value, textColorClass.value];
	});

	const labelTextColorClass = computed((): string =>
		(state.value.disabled || moveLabel.value) ? textColorClass.value : getSecondaryTextColor(textColorClass.value)
	);

	const labelIconClass = computed((): string =>
		(state.value.disabled || moveLabel.value) ? iconColorClass.value : getSecondaryIconColor(iconColorClass.value)
	);

	function getOptionClasses(option: T, optionIndex: number): string[] {
		const textAndColors = selectionMethods.isSelected(option) ? 'bg-ui-selected text-ui-onselected font-medium border border-ui-selected' : 'text-ui';
		const optionHighlighting = dropdown.highlightedIndex.value === optionIndex ? 'bg-ui-hover inset-ring-2 inset-ring-ui-neutral' : '';
		return [textAndColors, optionHighlighting];
	}


	const moveLabel = computed(() => selectionMethods.hasSelection() || (state.value.search !== '' && state.value.searchable));

	const showLabel = computed((): boolean =>
		!state.value.headless || !moveLabel.value
	);

	const showValidatorError = computed((): boolean =>
		!state.value.isValid && (!state.value.required || selectionMethods.hasSelection())
	);

	const focusClass = computed(() => {
		const result = { ...attrs };
		const stringClass = result.class;
		if (typeof stringClass === 'string') {
			if (stringClass.includes('contentFocusField')) {
				return 'contentFocusField';
			} else if (stringClass.includes('subNavigationFocusField')) {
				return 'subNavigationFocusField';
			}
		}
		return '';
	});

	return {
		filteredHtmlAttributes,
		backgroundColorClass,
		textColorClass,
		iconColorClass,
		borderColorClass,
		comboboxAriaAttrs,
		searchInputAriaAttrs,
		comboboxTabindex,
		headlessPadding,
		comboboxRole,
		comboboxClasses,
		labelClasses,
		labelTextColorClass,
		labelIconClass,
		getOptionClasses,
		moveLabel,
		showLabel,
		showValidatorError,
		focusClass,
		getSecondaryTextColor,
	};
}

/**
 * Generiert die passende sekundäre Iconfarbe für gesetzte Iconfarben von außen.
 *
 * @param color    die Iconfarbe, dessen Sekundärfarbe ermittelt werden soll.
 */
function getSecondaryIconColor(color: string): string {
	if (color.startsWith("icon-uistatic")) {
		return "icon-uistatic-25";
	}
	switch (color) {
		case "icon-ui":
			return "icon-ui-secondary";
		case "icon-ui-brand":
			return "icon-ui-brand-secondary";
		case "icon-ui-statistic":
			return "icon-ui-statistic-secondary";
		case "icon-ui-selected":
			return "icon-ui-selected-secondary";
		case "icon-ui-danger":
			return "icon-ui-danger-secondary";
		case "icon-ui-success":
			return "icon-ui-success-secondary";
		case "icon-ui-warning":
			return "icon-ui-warning-secondary";
		case "icon-ui-caution":
			return "icon-ui-caution-secondary";
		case "icon-ui-neutral":
			return "icon-ui-neutral-secondary";
		case "icon-ui-disabled":
			return "icon-ui-disabled-secondary";
		case "icon-ui-onbrand":
			return "icon-ui-onbrand-secondary";
		case "icon-ui-onstatistic":
			return "icon-ui-onstatistic-secondary";
		case "icon-ui-onselected":
			return "icon-ui-onselected-secondary";
		case "icon-ui-ondanger":
			return "icon-ui-ondanger-secondary";
		case "icon-ui-onsuccess":
			return "icon-ui-onsuccess-secondary";
		case "icon-ui-onwarning":
			return "icon-ui-onwarning-secondary";
		case "icon-ui-oncaution":
			return "icon-ui-oncaution-secondary";
		case "icon-ui-onneutral":
			return "icon-ui-onneutral-secondary";
		case "icon-ui-ondisabled":
			return "icon-ui-ondisabled-secondary";
		default:
			return "icon-ui-secondary";
	}
}

/**
 * Generiert die passende sekundäre Textfarbe für gesetzte Textfarben von außen.
 *
 * @param color    die Textfarbe, dessen Sekundärfarbe ermittelt werden soll.
 */
function getSecondaryTextColor(color: string): string {
	if (color.startsWith("text-uistatic")) {
		return "text-uistatic-25";
	}
	switch (color) {
		case "text-ui":
			return "text-ui-secondary";
		case "text-ui-brand":
			return "text-ui-brand-secondary";
		case "text-ui-statistic":
			return "text-ui-statistic-secondary";
		case "text-ui-selected":
			return "text-ui-selected-secondary";
		case "text-ui-danger":
			return "text-ui-danger-secondary";
		case "text-ui-success":
			return "text-ui-success-secondary";
		case "text-ui-warning":
			return "text-ui-warning-secondary";
		case "text-ui-caution":
			return "text-ui-caution-secondary";
		case "text-ui-neutral":
			return "text-ui-neutral-secondary";
		case "text-ui-disabled":
			return "text-ui-disabled-secondary";
		case "text-ui-onbrand":
			return "text-ui-onbrand-secondary";
		case "text-ui-onstatistic":
			return "text-ui-onstatistic-secondary";
		case "text-ui-onselected":
			return "text-ui-onselected-secondary";
		case "text-ui-ondanger":
			return "text-ui-ondanger-secondary";
		case "text-ui-onsuccess":
			return "text-ui-onsuccess-secondary";
		case "text-ui-onwarning":
			return "text-ui-onwarning-secondary";
		case "text-ui-oncaution":
			return "text-ui-oncaution-secondary";
		case "text-ui-onneutral":
			return "text-ui-onneutral-secondary";
		case "text-ui-ondisabled":
			return "text-ui-ondisabled-secondary";
		default:
			return "text-ui-secondary";
	}
}

function findFirstBackgroundClass(classes: string[]): string | undefined {
	return classes.find(c => c.startsWith('bg-ui'));
}
