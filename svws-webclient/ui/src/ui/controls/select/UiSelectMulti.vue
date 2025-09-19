<template>
	<div ref="uiSelect" class="ui-select relative rounded-md text-base inline-flex h-fit w-full" v-bind="filteredAttributes">
		<!-- Combobox -->
		<div :id="`uiSelectInput_${instanceId}`" ref="uiSelectCombobox" :tabindex="comboboxTabindex" :role="comboboxRole" v-bind="comboboxAriaAttrs"
			:class="[comboboxClasses, 'relative outline-none ring-ui-neutral w-full rounded-md flex items-center gap-1 min-w-16 m-[0.2em] select-none']"
			@click.stop="handleComponentClick" @focus="handleComboboxFocus" @keydown.stop="onKeyDown">
			<div :class="[headlessPadding, 'flex']">
				<!-- Expand-Icon + Clear-Button headless -->
				<div v-if="headless && !readonly" class="flex items-center">
					<span :class="[iconColorClass, 'icon-sm i-ri-expand-up-down-line cursor-pointer']" />
					<button v-if="removable" type="button" :disabled aria-label="Auswahl löschen" @click.stop="clearSelection" @keydown.enter.stop="clearSelection"
						class="hover:bg-ui-hover flex focus:ring-2 ring-ui-neutral outline-none rounded-sm">
						<span :class="[iconColorClass, 'icon-sm i-ri-close-line']" />
					</button>
				</div>
				<!-- Label -->
				<div v-if="showLabel"
					:class="[labelClasses, 'absolute transition-all duration-100 ease-in-out pointer-events-none rounded left-2 whitespace-nowrap max-w-fit flex justify-center items-center gap-1 px-1 -translate-y-1/2']">
					<span v-if="statistics" class="cursor-pointer flex">
						<svws-ui-tooltip position="right">
							<span :class="[disabled ? 'icon-ui-disabled' : 'icon-ui-statistic', 'icon i-ri-bar-chart-2-line pointer-events-auto']"
								aria-label="Relevant für die Statistik" />
							<template #content>
								Relevant für die Statistik
							</template>
						</svws-ui-tooltip>
					</span>

					<span :id="`uiSelectLabel_${instanceId}`" :class="[labelTextColorClass, 'overflow-hidden truncate']" aria-hidden="true">
						{{ label }}
					</span>
					<span v-if="selectionLimitText !== null" class="h-5 leading-none content-center" :class="[getSecondaryTextColor(textColorClass)]">
						<span>({{ selectionLimitText }})</span>
					</span>
					<span v-if="required" class="cursor-pointer flex items-end" aria-label="erforderlich">
						<span :class="[iconColorClass, 'icon-xs i-ri-asterisk font-normal relative -top-0.5']" />
					</span>
					<span v-if="showValidatorError" class="cursor-pointer flex items-end justify-center">
						<span :class="[iconColorClass, 'icon i-ri-alert-line']" />
					</span>
					<span v-if="!isValidatorValid" class="cursor-cursor-pointer flex justify-center items-center">
						<svws-ui-tooltip position="right">
							<span v-if="!validator().getFehler().isEmpty()" class="pointer-events-auto flex justify-center items-center">
								<span :class="[validatorErrorIcon, 'icon']" />
							</span>
							<template #content>
								<template v-if="showValidatorErrorMessage">
									<div class="text-headline-sm text-center pt-1"> Validatorfehler </div>
									<div v-for="fehler in validator().getFehler()" :key="fehler.getFehlermeldung() ?? '--'" class="pt-2 pb-2">
										<div :class="[validatorErrorBgClasses, 'rounded-sm pl-2']">
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
					<svws-ui-tooltip position="right" v-if="readonly" class="cursor-pointer pointer-events-auto">
						<span :class="[labelIconClass, 'icon-xs i-ri-lock-line flex-shrink-0']" aria-label="schreibgeschützt" />
						<template #content>
							Schreibgeschützt
						</template>
					</svws-ui-tooltip>
				</div>

				<!-- Wrapper für die aktuelle Selektion und das Suchfeld -->
				<div class="flex flex-wrap items-center gap-x-1 flex-1 min-w-0">
					<!-- Aktuelle Selektion -->
					<span v-for="item in model" :key="manager.getSelectionText(item)" tabindex="-1"
						:aria-label="`Auswahl ${props.manager.getSelectionText(item)}`"
						:class="[selectionBubbleClasses, 'rounded-md text-sm flex items-center overflow-hidden max-w-30 shrink-0 border ml-1 max-h-5 ']">

						<svws-ui-tooltip position="top" :indicator="false" class="truncate">
							<template #content>
								{{ manager.getSelectionText(item) }}
							</template>
							<div class="flex items-center justify-between w-full">
								<span class="truncate px-2">
									{{ manager.getSelectionText(item) }}
								</span>
								<button v-if="!readonly && (removable || (modelArray.length > 1))" @click.stop="deselectOption(item)" @keydown.enter.stop="deselectOption(item)"
									class="hover:bg-ui rounded-sm flex m-1 -ml-1 flex-shrink-0 focus:ring-2 ring-ui-brand outline-none"
									:aria-label="`Auswahl ${props.manager.getSelectionText(item)} löschen`">
									<span :class="[ disabled ? 'icon-ui-disabled' : 'icon-ui-onselected', 'icon-sm i-ri-close-line']" />
								</button>
							</div>
						</svws-ui-tooltip>
					</span>
					<div v-if="searchable" class="relative grid grid-cols-1 grid-rows-1 flex-1 min-w-5 order-last text-base">
						<!-- Such-Input -->
						<input v-if="searchable && !disabled && !readonly" :id="`uiSelectinput_${instanceId}`" ref="uiSelectSearch" type="text" role="combobox"
							:tabindex="searchInputTabindex" v-bind="searchAriaAttrs" v-model="search"
							:class="[searchInputFocusClass, 'row-start-1 col-start-1 outline-none font-normal h-5']"
							@focus="handleComboboxFocus" @blur="handleBlur" @input="handleInput">
					</div>
				</div>
			</div>

			<!-- Expand-Icon + Clear-Button -->
			<div v-if="!headless && !readonly" class="ml-auto flex items-center h-fit">
				<button v-if="removable" type="button" :disabled aria-label="Auswahl löschen" @click.stop="clearSelection" @keydown.enter.stop="clearSelection"
					class="hover:bg-ui-hover flex focus:ring-2 ring-ui-neutral outline-none rounded-sm">
					<span :class="[iconColorClass, 'icon-sm i-ri-close-line']" />
				</button>
				<span :class="[iconColorClass, 'icon i-ri-expand-up-down-line cursor-pointer']" />
			</div>
		</div>

		<!-- Dropdown -->
		<ul v-if="!disabled && !readonly" popover :aria-labelledby="`uiSelectLabel_${instanceId}`" :id="`uiSelectDropdown_${instanceId}`" ref="uiSelectDropdown" role="listbox"
			class="overflow-auto bg-ui select-none scrollbar-thin p-1 rounded-md border border-ui font-normal" :style="dropdownPositionStyles">
			<li v-if="manager.filteredOptions.isEmpty() || (searchFilteredOptions.size() === 0)" class="cursor-not-allowed p-2 hover:bg-ui-hover text-ui-secondary italic text-left">
				{{ "Keine passenden Einträge gefunden" }}
			</li>
			<li v-else :id="`uiSelectOption_${optionIndex}_${instanceId}`" v-for="(option, optionIndex) in searchFilteredOptions" :key="optionIndex"
				role="option" :aria-selected="isSelected(option)"
				:class="[optionClasses(option, optionIndex), 'cursor-pointer m-1 p-1 hover:bg-ui-hover hover:inset-ring-2 hover:inset-ring-ui-neutral rounded-lg text-left']"
				@click.stop="toggleSelection(option)">
				<template v-for="(part, index) in splitText(manager.getOptionText(option))" :key="index">
					<span v-if="part.hit" class="bg-ui-selected">{{ part.text }}</span>
					<span v-else>{{ part.text }}</span>
				</template>
			</li>
		</ul>
	</div>
</template>

<script setup lang="ts" generic="T, V extends Validator">

	import { computed, ref, toRaw, useAttrs, watch } from 'vue';
	import { useUiSelectUtils } from './selectManager/UiSelectUtils';
	import type { Validator } from '../../../../../core/src/asd/validate/Validator';
	import type { UiSelectMultiProps } from './selectManager/UiSelectProps';
	import { SelectManager } from './selectManager/SelectManager';
	import { DeveloperNotificationException } from '../../../../../core/src/core/exceptions/DeveloperNotificationException';
	import { ArrayList } from '../../../../../core/src/java/util/ArrayList';

	const props = withDefaults(defineProps<UiSelectMultiProps<T, V>>(), {
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
		validator: undefined,
		doValidate: (validator: V) : boolean => validator.run(),
	});


	// model mit der aktuellen Selektion
	const model = defineModel<Iterable<T> | null>();
	const modelArray = computed( () => Array.from(toRaw(model.value) ?? []));

	/**
	 * Watcher auf die aktuelle Selektion über das model.
	 * Bei nicht validen Selektionen wird diese korrigiert oder ein Fehler geworfen.
	 */
	watch(
		() => model.value,
		(newSelection) => {
			if (newSelection === undefined || newSelection === null) {
				if (!props.nullable)
					throw new DeveloperNotificationException("Ungültiges v-model: null oder undefined bei nullable = false");
				return;
			}

			const validSelection = getSelectionDiff();
			if (validSelection !== null)
				model.value = validSelection;
		}
	);


	/**
	 * Watcher auf die gefilterten Optionen.
	 * Falls diese sich ändern muss geprüft werden, ob die Selektion noch valide ist. Falls nicht wird diese angepasst.
	 */
	watch(
		() => props.manager.filteredOptions,
		() => {
			const validSelection = getSelectionDiff();
			if (validSelection !== null)
				model.value = validSelection;
		}
	);

	//Die Vererbung der Attribute wird abgestellt, damit diese manuell an die richtigen Stellen weitergeleitet werden kann
	defineOptions({ inheritAttrs: false });
	const attrs = useAttrs();

	// Der aktuelle Suchbegriff mit dazu passenden Optionen
	const search = ref("");
	const searchFilteredOptions = computed(() => {
		return props.searchable ? getMatchingOptions(search.value) : props.manager.filteredOptions;
	});

	// refs
	const uiSelect = ref<HTMLElement | null>(null);
	const uiSelectCombobox = ref<HTMLElement | null>(null);
	const uiSelectSearch = ref<HTMLElement | null>(null);
	const uiSelectDropdown = ref<HTMLDivElement | null>(null);

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
		if (props.required && !hasSelection())
			return false;
		if (!minOptionsValid.value || !maxOptionsValid.value)
			return false;
		return true;
	});

	/**
	 * Prüft, ob Eingaben abhänig von den Validatoren valide sind
	 */
	const isValidatorValid = computed((): boolean =>
		(props.validator !== undefined) ? props.doValidate(props.validator(), toRaw(model.value) ?? null) : true
	);

	/**
	 * Prüft, ob die gewählte Optionenanzahl im Falle von MultiSelects dem Minimum entspricht
	 */
	const minOptionsValid = computed((): boolean => {
		if ((props.minOptions === undefined) || (!hasSelection() && (props.minOptions <= 0)))
			return true;
		return (hasSelection() && (modelArray.value.length >= props.minOptions));
	});

	/**
	 * Prüft, ob die gewählte Optionenanzahl im Falle von MultiSelects dem Maximum entspricht
	 */
	const maxOptionsValid = computed((): boolean => {
		if ((props.maxOptions === undefined) || (!hasSelection() && (props.maxOptions <= 0)))
			return true;
		return modelArray.value.length <= props.maxOptions;
	});

	/**
	 * Generiert den Text, der bei einer Multi-Selektion die Limitierung der Optinonen anzeigt
	 */
	const selectionLimitText = computed(() => {
		const min = ((props.minOptions !== undefined) && (props.minOptions > 0)) ? props.minOptions : null;
		const max = ((props.maxOptions !== undefined) && (props.maxOptions > 0)) ? props.maxOptions : null;

		if ((min === null) && (max === null))
			return null;
		if ((min !== null) && (max !== null))
			return (min === max) ? `${min} Option` : `${Math.min(min, max)} - ${Math.max(min, max)} Optionen`;

		return (min !== null) ? `min. ${min}` : `max. ${max}`;
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
		if (isSelected(option))
			throw new DeveloperNotificationException(`Die Option ${props.manager.getOptionText(option)} ist bereits selektiert.`);
		modelArray.value.push(option);
		model.value = modelArray.value;
	}

	/**
	 * Deselektiert die angegebene Option
	 *
	 * @throws DeveloperNotificationException, wenn ein Löschen der Selektion durch removable = false nicht erlaubt ist.
	 */
	function deselectOption(option: T): void {
		if (!props.removable && (modelArray.value.length === 1))
			throw new DeveloperNotificationException("Das Select ist auf removable=false gesetzt, daher kann der Eintrag nicht deselektiert werden");

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
		model.value = [];
		resetSearch();
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
	function getSelectionDiff(): ArrayList<T> | null {
		if (!model.value)
			return null;
		const newSelected = new ArrayList<T>();
		let diff = false;

		for (const selection of toRaw(model.value))
			if (props.manager.filteredOptions.contains(selection))
				newSelected.add(selection);
			else
				diff = true;

		return diff ? newSelected : null;
	}

	// Helperklasse für einige Funktionen, die in Single- und Multi-Select-Komponenten benötigt werden.
	const {
		instanceId, filteredAttributes, textColorClass, iconColorClass, comboboxAriaAttrs, searchAriaAttrs, comboboxTabindex, searchInputTabindex,
		comboboxRole, dropdownPositionStyles, onKeyDown, searchInputFocusClass, handleComboboxFocus, handleBlur, handleComponentClick, comboboxClasses,
		headlessPadding, labelClasses, labelTextColorClass, labelIconClass, optionClasses, validatorErrorIcon, showLabel, showValidatorError,
		showValidatorErrorMessage, validatorErrorBgClasses, splitText, getSecondaryTextColor, handleInput, toggleSelection, getMatchingOptions, resetSearch,

	} = useUiSelectUtils(
		true,
		props,
		attrs,
		isValid,
		isValidatorValid,
		uiSelect,
		uiSelectCombobox,
		uiSelectSearch,
		uiSelectDropdown,
		search,
		selectOption,
		deselectOption,
		hasSelection,
		isSelected,
		deselectAllowed
	);


</script>
