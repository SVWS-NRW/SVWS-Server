<template>
	<div ref="uiSelect" class="ui-select relative rounded-md text-base inline-flex h-fit w-full" v-bind="filteredAttributes">
		<!-- Combobox -->
		<div :id="`uiSelectInput_${instanceId}`" ref="uiSelectCombobox" :tabindex="(searchable === true) ? -1 : 0"
			:role="(searchable === false) ? 'combobox' : undefined" :aria-labelledby="`uiSelectLabel_${instanceId}`"
			:aria-controls="(searchable === false) ? `uiSelectDropdown_${instanceId}` : undefined" aria-autocomplete="none"
			:aria-expanded="(searchable === false) ? showDropdown : undefined" :aria-disabled="((searchable === false) && (disabled === true)) ? true : undefined"
			:aria-activedescendant="((searchable === false) && (highlightedIndex !== -1)) ? `uiSelectOption_${highlightedIndex}_${instanceId}` : undefined"
			class="relative outline-none focus-within:ring-2 ring-ui-neutral w-full rounded-md flex items-center gap-1 hover:ring-ui-neutral hover:ring-2 min-w-16 m-[0.2em]"
			:class="[headless ? 'pl-1' : 'border mt-[0.8em] pl-3 pr-1 min-h-9', disabled ? 'pointer-events-none border-ui-disabled' : borderColorClass, backgroundColorClass,
				{ 'pointer-events-none border-ui-disabled': disabled, 'cursor-text': searchable, 'cursor-pointer': !searchable}, comboboxFocusClass]" @click="handleComponentClick"
			@focus="handleComboboxFocus" @keydown="onKeyDown">
			<div :class="[headless ? 'py-0' : (manager.multi ? 'py-1': 'py-1')]" class="flex">
				<!-- Expand-Icon + Clear-Button headless -->
				<div v-if="headless" class="flex items-center">
					<span class="icon-sm i-ri-expand-up-down-line cursor-pointer" :class="[ disabled ? 'icon-ui-disabled' : iconColorClass]" />
					<button v-if="manager.removable" type="button" :disabled aria-label="Auswahl löschen" @click="clearSelection" @keydown.enter="clearSelection"
						class="hover:bg-ui-hover flex focus:ring-2 ring-ui-neutral outline-none rounded-sm">
						<span class="icon-sm i-ri-close-line" :class="[ disabled ? 'icon-ui-disabled' : iconColorClass]" />
					</button>
				</div>
				<!-- Label -->
				<label v-if="label && ((headless && !moveLabel) || !headless)" :id="`uiSelectLabel_${instanceId}`"
					class="absolute transition-all duration-100 ease-in-out pointer-events-none rounded left-2 whitespace-nowrap  max-w-fit flex gap-1 px-1 -translate-y-1/2"
					:class="[ moveLabel ? 'absolute -top-0.5 text-xs' : 'absolute top-1/2 font-normal',
						manager.removable ? (moveLabel ? 'right-2' : 'right-11') : (moveLabel ? 'right-2' : 'right-6'),
						headless ? (manager.removable ? 'left-10' : 'left-6') : '', backgroundColorClass, textColorClass]">
					<span v-if="statistics" class="cursor-pointer flex">
						<svws-ui-tooltip position="right">
							<span class="icon i-ri-bar-chart-2-line pointer-events-auto" :class="[disabled ? 'icon-ui-disabled' : 'icon-ui-statistic']" />
							<template #content>
								Relevant für die Statistik
							</template>
						</svws-ui-tooltip>
					</span>

					<span class="leading-none content-center overflow-hidden truncate h-5"
						:class="disabled ? '' : moveLabel ? textColorClass : getSecondaryTextColor(textColorClass)">
						{{ label }}
					</span>
					<span v-if="selectionLimitText !== null" class="h-5 leading-none content-center" :class="[getSecondaryTextColor(textColorClass)]">
						<span>({{ selectionLimitText }})</span>
					</span>
					<span v-if="required" class="cursor-pointer flex items-end" aria-hidden>
						<span class="icon-xs i-ri-asterisk font-normal relative -top-2" :class="iconColorClass" />
					</span>
					<span v-if="required" class="sr-only">erforderlich</span>
					<span v-if="(isValid === false) && (!required || manager.hasSelection())" class="cursor-pointer flex items-end">
						<span class="icon i-ri-alert-line" :class="iconColorClass" />
					</span>
					<span v-if="isValidatorValid === false" class="cursor-pointer">
						<svws-ui-tooltip position="right">
							<span class="pointer-events-auto">
								<template v-if="!validator().getFehler().isEmpty()">
									<span class="icon i-ri-alert-fill icon-ui-danger" v-if="validator().getFehlerart() === ValidatorFehlerart.MUSS" />
									<span class="icon i-ri-error-warning-fill icon-ui-caution" v-if="validator().getFehlerart() === ValidatorFehlerart.KANN" />
									<span class="icon i-ri-question-fill icon-ui-warning" v-if="validator().getFehlerart() === ValidatorFehlerart.HINWEIS" />
								</template>
							</span>
							<template #content>
								<template
									v-if="(validator !== undefined) && (!validator().getFehler().isEmpty()) && (validator().getFehlerart() !== ValidatorFehlerart.UNGENUTZT)">
									<div class="text-headline-sm text-center pt-1"> Validatorfehler </div>
									<div v-for="fehler in validator().getFehler()" :key="fehler.getFehlermeldung() ?? '--'" class="pt-2 pb-2">
										<div class="rounded-sm pl-2" :class="{
											'bg-ui-danger': (validator().getFehlerart() === ValidatorFehlerart.MUSS),
											'bg-ui-caution': (validator().getFehlerart() === ValidatorFehlerart.KANN),
											'bg-ui-warning': (validator().getFehlerart() === ValidatorFehlerart.HINWEIS)}">
											{{ fehler.getFehlerart() }}
										</div>
										<div class="pl-2"> {{ fehler.getFehlermeldung() }} </div>
									</div>
								</template>
								<template v-else>
									<div class="text-headline-sm text-center"> Relevant für die Statistik </div>
								</template>
							</template>
						</svws-ui-tooltip>
					</span>
				</label>

				<!-- Wrapper für die aktuelle Selektion und das Suchfeld -->
				<div class="flex flex-wrap items-center gap-x-1 flex-1 min-w-0">
					<!-- Aktuelle Selektion (nur Mehrfachselektion) -->
					<template v-if="manager.multi">
						<span v-for="item in manager.selected" :key="manager.getSelectionText(item)" tabindex="0"
							:aria-label="`Auswahl ${props.manager.getSelectionText(item)}`"
							class="px-2 rounded-md text-sm flex items-center overflow-hidden max-w-30 shrink-0 border mt-1"
							:class="[ disabled ? 'bg-ui-disabled text-ui-ondisabled border-ui-disabled' : 'bg-ui-selected text-ui-onselected border-ui-selected']">

							<svws-ui-tooltip position="top" :indicator="false" class="truncate">
								<template #content>
									{{ manager.getSelectionText(item) }}
								</template>
								<div class="flex items-center justify-between w-full">
									<span class="truncate">
										{{ manager.getSelectionText(item) }}
									</span>
									<button v-if="manager.removable || manager.selected.size() > 1" @click="deselect($event, item)"
										class="hover:bg-ui rounded-sm flex ml-1 flex-shrink-0" @keydown.enter="deselect($event, item)"
										:aria-label="`Auswahl ${props.manager.getSelectionText(item)} löschen`">
										<span class="icon-sm i-ri-close-line" :class="[ disabled ? 'icon-ui-disabled' : 'icon-ui-onselected']" />
									</button>
								</div>
							</svws-ui-tooltip>
						</span>
					</template>

					<!-- Wrapper für das Such-Input und aktuelle Selektion (nur Einzelselektion) -->
					<div v-if="(manager.multi === false) || searchable" class="relative grid grid-cols-1 grid-rows-1 flex-1 min-w-5 order-last text-base">
						<!-- Aktuelle Selektion (nur Einzelselektion) -->
						<div v-if="manager.multi === false" class="flex items-center overflow-hidden row-start-1 col-start-1">
							<svws-ui-tooltip position="top" :indicator="false" class="truncate">
								<template #content>
									{{ manager.getSelectionText(manager.selected) }}
								</template>
								<div v-if="!manager.multi && (manager.selected !== null) && (search === '')"
									class="truncate z-0 cursor-pointer font-medium inline-block align-middle leading-none h-5 mt-1"
									:class="[(visualFocusOnCombobox || !domFocusOnCombobox) ? textColorClass : getSecondaryTextColor(textColorClass)]">
									{{ manager.getSelectionText(manager.selected) }}
								</div>
							</svws-ui-tooltip>
						</div>
						<!-- Such-Input -->
						<input v-if="searchable" :id="`uiSelectinput_${instanceId}`" ref="uiSelectSearch" type="text" tabindex="0" role="combobox" aria-autocomplete="none"
							:aria-controls="`uiSelectDropdown_${instanceId}`" :aria-expanded="showDropdown"
							:aria-activedescendant="(highlightedIndex !== -1) ? `uiSelectOption_${highlightedIndex}_${instanceId}` : undefined"
							:aria-labelledby="`uiSelectLabel_${instanceId}`" :aria-disabled="(disabled === true) ? true : undefined" v-model="search"
							class="row-start-1 col-start-1 outline-none font-normal h-5" :class="searchInputFocusClass" @focus="handleComboboxFocus"
							@blur="handleBlur" @input="handleInput">
					</div>
				</div>
			</div>

			<!-- Expand-Icon + Clear-Button -->
			<div v-if="!headless" class="ml-auto flex items-center h-fit">
				<button v-if="manager.removable" type="button" :disabled aria-label="Auswahl löschen" @click="clearSelection" @keydown.enter="clearSelection"
					class="hover:bg-ui-hover flex focus:ring-2 ring-ui-neutral outline-none rounded-sm">
					<span class="icon-sm i-ri-close-line" :class="[ disabled ? 'icon-ui-disabled' : iconColorClass]" />
				</button>
				<span class="icon i-ri-expand-up-down-line cursor-pointer" :class="[ disabled ? 'icon-ui-disabled' : iconColorClass]" />
			</div>
		</div>

		<!-- Dropdown -->
		<ul popover :aria-labelledby="`uiSelectLabel_${instanceId}`" :id="`uiSelectDropdown_${instanceId}`" ref="uiSelectDropdown" role="listbox"
			class="overflow-auto bg-ui select-none scrollbar-thin p-1 rounded-md border border-ui font-normal"
			:style="{ top: topPosition, left: leftPositionComputed, width: width + 'px', maxHeight: maxHeight + 'px' }">
			<li v-if="manager.filteredOptions.isEmpty()" class="cursor-not-allowed p-2 hover:bg-ui-hover text-ui-secondary italic">
				{{ "Keine passenden Einträge gefunden" }}
			</li>
			<li v-else :id="`uiSelectOption_${optionIndex}_${instanceId}`" v-for="(option, optionIndex) in manager.filteredOptions" :key="optionIndex"
				role="option" :aria-selected="manager.isSelected(option)"
				class="cursor-pointer m-1 p-1 hover:bg-ui-hover hover:inset-ring-2 hover:inset-ring-ui-neutral rounded-lg" :class="[(manager.isSelected(option)) ? 'bg-ui-selected text-ui-onselected font-medium border border-ui-selected' : 'text-ui',
					{'bg-ui-hover inset-ring-2 inset-ring-ui-neutral ': (highlightedIndex === optionIndex)}]" @click="toggleSelection($event, option)">
				<template v-for="(part, index) in splitText(manager.getOptionText(option))" :key="index">
					<span v-if="part.matchIsSearch" class="bg-ui-selected">{{ part.text }}</span>
					<span v-else>{{ part.text }}</span>
				</template>
			</li>
		</ul>
	</div>
</template>

<script setup lang="ts" generic="T, V extends Validator">

	import { computed, nextTick, onBeforeUnmount, onMounted, ref, useAttrs, watch } from 'vue';
	import { useElementBounding, useWindowSize } from '@vueuse/core';
	import { BaseSelectManager } from './selectManager/BaseSelectManager';
	import type { Validator } from '../../../../../core/src';
	import { ValidatorFehlerart } from '../../../../../core/src';
	import { SearchSelectFilter } from './filter/SearchSelectFilter';

	const props = withDefaults(defineProps<{
		label?: string;
		manager?: BaseSelectManager<T>;
		searchable?: boolean;
		required?: boolean;
		disabled?: boolean;
		statistics?: boolean;
		headless?: boolean;
		minOptions?: number | undefined;
		maxOptions?: number | undefined;
		validator?: () => V;
		doValidate?: (validator: V, value: string | null) => boolean;
	}>(), {
		label: '',
		manager: () => new BaseSelectManager<T>(),
		searchable: false,
		required: false,
		disabled: false,
		statistics: false,
		headless: false,
		minOptions: undefined,
		maxOptions: undefined,
		validator: undefined,
		doValidate: (validator: V, value: string | null) : boolean => validator.run(),
	});

	/** Die Vererbung der Attribute wird abgestellt, damit diese manuell an die richtigen Stellen weitergeleitet werden kann */
	defineOptions({ inheritAttrs: false });
	const attrs = useAttrs();

	/**
	 * Extrahiert alle gesetzten Klassen von außerhalb und setzt diese an den Rootknoten. Icon-Farben werden jedoch herausgefiltert, da diese den Hintergrund
	 * beeinflussen. Ebenso Fokusklassen, da diese an die Combobox bzw. das SearchInput weitergeleitet werden müssen.
	 */
	const filteredAttributes = computed (() => {
		const result = { ...attrs };

		const stringClass = result.class;

		if (typeof stringClass === 'string')
			result.class = stringClass.split(' ').filter(c => !c.startsWith('icon-ui') && (c !== 'contentFocusField')
				&& (c !== 'subNavigationFocusField')).join(' ');

		return result;
	});

	/**
	 * Berechnet die Hintergrundfarbe der Combobox. Wird von außen eine gesetzt, dann wird diese verwendet. Andernfalls bg-ui.
	 */
	const backgroundColorClass = computed(() => {
		const classString = attrs.class;
		if (typeof classString !== "string")
			return props.headless ? "" : "bg-ui";
		const classes = classString.split(' ');

		// Nach erster bg-ui* Klasse suchen
		const match = classes.find(c => c.startsWith('bg-ui'));
		return match ?? (props.headless ? "" : "bg-ui");
	});

	/**
	 * Berechnet die Textfarbe. Wird von außen eine gesetzt, dann wird diese verwendet. Andernfalls text-ui. Für Sekundärfarben wird dann automatisch
	 * die sekundäre Variante der übergebenen Farbe verwendet.
	 */
	const textColorClass = computed(() => {
		if (props.disabled === true)
			return "text-ui-disabled";
		if (isValid.value === false)
			return "text-ui-danger";
		if (isValidatorValid.value === false)
			switch (props.validator!().getFehlerart()) {
				case (ValidatorFehlerart.HINWEIS):
					return "text-ui-warning";
				case (ValidatorFehlerart.KANN):
					return "text-ui-caution";
				case (ValidatorFehlerart.MUSS):
					return "text-ui-danger";
			}

		if (props.required && props.manager.hasSelection() === false)
			return "text-ui-danger";
		const classString = attrs.class;
		if (typeof classString !== "string")
			return "text-ui";
		const classes = classString.split(' ');

		// Nach erster text-ui* Klasse suchen
		const match = classes.find(c => c.startsWith('text-ui'));
		return match ?? 'text-ui';
	});

	/**
	 * Berechnet die Iconfarbe. Wird von außen eine gesetzt, dann wird diese verwendet. Andernfalls icon-ui.
	 */
	const iconColorClass = computed(() => {
		if (props.disabled === true)
			return "icon-ui-disabled";
		if (isValid.value === false)
			return "icon-ui-danger";
		if (isValidatorValid.value === false)
			switch (props.validator!().getFehlerart()) {
				case (ValidatorFehlerart.HINWEIS):
					return "icon-ui-warning";
				case (ValidatorFehlerart.KANN):
					return "icon-ui-caution";
				case (ValidatorFehlerart.MUSS):
					return "icon-ui-danger";
			}
		const classString = attrs.class;
		if (typeof classString !== "string")
			return "icon-ui";
		const classes = classString.split(' ');

		// Nach erster icon-ui* Klasse suchen
		const match = classes.find(c => c.startsWith('icon-ui'));
		return match ?? 'icon-ui';
	});

	/**
	 * Berechnet die Borderfarbe der Combobox. Wird von außen eine gesetzt, dann wird diese verwendet. Andernfalls bg-ui-neutral.
	 */
	const borderColorClass = computed(() => {
		if (isValid.value === false)
			return "border-ui-danger";
		if (isValidatorValid.value === false)
			switch (props.validator!().getFehlerart()) {
				case (ValidatorFehlerart.HINWEIS):
					return "border-ui-warning";
				case (ValidatorFehlerart.KANN):
					return "border-ui-caution";
				case (ValidatorFehlerart.MUSS):
					return "border-ui-danger";
			}

		const classString = attrs.class;
		if (typeof classString !== "string")
			return "border-ui";
		const classes = classString.split(' ');

		// Nach erster border-ui* Klasse suchen
		const match = classes.find(c => c.startsWith('border-ui'));
		return match ?? 'border-ui';
	});



	// ID zur Eindeutigen Kennzeichnung der Komponente. Wird für die HTML IDs benötigt
	const instanceId = crypto.randomUUID();
	const model = defineModel<any>();

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

	// Größen und Positionen zur Berechnung des Dropdowns
	const { top, left, width, height } = useElementBounding(uiSelectCombobox);
	const { height: windowHeight } = useWindowSize();

	// Index des visuell hervorghobenen Dropdownlistenelements bei Tastennavigation
	const highlightedIndex = ref(-1);

	// Berechnet die top Position des Dropdowns abhängig von Position und Größe der Combobox
	const topPosition = computed (() => {
		if (flip.value) {
			const dropdownHeight = uiSelectDropdown.value?.scrollHeight !== undefined
				? Math.min(maxHeight.value, uiSelectDropdown.value.scrollHeight)
				: maxHeight.value;
			return `${top.value - dropdownHeight - 2}px`;
		} else
			return `${top.value + height.value + 3}px`;

	});

	// Berechnet die left Position des Dropdowns abhängig von der Position der Combobox
	const leftPositionComputed = computed(() => {
		return left.value + 'px';
	});

	// Berechnet, ab wann das Dropdown über der Combobox positioniert wird, statt darunter
	const flip = computed (() => {
		if (!showDropdown.value)
			return false;
		const below = windowHeight.value - (top.value + height.value);
		const uppon = top.value;
		if (uiSelectDropdown.value?.clientHeight !== undefined && below > 100)
			return false;
		return (below < uppon);
	});

	// Berechnet die maximale Höhe des Dropdowns, sodass dieses nie aus dem Viewport verschwindet. Sollte diese 235 übersteigen, dann wird die maxHeight auf
	// 235px gesetzt
	const maxHeight = computed(() => {
		let maxHeight = 0;
		if (flip.value)
			maxHeight = top.value - 5;
		else
			maxHeight = windowHeight.value - (top.value + height.value) - 5;
		return (maxHeight > 235) ? 235 : maxHeight;
	});

	// Definiert, wann das Label der Combobox nach oben rutscht
	const moveLabel = computed(() => {
		const selected = props.manager.selected;
		const hasSelection = (props.manager.multi) ? (selected.size() > 0) : ((selected !== null) && (selected !== undefined));

		return hasSelection || (search.value !== '' && props.searchable)
	})

	// Generiert den Text, der bei einer Multi-Selektion die Limitierung der Optinonen anzeigt
	const selectionLimitText = computed(() => {
		if (!props.manager.multi)
			return null;
		const min = (props.minOptions !== undefined) && (props.minOptions > 0) ? props.minOptions : null;
		const max = (props.maxOptions !== undefined) && (props.maxOptions > 0) ? props.maxOptions : null;

		if ((min === null) && (max === null))
			return null;
		if ((min !== null) && (max !== null))
			return (min === max) ? `${min} Option` : `${Math.min(min, max)} - ${Math.max(min, max)} Optionen`;

		return (min !== null) ? `min. ${min}` : `max. ${max}`;
	});

	// Generiert die focusClass für die Combobox, falls eine gesetzt ist. Diese wird nur an die Combobox gesetzt, wenn das Select nicht searchable ist.
	const comboboxFocusClass = computed(() => {
		const result = { ...attrs };
		const stringClass = result.class;
		if (typeof stringClass === 'string')
			if (!props.searchable && stringClass.includes('contentFocusField'))
				return 'contentFocusField';
			else if (!props.searchable && stringClass.includes('subNavigationFocusField'))
				return 'subNavigationFocusField';
		return '';
	});

	// Generiert die focusClass für das Searchinput, falls eine gesetzt ist. Diese wird nur an das Searchinput gesetzt, wenn das Select searchable ist.
	const searchInputFocusClass = computed(() => {
		const result = { ...attrs };
		const stringClass = result.class;
		if (typeof stringClass === 'string')
			if (props.searchable && stringClass.includes('contentFocusField'))
				return 'contentFocusField';
			else if (props.searchable && stringClass.includes('subNavigationFocusField'))
				return 'contentFocusField';
		return '';
	});

	onMounted(() => {
		const result = { ...attrs };
		const stringClass = result.class;
		if (typeof stringClass === 'string') {
			if (props.searchable === true && stringClass.includes('contentFocusField'))
				uiSelectSearch.value?.classList.add('contentFocusField');
			else if (props.searchable === false && stringClass.includes('contentFocusField'))
				uiSelectCombobox.value?.classList.add('contentFocusField');
		}
		// Wenn das Select durchsuchbar ist, wird ein SearchSelectFilter hinzugefügt. Sollte bereits einer existieren, wird nur der neue Suchbegriff gesetzt.
		if (props.searchable) {
			const tmpSearchFilter = props.manager.getFilterByKey("search") as SearchSelectFilter<T> | null;
			if (tmpSearchFilter !== null) {
				tmpSearchFilter.search = search.value;
				props.manager.updateFilteredOptions(tmpSearchFilter);
			}	else {
				props.manager.addFilter(new SearchSelectFilter("search", search.value, (item: T) => props.manager.getOptionText(item)));
			}
		}

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

		if (props.manager.hasSelection() === false)
			props.manager.selected = model.value;

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

	watch(
		() => model.value,
		(newSelection) => {
			props.manager.selected = newSelection;
		},
		{ deep: true }
	);

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
		updateSearchFilter();
		openDropdown();
	}

	function updateSearchFilter() {
		if (!props.searchable)
			return;
		let searchFilter = props.manager.getFilterByKey("search") as SearchSelectFilter<T> | null;
		if (searchFilter === null)
			searchFilter = new SearchSelectFilter("search", search.value, (item: T) => props.manager.getOptionText(item));
		else {
			searchFilter = props.manager.getFilterByKey("search") as SearchSelectFilter<T>;
			searchFilter.search = search.value;
		}
		props.manager.updateFilteredOptions(searchFilter);
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
		if (!props.manager.multi) {
			closeDropdown();
		}

		props.manager.toggleSelection(option);
		resetSearch();
		model.value = props.manager.selected;

	};

	/**
	 * Leert die aktuelle Selektion und auch den Suchtext
	 *
	 * @param event   das Klickevent bzw. Keyboardevent. Wird benötigt, um Eventbubbling zu verhindern
	 */
	function clearSelection(event: MouseEvent | KeyboardEvent) {
		event.stopPropagation();
		closeDropdown();
		props.manager.clearSelection();
		resetSearch();
		model.value = props.manager.selected;
	}

	/**
	 * Deselektiert die gegebene Option
	 *
	 * @param event   das Klickevent bzw. Keyboardevent. Wird benötigt, um Eventbubbling zu verhindern
	 * @param option  die Option, die deselektiert werden soll
	 */
	function deselect(event: MouseEvent | KeyboardEvent, option: T) {
		event.stopPropagation();
		props.manager.deselect(option);
		model.value = props.manager.selected;
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
		showDropdown.value = true;
		uiSelectDropdown.value.showPopover();
		visualFocusOnCombobox.value = false;
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
	 * Tastaturbedinung des Komponente. Sie orierntiert sich an den Vorgaben von https://www.w3.org/WAI/ARIA/apg/patterns/combobox/
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
				highlightedIndex.value = props.manager.filteredOptions.size() -1;
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
				highlightedIndex.value = props.manager.filteredOptions.size() - 1;
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
				if (!props.manager.multi)
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
				if (!props.manager.multi)
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
					highlightedIndex.value = props.manager.filteredOptions.size() - 1;
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
			if ((highlightedIndex.value + 1) >= props.manager.filteredOptions.size()) {
				focusOptionThatStartsWith(lastInput);
				return;
			}

			const nextOption = props.manager.filteredOptions.get(highlightedIndex.value + 1);
			if (props.manager.getOptionText(nextOption).toLowerCase().startsWith(event.key))
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
		const optionsSize = props.manager.filteredOptions.size();

		// Suche nach einer Option, die mit 'char' beginnt
		for (let i = startIndex; i < optionsSize + startIndex; i++) {
			const index = i % optionsSize; // Um auf den Index am Anfang der Liste zurückzukommen
			const option = props.manager.filteredOptions.get(index);
			if (props.manager.getOptionText(option).toLowerCase().startsWith(char)) {
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
		if (search.value === "")
			return;
		search.value = "";
		updateSearchFilter();
	}

	/**
	 * Navigiert den visuellen Fokus im Dropdown.
	 *
	 * @param direction   die Richtung der Navigation. Wenn negativ, dann wird rückwärts navigiert. Die Zahl gibt die Anzahl der Schritte an.
	 */
	async function navigateList (direction: number) {
		let newIndex = highlightedIndex.value + direction;
		if (newIndex >= props.manager.filteredOptions.size())
			newIndex = 0;
		else if (newIndex < 0)
			newIndex = props.manager.filteredOptions.size() - 1;
		highlightedIndex.value = newIndex;
	};


	/**
	 * Selektiert eine visuell fokussierte Option
	 */
	function selectFocussedOption () {
		if (highlightedIndex.value !== -1) {
			const option = props.manager.filteredOptions.get(highlightedIndex.value);
			props.manager.toggleSelection(option);
			resetSearch();
		}
		handleComboboxFocus();
	}


	/**
	 * Generiert die passende sekundäre Textfarbe für gesetzte Textfarben von außen.
	 *
	 * @param color    die Textfarbe, dessen Sekundärfarbe ermittler werden soll.
	 */
	function getSecondaryTextColor (color: string) {
		if (color.startsWith("text-uistatic"))
			return "text-uistatic-25";
		switch (color) {
			case "text-ui":
				return "text-ui-secondary";
			case "text-ui-brand":
				return "text-ui-brand-secondary"
			case "text-ui-statistic":
				return "text-ui-statistic-secondary"
			case "text-ui-selected":
				return "text-ui-selected-secondary"
			case "text-ui-danger":
				return "text-ui-danger-secondary"
			case "text-ui-success":
				return "text-ui-success-secondary"
			case "text-ui-warning":
				return "text-ui-warning-secondary"
			case "text-ui-caution":
				return "text-ui-caution-secondary"
			case "text-ui-neutral":
				return "text-ui-neutral-secondary"
			case "text-ui-disabled":
				return "text-ui-disabled-secondary"
			case "text-ui-onbrand":
				return "text-ui-onbrand-secondary"
			case "text-ui-onstatistic":
				return "text-ui-onstatistic-secondary"
			case "text-ui-onselected":
				return "text-ui-onselected-secondary"
			case "text-ui-ondanger":
				return "text-ui-ondanger-secondary"
			case "text-ui-onsuccess":
				return "text-ui-onsuccess-secondary"
			case "text-ui-onwarning":
				return "text-ui-onwarning-secondary"
			case "text-ui-oncaution":
				return "text-ui-oncaution-secondary"
			case "text-ui-onneutral":
				return "text-ui-onneutral-secondary"
			case "text-ui-ondisabled":
				return "text-ui-ondisabled-secondary"
			default:
				return "text-ui-secondary";
		}
	}

	/**
	 * Prüft, ob die Eingaben valide sind
	 */
	const isValid = computed((): boolean => {
		if (props.required && props.manager.hasSelection() === false)
			return false;
		if (minOptionsValid.value === false)
			return false;
		if (maxOptionsValid.value === false)
			return false;
		return true;
	});

	/**
	 * Prüft, ob Eingaben abhänig von den Validatoren valide sind
	 */
	const isValidatorValid = computed((): boolean =>
		(props.validator !== undefined) ? props.doValidate(props.validator(), model.value) : true
	);

	/**
	 * Prüft, ob die gewählte Optionenanzahl im Falle von MultiSelects dem Minimum entspricht
	 */
	const minOptionsValid = computed((): boolean => {
		if (props.manager.multi === false)
			return true;
		if ((props.minOptions === undefined) || ((props.manager.hasSelection() === false) && (props.minOptions <= 0)))
			return true;
		return (props.manager.hasSelection() === true) && (props.manager.selected.size() >= props.minOptions);
	})

	/**
	 * Prüft, ob die gewählte Optionenanzahl im Falle von MultiSelects dem Maximum entspricht
	 */
	const maxOptionsValid = computed((): boolean => {
		if ((props.maxOptions === undefined) || ((props.manager.hasSelection() === false) && (props.maxOptions <= 0)))
			return true;
		return (props.manager.selected.size() <= props.maxOptions);
	})

</script>
