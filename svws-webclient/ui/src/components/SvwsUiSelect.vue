<template>
	<div class="wrapper"
		:class="{ 'z-50': showList, 'wrapper--filled': hasSelected() || showList, 'col-span-full': span === 'full', 'wrapper--headless': headless }">
		<div class="multiselect-input-component"
			:class="{ 'with-open-list': showList, 'multiselect-input-component--statistics': statistics, 'with-value': hasSelected(), 'multiselect-input-component--danger': danger, 'multiselect-input-component--disabled': disabled }">
			<div :class="['input', !showInput ? 'sr-only' : '']">
				<svws-ui-text-input ref="inputEl"
					:model-value="dynModelValue"
					:readonly="!autocomplete || !showInput"
					:placeholder="title"
					:statistics="statistics"
					:headless="headless"
					:disabled="disabled"
					:removable="removable"
					role="combobox"
					:aria-label="placeholder"
					:aria-expanded="showList"
					aria-haspopup="listbox"
					aria-autocomplete="list"
					:aria-controls="showList ? listIdPrefix : null"
					:aria-activedescendant="refList && refList.activeItemIndex > -1 ? `${listIdPrefix}-${refList.activeItemIndex}` : null"
					@update:model-value="onInput"
					@click.stop="toggleListbox"
					@focus="onInputFocus"
					@blur="onInputBlur"
					@keyup.down.prevent
					@keyup.up.prevent
					@keydown.down.prevent="onArrowDown"
					@keydown.up.prevent="onArrowUp"
					@keydown.right.prevent="onArrowDown"
					@keydown.left.prevent="onArrowUp"
					@keydown.enter.prevent="selectCurrentActiveItem"
					@keydown.backspace="onBackspace"
					@keydown.esc.prevent="onEscape"
					@keydown.space="onSpace"
					@keydown.tab="onTab" />
			</div>
			<div v-if="removable && hasSelected()" @click.stop="removeItem" class="remove-icon">
				<span class="icon">
					<i-ri-close-line />
				</span>
			</div>
			<div class="dropdown-icon" @click.stop="showList ? closeListbox() : openListbox()">
				<span class="icon">
					<i-ri-expand-up-down-fill />
				</span>
			</div>
		</div>
		<Teleport to="body">
			<svws-ui-dropdown-list v-if="showList" :statistics="statistics" :tags="false" :filtered-list="filteredList" :item-text="itemText"
				:strategy="strategy" :floating-left="floatingLeft" :floating-top="floatingTop" :selected-item-list="selectedItemList"
				:select-item="selectItem" ref="refList" />
		</Teleport>
	</div>
</template>

<script setup lang="ts" generic="Item">

	import type { ComputedRef, Ref, WritableComputedRef } from "vue";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { MaybeElement } from "@floating-ui/vue";
	import type TextInput from "./SvwsUiTextInput.vue";
	import { useFloating, autoUpdate, flip, offset, shift, size } from "@floating-ui/vue";
	import { computed, nextTick, ref, shallowRef, watch } from "vue";
	import { genId } from "../utils";
	import SvwsUiDropdownList from "./SvwsUiDropdownList.vue";

	type InputDataType = Item | null | undefined;

	const props = withDefaults(defineProps<{
		placeholder?: string;
		title?: string;
		autocomplete?: boolean;
		disabled?: boolean;
		statistics?: boolean;
		danger?: boolean;
		items: Iterable<Item> | Map<number, Item>;
		itemText: (item: Item) => string;
		itemSort?: (a: Item, b: Item) => number;
		itemFilter?: ((items: Iterable<Item>, searchText: string) => Item[]) | ((items: Item[], searchText: string) => Item[]);
		modelValue: InputDataType;
		useNull?: boolean;
		headless?: boolean;
		removable?: boolean;
		required?: boolean;
		span?: 'full';
		indeterminate?: boolean;
	}>(), {
		placeholder: '',
		title: '',
		autocomplete: false,
		disabled: false,
		statistics: false,
		danger: false,
		itemSort: (a: Item, b: Item) => 0,
		itemFilter: (items: Iterable<Item> | Item[], searchText: string) => Array.isArray(items) ? items : [...items],
		useNull: false,
		headless: false,
		removable: false,
		span: undefined,
		indeterminate: false,
	})

	const emit = defineEmits<{
		(e: "update:modelValue", items: InputDataType): void;
		(e: "blur", event: Event): void;
	}>();

	const refList = ref<ComponentExposed<typeof SvwsUiDropdownList> | null>(null);

	const showList = ref(false);
	const itemRefs = shallowRef<HTMLLIElement[]>([]);
	const listIdPrefix = genId();
	const showInput = computed(() => {
		switch (true) {
			// case props.autocomplete && !showList.value:
			// 	return false;
			default:
				return true;
		}
	});

	// Input element
	const inputEl = ref(null);
	const hasFocus = ref(false);
	const searchText = ref("");

	function onInputFocus() {
		hasFocus.value = true;
	}

	function onInputBlur() {
		hasFocus.value = false;
		closeListbox();
	}

	const dynModelValue = computed<string>(() => {
		switch (true) {
			case showList.value && props.autocomplete:
				return searchText.value;
			default:
				return generateInputText() ?? '';
		}
	});

	function generateInputText() {
		if ((selectedItem.value === null) || (selectedItem.value === undefined))
			return "";
		return props.itemText(selectedItem.value);
	}

	function onInput(value: string | number) {
		searchText.value = "" + value;
	}

	// eslint-disable-next-line vue/no-setup-props-destructure
	const data = shallowRef<InputDataType>(props.modelValue);

	watch(() => props.modelValue, (value: InputDataType) => updateData(value), { immediate: false });

	function updateData(value: InputDataType) {
		if (((value === null) || (value === undefined)) && ((data.value === null) || (data.value === undefined)))
			return;
		if (data.value === value)
			return;
		data.value = value;
		emit("update:modelValue", data.value);
		if (props.indeterminate === true) {
			data.value = undefined;
		}
	}

	const selectedItem: WritableComputedRef<Item | null | undefined> = computed({
		get: () => {
			return data.value;
		},
		set: (item) => {
			if ((item === null) || (item === undefined)) {
				updateData(item);
				return;
			}
			updateData(item);
		}
	});

	const selectedItemList = computed<Set<Item>>(() => {
		const set = new Set<Item>();
		if ((data.value !== null) && (data.value !== undefined))
			set.add(data.value);
		return set;
	});

	function hasSelected(): boolean {
		return (selectedItem.value !== null) && (selectedItem.value !== undefined);
	}

	function selectItem(item: Item | null | undefined) {
		selectedItem.value = item;
		if (props.autocomplete)
			searchText.value = "";
		closeListbox();
		doFocus();
	}

	function removeItem() {
		selectedItem.value = props.useNull ? null : undefined;
		doFocus();
	}

	const sortedList: ComputedRef<Item[]> = computed(() => {
		let arr
		if (Array.isArray(props.items))
			arr = props.items;
		else if (props.items instanceof Map)
			arr = [...props.items.values()];
		else
			arr = [...props.items];
		if (props.itemSort)
			return arr.sort(props.itemSort);
		return arr;
	});

	const filteredList: ComputedRef<Item[]> = computed(() => {
		if (props.autocomplete) {
			return props.itemFilter(sortedList.value, searchText.value);
		} else {
			return sortedList.value;
		}
	});

	function doFocus() {
		const el: typeof TextInput = inputEl.value!;
		void nextTick(() => el?.input.focus());
	}

	function openListbox() {
		doFocus();
		showList.value = true;
		void nextTick(() => {
			if ((selectedItem.value !== null) && (selectedItem.value !== undefined) && (refList.value !== null)) {
				refList.value.activeItemIndex = filteredList.value.findIndex(item => item === selectedItem.value);
				scrollToActiveItem();
			} else if (refList.value !== null) {
				refList.value.activeItemIndex = 0;
				scrollToActiveItem();
			}
		});
	}

	function closeListbox() {
		showList.value = false;
		if (refList.value !== null)
			refList.value.activeItemIndex = -1;
	}

	function selectCurrentActiveItem() {
		if ((refList.value === null) || (refList.value.activeItemIndex < 0))
			return;
		selectItem(filteredList.value[refList.value.activeItemIndex]);
	}

	function toggleListbox() {
		showList.value ? closeListbox() : openListbox();
		doFocus();
	}

	// Arrow Navigation
	function onArrowDown() {
		if (!showList.value || refList.value === null) {
			openListbox();
			return;
		}
		const listLength = filteredList.value.length;
		if (refList.value.activeItemIndex < listLength - 1)
			refList.value.activeItemIndex++;
		else
			refList.value.activeItemIndex = 0;
		scrollToActiveItem();
	}

	function onArrowUp() {
		if (refList.value === null)
			return;
		const listLength = filteredList.value.length;
		if (refList.value.activeItemIndex === 0)
			refList.value.activeItemIndex = listLength - 1;
		else if (refList.value.activeItemIndex >= 1)
			refList.value.activeItemIndex--;
		scrollToActiveItem();
	}

	function onBackspace() {
		if (showList.value === false)
			openListbox();
	}

	function onEscape() {
		if (showList.value) {
			closeListbox();
		} else {
			openListbox();
		}
	}

	function onSpace(e: InputEvent) {
		if (!props.autocomplete) {
			e.preventDefault();
			if (!showList.value) {
				openListbox();
			} else {
				selectCurrentActiveItem();
			}
		}
	}

	function onTab(e: InputEvent) {
		if (props.autocomplete && refList.value !== null) {
			e.preventDefault();
			refList.value.activeItemIndex = 0;
			selectCurrentActiveItem();
		}
	}

	function scrollToActiveItem() {
		if (refList.value !== null)
			(itemRefs.value as HTMLElement[])[refList.value.activeItemIndex]?.scrollIntoView({
				block: "nearest",
				inline: "nearest"
			});
	}

	const {x, y, strategy} = useFloating(
		inputEl,
		refList as Readonly<Ref<MaybeElement<HTMLElement>>>,
		{
			placement: 'bottom',
			middleware: [flip(), shift(), offset(2), size({
				apply({rects, elements}) {
					Object.assign(elements.floating.style, {
						width: `${rects.reference.width}px`
					});
				}
			})],
			whileElementsMounted: autoUpdate,
		}
	);

	const floatingTop = computed(() => `${y.value ?? 0}px`);
	const floatingLeft = computed(() => `${x.value ?? 0}px`);

	const content = computed<InputDataType>(() => {
		return data.value;
	});

	defineExpose<{
		content: ComputedRef<InputDataType>,
	}>({
		content
	});

</script>


<style lang="postcss" scoped>

	.multiselect-input-component {
		@apply flex;
		@apply relative;
		@apply w-full;
		@apply inline-block overflow-visible;

		&--danger {
			@apply text-error;
		}

		&.with-open-list,
		&:focus-within {
			.dropdown-icon {
				@apply opacity-100;
			}
		}

		&.with-value:not(:focus-within):not(:hover) .tag-list-wrapper {
			@apply border-black/25 dark:border-white/25;
		}

		&.with-value:not(:focus-within):hover .tag-list-wrapper {
			@apply border-black/50 dark:border-white/50;
		}
	}

	.wrapper {
		@apply relative;
	}

	.dropdown-icon {
		@apply absolute p-1;
		@apply flex;
		@apply inset-y-0 right-0;
		@apply items-center justify-center cursor-pointer text-base;
		@apply opacity-25;

		&:hover {
			@apply opacity-100;
		}

		.icon {
			@apply py-1;
			@apply rounded;
			font-size: 1.1em;
		}

		&:hover .icon {
			@apply bg-black text-white dark:bg-white dark:text-black;

			.multiselect-input-component--statistics:not(.multiselect-input-component--disabled) & {
				@apply bg-violet-500 dark:bg-violet-800 dark:text-white;
			}
		}

		.multiselect-input-component--disabled & {
			@apply pointer-events-none;
			@apply opacity-10 !important;
		}

		.data-table &,
		.svws-ui-table & {
			@apply p-0;

			.icon {
				font-size: 0.9em;
			}
		}

		.multiselect-input-component--statistics & {
			@apply text-purple-500;
		}
	}

	.wrapper--tag-list {
		.input:not(.sr-only) {
			& + .tag-list-wrapper {
				@apply border-t-0 rounded-t-none;
			}
		}

		&.wrapper--headless {
			.tag-list-wrapper {
				@apply border-black/25 dark:border-white/25 bg-white dark:bg-black;
			}
		}

		.dropdown-icon {
			.icon, svg {
				@apply h-full;
			}
		}
	}

	.tag-remove .icon,
	.remove-icon .icon {
		@apply inline-block mt-0 cursor-pointer;
	}

	.tag-remove .icon:hover,
	.remove-icon:hover .icon {
		@apply bg-black dark:bg-white text-white dark:text-black rounded;
	}

	.remove-icon {
		@apply absolute inset-y-0;
		@apply cursor-pointer;
		@apply flex items-center justify-center;
		@apply opacity-50;
		right: 1.7rem;
		font-size: 1rem;

		.data-table &,
		.svws-ui-table & {
			right: 0.9rem;
			font-size: 0.9rem;
		}

		&:hover {
			@apply opacity-100;
		}
	}

</style>
