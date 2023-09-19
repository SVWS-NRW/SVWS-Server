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
			<div class="dropdown-icon" @click.stop="toggleListbox">
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
	import type TextInput from "./SvwsUiTextInput.vue";
	import { computed, nextTick, ref, shallowRef, watch } from "vue";
	import type { MaybeElement } from "@floating-ui/vue";
	import { useFloating, autoUpdate, flip, offset, shift, size } from "@floating-ui/vue";
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
		span: undefined
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
		const current = document.activeElement;
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
			if (props.itemFilter)
				return props.itemFilter(sortedList.value, searchText.value);
			else
				return sortedList.value.filter(i => props.itemText(i).startsWith(searchText.value ?? ""));
		} else {
			return sortedList.value;
		}
	});

	function doFocus() {
		const el: typeof TextInput = inputEl.value!;
		void nextTick(() => el?.input.focus());
	}

	function openListbox() {
		showList.value = true;
		if ((selectedItem.value !== null) && (selectedItem.value !== undefined) && refList.value !== null) {
			refList.value.activeItemIndex = filteredList.value.findIndex(item => item === selectedItem.value);
			void nextTick(() => scrollToActiveItem());
		}
	}

	function closeListbox() {
		showList.value = false;
		searchText.value = "";
		if (refList.value !== null)
			refList.value.activeItemIndex = -1;
	}

	function selectCurrentActiveItem() {
		if (!showList.value || refList.value === null)
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

	&--statistics {
		.multiselect-tags--placeholder {
			@apply text-violet-500;
		}

		.tooltip-trigger--triggered svg {
			@apply text-violet-800;
		}
	}

	&--danger {
		@apply text-error;
	}

	&.with-open-list,
	&.with-value,
	&:focus-within {
		.tag-list-wrapper {
			@apply border-black dark:border-white;
			@apply outline-none;
		}
	}

	&--statistics.with-open-list,
	&--statistics.with-value,
	&--statistics:focus-within {
		.tag-list-wrapper {
			@apply border-violet-500 dark:border-violet-800;
		}
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

	.multiselect-tags--placeholder {
		@apply absolute;
		@apply pointer-events-none;
		@apply opacity-60;
		@apply transform;
		@apply flex items-center font-medium;

		top: 0.5em;
		left: 0.7em;
		line-height: 1.33;
	}

	&:not(.with-value) .multiselect-tags--placeholder {
		@apply font-normal;
	}

	&:not(.with-open-list):not(.with-value):not(:focus-within):hover .multiselect-tags--placeholder {
		@apply opacity-100;
	}

	&.with-value,
	&:focus-within {
		.multiselect-tags--placeholder {
			@apply -translate-y-1/2;
			@apply bg-white dark:bg-black opacity-100;
			@apply rounded;
			@apply px-1;

			top: -0.2em;
			left: 0.7em;
			font-size: 0.78rem;

			&:after {
				content: '';
			}
		}
	}

	.data-table & {
		.text-input-component {
			@apply pl-1;
		}
	}
}

.wrapper {
	@apply relative;

	.data-table & {
		@apply w-full;
	}

	.data-table .data-table__td__no-padding & {
		.text-input-component {
			padding-left: 0.5rem;
		}
	}
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

.tag-list {
	@apply flex flex-wrap gap-1 pr-4;
}

.multiselect--items-wrapper {
	@apply absolute z-50 w-full min-w-[11rem];
	@apply rounded-md border border-black/25 dark:border-white/25 bg-white dark:bg-black;
	@apply shadow-2xl shadow-black/25 dark:shadow-white/5;
	@apply overflow-hidden;
}

.multiselect--items-list {
	@apply overflow-y-auto overflow-x-hidden;
	@apply px-1.5 py-0.5;
	max-height: 40ex;

	.multiselect--item {
		@apply text-black dark:text-white bg-white dark:bg-black rounded border border-transparent;
		@apply text-base;
		@apply py-1 my-1 px-2;

		.multiselect--check {
			@apply hidden -mt-0.5;
		}

		&.active {
			@apply ring ring-svws/50 border-svws bg-svws text-white;

			.page--statistik & {
				@apply ring-violet-500/50 border-violet-500 bg-violet-500;
			}
		}

		&:hover {
			@apply cursor-pointer;
			@apply bg-svws text-white;

			.page--statistik & {
				@apply bg-violet-500;
			}
		}

		&.selected {
			@apply font-bold bg-svws text-white;

			.page--statistik & {
				@apply bg-violet-500;
			}
		}
	}

	&--tags {
		.multiselect--item .multiselect--check {
			@apply inline-block;
		}

		.multiselect--item.selected {
			@apply bg-transparent text-svws dark:text-svws;

			&.active {
				@apply ring-svws/25 dark:ring-svws/25;
			}

			&:hover {
				@apply bg-svws text-white dark:text-white;
				/*svg {
					@apply hidden;
				}

				&:after {
					@apply opacity-75 font-normal;
					content: '\0000a0entfernen';
				}*/
			}
		}
	}
}

.multiselect--items-wrapper--statistics {
	.multiselect--item.active {
		@apply ring-violet-500/50 border-violet-500 bg-violet-500;
	}

	.multiselect--item:hover {
		@apply bg-violet-500;
	}

	.multiselect--item.selected {
		@apply bg-violet-500;
	}

	&.multiselect--items-wrapper--tags {
		.multiselect--item.selected {
			@apply bg-transparent text-violet-500 dark:text-violet-500;

			&.active {
				@apply ring-violet-500/25 dark:ring-violet-500/25;
			}

			&:hover {
				@apply bg-violet-500 text-white dark:text-white;
			}
		}
	}
}

.tag-list-wrapper {
	@apply flex w-full items-center justify-between overflow-x-auto;
	@apply bg-white dark:bg-black;
	@apply rounded-md border border-black/5 dark:border-white/5;
	@apply w-full;
	@apply text-base;
	@apply whitespace-nowrap;
	@apply h-full;
	@apply cursor-pointer;
	padding: 0.3em 1.7em 0.3em 0.35em;
	min-height: 2.25em;

	&:hover {
		@apply border-black/25 dark:border-white/25;
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

.tag-badge {
	@apply rounded cursor-auto relative z-10;
	@apply bg-svws/5 text-svws border-svws/25 border font-medium;
	@apply flex items-center leading-none;
	@apply max-w-sm;
	padding: 0.2em 0.4em 0.2em 0.7em;

	> span:first-child {
		@apply overflow-ellipsis overflow-hidden whitespace-nowrap;
	}

	.page--statistik &,
	.multiselect-input-component--statistics & {
		@apply bg-violet-500/5 text-violet-500 border-violet-500;
	}

	.tag-remove {
		@apply text-sm -mr-0.5;
		height: 1rem;
	}
}

.tag-badge--placeholder {
	@apply bg-transparent pointer-events-none;
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
