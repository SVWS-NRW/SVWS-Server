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
					<!-- Wrapper für das Such-Input und aktuelle Selektion -->
					<div class="relative grid grid-cols-1 grid-rows-1 flex-1 min-w-5 order-last text-base">
						<!-- Aktuelle Selektion -->
						<div v-if="manager.hasSelection()" class="flex items-center overflow-hidden row-start-1 col-start-1">
							<svws-ui-tooltip position="top" :indicator="false" class="truncate">
								<template #content>
									{{ manager.getSelectionText(manager.selected) }}
								</template>
								<div v-if="showSelection" :class="[focusBasedTextColorClass, 'truncate z-0 cursor-pointer font-medium inline-block align-middle leading-none h-5 mt-1']">
									{{ manager.getSelectionText(manager.selected) }}
								</div>
							</svws-ui-tooltip>
						</div>
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

	import { computed, onMounted, ref, useAttrs, watch } from 'vue';
	import { useUiSelectUtils } from './selectManager/UiSelectUtils';
	import type { UiSelectSingleProps } from './selectManager/UiSelectProps';
	import type { Validator } from '../../../../../core/src/asd/validate/Validator';
	import { SelectManagerSingle } from './selectManager/SelectManagerSingle';

	const props = withDefaults(defineProps<UiSelectSingleProps<T, V>>(), {
		label: '',
		manager: () => new SelectManagerSingle<T>(),
		searchable: false,
		required: false,
		disabled: false,
		statistics: false,
		headless: false,
		validator: undefined,
		doValidate: (validator: V): boolean => validator.run(),
	});

	/** Die Vererbung der Attribute wird abgestellt, damit diese manuell an die richtigen Stellen weitergeleitet werden kann */
	defineOptions({ inheritAttrs: false });
	const attrs = useAttrs();

	type MaybeNull<T> = T | null;
	const model = defineModel<MaybeNull<T>>();

	// refs
	const uiSelect = ref<HTMLElement | null>(null);
	const uiSelectCombobox = ref<HTMLElement | null>(null);
	const uiSelectSearch = ref<HTMLElement | null>(null);
	const uiSelectDropdown = ref<HTMLDivElement | null>(null);

	/**
	 * Prüft, ob die Eingaben valide sind
	 */
	const isValid = computed((): boolean => {
		if (props.required && props.manager.hasSelection() === false)
			return false;
		return true;
	});


	/**
	 * Prüft, ob Eingaben abhänig von den Validatoren valide sind
	 */
	const isValidatorValid = computed((): boolean =>
		(props.validator !== undefined) ? props.doValidate(props.validator(), model.value ?? null) : true
	);

	const {
		instanceId, newSelection, search, filteredAttributes, iconColorClass, focusBasedTextColorClass, comboboxAriaAttrs, searchAriaAttrs, comboboxTabindex,
		searchInputTabindex, comboboxRole, dropdownPositionStyles, onKeyDown, searchInputFocusClass, handleComboboxFocus, handleBlur, handleComponentClick,
		comboboxClasses, headlessPadding, labelClasses, labelTextColorClass, optionClasses, validatorErrorIcon, showLabel, showValidatorError,
		showValidatorErrorMessage, validatorErrorBgClasses, showSelection, splitText, handleInput, toggleSelection, deselect,

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

	onMounted(() => {
		if (model.value !== null)
			props.manager.selected = model.value;
	});

</script>
