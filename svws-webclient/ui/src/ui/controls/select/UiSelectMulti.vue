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
					:class="[labelClasses, 'ui-select-multi--label z-10 absolute transition-all duration-100 ease-in-out pointer-events-none rounded left-2 whitespace-nowrap max-w-fit flex justify-center items-center gap-1 px-1 -translate-y-1/2']">
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
					<span class="cursor-pointer pointer-events-auto inline-block -my-1">
						<ui-validation-tooltip v-if="validationResult.hasFehler" :validation-result :disabled />
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
								<span :class="[selectionBubbleTextClasses, 'ui-select-multi--selection--text truncate px-2']">
									{{ manager.getSelectionText(item) }}
								</span>
								<button v-if="!readonly && (removable || (modelArray.length > 1))" @click.stop="deselectOption(item)" @keydown.enter.stop="deselectOption(item)"
									class="ui-select-multi--selection--removebutton hover:bg-ui rounded-sm flex m-1 -ml-1 shrink-0 focus:ring-2 ring-ui-brand outline-none"
									:aria-label="`Auswahl ${props.manager.getSelectionText(item)} löschen`">
									<span :class="[ selectionBubbleIconClasses, 'icon-sm i-ri-close-line']" />
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
	import { SelectManager } from './manager/SelectManager';
	import type { UiSelectHTMLElements, UiSelectMultiProps, UiSelectSelectionMethods, UiSelectState } from './manager/UiSelectTypes';
	import type { BasicValidator } from '@core/asd/validate/BasicValidator';
	import type { ValidatorFehler } from '@core/asd/validate/ValidatorFehler';
	import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';
	import { JavaObject } from '@core/java/lang/JavaObject';
	import { ArrayList } from '@core/java/util/ArrayList';
	import type { List } from '@core/java/util/List';
	import { ValidatorInputRequired } from '@ui/validation/common/ValidatorInputRequired';
	import { ValidatorSelectMultiOptionsRange } from '@ui/validation/common/ValidatorSelectMultiOptionsRange';
	import { ValidatorSelectOptionsValid } from '@ui/validation/common/ValidatorSelectOptionsValid';
	import { ValidationResult } from '@ui/validation/ValidationResult';

	const props = withDefaults(defineProps<UiSelectMultiProps<T>>(), {
		label: '',
		manager: () => new SelectManager<T>(),
		searchable: true,
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
		validation: undefined,
	});


	// model mit der aktuellen Selektion
	const model = defineModel<Iterable<T> | null>();
	const modelArray = computed(() => Array.from(toRaw(model.value) ?? []));

	watch(
		() => model.value,
		(newSelection) => {
			if (!props.nullable && ((newSelection === undefined) || (newSelection === null))) {
				throw new DeveloperNotificationException("Ungültiges v-model: null oder undefined bei nullable = false");
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

	const validationResult = computed(() => new ValidationResult(validierungFehler.value));

	const validatorRequired = computed<ValidatorInputRequired<T[]> | null>(() => {
		if (props.required && (props.validation === undefined)) {
			return new ValidatorInputRequired(() => modelArray.value);
		}
		return null;
	});

	const validatorOptionsRange = computed<ValidatorSelectMultiOptionsRange<T> | null>(() => {
		if (((props.minOptions !== undefined) || (props.maxOptions !== undefined)) && (props.validation === undefined)) {
			return new ValidatorSelectMultiOptionsRange(() => modelArray.value, props.minOptions, props.maxOptions);
		}
		return null;
	});

	const validatorValidSelection = computed(() => {
		return new ValidatorSelectOptionsValid(
			() => modelArray.value,
			props.manager
		);
	});

	const validierungFehler = computed<List<ValidatorFehler>>(() => {
		const externeFehler = new ArrayList<ValidatorFehler>();
		if (props.validation !== undefined) {
			externeFehler.addAll(props.validation());
		}
		return collectValidatorFehler(externeFehler, [validatorRequired.value, validatorOptionsRange.value, validatorValidSelection.value]);
	});

	/**
	 * Fügt einer vorhandenen Fehlerliste die Fehler weiterer, übergebener Validatoren hinzu.
	 *
	 * @param fehler       Fehler, die von außen an die Komponente weitergereicht wurden zum Beispiel von einem ModelProxy.
	 * @param validators   Zusätzliche Validatoren, die ausgeführt und deren Fehler hinzugefügt werden sollen. Diese Fehler verhindern kein Patch!
	 */
	function collectValidatorFehler(fehler: List<ValidatorFehler>, validators: Array<BasicValidator | null>): List<ValidatorFehler> {
		for (const validator of validators) {
			if (validator !== null) {
				validator.run();
				fehler.addAll(validator.getFehler());
			}
		}
		return fehler;
	}

	/**
	 * Berechnet die Farbe der Selektion-Bubbles abhängig davon, ob sie disabled sind
	 */
	const selectionBubbleClasses = computed((): string => {
		const colors = props.disabled ? 'bg-ui-disabled border-ui-ondisabled' : 'bg-ui-selected border-ui-selected';
		const height = props.headless ? '' : 'mt-[0.35rem]';
		return `${colors} ${height}`;
	});

	/**
	 * Berechnet die Farbe der Selektion-Bubble-Texte abhängig davon, ob sie disabled sind
	 */
	const selectionBubbleTextClasses = computed((): string => {
		return props.disabled ? 'text-ui-ondisabled' : 'text-ui-onselected';
	});

	/**
	 * Berechnet die Farbe der Selektion-Bubble-Icons abhängig davon, ob sie disabled sind
	 */
	const selectionBubbleIconClasses = computed((): string => {
		return props.disabled ? 'icon-ui-ondisabled' : 'icon-ui-onselected';
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
		return modelArray.value.some(selected => JavaObject.equalsTranspiler(toRaw(selected), toRaw(option)));
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

		const index = modelArray.value.findIndex(selected =>
			JavaObject.equalsTranspiler(toRaw(selected), toRaw(option))
		);

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
