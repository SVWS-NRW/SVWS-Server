<template>
	<div ref="uiSelect" class="ui-select relative rounded-md text-base inline-flex h-fit w-full" v-bind="filteredAttributes">
		<!-- Combobox -->
		<div :id="`uiSelectInput_${instanceId}`" ref="uiSelectCombobox" :tabindex="comboboxTabindex" :role="comboboxRole" v-bind="comboboxAriaAttrs"
			:class="[comboboxClasses, 'relative outline-none focus-within:ring-2 ring-ui-neutral w-full rounded-md flex items-center gap-1 hover:ring-ui-neutral hover:ring-2 min-w-16 m-[0.2em]']"
			@click="handleComponentClick" @focus="handleComboboxFocus" @keydown="onKeyDown">
			<div :class="[headlessPadding, 'flex']">
				<!-- Expand-Icon + Clear-Button headless -->
				<div v-if="headless" class="flex items-center">
					<span :class="[iconColorClass, 'icon-sm i-ri-expand-up-down-line cursor-pointer']" />
					<button v-if="manager.removable" type="button" :disabled aria-label="Auswahl löschen" @click="deselect" @keydown.enter="deselect"
						class="hover:bg-ui-hover flex focus:ring-2 ring-ui-neutral outline-none rounded-sm">
						<span :class="[iconColorClass, 'icon-sm i-ri-close-line']" />
					</button>
				</div>
				<!-- Label -->
				<label v-if="showLabel" :id="`uiSelectLabel_${instanceId}`"
					:class="[labelClasses, 'absolute transition-all duration-100 ease-in-out pointer-events-none rounded left-2 whitespace-nowrap max-w-fit flex gap-1 px-1 -translate-y-1/2']">
					<span v-if="statistics" class="cursor-pointer flex">
						<svws-ui-tooltip position="right">
							<span :class="[disabled ? 'icon-ui-disabled' : 'icon-ui-statistic', 'icon i-ri-bar-chart-2-line pointer-events-auto']" />
							<template #content>
								Relevant für die Statistik
							</template>
						</svws-ui-tooltip>
					</span>

					<span :class="[labelTextColorClass, 'leading-none content-center overflow-hidden truncate h-5.5']">
						{{ label }}
					</span>
					<span v-if="selectionLimitText !== null" class="h-5 leading-none content-center" :class="[getSecondaryTextColor(textColorClass)]">
						<span>({{ selectionLimitText }})</span>
					</span>
					<span v-if="required" class="cursor-pointer flex items-end" aria-hidden>
						<span :class="[iconColorClass, 'icon-xs i-ri-asterisk font-normal relative -top-2']" />
					</span>
					<span v-if="required" class="sr-only">erforderlich</span>
					<span v-if="showValidatorError" class="cursor-pointer flex items-end">
						<span :class="[iconColorClass, 'icon i-ri-alert-line']" />
					</span>
					<span v-if="!isValidatorValid" class="cursor-pointer">
						<svws-ui-tooltip position="right">
							<span class="pointer-events-auto">
								<template v-if="!validator().getFehler().isEmpty()">
									<span :class="[validatorErrorIcon, 'icon']" />
								</template>
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
				</label>

				<!-- Wrapper für die aktuelle Selektion und das Suchfeld -->
				<div class="flex flex-wrap items-center gap-x-1 flex-1 min-w-0">
					<!-- Aktuelle Selektion -->
					<span v-for="item in manager.selected" :key="manager.getSelectionText(item)" tabindex="0"
						:aria-label="`Auswahl ${props.manager.getSelectionText(item)}`"
						:class="[selectionBubbleClasses, 'px-2 rounded-md text-sm flex items-center overflow-hidden max-w-30 shrink-0 border ml-1 max-h-5 ']">

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
									<span :class="[ disabled ? 'icon-ui-disabled' : 'icon-ui-onselected', 'icon-sm i-ri-close-line']" />
								</button>
							</div>
						</svws-ui-tooltip>
					</span>
					<div v-if="searchable" class="relative grid grid-cols-1 grid-rows-1 flex-1 min-w-5 order-last text-base">
						<!-- Such-Input -->
						<input v-if="searchable && !disabled" :id="`uiSelectinput_${instanceId}`" ref="uiSelectSearch" type="text" role="combobox"
							:tabindex="searchInputTabindex" v-bind="searchAriaAttrs" v-model="search"
							:class="[searchInputFocusClass, 'row-start-1 col-start-1 outline-none font-normal h-5']"
							@focus="handleComboboxFocus" @blur="handleBlur" @input="handleInput">
					</div>
				</div>
			</div>

			<!-- Expand-Icon + Clear-Button -->
			<div v-if="!headless" class="ml-auto flex items-center h-fit">
				<button v-if="manager.removable" type="button" :disabled aria-label="Auswahl löschen" @click="deselect" @keydown.enter="deselect"
					class="hover:bg-ui-hover flex focus:ring-2 ring-ui-neutral outline-none rounded-sm">
					<span :class="[iconColorClass, 'icon-sm i-ri-close-line']" />
				</button>
				<span :class="[iconColorClass, 'icon i-ri-expand-up-down-line cursor-pointer']" />
			</div>
		</div>

		<!-- Dropdown -->
		<ul popover :aria-labelledby="`uiSelectLabel_${instanceId}`" :id="`uiSelectDropdown_${instanceId}`" ref="uiSelectDropdown" role="listbox"
			class="overflow-auto bg-ui select-none scrollbar-thin p-1 rounded-md border border-ui font-normal" :style="dropdownPositionStyles">
			<li v-if="manager.filteredOptions.isEmpty()" class="cursor-not-allowed p-2 hover:bg-ui-hover text-ui-secondary italic">
				{{ "Keine passenden Einträge gefunden" }}
			</li>
			<li v-else :id="`uiSelectOption_${optionIndex}_${instanceId}`" v-for="(option, optionIndex) in manager.filteredOptions" :key="optionIndex"
				role="option" :aria-selected="manager.isSelected(option)"
				:class="[optionClasses(option, optionIndex), 'cursor-pointer m-1 p-1 hover:bg-ui-hover hover:inset-ring-2 hover:inset-ring-ui-neutral rounded-lg']"
				@click="toggleSelection($event, option)">
				<template v-for="(part, index) in splitText(manager.getOptionText(option))" :key="index">
					<span v-if="part.hit" class="bg-ui-selected">{{ part.text }}</span>
					<span v-else>{{ part.text }}</span>
				</template>
			</li>
		</ul>
	</div>
</template>

<script setup lang="ts" generic="T, V extends Validator">

	import { computed, ref, useAttrs, watch } from 'vue';
	import { useUiSelectUtils } from './selectManager/UiSelectUtils';
	import type { Validator } from '../../../../../core/src/asd/validate/Validator';
	import type { UiSelectMultiProps } from './selectManager/UiSelectProps';
	import { SelectManagerMulti } from './selectManager/SelectManagerMulti';

	const props = withDefaults(defineProps<UiSelectMultiProps<T, V>>(), {
		label: '',
		manager: () => new SelectManagerMulti<T>(),
		searchable: false,
		required: false,
		disabled: false,
		statistics: false,
		headless: false,
		minOptions: undefined,
		maxOptions: undefined,
		validator: undefined,
		doValidate: (validator: V) : boolean => validator.run(),
	});

	/** Die Vererbung der Attribute wird abgestellt, damit diese manuell an die richtigen Stellen weitergeleitet werden kann */
	defineOptions({ inheritAttrs: false });
	const attrs = useAttrs();
	const model = defineModel<Iterable<T> | null>();

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
		(props.validator !== undefined) ? props.doValidate(props.validator(), model.value ?? null) : true
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
	});

	/**
	 * Prüft, ob die gewählte Optionenanzahl im Falle von MultiSelects dem Maximum entspricht
	 */
	const maxOptionsValid = computed((): boolean => {
		if ((props.maxOptions === undefined) || ((props.manager.hasSelection() === false) && (props.maxOptions <= 0)))
			return true;
		return (props.manager.selected.size() <= props.maxOptions);
	});

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

	const {
		instanceId, newSelection, search, filteredAttributes, textColorClass, iconColorClass, comboboxAriaAttrs, searchAriaAttrs, comboboxTabindex,
		searchInputTabindex, comboboxRole, dropdownPositionStyles, onKeyDown, searchInputFocusClass, handleComboboxFocus, handleBlur, handleComponentClick,
		comboboxClasses, headlessPadding, labelClasses, labelTextColorClass, optionClasses, validatorErrorIcon, showLabel, showValidatorError,
		showValidatorErrorMessage, validatorErrorBgClasses, splitText, getSecondaryTextColor, handleInput, toggleSelection, deselect,
	} = useUiSelectUtils(
		props,
		attrs,
		isValid,
		isValidatorValid,
		uiSelect,
		uiSelectCombobox,
		uiSelectSearch,
		uiSelectDropdown
	);

	watch(
		() => model.value,
		(newSelection) => {
			props.manager.selected = newSelection;
		},
		{ deep: true }
	);

	watch(
		() => newSelection.value,
		(val) => {
			if (val) {
				model.value = props.manager.selected;
				newSelection.value = false;
			}
		}
	);

</script>
