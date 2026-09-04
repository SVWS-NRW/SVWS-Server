<template>
	<div ref="uiSelect" @focusout="onFocusOut" class="ui-select relative rounded-md text-base inline-flex h-fit w-full group" v-bind="filteredHtmlAttributes">
		<!-- Combobox -->
		<div :id="`uiSelect_${state.instanceId}`" ref="uiSelectCombobox" :tabindex="comboboxTabindex" :role="comboboxRole" v-bind="comboboxAriaAttrs"
			:class="[comboboxClasses, {[focusClass]: !props.searchable}, 'ui-select--combobox relative outline-none ring-ui-neutral w-full rounded-md flex items-center gap-1 min-w-16 m-[0.2em] select-none group-focus-within:ring-2 hover:ring-2']"
			@click.stop="handleComponentClick" @focus="focusSelect" @keydown.stop="handleKeyDown">
			<div :class="[headlessPadding, 'flex']">
				<!-- Expand-Icon + Clear-Button headless -->
				<div v-if="headless && !readonly" class="ui-select--icons-left flex items-center">
					<span :class="[iconColorClass, 'icon-sm i-ri-expand-up-down-line cursor-pointer']" />
					<button v-if="removable" type="button" :disabled aria-label="Auswahl löschen" @click.stop="clearSelection"
						@keydown.enter.stop="clearSelection"
						class="hover:bg-ui-hover flex focus:ring-2 ring-ui-neutral outline-none rounded-sm">
						<span :class="[iconColorClass, 'icon-sm i-ri-close-line']" />
					</button>
				</div>
				<!-- Label -->
				<div v-if="showLabel"
					:class="[labelClasses, 'z-10 ui-select--label absolute transition-all duration-100 ease-in-out pointer-events-none rounded whitespace-nowrap max-w-fit flex justify-center items-center gap-1 px-1 -translate-y-1/2']">
					<span v-if="statistics" class="ui-select--label--statistics cursor-pointer flex">
						<svws-ui-tooltip position="right">
							<span :class="[disabled ? 'icon-ui-disabled' : 'icon-ui-statistic', 'icon i-ri-bar-chart-2-line pointer-events-auto']"
								aria-label="Relevant für die Statistik" />
							<template #content>
								Relevant für die Statistik
							</template>
						</svws-ui-tooltip>
					</span>

					<span :id="`uiSelectLabel_${state.instanceId}`" :class="[labelTextColorClass, 'ui-select--label--text overflow-hidden truncate']" aria-hidden="true">
						{{ label }}
					</span>
					<span v-if="required" class="ui-select--label--required cursor-pointer flex items-end" aria-label="erforderlich">
						<span :class="[iconColorClass, 'icon-xs i-ri-asterisk font-normal relative -top-0.5']" />
					</span>
					<span class="cursor-pointer pointer-events-auto inline-block -my-1">
						<ui-validation-tooltip v-if="validationResult.hasFehler" :validation-result :disabled />
					</span>
					<svws-ui-tooltip position="right" v-if="readonly" class="ui-select--label--readonly cursor-pointer pointer-events-auto">
						<span :class="[labelIconClass, 'icon-xs i-ri-lock-line shrink-0']" aria-label="schreibgeschützt" />
						<template #content>
							Schreibgeschützt
						</template>
					</svws-ui-tooltip>
				</div>

				<!-- Wrapper für die aktuelle Selektion und das Suchfeld -->
				<div class="ui-select--selection-search-wrapper flex flex-wrap items-center gap-x-1 flex-1 min-w-0">
					<!-- Wrapper für das Such-Input und aktuelle Selektion -->
					<div class="relative grid grid-cols-1 grid-rows-1 flex-1 min-w-5 order-last text-base">
						<!-- Aktuelle Selektion -->
						<div v-if="(model !== undefined) && (model !== null)" class="ui-select--selection flex items-center overflow-hidden row-start-1 col-start-1">
							<svws-ui-tooltip position="top" :indicator="false" class="truncate">
								<template #content>
									{{ manager.getSelectionText(model) ?? "-" }}
								</template>
								<div v-if="showSelection" :class="[selectionTextColor, 'truncate z-0 cursor-pointer font-medium inline-block align-middle leading-none h-5 mt-1']">
									{{ manager.getSelectionText(model) ?? "-" }}
								</div>
							</svws-ui-tooltip>
						</div>
						<!-- Such-Input -->
						<input v-if="searchable && !disabled && !readonly" :id="`uiSelectInput_${state.instanceId}`" ref="uiSelectSearch" type="text" role="combobox"
							tabindex="0" v-bind="searchInputAriaAttrs" v-model="search"
							:class="[focusClass, 'ui-select--search row-start-1 col-start-1 outline-none font-normal h-5 w-full']"
							@focus="focusSelect" @blur="unfocusInput" @input="handleSearchInput">
					</div>
				</div>
			</div>

			<!-- Expand-Icon + Clear-Button -->
			<div v-if="!headless && !readonly" class="ui-select--icons-right ml-auto flex items-center h-fit">
				<button v-if="removable && !readonly" type="button" :disabled aria-label="Auswahl löschen" @click.stop="clearSelection"
					@keydown.enter.stop="clearSelection"
					class="hover:bg-ui-hover flex focus:ring-2 ring-ui-neutral outline-none rounded-sm">
					<span :class="[iconColorClass, 'icon-sm i-ri-close-line']" />
				</button>
				<span :class="[iconColorClass, 'icon i-ri-expand-up-down-line cursor-pointer']" />
			</div>
		</div>

		<!-- Dropdown -->
		<ul v-if="!disabled && !readonly" popover="manual" :aria-labelledby="`uiSelectLabel_${state.instanceId}`" :id="`uiSelectDropdown_${state.instanceId}`" ref="uiSelectDropdown" role="listbox"
			class="ui-select--dropdown overflow-auto bg-ui select-none scrollbar-thin p-1 rounded-md border border-ui font-normal" :style="dropdownPositionStyles">
			<li v-if="manager.filteredOptions.isEmpty() || (optionsMatchingSearch.size() === 0)" class="cursor-not-allowed p-2 hover:bg-ui-hover text-ui-secondary italic text-left">
				{{ "Keine passenden Einträge gefunden" }}
			</li>
			<li v-else :id="`uiSelectOption_${optionIndex}_${state.instanceId}`" v-for="(option, optionIndex) in optionsMatchingSearch" :key="optionIndex"
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
	import type { BasicValidator } from '@core/asd/validate/BasicValidator';
	import type { ValidatorFehler } from '@core/asd/validate/ValidatorFehler';
	import { DeveloperNotificationException } from '@core/core/exceptions/DeveloperNotificationException';
	import { JavaObject } from '@core/java/lang/JavaObject';
	import { ArrayList } from '@core/java/util/ArrayList';
	import type { List } from '@core/java/util/List';
	import { ValidatorInputRequired } from '@ui/validation/common/ValidatorInputRequired';
	import { ValidatorSelectOptionsValid } from '@ui/validation/common/ValidatorSelectOptionsValid';
	import { ValidationResult } from '@ui/validation/ValidationResult';
	import type { UiSelectSingleProps, UiSelectState, UiSelectHTMLElements, UiSelectSelectionMethods } from './manager/UiSelectTypes';

	const props = withDefaults(defineProps<UiSelectSingleProps<T>>(), {
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
		validation: undefined,
	});

	// model mit der aktuellen Selektion
	type MaybeNull<T> = T | null;
	const model = defineModel<MaybeNull<T>>();

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

	const validatorRequired = computed<ValidatorInputRequired<T> | null>(() => {
		if (props.required && (props.validation === undefined)) {
			return new ValidatorInputRequired(() => model.value);
		}
		return null;
	});

	const validatorValidSelection = computed(() => {
		return new ValidatorSelectOptionsValid(
			() => (model.value === null) || (model.value === undefined) ? [] : [model.value],
			props.manager
		);
	});

	const validierungFehler = computed<List<ValidatorFehler>>(() => {
		const externeFehler = new ArrayList<ValidatorFehler>();
		if (props.validation !== undefined) {
			externeFehler.addAll(props.validation());
		}
		return collectValidatorFehler(externeFehler, [validatorRequired.value, validatorValidSelection.value]);
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
	 * Die aktuelle Selektion wird nicht angezeigt, falls gerade ein Suchbegriff eingegeben ist
	 */
	const showSelection = computed((): boolean =>
		selectionMethods.hasSelection() && (state.value.search === '')
	);

	/**
	 * Prüft, ob die angegebene Option bereits selektiert ist.
	 *
	 * @param option
	 */
	function isSelected(option: T): boolean {
		if ((model.value === undefined) || (model.value === null)) {
			return false;
		}
		return JavaObject.equalsTranspiler(toRaw(model.value), toRaw(option));
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

		model.value = option;
	}

	/**
	 * Deselektiert die komplette Selektion.
	 *
	 * @throws DeveloperNotificationException, wenn ein Löschen der Selektion durch removable = false nicht erlaubt ist.
	 */
	function clearSelection(): void {
		if (!props.removable) {
			throw new DeveloperNotificationException("Das Select ist auf removable=false gesetzt, daher kann der Eintrag nicht deselektiert werden");
		}
		model.value = undefined;
		resetSearch();
		closeDropdown();
	}

	/**
	 * Gibt an, ob das Deselektieren erlaubt ist.
	 */
	function deselectAllowed(): boolean {
		return props.removable;
	}

	/**
	 * Prüft, ob etwas selektiert ist.
	 */
	function hasSelection(): boolean {
		return ((model.value !== undefined) && (model.value !== null));
	}

	const selectionTextColor = computed((): string =>
		(props.searchable && focusOnInput.value) ? getSecondaryTextColor(textColorClass.value) : textColorClass.value
	);

	const destructedProps = toRefs(props);

	const state = computed((): UiSelectState<T> => {
		return {
			instanceId: crypto.randomUUID(),
			multi: false,
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
	const selectionMethods = { isSelected, deselectAllowed, deselectOption: clearSelection, selectOption, hasSelection } as UiSelectSelectionMethods<T>;

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
		focusOnInput,
	} = useUiSelectUtils(state, attrs, elements, search, selectionMethods);

</script>
