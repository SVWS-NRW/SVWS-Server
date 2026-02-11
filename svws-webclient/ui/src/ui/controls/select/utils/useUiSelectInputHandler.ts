// composables/useUiSelectKeyboardHandler.ts
import { computed, ref, type ComputedRef, type Ref } from 'vue';
import type { List } from "../../../../../../core/src/java/util/List";
import type { UiSelectDropdown, UiSelectState } from "../manager/UiSelectTypes";
import { ArrayList } from "../../../../../../core/src/java/util/ArrayList";
import type { BaseSelectManager } from "../manager/BaseSelectManager";

export function useUiSelectInputHandler<T>(
	state: ComputedRef<UiSelectState<T>>,
	search: Ref<string>,
	dropdown: UiSelectDropdown<T>
): {
	handleKeyDown: (event: KeyboardEvent) => Promise<void>,
	handleSearchInput: () => void,
	resetSearch: () => void,
	splitTextIntoHits: (text: string) => { text: string, hit: boolean; } [],
	optionsMatchingSearch: ComputedRef<List<T> | ArrayList<T>>,
} {

	const optionsMatchingSearch = computed(() => {
		return state.value.searchable ? getSearchedOptions(search.value, state.value.manager) : state.value.manager.filteredOptions;
	});

	/**
	 * Tastaturbedienung der Komponente. Sie orientiert sich an den Vorgaben von https://www.w3.org/WAI/ARIA/apg/patterns/combobox/
	 *
	 * @param event   das Keyboardevent, das die gedrückte Taste enthält
	 */
	async function handleKeyDown(event: KeyboardEvent): Promise<void> {
		if (state.value.disabled || state.value.readonly) {
			return;
		}

		// Nur bei geöffnetem Dropdown, oder wenn Navigation ausgelöst wird. Verhindert, dass die Seite beim Navigieren des Dropdowns gescrollt wird.
		const isNavigationKey = [
			'ArrowUp',
			'ArrowDown',
			'PageUp',
			'PageDown',
			...(state.value.searchable ? [] : [' ']),
		].includes(event.key);

		if (isNavigationKey) {
			event.preventDefault();
		}

		const handlers: Record<string, () => Promise<void> | void> = {
			"Enter": () => handleEnter(),
			" ": () => handleSpace(),
			"Tab": () => handleTab(),
			"ArrowDown": async () => await handleArrowDown(event),
			"ArrowUp": async () => await handleArrowUp(event),
			"PageUp": async () => await handlePageUp(),
			"PageDown": async () => await handlePageDown(),
			"Home": () => handleHome(),
			"End": () => handleEnd(),
			"Escape": () => handleEscape(),
			"*": () => handleDefault(event),
		};
		const handler = handlers[event.key] ?? handlers["*"];
		await handler();
	}

	function handleEnter(): void {
		if (openDropdownIfClosed()) {
			return;
		}

		dropdown.selectHighlightedOption(optionsMatchingSearch.value);
		if (!state.value.multi || !dropdown.hasHighlightedOption()) {
			dropdown.closeDropdown();
		}
	}

	function handleSpace(): void {
		if (openDropdownIfClosed()) {
			return;
		}

		if (state.value.searchable) {
			return;
		}
		dropdown.selectHighlightedOption(optionsMatchingSearch.value);
		if (!state.value.multi || !dropdown.hasHighlightedOption()) {
			dropdown.closeDropdown();
		}
	}

	function handleTab(): void {
		dropdown.closeDropdown();
	}

	async function handleArrowDown(event: KeyboardEvent): Promise<void> {
		const alreadyOpen = !openDropdownIfClosed();

		if (alreadyOpen && event.altKey) {
			dropdown.selectHighlightedOption(optionsMatchingSearch.value);
			dropdown.closeDropdown();
			return;
		}

		if (!event.altKey) {
			await navigateDropdown(1);
		}
	}

	async function handleArrowUp(event: KeyboardEvent): Promise<void> {
		const alreadyOpen = !openDropdownIfClosed();

		if (alreadyOpen && event.altKey) {
			dropdown.selectHighlightedOption(optionsMatchingSearch.value);
			dropdown.closeDropdown();
			return;
		}

		if (!event.altKey) {
			await navigateDropdown(-1);
		}
	}

	function handleHome(): void {
		openDropdownIfClosed();
		if (!state.value.searchable) {
			dropdown.highlightFirstOption();
		}
	}

	function handleEnd(): void {
		openDropdownIfClosed();
		if (!state.value.searchable) {
			dropdown.highlightLastOption(optionsMatchingSearch.value.size());
		}
	}

	function handleEscape(): void {
		dropdown.closeDropdown();
		resetSearch();
	}

	async function handlePageUp(): Promise<void> {
		if (openDropdownIfClosed()) {
			return;
		}
		await navigateDropdown(-10);
	}

	async function handlePageDown(): Promise<void> {
		if (openDropdownIfClosed()) {
			return;
		}
		await navigateDropdown(10);
	}

	/**
	 * KeyboardEvent Handler für alle restlichen Keys.
	 *
	 * @param event   das Event mit der gedrückten Taste
	 */
	function handleDefault(event: KeyboardEvent): void {
		if (event.shiftKey || event.altKey) {
			return;
		}
		dropdown.openDropdown();
		handlePrintableKeyInputInNonSearchable(event);
	}

	function openDropdownIfClosed(): boolean {
		if (!dropdown.dropdownIsOpen.value) {
			dropdown.openDropdown();
			return true;
		}
		return false;
	}

	/**
	 * Ermittelt die Option im Dropdown, die hervorgehoben werden soll basierend auf dem eingegebenen druckbaren Zeichen. Falls das Zeichen innerhalb von 0,5s
	 * wiederholt eingegeben wird, dann wird bei jeder Eingabe davon der nächste passende Eintrag hervorgehoben. Passend bedeutet dabei, dass der Eintrag mit dem
	 * eingegebenen Zeichen beginnt. Wird innerhalb kurzer Zeit (0,5s) mehr als ein Zeichen eingegeben und zudem auch unterschiedliche Zeichen, dann wird nach
	 * einer Option gesucht, die mit dem gesamten eingegebenen Begriff beginnt.
	 *
	 * @param event   das Keyboardevent, welches das eingegebene Zeichen enthält.
	 */
	function handlePrintableKeyInputInNonSearchable(event: KeyboardEvent): void {
		if ((state.value.searchable) || (!isPrintableChar(event.key))) {
			return;
		}

		generateSearchValue(event.key);
		dropdown.highlightOptionThatStartsWith(optionsMatchingSearch.value, search.value);
	}

	// Timeout für die Eingabe von Buchstaben. Dieser würd bei searchable = false benötigt, um Highlighting von Optionen mit mehreren Buchstaben zu erlauben
	const keyTimeout: Ref<ReturnType<typeof setTimeout> | undefined> = ref();
	/**
	 * Generiert den Suchwert basierend auf der Eingabe und einem aktuellen Timer
	 *
	 * @param key   das neue eingegebene Zeichen
	 */
	function generateSearchValue(key: string): void {
		if ((keyTimeout.value === undefined) || (search.value === key)) {
			resetSearch();
		}

		search.value += key;

		clearTimeout(keyTimeout.value);
		keyTimeout.value = setTimeout(() => {
			keyTimeout.value = undefined; resetSearch();
		}, 500);
	}

	/**
	 * Verschiebt die aktuell hervorgehobene Option um die angegebenen steps.
	 *
	 * @param steps   Wenn negativ, dann wird rückwärts navigiert. Die Zahl gibt die Anzahl der Schritte an.
	 */
	async function navigateDropdown(steps: number): Promise<void> {
		const size = optionsMatchingSearch.value.size();
		if (size === 0) {
			return;
		}

		let index = dropdown.highlightedIndex.value;

		// falls nichts hervorgehoben, initial auf 0 (rückwärts) oder -1 (vorwärts)
		if ((steps < 0) && (index === -1)) {
			index = 0;
		}

		index = (index + steps) % size;

		if (index < 0) {
			index = index + size;
		}

		dropdown.highlightedIndex.value = index;
	}

	function handleSearchInput(): void {
		dropdown.removeOptionHighlighting();
		dropdown.openDropdown();
	}

	function resetSearch(): void {
		search.value = "";
	}

	/**
	 * Wendet den Suchbegriff auf die Optionen an und gibt ein Array der passenden Optionen zurück, die dann im Dropdown angezeigt werden.
	 * Dabei werden auch deepSearchAttribute berücksichtigt
	 *
	 * @param searchText   der Suchbegriff
	 * @param manager	   der Manager für das Select mit den gefilterten Optionen
	 *
	 * @returns ein Array mit validen Optionen
	 */
	function getSearchedOptions(searchText: string, manager: BaseSelectManager<T>): List<T> {
		const filteredOptions = new ArrayList<T>();

		if (searchText === "") {
			return manager.filteredOptions;
		}

		for (const option of manager.filteredOptions) {
			if (stringContainsIgnoreCase(manager.getOptionText(option), searchText)) {
				filteredOptions.add(option);
				continue;
			}

			if (hasMatchInDeepSearchAttributes(option, searchText)) {
				filteredOptions.add(option);
			}
		}

		return filteredOptions;
	}

	/**
	 * Prüft, ob der Suchbegriff in einem der deepSearchAttribute vorkommt
	 *
	 * @param option       die Option, deren Attribute durchsucht werden sollen
	 * @param searchText   der Suchbegriff
	 *
	 * @returns ob der Suchbegriff in einem deepSearchAttribute vorkommt
	 */
	function hasMatchInDeepSearchAttributes(option: T, searchText: string): boolean {
		for (const attr of state.value.deepSearchAttributes) {
			const value = option[attr as keyof T];
			const stringValue = (value ?? '').toString();
			if (stringContainsIgnoreCase(stringValue, searchText)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Teilt den Text in Teile auf, die mit der Suchanfrage übereinstimmen. Dies wird dazu verwendet, die übereinstimmenden Teile farblich hervorzuheben.
	 *
	 * @param text   der Suchtext
	 *
	 * @returns ein Objekt-Array mit den Textparts bestehend aus dem String und der Angabe, ob dieser zum Suchtext passt oder nicht
	 */
	function splitTextIntoHits(text: string): { text: string, hit: boolean } [] {
		if ((search.value === "") || (!state.value.searchable)) {
			return [{ text, hit: false }];
		}

		const escapedSearch = search.value.replaceAll(/[-/\\^$*+?.()|[\]{}]/g, String.raw`\$&`);
		const regex = new RegExp(`(${escapedSearch})`, 'gi');

		return text.split(regex).map((part) => ({
			text: part,
			hit: part.toLowerCase() === search.value.toLowerCase(),
		}));

	}

	return {
		handleKeyDown,
		handleSearchInput,
		resetSearch,
		splitTextIntoHits,
		optionsMatchingSearch,
	};
}

function isPrintableChar(char: string): boolean {
	return /^[\x20-\x7E]$/.test(char);
}

function stringContainsIgnoreCase(string: string, substring: string): boolean {
	return string.toLocaleLowerCase("de-DE").includes(substring.toLocaleLowerCase("de-DE"));
}
