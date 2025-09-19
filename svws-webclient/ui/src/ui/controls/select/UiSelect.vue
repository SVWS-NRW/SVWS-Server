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
					<button v-if="removable" type="button" :disabled aria-label="Auswahl löschen" @click.stop="clearSelection"
						@keydown.enter.stop="clearSelection"
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
					<span v-if="required" class="cursor-pointer flex items-end" aria-label="erforderlich">
						<span :class="[iconColorClass, 'icon-xs i-ri-asterisk font-normal relative -top-0.5']" />
					</span>
					<span v-if="showValidatorError" class="cursor-pointer flex items-end justify-center">
						<span :class="[iconColorClass, 'icon i-ri-alert-line']" />
					</span>
					<span v-if="!isValidatorValid" class="cursor-pointer flex justify-center items-center">
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
					<!-- Wrapper für das Such-Input und aktuelle Selektion -->
					<div class="relative grid grid-cols-1 grid-rows-1 flex-1 min-w-5 order-last text-base">
						<!-- Aktuelle Selektion -->
						<div v-if="hasSelection()" class="flex items-center overflow-hidden row-start-1 col-start-1">
							<svws-ui-tooltip position="top" :indicator="false" class="truncate">
								<template #content>
									{{ manager.getSelectionText(model ?? null) }}
								</template>
								<div v-if="showSelection" :class="[focusBasedTextColorClass, 'truncate z-0 cursor-pointer font-medium inline-block align-middle leading-none h-5 mt-1']">
									{{ manager.getSelectionText(model ?? null) }}
								</div>
							</svws-ui-tooltip>
						</div>
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
				<button v-if="removable && !readonly" type="button" :disabled aria-label="Auswahl löschen" @click.stop="clearSelection"
					@keydown.enter.stop="clearSelection"
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
	import type { UiSelectSingleProps } from './selectManager/UiSelectProps';
	import type { Validator } from '../../../../../core/src/asd/validate/Validator';
	import { SelectManager } from './selectManager/SelectManager';
	import { DeveloperNotificationException } from '../../../../../core/src/core/exceptions/DeveloperNotificationException';

	const props = withDefaults(defineProps<UiSelectSingleProps<T, V>>(), {
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
		validator: undefined,
		doValidate: (validator: V): boolean => validator.run(),
	});

	// model mit der aktuellen Selektion
	type MaybeNull<T> = T | null;
	const model = defineModel<MaybeNull<T>>();

	/**
	 * Watcher auf die aktuelle Selektion über das model.
	 * Bei nicht validen Selektionen wird diese korrigiert oder ein Fehler geworfen.
	 */
	watch(
		() => model.value,
		(newSelection) => {
			if ((newSelection === undefined) || (newSelection === null)) {
				if (!props.nullable)
					throw new DeveloperNotificationException("Ungültiges v-model: null oder undefined bei nullable = false");
				return;
			}
			if (!props.manager.filteredOptions.contains(toRaw(newSelection)))
				model.value = undefined;
		}
	);

	/**
	 * Watcher auf die gefilterten Optionen.
	 * Falls diese sich ändern muss geprüft werden, ob die Selektion noch valide ist. Falls nicht wird diese angepasst.
	 */
	watch(
		() => props.manager.filteredOptions,
		(newOptions) => {
			if ((model.value === undefined) || (model.value === null))
				return;
			if (!newOptions.contains(toRaw(model.value)))
				model.value = undefined;
		}
	);

	// Die Vererbung der Attribute wird abgestellt, damit diese manuell an die richtigen Stellen weitergeleitet werden kann
	defineOptions({ inheritAttrs: false });
	const attrs = useAttrs();

	// Der aktuelle Suchbegriff mit dazu passenden Optionen
	const search = ref("");
	const searchFilteredOptions = computed(() => {
		return (props.searchable) ? getMatchingOptions(search.value) : props.manager.filteredOptions;
	});

	// refs
	const uiSelect = ref<HTMLElement | null>(null);
	const uiSelectCombobox = ref<HTMLElement | null>(null);
	const uiSelectSearch = ref<HTMLElement | null>(null);
	const uiSelectDropdown = ref<HTMLDivElement | null>(null);

	/**
	 * Prüft, ob die Eingaben valide sind
	 */
	const isValid = computed((): boolean => {
		return !props.required || hasSelection();
	});


	/**
	 * Prüft, ob Eingaben abhänig von den Validatoren valide sind
	 */
	const isValidatorValid = computed((): boolean =>
		(props.validator !== undefined) ? props.doValidate(props.validator(), toRaw(model.value) ?? null) : true
	);

	/**
	 * Prüft, ob die angegebene Option bereits selektiert ist.
	 *
	 * @param option
	 */
	function isSelected(option: T): boolean {
		if (model.value === undefined)
			return false;
		return toRaw(model.value) === option;
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

		model.value = option;
		resetSearch();
	}

	/**
	 * Deselektiert die komplette Selektion.
	 *
	 * @throws DeveloperNotificationException, wenn ein Löschen der Selektion durch removable = false nicht erlaubt ist.
	 */
	function clearSelection(): void {
		if (!props.removable)
			throw new DeveloperNotificationException("Das Select ist auf removable=false gesetzt, daher kann der Eintrag nicht deselektiert werden");
		model.value = undefined;
		resetSearch();
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

	// Helperklasse für einige Funktionen, die in Single- und Multi-Select-Komponenten benötigt werden.
	const {
		instanceId, filteredAttributes, iconColorClass, focusBasedTextColorClass, comboboxAriaAttrs, searchAriaAttrs, comboboxTabindex,
		searchInputTabindex, comboboxRole, dropdownPositionStyles, onKeyDown, searchInputFocusClass, handleComboboxFocus, handleBlur, handleComponentClick,
		comboboxClasses, headlessPadding, labelClasses, labelTextColorClass, labelIconClass, optionClasses, validatorErrorIcon, showLabel, showValidatorError,
		showValidatorErrorMessage, validatorErrorBgClasses, showSelection, splitText, handleInput, toggleSelection, getMatchingOptions, resetSearch,

	} = useUiSelectUtils(
		false,
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
		clearSelection,
		hasSelection,
		isSelected,
		deselectAllowed
	);

</script>
