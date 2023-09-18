<template>
	<div class="wrapper"
		:class="{ 'z-50': showList, 'wrapper--tag-list' : tags, 'wrapper--filled': hasSelected() || showList, 'col-span-full': span === 'full', 'wrapper--headless': headless }">
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
					:class="{'text-input--control--multiselect-tags': tags}"
					:debounce-ms="0"
					role="combobox"
					:aria-label="placeholder"
					:aria-expanded="showList"
					aria-haspopup="listbox"
					aria-autocomplete="list"
					:aria-controls="showList ? listIdPrefix : null"
					:aria-activedescendant="activeItemIndex > -1 ? `${listIdPrefix}-${activeItemIndex}` : null"
					@update:model-value="onInput"
					@click="toggleListbox"
					@focus="onFocus"
					@blur="onBlur"
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
					@keydown.tab.prevent="onTab" />
			</div>
			<div v-if="tags" class="tag-list-wrapper"
				@click.self="toggleListbox" ref="inputElTags">
				<div class="tag-list" @click.self="toggleListbox">
					<slot v-if="(selectedItemList.size === 0) && !showList" name="no-content" />
					<div class="opacity-50 pl-2 text-sm" v-if="(selectedItemList.size === 0) && showList">
						Noch nichts ausgewählt.
					</div>
					<div v-for="(item, index) in selectedItemList" v-else :key="index" class="tag">
						<span class="tag-badge">
							<span :class="{'pr-1': showList}">{{ itemText(item) }}</span>
							<span class="tag-remove ml-1" @click="removeTag(item)" title="Entfernen" v-if="!showList">
								<span class="icon">
									<i-ri-close-line />
								</span>
							</span>
						</span>
					</div>
				</div>
				<span v-if="!showInput" class="multiselect-tags--placeholder">
					<span>{{ title }}</span>
					<i-ri-bar-chart-2-line v-if="statistics" class="ml-1" />
				</span>
			</div>
			<div v-if="removable && hasSelected()" @click="removeItem" class="remove-icon">
				<span class="icon">
					<i-ri-close-line />
				</span>
			</div>
			<div class="dropdown-icon" @click="toggleListbox">
				<span class="icon">
					<i-ri-expand-up-down-fill />
				</span>
			</div>
		</div>
		<Teleport to="body">
			<div v-if="showList" class="multiselect--items-wrapper"
				:class="{'multiselect--items-wrapper--statistics': statistics, 'multiselect--items-wrapper--tags': tags}"
				:style="{ position: strategy, top: floatingTop, left: floatingLeft }"
				ref="floating">
				<ul :id="listIdPrefix"
					class="multiselect--items-list"
					:class="{'multiselect--items-list--tags' : tags}"
					role="listbox"
					@mouseenter="activeItemIndex = -1">
					<li v-if="filteredList.length === 0" class="px-1 py-1 text-base opacity-50 inline-block">
						Keine Ergebnisse
					</li>
					<li v-for="(item, index) in filteredList"
						:id="`${listIdPrefix}-${index}`"
						:key="index"
						ref="itemRefs"
						role="option"
						class="multiselect--item"
						:class="{
							active: activeItemIndex === index,
							selected: selectedItemList.has(item)
						}"
						:aria-selected="selectedItemList.has(item) ? 'true' : 'false'"
						@mousedown.prevent
						@click="
							() => {
								selectItem(item);
								!tags && closeListbox();
							}
						">
						<span v-if="itemText?.(item).length === 0" class="opacity-25">—</span>
						<span>{{ itemText(item) }}</span>
						<i-ri-check-line v-if="selectedItemList.has(item)" class="multiselect--check opacity-75" />
					</li>
				</ul>
			</div>
		</Teleport>
	</div>
</template>


<script setup lang="ts" generic="Item">

	import type { ComputedRef, WritableComputedRef } from "vue";
	import type TextInput from "./SvwsUiTextInput.vue";
	import { computed, nextTick, ref, shallowRef, watch, Teleport, toRef } from "vue";
	import { useFloating, autoUpdate, flip, offset, shift, size } from "@floating-ui/vue";
	import { genId } from "../utils";

	type InputDataType = Item[] | Item | null | undefined;

	const props = withDefaults(defineProps<{
		placeholder?: string;
		title?: string;
		tags?: boolean;
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
		tags: false,
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
		(e: "focus", event: Event): void;
		(e: "blur", event: Event): void;
	}>();

	const showList = ref(false);
	const itemRefs = shallowRef<HTMLLIElement[]>([]);
	const activeItemIndex = ref(-1);
	const listIdPrefix = genId();
	const showInput = computed(() => {
		switch (true) {
			case props.tags && !props.autocomplete:
				return false;
			case props.tags && props.autocomplete && !showList.value:
				return false;
			default:
				return true;
		}
	});

	// Input element
	const inputEl = ref(null);
	const inputElTags = ref(null);
	const hasFocus = ref(false);
	const searchText = ref("");

	function onFocus() {
		hasFocus.value = true;
	}

	function onBlur() {
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
		// Fall 1: Multi-Select möglich
		if (props.tags)
			return [...selectedItemList.value].map(item => props.itemText(item)).join(", ");
		// Fall 2: Kein Multi-Select möglich
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
		// Fall 1: Multi-Select möglich
		if (props.tags) {
			const a = ((data.value === null) || (data.value === undefined)) ? [] : data.value as Item[];
			const b = ((value === null) || (value === undefined)) ? [] : value as Item[];
			if ((a.length === b.length) && (a.every((v, i) => v === b[i])))
				return;
			data.value = b;
			emit("update:modelValue", data.value);
			return;
		}
		// Fall 2: Kein Multi-Select möglich
		if (data.value === value)
			return;
		data.value = value;
		emit("update:modelValue", data.value);
	}

	const selectedItem: WritableComputedRef<Item | null | undefined> = computed({
		get: () => {
			// Fall 1: Multi-Select möglich
			if (props.tags && Array.isArray(data.value))
				return data.value[0];
			// Fall 2: Kein Multi-Select möglich
			return data.value as Item | null | undefined;
		},
		set: (item) => {
			// Fall 0: null or undefined (kein Multi-Select möglich)
			if ((item === null) || (item === undefined)) {
				updateData(item);
				return;
			}
			// Fall 1: Multi-Select möglich
			if (props.tags) {
				if (selectedItemList.value.has(item)) {
					selectedItemList.value.delete(item);
				} else {
					selectedItemList.value.add(item);
				}
				updateData([...selectedItemList.value]);
				return;
			}
			// Fall 2: Kein Multi-Select möglich
			updateData(item);
		}
	});

	const selectedItemList = computed<Set<Item>>(() => {
		// Fall 1: Multi-Select möglich
		if (props.tags && Array.isArray(data.value))
			return new Set<Item>(data.value);
		// Fall 2: Kein Multi-Select möglich
		const set = new Set<Item>();
		if ((data.value !== null) && (data.value !== undefined))
			set.add(data.value as Item);
		return set;
	});

	function hasSelected(): boolean {
		return (selectedItem.value !== null) && (selectedItem.value !== undefined);
	}

	function selectItem(item: Item | null | undefined) {
		selectedItem.value = item;
	}

	function removeItem() {
		selectedItem.value = props.useNull ? null : undefined;
	}

	function removeTag(item: Item) {
		if (selectedItemList.value.has(item))
			selectedItem.value = item;
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

	function openListbox() {
		showList.value = true;
		if ((selectedItem.value !== null) && (selectedItem.value !== undefined)) {
			activeItemIndex.value = filteredList.value.findIndex(item => item === selectedItem.value);
			void nextTick(() => scrollToActiveItem());
		}
		const el: typeof TextInput = inputEl.value!;
		void nextTick(() => el?.input.focus());
	}

	function closeListbox() {
		showList.value = false;
		searchText.value = "";
		activeItemIndex.value = -1;
	}

	function selectCurrentActiveItem() {
		if (!showList.value)
			return;
		selectItem(filteredList.value[activeItemIndex.value]);
		if (!props.tags)
			closeListbox();
	}

	function toggleListbox() {
		showList.value ? closeListbox() : openListbox();
	}

	// Arrow Navigation
	function onArrowDown() {
		if (!showList.value) {
			openListbox();
			return;
		}
		const listLength = filteredList.value.length;
		if (activeItemIndex.value < listLength - 1)
			activeItemIndex.value++;
		else
			activeItemIndex.value = 0;
		scrollToActiveItem();
	}

	function onArrowUp() {
		const listLength = filteredList.value.length;
		if (activeItemIndex.value === 0)
			activeItemIndex.value = listLength - 1;
		else if (activeItemIndex.value >= 1)
			activeItemIndex.value--;
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
		if (props.autocomplete) {
			e.preventDefault();
			activeItemIndex.value = 0;
			selectCurrentActiveItem();
		}
	}

	function scrollToActiveItem() {
		(itemRefs.value as HTMLElement[])[activeItemIndex.value]?.scrollIntoView({
			block: "nearest",
			inline: "nearest"
		});
	}

	const floating = ref(null);
	const tags = toRef(props, 'tags');

	const {x, y, strategy, placement} = useFloating(
		tags.value ? inputElTags : inputEl,
		floating,
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
