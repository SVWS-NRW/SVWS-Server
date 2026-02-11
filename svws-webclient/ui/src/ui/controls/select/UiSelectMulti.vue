<template>
	<div ref="uiSelect" @focusout="onFocusOut" class="ui-select-multi relative rounded-md text-base inline-flex h-fit w-full group" v-bind="filteredHtmlAttributes">
		<!-- Combobox -->
		<div :id="`uiSelectMulti_${state.instanceId}`" ref="uiSelectCombobox" :tabindex="comboboxTabindex" :role="comboboxRole" v-bind="comboboxAriaAttrs"
			:class="[comboboxClasses, { [focusClass]: !props.searchable }, 'ui-select-multi--combobox relative outline-none ring-ui-neutral w-full rounded-md flex items-center gap-1 min-w-16 m-[0.2em] select-none group-focus-within:ring-2 hover:ring-2']"
			@click.stop="handleComponentClick" @focus="focusSelect" @keydown.stop="handleKeyDown">
			<div :class="[headlessPadding, 'flex']">
				<!-- Expand-Icon + Clear-Button headless -->
				<div v-if="headless && !readonly" class="ui-select-multi--icons-left flex items-center">
					<span :class="[iconColorClass, 'icon-sm i-ri-expand-up-down-line cursor-pointer']" />
					<button v-if="removable" type="button" :disabled aria-label="Auswahl löschen" @click.stop="clearSelection" @keydown.enter.stop="clearSelection"
						class="hover:bg-ui-hover flex focus:ring-2 ring-ui-neutral outline-none rounded-sm">
						<span :class="[iconColorClass, 'icon-sm i-ri-close-line']" />
					</button>
				</div>
				<!-- Label -->
				<div v-if="showLabel"
					:class="[labelClasses, 'ui-select-multi--label absolute transition-all duration-100 ease-in-out pointer-events-none rounded left-2 whitespace-nowrap max-w-fit flex justify-center items-center gap-1 px-1 -translate-y-1/2']">
					<span v-if="statistics" class="ui-select-multi--label--statistics cursor-pointer flex">
						<svws-ui-tooltip position="right">
							<span :class="[disabled ? 'icon-ui-disabled' : 'icon-ui-statistic', 'icon i-ri-bar-chart-2-line pointer-events-auto']"
								aria-label="Relevant für die Statistik" />
							<template #content>
								Relevant für die Statistik
							</template>
						</svws-ui-tooltip>
					</span>

					<span :id="`uiSelectMultiLabel_${state.instanceId}`" :class="[labelTextColorClass, 'ui-select-multi--label--text overflow-hidden truncate']" aria-hidden="true">
						{{ label }}
					</span>
					<span v-if="selectionLimitText !== null" class="h-5 leading-none content-center" :class="[getSecondaryTextColor(textColorClass), 'ui-select-multi--label--limitText']">
						<span>({{ selectionLimitText }})</span>
					</span>
					<span v-if="required" class="ui-select-multi--label--required cursor-pointer flex items-end" aria-label="erforderlich">
						<span :class="[iconColorClass, 'icon-xs i-ri-asterisk font-normal relative -top-0.5']" />
					</span>
					<span v-if="!validation().isEmpty()" class="cursor-pointer flex justify-center items-center">
						<ui-validation-tooltip :validation-result />
					</span>
					<svws-ui-tooltip position="right" v-if="readonly" class="ui-select-multi--label--readonly cursor-pointer pointer-events-auto">
						<span :class="[labelIconClass, 'icon-xs i-ri-lock-line shrink-0']" aria-label="schreibgeschützt" />
						<template #content>
							Schreibgeschützt
						</template>
					</svws-ui-tooltip>
				</div>

				<!-- Wrapper für die aktuelle Selektion und das Suchfeld -->
				<div class="ui-select-multi--selection-search-wrapper flex flex-wrap items-center gap-x-1 flex-1 min-w-0">
					<!-- Aktuelle Selektion -->
					<span v-for="item in model" :key="manager.getSelectionText(item)" tabindex="-1"
						:aria-label="`Auswahl ${props.manager.getSelectionText(item)}`"
						:class="[selectionBubbleClasses, 'ui-select-multi--selection rounded-md text-sm flex items-center overflow-hidden max-w-30 shrink-0 border ml-1 max-h-5 ']">

						<svws-ui-tooltip position="top" :indicator="false" class="truncate">
							<template #content>
								{{ manager.getSelectionText(item) }}
							</template>
							<div class="flex items-center justify-between w-full">
								<span :class="[textColorClass, 'ui-select-multi--selection--text truncate px-2']">
									{{ manager.getSelectionText(item) }}
								</span>
								<button v-if="!readonly && (removable || (modelArray.length > 1))" @click.stop="deselectOption(item)" @keydown.enter.stop="deselectOption(item)"
									class="ui-select-multi--selection--removebutton hover:bg-ui rounded-sm flex m-1 -ml-1 shrink-0 focus:ring-2 ring-ui-brand outline-none"
									:aria-label="`Auswahl ${props.manager.getSelectionText(item)} löschen`">
									<span :class="[ iconColorClass, 'icon-sm i-ri-close-line']" />
								</button>
							</div>
						</svws-ui-tooltip>
					</span>
					<div v-if="searchable" class="ui-select-multi--search relative grid grid-cols-1 grid-rows-1 flex-1 min-w-5 order-last text-base">
						<!-- Such-Input -->
						<input v-if="searchable && !disabled && !readonly" :id="`uiSelectMultiInput_${state.instanceId}`" ref="uiSelectSearch" type="text" role="combobox"
							tabindex="0" v-bind="searchInputAriaAttrs" v-model="search"
							:class="[focusClass, 'ui-select-multi--search row-start-1 col-start-1 outline-none font-normal h-5']"
							@focus="focusSelect" @blur="unfocusInput" @input="handleSearchInput">
					</div>
				</div>
			</div>

			<!-- Expand-Icon + Clear-Button -->
			<div v-if="!headless && !readonly" class="ui-select-multi--icons-right ml-auto flex items-center h-fit">
				<button v-if="removable" type="button" :disabled aria-label="Auswahl löschen" @click.stop="clearSelection" @keydown.enter.stop="clearSelection"
					class="hover:bg-ui-hover flex focus:ring-2 ring-ui-neutral outline-none rounded-sm">
					<span :class="[iconColorClass, 'icon-sm i-ri-close-line']" />
				</button>
				<span :class="[iconColorClass, 'icon i-ri-expand-up-down-line cursor-pointer']" />
			</div>
		</div>

		<!-- Dropdown -->
		<ul v-if="!disabled && !readonly" popover="manual" :aria-labelledby="`uiSelectMultiLabel_${state.instanceId}`" :id="`uiSelectMultiDropdown_${state.instanceId}`" ref="uiSelectDropdown" role="listbox"
			class="ui-select-multi--dropdown overflow-auto bg-ui select-none scrollbar-thin p-1 rounded-md border border-ui font-normal" :style="dropdownPositionStyles">
			<li v-if="manager.filteredOptions.isEmpty() || (optionsMatchingSearch.size() === 0)" class="cursor-not-allowed p-2 hover:bg-ui-hover text-ui-secondary italic text-left">
				{{ "Keine passenden Einträge gefunden" }}
			</li>
			<li v-else :id="`uiSelectMultiOption_${optionIndex}_${state.instanceId}`" v-for="(option, optionIndex) in optionsMatchingSearch" :key="optionIndex"
				role="option" :aria-selected="isSelected(option)"
				:class="[getOptionClasses(option, optionIndex), 'cursor-pointer m-1 p-1 hover:bg-ui-hover hover:inset-ring-2 hover:inset-ring-ui-neutral rounded-lg text-left']"
				@mousedown.stop="toggleSelection(option)">
				<template v-for="(part, index) in splitTextIntoHits(manager.getOptionText(option))" :key="index">
					<span v-if="part.hit" class="bg-ui-selected">{{ part.text }}</span>
					<span v-else>{{ part.text }}</span>
				</template>
			</li>
		</ul>
	</div>
</template>

<script setup lang="ts" generic="T">

	import { computed, ref, toRaw, toRefs, useAttrs, watch } from 'vue';
	import { useUiSelectUtils } from './utils/useUiSelectUtils';
	import type { UiSelectMultiProps, UiSelectHTMLElements, UiSelectSelectionMethods, UiSelectState } from './manager/UiSelectTypes';
	import { SelectManager } from './manager/SelectManager';
	import { DeveloperNotificationException } from '../../../../../core/src/core/exceptions/DeveloperNotificationException';
	import type { List } from '../../../../../core/src/java/util/List';
	import type { ValidatorFehler } from '../../../../../core/src/asd/validate/ValidatorFehler';
	import { ArrayList } from '../../../../../core/src/java/util/ArrayList';
	import { ValidationResult } from "../../../validation/ValidationResult";

	const props = withDefaults(defineProps<UiSelectMultiProps<T>>(), {
		label: '',
		manager: () => new SelectManager<T>(),
		searchable: false,
		deepSearchAttributes: () => [],
		required: false,
		readonly: false,
		removable: true,
		nullable: true,
		disabled: false,
		statistics: false,
		headless: false,
		minOptions: undefined,
		maxOptions: undefined,
		validation: (): List<ValidatorFehler> => new ArrayList<ValidatorFehler>(),
	});


	// model mit der aktuellen Selektion
	const model = defineModel<Iterable<T> | null>();
	const modelArray = computed(() => Array.from(toRaw(model.value) ?? []));

	/**
	 * Watcher auf die aktuelle Selektion über das model.
	 * Bei nicht validen Selektionen wird diese korrigiert oder ein Fehler geworfen.
	 */
	watch(
		() => model.value,
		(newSelection) => {
			if ((newSelection === undefined) || (newSelection === null)) {
				if (!props.nullable) {
					throw new DeveloperNotificationException("Ungültiges v-model: null oder undefined bei nullable = false");
				}
				return;
			}

			const validSelection = getSelectionDiff();
			if (validSelection !== null) {
				model.value = validSelection;
			}
		}, { immediate: true }
	);


	/**
	 * Watcher auf die gefilterten Optionen.
	 * Falls diese sich ändern muss geprüft werden, ob die Selektion noch valide ist. Falls nicht, wird diese angepasst.
	 */
	watch(
		() => props.manager.filteredOptions,
		() => {
			const validSelection = getSelectionDiff();
			if (validSelection !== null) {
				model.value = validSelection;
			}
		}, { immediate: true }
	);

	// Die Vererbung der Attribute wird abgestellt, damit diese manuell an die richtigen Stellen weitergeleitet werden kann
	defineOptions({ inheritAttrs: false });
	const attrs = useAttrs();

	// Der aktuelle Suchbegriff
	const search = ref("");

	// refs
	const uiSelect = ref<HTMLElement | null>(null);
	const uiSelectCombobox = ref<HTMLElement | null>(null);
	const uiSelectSearch = ref<HTMLElement | null>(null);
	const uiSelectDropdown = ref<HTMLDivElement | null>(null);

	const validationResult = computed(() => new ValidationResult(props.validation()));

	/**
	 * Berechnet die Farbe der Selektion-Bubbles abhängig davon, ob sie disabled sind
	 */
	const selectionBubbleClasses = computed((): string => {
		const colors = props.disabled ? 'bg-ui-disabled text-ui-ondisabled border-ui-disabled' : 'bg-ui-selected text-ui-onselected border-ui-selected';
		const height = props.headless ? '' : 'mt-[0.35rem]';
		return `${colors} ${height}`;
	});


	/**
	 * Prüft, ob die Eingaben valide sind
	 */
	const isValid = computed((): boolean => {
		if (props.required && !hasSelection()) {
			return false;
		}
		return minOptionsValid.value && maxOptionsValid.value;
	});

	/**
	 * Prüft, ob die gewählte Optionenanzahl im Falle von MultiSelects dem Minimum entspricht
	 */
	const minOptionsValid = computed((): boolean => {
		if ((props.minOptions === undefined) || (!hasSelection() && (props.minOptions <= 0))) {
			return true;
		}
		return (hasSelection() && (modelArray.value.length >= props.minOptions));
	});

	/**
	 * Prüft, ob die gewählte Optionenanzahl im Falle von MultiSelects dem Maximum entspricht
	 */
	const maxOptionsValid = computed((): boolean => {
		if ((props.maxOptions === undefined) || (!hasSelection() && (props.maxOptions <= 0))) {
			return true;
		}
		return modelArray.value.length <= props.maxOptions;
	});

	/**
	 * Generiert den Text, der bei einer Multi-Selektion die Limitierung der Optinonen anzeigt
	 */
	const selectionLimitText = computed(() => {
		const min = ((props.minOptions !== undefined) && (props.minOptions > 0)) ? props.minOptions : null;
		const max = ((props.maxOptions !== undefined) && (props.maxOptions > 0)) ? props.maxOptions : null;

		if ((min === null) && (max === null)) {
			return null;
		}
		if ((min !== null) && (max !== null)) {
			const lower = Math.min(min, max);
			const upper = Math.max(min, max);
			return lower === upper ? `${lower} Option${lower > 1 ? "en" : ""}` : `${lower} - ${upper} Optionen`;
		}


		return (min === null) ? `max. ${max}` : `min. ${min}`;
	});

	/**
	 * Prüft, ob die angegebene Option bereits selektiert ist.
	 *
	 * @param option
	 */
	function isSelected(option: T): boolean {
		return modelArray.value.includes(option);
	}

	/**
	 * Selektiert die angegebene Option.
	 *
	 * @param option   die zu selektierende Option
	 *
	 * @throws DeveloperNotificationException, wenn die Option bereits selektiert ist
	 */
	function selectOption(option: T): void {
		if (isSelected(option)) {
			throw new DeveloperNotificationException(`Die Option ${props.manager.getOptionText(option)} ist bereits selektiert.`);
		}
		modelArray.value.push(option);
		model.value = modelArray.value;
	}

	/**
	 * Deselektiert die angegebene Option
	 *
	 * @throws DeveloperNotificationException, wenn ein Löschen der Selektion durch removable = false nicht erlaubt ist.
	 */
	function deselectOption(option: T): void {
		if (!deselectAllowed()) {
			throw new DeveloperNotificationException("Das Select ist auf removable=false gesetzt, daher kann der Eintrag nicht deselektiert werden");
		}

		const index = modelArray.value.indexOf(option);
		if (index !== -1) {
			modelArray.value.splice(index, 1);
			model.value = modelArray.value;
		}
		resetSearch();
	}

	/**
	 * Deselektiert die komplette Selektion.
	 */
	function clearSelection(): void {
		if (!props.removable) {
			throw new DeveloperNotificationException("Das Select ist auf removable=false gesetzt, daher kann die komplette Selektion nicht gelöscht werden.");
		}
		model.value = [];
		resetSearch();
		closeDropdown();
	}

	/**
	 * Gibt an, ob das Deselektieren erlaubt ist.
	 */
	function deselectAllowed(): boolean {
		return props.removable || (modelArray.value.length > 1);
	}

	/**
	 * Prüft, ob etwas selektiert ist.
	 */
	function hasSelection(): boolean {
		return modelArray.value.length > 0;
	}

	/**
	 * Prüft, ob die aktuelle Selektion bei den angegebenen Optionen möglich ist. Falls ja, wird null zurückgegeben. Falls nein, wird die neue, dezimierte
	 * Liste von Selektionen zurückgegeben, die zu den Optionen des Selekcts passt.
	 */
	function getSelectionDiff(): T[] | null {
		if (!hasSelection()) {
			return null;
		}
		const newSelected: T[] = [];
		let diff = false;

		for (const selection of modelArray.value) {
			if (props.manager.filteredOptions.contains(selection)) {
				newSelected.push(selection);
			} else {
				diff = true;
			}
		}

		return diff ? newSelected : null;
	}

	const destructedProps = toRefs(props);

	const state = computed((): UiSelectState<T> => {
		return {
			instanceId: crypto.randomUUID(),
			multi: true,
			label: destructedProps.label.value,
			manager: destructedProps.manager.value,
			searchable: destructedProps.searchable.value,
			deepSearchAttributes: destructedProps.deepSearchAttributes.value,
			required: destructedProps.required.value,
			removable: destructedProps.removable.value,
			disabled: destructedProps.disabled.value,
			readonly: destructedProps.readonly.value,
			headless: destructedProps.headless.value,
			isValid: isValid.value,
			validationResult: validationResult.value,
			search: search.value,
		};
	});
	const elements = { uiSelect, uiSelectCombobox, uiSelectSearch, uiSelectDropdown } as UiSelectHTMLElements;
	const selectionMethods = { isSelected, deselectAllowed, deselectOption, selectOption, hasSelection } as UiSelectSelectionMethods<T>;

	const {
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
	} = useUiSelectUtils(state, attrs, elements, search, selectionMethods);


</script>
