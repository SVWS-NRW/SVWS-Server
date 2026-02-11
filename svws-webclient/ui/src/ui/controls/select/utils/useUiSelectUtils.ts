import { computed, nextTick, ref, watch, type ComputedRef, type Ref } from 'vue';
import type { UiSelectDropdown, UiSelectHTMLElements, UiSelectSelectionMethods, UiSelectState } from "../manager/UiSelectTypes";
import type { List } from "../../../../../../core/src/java/util/List";
import { useElementBounding, useWindowSize } from "@vueuse/core";
import { useUiSelectFocusHandler } from "./useUiSelectFocusHandler";
import { useUiSelectInputHandler } from "./useUiSelectInputHandler";
import { useUiSelectStyles } from "./useUiSelectStyles";
import type { ArrayList } from "../../../../../../core/src/java/util/ArrayList";

export function useUiSelectUtils<T>(
	state: ComputedRef<UiSelectState<T>>,
	attrs: Record<string, any>,
	elements: UiSelectHTMLElements,
	search: Ref<string>,
	selectionMethods: UiSelectSelectionMethods<T>
): {
	// Dropdown
	dropdownPositionStyles: ComputedRef<{ top: string; left: string; width: string; maxHeight: string; }>,
	toggleSelection: (option: T) => void,
	closeDropdown: () => void,
	// Styles und Attribute
	focusClass: ComputedRef<"" | "contentFocusField" | "subNavigationFocusField">,
	comboboxRole: ComputedRef<string | undefined>,
	comboboxAriaAttrs: ComputedRef,
	comboboxClasses: ComputedRef<string[]>,
	comboboxTabindex: ComputedRef,
	filteredHtmlAttributes: ComputedRef<Record<string, any>>,
	headlessPadding: ComputedRef<string>,
	iconColorClass: ComputedRef<string>,
	labelClasses: ComputedRef<string[]>,
	labelTextColorClass: ComputedRef<string>,
	labelIconClass: ComputedRef<string>,
	textColorClass: ComputedRef<string>,
	getSecondaryTextColor: (color: string) => string,
	searchInputAriaAttrs: ComputedRef,
	getOptionClasses: (option: T, optionIndex: number) => string[],
	// Anzeige
	showLabel: ComputedRef<boolean>,
	showValidatorError: ComputedRef<boolean>,
	// Suche
	splitTextIntoHits: (text: string) => { text: string, hit: boolean; }[],
	resetSearch: () => void,
	optionsMatchingSearch: ComputedRef<List<T> | ArrayList<T>>,
	// Events
	handleSearchInput: () => void,
	focusSelect: () => void,
	unfocusInput: () => void,
	handleComponentClick: () => void,
	onFocusOut: () => void,
	handleKeyDown: (event: KeyboardEvent) => Promise<void>,
	focusOnInput: Ref<boolean>,

} {
	const dropdownIsOpen = ref(false);
	// Größen und Positionen zur Berechnung des Dropdowns
	const distanceWindowTopToComboboxTop = ref(0);
	const distanceWindowTopToComboboxBottom = ref(0);
	const distanceWindowLeftToComboboxLeft = ref(0);
	const widthComboBox = ref(0);
	const heightComboBox = ref(0);
	const { height: windowHeight } = useWindowSize();
	const bounding = useElementBounding(elements.uiSelectCombobox);
	const justOpened = ref(false);

	// Beobachtet Größe und Position der Combobox, um bei Änderungen das Dropdown zu schließen
	watch(
		[bounding.x, bounding.y, bounding.height, bounding.width],
		() => {
			if (!justOpened.value) {
				closeDropdown();
			}
		}
	);

	// Index des visuell hervorgehobenen Dropdownlistenelements bei Tastennavigation
	const highlightedIndex = ref(-1);

	// Composables
	const dropdown = { dropdownIsOpen,
		highlightedIndex,
		selectHighlightedOption,
		hasHighlightedOption,
		closeDropdown, openDropdown,
		toggleDropdown,
		highlightFirstOption,
		highlightLastOption,
		highlightOptionThatStartsWith,
		removeOptionHighlighting } as UiSelectDropdown<T>;

	const { handleKeyDown,
		handleSearchInput,
		resetSearch,
		splitTextIntoHits,
		optionsMatchingSearch } = useUiSelectInputHandler(state, search, dropdown);

	const { focusSelect,
		unfocusInput,
		handleComponentClick,
		onFocusOut,
		focusOnInput } = useUiSelectFocusHandler(state, elements, resetSearch, dropdown);

	const { focusClass,
		comboboxRole,
		comboboxAriaAttrs,
		comboboxClasses,
		comboboxTabindex,
		filteredHtmlAttributes,
		headlessPadding,
		iconColorClass,
		labelClasses,
		labelTextColorClass,
		labelIconClass,
		textColorClass,
		getSecondaryTextColor,
		searchInputAriaAttrs,
		getOptionClasses,
		showValidatorError,
		showLabel } = useUiSelectStyles(state, attrs, selectionMethods, dropdown);

	watch(highlightedIndex, async () => {
		await nextTick(() => {
			scrollDropdownToHighlightedElement();
		});
	});

	const dropdownPositionStyles = computed(() => ({
		top: dropdownTopPosition.value + 'px',
		left: distanceWindowLeftToComboboxLeft.value + 'px',
		width: widthComboBox.value + 'px',
		maxHeight: dropdownMaxHeight.value + 'px',
	}));

	const dropdownTopPosition = computed(() => {
		if (elements.uiSelectDropdown.value === null) {
			return 0;
		}
		if (dropdownShouldBeDisplayedAboveComboBox.value) {
			const dropdownHeight = Math.min(dropdownMaxHeight.value, elements.uiSelectDropdown.value.scrollHeight);
			return `${distanceWindowTopToComboboxTop.value - dropdownHeight - 2}`;
		} else {
			return `${distanceWindowTopToComboboxTop.value + heightComboBox.value + 3}`;
		}
	});

	const dropdownShouldBeDisplayedAboveComboBox = computed(() => {
		if (!dropdownIsOpen.value) {
			return false;
		}

		const freeSpaceBelowCombobox = windowHeight.value - distanceWindowTopToComboboxBottom.value;
		const freeSpaceAboveCombobox = distanceWindowTopToComboboxTop.value;
		const minimumSpace = 100;
		if (freeSpaceBelowCombobox > minimumSpace) {
			return false;
		}

		return (freeSpaceBelowCombobox < freeSpaceAboveCombobox);
	});

	/**
	 * Berechnet die maximale Höhe des Dropdowns, sodass dieses nie aus dem Viewport verschwindet. Sollte diese 235px übersteigen, dann wird die maxHeight auf
	 * 235px gesetzt
	 */
	const dropdownMaxHeight = computed(() => {
		let maxHeight;
		if (dropdownShouldBeDisplayedAboveComboBox.value) {
			maxHeight = distanceWindowTopToComboboxTop.value - 5;
		} else {
			maxHeight = windowHeight.value - distanceWindowTopToComboboxBottom.value - 5;
		}
		return Math.min(235, maxHeight);
	});

	function updateDropdownSizeAndPosition(): void {
		distanceWindowTopToComboboxTop.value = bounding.top.value;
		distanceWindowTopToComboboxBottom.value = bounding.bottom.value;
		distanceWindowLeftToComboboxLeft.value = bounding.left.value;
		widthComboBox.value = bounding.width.value;
		heightComboBox.value = bounding.height.value;
	}

	function toggleDropdown(): void {
		if (dropdownIsOpen.value) {
			closeDropdown();
		} else {
			openDropdown();
		}
	}

	function openDropdown(): void {
		if ((elements.uiSelectDropdown.value === null) || dropdownIsOpen.value) {
			return;
		}
		justOpened.value = true;
		updateDropdownSizeAndPosition();
		focusSelect();
		elements.uiSelectDropdown.value.showPopover();
		dropdownIsOpen.value = true;
		if (!hasHighlightedOption()) {
			scrollDropdownToTop();
		}

		window.addEventListener('resize', () => closeDropdown());
		// Manchmal verschiebt sich das Select, wenn reingeklickt wird. Dieses Flag soll verhindern, dass es in dem Fall
		// sofort wieder geschlossen wird
		requestAnimationFrame(() => {
			justOpened.value = false;
		});
	}

	function closeDropdown(): void {
		if (!dropdownIsOpen.value) {
			return;
		}
		dropdownIsOpen.value = false;
		elements.uiSelectDropdown.value?.hidePopover();
		removeOptionHighlighting();
		window.removeEventListener('resize', () => closeDropdown());
	}

	function scrollDropdownToTop(): void {
		if (elements.uiSelectDropdown.value) {
			elements.uiSelectDropdown.value.scrollTop = 0;
		}
	}

	function scrollDropdownToHighlightedElement(): void {
		if (!elements.uiSelectDropdown.value) {
			return;
		}
		const id = `uiSelect${state.value.multi ? "Multi" : ""}Option_${highlightedIndex.value}_${state.value.instanceId}`;
		const highlightedElement = document.getElementById(id);
		if (!highlightedElement) {
			scrollDropdownToTop();
			return;
		}
		const ringSize = 6; // Zusätzlicher Abstand für den Auswahlring
		const highlightedElementTop = highlightedElement.offsetTop - ringSize;
		const highlightedElementBottom = highlightedElement.offsetTop + highlightedElement.offsetHeight + ringSize;
		const dropdownTop = elements.uiSelectDropdown.value.scrollTop;
		const dropdownBottom = dropdownTop + elements.uiSelectDropdown.value.clientHeight;

		if (highlightedElementBottom > dropdownBottom) {
			// Falls das Element unten rausgeht → Liste nach unten scrollen
			elements.uiSelectDropdown.value.scrollTop = highlightedElementBottom - elements.uiSelectDropdown.value.clientHeight;
		} else if (highlightedElementTop < dropdownTop) {
			// Falls das Element oben rausgeht → Liste nach oben scrollen
			elements.uiSelectDropdown.value.scrollTop = highlightedElementTop;
		}

	}

	function hasHighlightedOption(): boolean {
		return highlightedIndex.value !== -1;
	}

	function highlightFirstOption(): void {
		highlightedIndex.value = 0;
	}

	function highlightLastOption(filteredOptionsSize: number): void {
		highlightedIndex.value = filteredOptionsSize - 1;
	}

	function removeOptionHighlighting(): void {
		if (hasHighlightedOption()) {
			highlightedIndex.value = -1;
		}
	}

	function selectHighlightedOption(filteredOptions: List<T>): void {
		if (!hasHighlightedOption()) {
			return;
		}

		const option = filteredOptions.get(highlightedIndex.value);
		toggleSelection(option);
	}

	/**
	 * Ermittelt die Option, die mit dem übergebenen String beginnt und hebt diese hervor. Falls keine gefunden wurde, wird nichts (mehr) hervorgehoben.
	 *
	 * @param filteredOptions   die Liste der gefilterten Optionen, die tatsächlich im Dropdown angezeigt werden
	 * @param search            String, mit dem die Option beginnen muss
	 */
	function highlightOptionThatStartsWith(filteredOptions: List<T>, search: string): void {
		const startIndex = (hasHighlightedOption())	? highlightedIndex.value + 1 : 0;
		const numberOfOptionsInDropdown = filteredOptions.size();

		// Suche nach einer Option, die mit dem übergebenen String beginnt
		for (let i = startIndex; i < numberOfOptionsInDropdown + startIndex; i++) {
			const index = i % numberOfOptionsInDropdown; // Um auf den Index am Anfang der Liste zurückzukommen
			if (optionStartMatchesSearch(index, search)) {
				highlightedIndex.value = index;
				return;
			}
		}
		// Wurde keine passende Option gefunden, dann wird nichts mehr hervorgehoben
		removeOptionHighlighting();
	}

	function optionStartMatchesSearch(index: number, search: string): boolean {
		const option = state.value.manager.filteredOptions.get(index);
		return stringStartsWithIgnoreCase(state.value.manager.getOptionText(option), search);
	}

	function toggleSelection(option: T): void {
		if (!state.value.multi) {
			closeDropdown();
		}

		if (!selectionMethods.isSelected(option)) {
			selectionMethods.selectOption(option);
		} else if (selectionMethods.deselectAllowed()) {
			selectionMethods.deselectOption(option);
		}

		resetSearch();
		// Bei der Selektion landet der Fokus auf dem Body und muss ggf. wieder auf die Combobox gesetzt werden
		requestAnimationFrame(() => {
			focusSelect();
		});
	}


	return {
		// Dropdown
		dropdownPositionStyles,
		toggleSelection,
		closeDropdown,
		// Styles und Attribute
		focusClass,
		comboboxRole,
		comboboxAriaAttrs,
		comboboxClasses,
		comboboxTabindex,
		filteredHtmlAttributes,
		headlessPadding,
		iconColorClass,
		labelClasses,
		labelTextColorClass,
		labelIconClass,
		textColorClass,
		getSecondaryTextColor,
		searchInputAriaAttrs,
		getOptionClasses,
		// Anzeige
		showLabel,
		showValidatorError,
		// Suche
		splitTextIntoHits,
		resetSearch,
		optionsMatchingSearch,
		// Events
		handleSearchInput,
		focusSelect,
		unfocusInput,
		handleComponentClick,
		onFocusOut,
		handleKeyDown,
		focusOnInput,
	};
}

function stringStartsWithIgnoreCase(string: string, substring: string): boolean {
	return string.toLocaleLowerCase("de-DE").startsWith(substring.toLocaleLowerCase("de-DE"));
}

