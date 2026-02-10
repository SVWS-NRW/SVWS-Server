import { ref, type ComputedRef, type Ref } from "vue";
import type { UiSelectDropdown, UiSelectHTMLElements, UiSelectState } from "../manager/UiSelectTypes";

export function useUiSelectFocusHandler<T>(
	state: ComputedRef<UiSelectState<T>>,
	elements: UiSelectHTMLElements,
	resetSearch: () => void,
	dropdown: UiSelectDropdown<T>
): {
	focusOnInput: Ref<boolean>,
	focusSelect: () => void,
	onFocusOut: () => void,
	unfocusInput: () => void,
	handleComponentClick: () => void,
} {
	// Definiert, ob das Inputfeld gerade den Fokus hat (im Falle von searchable === true; bei searchable === false gibt es kein Inputfeld)
	const focusOnInput = ref(false);

	/**
	 * Je nachdem, ob das Select durchsuchbar ist (searchable = true), wird der Dom-Fokus auf die Combobox oder das darin befindliche Suchfeld gesetzt.
	 */
	function focusSelect(): void {
		if (state.value.readonly || state.value.disabled) {
			return;
		}

		if (state.value.searchable) {
			elements.uiSelectSearch.value?.focus();
			focusOnInput.value = true;
		} else {
			elements.uiSelectCombobox.value?.focus();
		}

		document.addEventListener('click', handleClickOutside);
	}

	function onFocusOut(): void {
		// Bei einem Fokuswechsel ist der Fokus kurz auf dem Body. Das muss hiermit abgewartet werden
		requestAnimationFrame(() => {
			if (!((elements.uiSelectCombobox.value?.contains(document.activeElement)) ?? false)) {
				deactivateSelect();
			}
		});
	}

	function unfocusInput(): void {
		focusOnInput.value = false;
	}

	/**
	 * Wird die Komponente geklickt, dann wird automatisch das richtige Element fokussiert (Combobox oder Suchfeld je nach Wert von searchable). Dabei wird
	 * außerdem auch das Dropdown geöffnet oder geschlossen. Das ist insbesondere relevant, wenn der Benutzer in das Suchfeld schreiben möchte, aber dieses
	 * nicht beim Klick getroffen hat, weil es nicht die gesamte Combobox ausfüllt.
	 */
	function handleComponentClick(): void {
		focusSelect();
		dropdown.toggleDropdown();
	}

	function handleClickOutside(event: MouseEvent): void {
		if (!((elements.uiSelect.value?.contains(event.target as Node)) ?? false)) {
			deactivateSelect();
		}
	}

	function deactivateSelect(): void {
		dropdown.closeDropdown();
		resetSearch();
		document.removeEventListener('click', handleClickOutside);
	}

	return {
		focusOnInput,
		focusSelect,
		onFocusOut,
		unfocusInput,
		handleComponentClick,
	};
}
