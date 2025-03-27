<template>
	<div ref="uiSelect" class="ui-select relative">
		<!-- Combobox -->
		<div :id="`uiSelectInput_${instanceId}`" ref="uiSelectCombobox" :tabindex="(searchable === true) ? -1 : 0"
			:role="(searchable === false) ? 'combobox' : undefined" :aria-labelledby="`uiSelectLabel_${instanceId}`"
			:aria-controls="(searchable === false) ? `uiSelectDropdown_${instanceId}` : undefined" aria-autocomplete="none"
			:aria-expanded="(searchable === false) ? showDropdown : undefined"
			:aria-disabled="((searchable === false) && (disabled === true)) ? true : undefined"
			:aria-activedescendant="((searchable === false) && (highlightedIndex !== -1)) ? `uiSelectOption_${highlightedIndex}_${instanceId}` : undefined"
			class="outline-none focus-within:ring-3 ring-ui w-full border px-2 py-1 rounded-md flex gap-1 min-w-fit min-h-10 hover:ring-ui-neutral hover:ring-3"
			:class="{ 'pointer-events-none border-ui-disabled': disabled, 'cursor-text': searchable, 'cursor-pointer': !searchable }"
			@click="handleComponentClick" @focus="handleComboboxFocus" @keydown="onKeyDown">
			<!-- Label -->
			<label v-if="label" :id="`uiSelectLabel_${instanceId}`"
				class="absolute left-2 transition-all duration-100 ease-in-out pointer-events-none flex items-center"
				:class="[ moveLabel ? 'text-xs font-medium -top-2 bg-ui px-1' : 'top-1/2 transform -translate-y-1/2']">
				<span v-if="statistics" class="icon icon-sm icon-ui-statistic i-ri-bar-chart-2-line mx-1" />
				<span :class="disabled ? 'text-ui-disabled' : moveLabel ? 'text-ui' : 'text-ui-secondary'">{{ label }}</span>
				<span v-if="selectionLimitText !== null" class="text-ui-secondary text-xs pl-1"> ({{ selectionLimitText }}) </span>
			</label>

			<!-- Wrapper für die aktuelle Selektion und das Suchfeld -->
			<div class="flex flex-wrap items-center gap-x-1 gap-y-1 flex-1 min-w-0 mt-1">
				<!-- Aktuelle Selektion (nur Mehrfachselektion) -->
				<template v-if="selectManager.multi && !selectManager.selected.isEmpty()">
					<span v-for="item in selectManager.selected" :key="selectManager.getOptionText(item)" tabindex="0"
						:aria-label="`Auswahl ${props.selectManager.getSelectionText(item)}`"
						class="px-2 py-1 rounded-md text-sm flex items-center overflow-hidden max-w-30 shrink-0"
						:class="[ disabled ? 'bg-ui-disabled text-ui-ondisabled' : 'bg-ui-selected text-ui-onselected']">

						<svws-ui-tooltip position="top" :indicator="false" class="truncate">
							<template #content>
								{{ selectManager.getSelectionText(item) }}
							</template>
							<div class="flex items-center justify-between w-full">
								<span class="truncate">
									{{ selectManager.getSelectionText(item) }}
								</span>
								<button @click="deselect($event, item)" class="hover:bg-ui rounded-sm flex ml-1 flex-shrink-0"
									@keydown.enter="deselect($event, item)"
									:aria-label="`Auswahl ${props.selectManager.getSelectionText(item)} löschen`">
									<span class="icon-sm icon i-ri-close-line" :class="[ disabled ? 'icon-ui-disabled' : 'icon-ui-onselected']" />
								</button>
							</div>
						</svws-ui-tooltip>
					</span>
				</template>

				<!-- Wrapper für das Such-Input und aktuelle Selektion (nur Einzelselektion) -->
				<div class="relative grid grid-cols-1 grid-rows-1 flex-1 min-w-15 shrink-0 order-last">
					<!-- Aktuelle Selektion (nur Einzelselektion) -->
					<div class="flex items-center overflow-hidden row-start-1 col-start-1">
						<svws-ui-tooltip position="top" :indicator="false" class="truncate">
							<template #content>
								{{ selectManager.getSelectionText(selectManager.selected.getFirst()) }}
							</template>
							<div v-if="!selectManager.multi && !selectManager.selected.isEmpty() && search === ''"
								class="truncate z-0 cursor-pointer"
								:class="[disabled ? 'text-ui-disabled' : (visualFocusOnCombobox || !domFocusOnCombobox) ? 'text-ui' : 'text-ui-secondary']">
								{{ selectManager.getSelectionText(selectManager.selected.getFirst()) }}
							</div>
						</svws-ui-tooltip>
					</div>
					<!-- Such-Input -->
					<input v-if="searchable" :id="`uiSelectinput_${instanceId}`" ref="uiSelectSearch" type="text" tabindex="0" role="combobox"
						aria-autocomplete="none" :aria-controls="`uiSelectDropdown_${instanceId}`" :aria-expanded="showDropdown"
						:aria-activedescendant="(highlightedIndex !== -1) ? `uiSelectOption_${highlightedIndex}_${instanceId}` : undefined"
						:aria-labelledby="`uiSelectLabel_${instanceId}`" :aria-disabled="(disabled === true) ? true : undefined"
						v-model="search" class="row-start-1 col-start-1 outline-none" @focus="handleComboboxFocus" @blur="handleBlur"
						@input="handleInput">
				</div>
			</div>


			<!-- Expand-Icon + Clear-Button -->
			<div class="ml-auto flex items-center">
				<button v-if="removable" type="button" :disabled aria-label="Auswahl löschen" @click="clearSelection" @keydown.enter="clearSelection"
					class="hover:bg-ui-hover flex ml-1 focus:ring-3 ring-ui outline-none rounded-sm">
					<span class="icon-sm icon i-ri-close-line" :class="[ disabled ? 'icon-ui-disabled' : 'icon-ui']" />
				</button>
				<span class="icon i-ri-expand-up-down-line cursor-pointer" :class="[ disabled ? 'icon-ui-disabled' : 'icon-ui']" />
			</div>
		</div>

		<!-- Dropdown -->
		<ul popover :aria-labelledby="`uiSelectLabel_${instanceId}`" :id="`uiSelectDropdown_${instanceId}`" ref="uiSelectDropdown" role="listbox"
			class="overflow-auto bg-ui select-none scrollbar-thin px-1 rounded-md border border-ui"
			:style="{ top: topPosition, left: leftPositionComputed, width: comboboxDimensions.width + 'px', maxHeight: maxHeight + 'px' }">
			<li v-if="selectManager.filtered.isEmpty()" class="cursor-not-allowed p-2 hover:bg-ui-hover text-ui-secondary italic">
				{{ "Keine passenden Einträge gefunden" }}
			</li>
			<li v-else :id="`uiSelectOption_${optionIndex}_${instanceId}`" v-for="(option, optionIndex) in selectManager.filtered"
				:key="selectManager.getOptionText(option)" role="option" :aria-selected="selectManager.isSelected(option)"
				class="cursor-pointer p-1 hover:bg-ui-hover hover:ring-3 hover:ring-ui-neutral hover:p-[4px] rounded-sm my-1"
				:class="[(selectManager.isSelected(option)) ? 'bg-ui-selected text-ui-onselected font-medium border border-ui-selected' : 'text-ui',
					{'bg-ui-hover ring-3 ring-ui-neutral ': (highlightedIndex === optionIndex)}]"
				@click="toggleSelection($event, option)">
				<template v-for="(part, index) in splitText(selectManager.getOptionText(option))" :key="index">
					<span v-if="part.matchIsSearch" class="bg-ui-selected">{{ part.text }}</span>
					<span v-else>{{ part.text }}</span>
				</template>
			</li>
		</ul>
	</div>
</template>

<script setup lang="ts" generic="T">

	import { computed, nextTick, onBeforeUnmount, onMounted, ref, useId, watch } from 'vue';
	import type { BaseSelectManager } from './BaseSelectManager';
	import type { List } from '../../../../../core/src/java/util/List';

	const props = withDefaults(defineProps<{
		label?: string;
		selectManager: BaseSelectManager<T>;
		searchable?: boolean;
		removable?: boolean;
		disabled?: boolean;
		statistics?: boolean;
		minOptions?: number | undefined;
		maxOptions?: number | undefined;
	}>(), {
		label: '',
		filter: false,
		removable: true,
		disabled: false,
		statistics: false,
		minOptions: undefined,
		maxOptions: undefined,
	});

	// ID zur Eindeutigen Kennzeichnung der Komponente. Wird für die HTML IDs benötigt
	const instanceId = crypto.randomUUID();
	const model = defineModel<List<T>>();

	// Suchtext für durchsuchbare Listen (searchable = true)
	const search = ref('');
	const showDropdown = ref(false);

	// Definiert, ob das Inputfeld gerade den visuellen bzw. den DOM-Fokus hat
	const visualFocusOnCombobox = ref(false);
	const domFocusOnCombobox = ref(false);

	// refs
	const uiSelect = ref<HTMLElement | null>(null);
	const uiSelectCombobox = ref<HTMLElement | null>(null);
	const uiSelectSearch = ref<HTMLElement | null>(null);
	const uiSelectDropdown = ref<HTMLDivElement | null>(null);

	// Dimensionen der Combobox für die Berechnung der Größe und Position des Dropdowns
	const comboboxDimensions = ref({
		top: 0,
		left: 0,
		height: 0,
		width: 0,
	});
	// Höhe des Browserfensters, um die Höhe des Dropdowns zu berechnen
	const windowHeight = ref(0);

	// Index des visuell hervorghobenen Dropdownlistenelements bei Tastennavigation
	const highlightedIndex = ref(-1);

	// Berechnet die top Position des Dropdowns abhängig von Position und Größe der Combobox
	const topPosition = computed (() => {
		if (flip.value) {
			const dropdownHeight = uiSelectDropdown.value?.scrollHeight !== undefined
				? Math.min(maxHeight.value, uiSelectDropdown.value.scrollHeight)
				: maxHeight.value;
			return `${comboboxDimensions.value.top - dropdownHeight - 2}px`;
		} else
			return `${comboboxDimensions.value.top + comboboxDimensions.value.height}px`;

	});

	// Berechnet die left Position des Dropdowns abhängig von der Position der Combobox
	const leftPositionComputed = computed(() => {
		return comboboxDimensions.value.left + 'px';
	});

	// Berechnet, ab wann das Dropdown über der Combobox positioniert wird, statt darunter
	const flip = computed (() => {
		if (!showDropdown.value)
			return false;
		const below = windowHeight.value - (comboboxDimensions.value.top + comboboxDimensions.value.height);
		const uppon = comboboxDimensions.value.top;
		if (uiSelectDropdown.value?.clientHeight !== undefined && below > 100)
			return false;
		return (below < uppon);
	});

	// Berechnet die maximale Höhe des Dropdowns, sodass dieses nie aus dem Viewport verschwindet. Sollte diese 235 übersteigen, dann wird die maxHeight auf
	// 235px gesetzt
	const maxHeight = computed(() => {
		let maxHeight = 0;
		if (flip.value)
			maxHeight = comboboxDimensions.value.top - 5;
		else
			maxHeight = windowHeight.value - (comboboxDimensions.value.top + comboboxDimensions.value.height) - 5;
		return (maxHeight > 235) ? 235 : maxHeight;
	});

	// Definiert, wann das Label der Combobox nach oben rutscht
	const moveLabel = computed(() => {
		return !props.selectManager.selected.isEmpty() || ((search.value !== '') && (props.searchable));
	});

	// Generiert den Text, der bei einer Multi-Selektion die Limitierung der Optinonen anzeigt
	const selectionLimitText = computed(() => {
		if (!props.selectManager.multi)
			return null;
		const min = (props.minOptions !== undefined) && (props.minOptions > 0) ? props.minOptions : null;
		const max = (props.maxOptions !== undefined) && (props.maxOptions > 0) ? props.maxOptions : null;

		if ((min === null) && (max === null))
			return null;
		if ((min !== null) && (max !== null))
			return (min === max) ? `${min} Option` : `${Math.min(min, max)} - ${Math.max(min, max)} Optionen`;

		return (min !== null) ? `min. ${min}` : `max. ${max}`;
	});

	onMounted(() => {
		document.addEventListener('click', handleClickOutside);
		window.addEventListener('resize', () => closeDropdown());

		// Das Dropdown schließt sich zb. automatisch durch Klicks außerhalb des Popovers. Diese Zustandsänderung wird hiermit synchronisiert
		uiSelectDropdown.value?.addEventListener("toggle", (event: Event) => {
			if ((event as ToggleEvent).newState === "closed") {
				showDropdown.value = false;
				highlightedIndex.value = -1;
				handleBlur();
			}
		});
	});

	onBeforeUnmount(() => {
		document.removeEventListener('click', handleClickOutside);
		window.removeEventListener('resize', () => closeDropdown());
	});

	// Passt die Scrollposition des Dropdowns an, falls der visuelle Fokus außerhalb des Sichtfelds landet
	watch(highlightedIndex, async () => {
		await nextTick(() => {
			if (highlightedIndex.value === -1)
				return;
			const highlightedElement = document.getElementById(`uiSelectOption_${highlightedIndex.value}_${instanceId}`);

			if (uiSelectDropdown.value && highlightedElement) {
				const listRect = uiSelectDropdown.value.getBoundingClientRect();
				const itemRect = highlightedElement.getBoundingClientRect();
				const ringSize = 6; // Zusätzlicher Abstand für den Auswahlring

				// Falls das Element unten rausgeht → Liste nach unten scrollen
				if (itemRect.bottom + ringSize > listRect.bottom) {
					uiSelectDropdown.value.scrollTop += (itemRect.bottom + ringSize) - listRect.bottom;
				}
				// Falls das Element oben rausgeht → Liste nach oben scrollen
				else if (itemRect.top - ringSize < listRect.top) {
					uiSelectDropdown.value.scrollTop -= listRect.top - (itemRect.top - ringSize);
				}
			}
		});
	});

	/**
	 * Teilt den Text in Teile auf, die mit der Suchanfrage übereinstimmen. Dies wird dazu verwendet, die übereinstimmenden Teile farblich hervorzuheben.
	 *
	 * @param text   der Suchtext
	 */
	function splitText(text: string) {
		if (search.value === "" || !props.searchable)
			return [{ text, matchIsSearch: false }];

		const escapedSearch = search.value.replace(/[-/\\^$*+?.()|[\]{}]/g, '\\$&');
		const regex = new RegExp(`(${escapedSearch})`, 'gi');

		return text.split(regex).map((part) => ({
			text: part,
			matchIsSearch: part.toLowerCase() === search.value.toLowerCase(),
		}));
	}

	/**
	 * Schließt das Dropdown bei einem Klick außerhalb der Komponente und resettet den Suchtext und den Fokus.
	 *
	 * @param event   das Klickevent
	 */
	function handleClickOutside(event: MouseEvent) {
		if (uiSelect.value && !uiSelect.value.contains(event.target as Node)) {
			closeDropdown(false);
			resetSearch();
			handleBlur();
		}
	}

	/**
	 * Je nachdem, ob das Select durchsuchbar ist (searchable), wird der Dom-Fokus auf die Combobox oder das darin befindliche Suchfeld gesetzt. Auch die Flags
	 * für den visuellen Fokus (visualFocusOnCombobox) und den Dom-Fokus (domFocusOnCombobox) werden auf true gesetzt.
	 */
	function handleComboboxFocus() {
		if (props.searchable)
			uiSelectSearch.value?.focus();
		else
			uiSelectCombobox.value?.focus();
		visualFocusOnCombobox.value = true;
		domFocusOnCombobox.value = true;
	}

	/**
	 * Entfernt die Flags für den Fokus von der Combobox.
	 */
	function handleBlur() {
		visualFocusOnCombobox.value = false;
		domFocusOnCombobox.value = false;
	}

	/**
	 * Beginnt eine Suche bei der Eingabe von Zeichen in das Suchfeld. Dabei wird außerdem auch das Dropdown geöffnet, falls dies nicht bereits der Fall ist.
	 */
	function handleInput() {
		props.selectManager.filter(search.value);
		openDropdown();
	}

	/**
	 * Wird die Komponente geklickt, dann wird automatisch das richtige Element fokussiert (Combobox oder Suchfeld je nach Wert von searchable). Dabei wird
	 * außerdem auch das Dropdown geöffnet oder geschlossen. Das ist insbesondere relavant, wenn der Benutzer in das Suchfeld schreiben möchte, aber dieses
	 * nicht beim Klick getroffen hat, weil es nicht die gesamte Combobox ausfüllt.
	 */
	function handleComponentClick() {
		handleComboboxFocus();
		toggleDropdown();
	}

	/**
	 * Toggelt die aktuelle Selektion. Bei searchable = true wird auch das Suchfeld zurückgesetzt
	 *
	 * @param event    das Klickevent. Wird benötigt, um Eventbubbling zu verhindern
	 * @param option   die Option, die selektiert bzw. deselektiert werden soll
	 */
	function toggleSelection(event: MouseEvent, option: T) {
		event.stopPropagation();
		if (!props.selectManager.multi) {
			closeDropdown();
		}

		props.selectManager.toggleSelection(option);
		resetSearch();
		model.value = props.selectManager.selected;
	};

	/**
	 * Leert die aktuelle Selektion und auch den Suchtext
	 *
	 * @param event   das Klickevent bzw. Keyboardevent. Wird benötigt, um Eventbubbling zu verhindern
	 */
	function clearSelection(event: MouseEvent | KeyboardEvent) {
		event.stopPropagation();
		closeDropdown();
		props.selectManager.clearSelection();
		resetSearch();
	}

	/**
	 * Deselektiert die gegebene Option
	 *
	 * @param event   das Klickevent bzw. Keyboardevent. Wird benötigt, um Eventbubbling zu verhindern
	 * @param option  die Option, die deselektiert werden soll
	 */
	function deselect(event: MouseEvent | KeyboardEvent, option: T) {
		event.stopPropagation();
		props.selectManager.deselect(option);
		model.value = props.selectManager.selected;
	}

	/**
	 * Öffnet oder schließt das Dropdown
	 */
	function toggleDropdown() {
		if (showDropdown.value)
			closeDropdown();
		else
			openDropdown();
	}

	/**
	 * Öffnet das Dropdown
	 */
	function openDropdown() {
		handleComboboxFocus();
		if (uiSelectDropdown.value === null || showDropdown.value)
			return;
		calculateDropdownDimensions();
		showDropdown.value = true;
		uiSelectDropdown.value.showPopover();
		visualFocusOnCombobox.value = false;
	}

	/**
	 * Berechnet Größe und Position des Dropdowns
	 */
	function calculateDropdownDimensions() {
		if (uiSelectCombobox.value === null)
			return 0;
		comboboxDimensions.value.top = uiSelectCombobox.value.getBoundingClientRect().top;
		comboboxDimensions.value.left = uiSelectCombobox.value.getBoundingClientRect().left;
		comboboxDimensions.value.height = uiSelectCombobox.value.getBoundingClientRect().height;
		comboboxDimensions.value.width = uiSelectCombobox.value.getBoundingClientRect().width;
		windowHeight.value = window.innerHeight;
	}

	/**
	 * Schließt das Dropdown. Es kann außerdem angegeben werden, ob dabei die Combobox wieder dokussiert werden soll.
	 *
	 * @param comboBoxFocus   true, wenn die Combobox fokussiert werden soll. Default ist true
	 */
	function closeDropdown(comboBoxFocus: boolean = true) {
		if (!showDropdown.value)
			return;
		showDropdown.value = false;
		uiSelectDropdown.value?.hidePopover();
		if (comboBoxFocus)
			handleComboboxFocus();
		else
			handleBlur();
	}

	/**
	 * Tastaturbedinung des Komponente
	 *
	 * @param event das Keyboardevent, das die gedrückte Taste enthält
	 */
	async function onKeyDown(event: KeyboardEvent) {
		if (props.disabled)
			return;
		if (!showDropdown.value) {
			// Alt + Arrow Down
			if (event.altKey && (event.key === "ArrowDown")) {
				highlightedIndex.value = -1;
				openDropdown();
			}
			// Arrow Down
			else if ((event.key === "ArrowDown")) {
				openDropdown();
				highlightedIndex.value = 0;
			}
			// Arrow Up
			else if (event.key === "ArrowUp") {
				openDropdown();
				highlightedIndex.value = props.selectManager.filtered.size() -1;
			}
			// Enter, Space
			else if ((event.key === "Enter") || (event.key === " "))
				openDropdown();
			// Home
			else if (event.key === "Home") {
				openDropdown();
				highlightedIndex.value = 0;
			}
			// End
			else if (event.key === "End") {
				openDropdown();
				highlightedIndex.value = props.selectManager.filtered.size() - 1;
			}
			// Escape
			else if ((event.key === "Escape") && props.searchable)
				resetSearch();
			// Printable Characters
			else if (isPrintableChar(event.key))
				// Wenn searchable = false wird der visuelle Fokus in der Liste verändert. Andernfalls wird nur das Suchfeld befüllt und es ist hier keine
				// gesonderte Behandlung nötig.
				if (!props.searchable) {
					openDropdown();
					handlePrintableKeyInput(event);
				}
		} else {
			// Enter, Space
			if ((event.key === "Enter") || (event.key === " ")) {
				selectFocussedOption();
				if (!props.selectManager.multi)
					closeDropdown();
			}
			// Tab (verhält sich wie Enter und Space, aber verschiebt automatisch auch den Focus auf das nächste Element und schließt immer die Liste)
			if (event.key === "Tab") {
				selectFocussedOption();
				closeDropdown();
			}
			// Arrow Down
			else if (event.key === "ArrowDown")
				await navigateList(1);
			// Alt + Arrow Up
			else if (event.altKey && event.key === "ArrowUp") {
				selectFocussedOption();
				if (!props.selectManager.multi)
					closeDropdown();
			}
			// Arrow Up
			else if (event.key === "ArrowUp")
				await navigateList(-1);
			// PageUp
			else if (event.key === "PageUp")
				await navigateList(-10);
			// PageDown
			else if (event.key === "PageDown")
				await navigateList(10);
			// Home (bezieht sich auf die Listenposition (searchable = false) oder auf das Suchfeld (searchable = true))
			if (event.key === "Home")
				if (props.searchable)
					highlightedIndex.value = -1; // Der Cursor wird versetzt un der visuelle Fokus dafür entfernt
				else
					highlightedIndex.value = 0; // Der visuelle Fokus wird versetzt

			// End (bezieht sich auf die Listenposition (searchable = false) oder auf das Suchfeld (searchable = true))
			else if (event.key === "End")
				if (props.searchable)
					highlightedIndex.value = -1; // Der Cursor wird automatisch versetzt und der visuelle Fokus dafür entfernt
				else
					highlightedIndex.value = props.selectManager.filtered.size() - 1;
			// Printable Characters
			else if (isPrintableChar(event.key))
				// Wenn searchable = false wird der visuelle Fokus in der Liste verändert. Andernfalls wird nur das Suchfeld befüllt und es ist hier keine
				// gesonderte Behandlung nötig.
				if (!props.searchable)
					handlePrintableKeyInput(event);
		}
	}

	/**
	 * Prüft, ob das Zeichen ein druckbares Zeichen ist
	 *
	 * @param char das zu prüfende Zeichen
	 */
	function isPrintableChar(char: string) {
		return /^[\x20-\x7E]$/.test(char);
	}

	/**
	 * Ermittelt die Option im Dropdown, die fokussiert werden soll basierend auf dem eingegebenen druckbaren Zeichen. Falls das Zeichen innerhalb von 0,5s
	 * wiederholt eingegeben wird, dann wird bei jeder Eingabe davon der nächste passende Eintrag fokussiert. Passend bedeutet dabei, dass der Eintrag mit dem
	 * eingebenen Zeichen beginnt. Wird innerhalb kurzer Zeit (0,5s) mehr als ein Zeichen eingebeben und zudem auch unterschiedliche Zeichen, dann wird nach
	 * einem Eintrag gesucht, der mit dem gesamten eingegebenen Begriff beginnt.
	 *
	 * @param event   das Keyboardevent, welches das eingebene Zeichen enthält.
	 */
	let timer: any = null;
	let lastInput: any = null;
	function handlePrintableKeyInput(event: KeyboardEvent) {
		if ((lastInput === event.key) && (highlightedIndex.value !== -1)) {
			if ((highlightedIndex.value + 1) >= props.selectManager.filtered.size()) {
				focusOptionThatStartsWith(lastInput);
				return;
			}

			const nextOption = props.selectManager.filtered.get(highlightedIndex.value + 1);
			if (props.selectManager.getOptionText(nextOption).toLowerCase().startsWith(event.key))
				highlightedIndex.value += 1;
			else
				focusOptionThatStartsWith(lastInput);
			return;
		}

		if (!props.searchable) {
			lastInput = event.key;
			// Wenn der Timer bereits läuft, füge den neuen Buchstaben an `search.value` an
			if (timer !== null)
				search.value += event.key;
			else {
				// Setzt den Timer, wenn noch keiner läuft und setzt `search.value` auf den aktuellen Buchstaben
				search.value = event.key;
				timer = setTimeout(() => {
					// Reset des Timers nach 0,5 Sekunden
					timer = null;
					resetSearch();
				}, 500);
			}
		}

		focusOptionThatStartsWith(search.value);
	}

	/**
	 * Ermittelt die Option, die mit dem übergebenen String beginnt und fokussiert diese visuell. Falls keine gefunden wurde, wird nichts fokussiert bzw. der
	 * vorhandene Fokus wird entfernt.
	 *
	 * @param start   String, mit dem die Option beginnen muss
	 */
	function focusOptionThatStartsWith(start: string) {
		const char = start.toLowerCase();
		const startIndex = (highlightedIndex.value === -1) ? 0 : highlightedIndex.value + 1;
		const optionsSize = props.selectManager.filtered.size();

		// Suche nach einer Option, die mit 'char' beginnt
		for (let i = startIndex; i < optionsSize + startIndex; i++) {
			const index = i % optionsSize; // Um auf den Index am Anfang der Liste zurückzukommen
			const option = props.selectManager.filtered.get(index);
			if (props.selectManager.getOptionText(option).toLowerCase().startsWith(char)) {
				highlightedIndex.value = index;
				return;
			}
		}
		// Wurde keine passende Option gefunden, dann wird der visuelle Fokus entfernt
		highlightedIndex.value = -1;
	}


	/**
	 * Setzt die Eingabe im Suchfeld zurück und aktualisiert die gefilterte Liste.
	 */
	function resetSearch() {
		search.value = "";
		props.selectManager.filter(search.value);
	}

	/**
	 * Navigiert den visuellen Fokus im Dropdown.
	 *
	 * @param direction   die Richtung der Navigation. Wenn negativ, dann wird rückwärts navigiert. Die Zahl gibt die Anzahl der Schritte an.
	 */
	async function navigateList (direction: number) {
		let newIndex = highlightedIndex.value + direction;
		if (newIndex >= props.selectManager.filtered.size())
			newIndex = 0;
		else if (newIndex < 0)
			newIndex = props.selectManager.filtered.size() - 1;
		highlightedIndex.value = newIndex;
	};


	/**
	 * Selektiert eine visuell fokussierte Option
	 */
	function selectFocussedOption () {
		if (highlightedIndex.value !== -1) {
			const option = props.selectManager.filtered.get(highlightedIndex.value);
			props.selectManager.toggleSelection(option);
			resetSearch();
		}
		handleComboboxFocus();
	}

</script>
