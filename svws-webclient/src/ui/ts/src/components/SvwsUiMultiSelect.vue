<script setup lang="ts" generic="Item">
	import type { ComputedRef, ShallowRef } from "vue";
	import type TextInput from "./SvwsUiTextInput.vue";
	import { computed, nextTick, ref, shallowReactive, shallowRef, watch, Teleport } from "vue";
	import { useFloating, autoUpdate, flip, offset, shift, size } from "@floating-ui/vue";
	import { genId } from "../utils";

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
		modelValue: Item[] | Item | undefined;
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
		headless: false,
		removable: false,
		span: undefined
	})

	const emit = defineEmits<{
		(e: "update:modelValue", items: Item[] | Item | null | undefined): void;
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
		return props.tags
			? [...selectedItemList].map(item => props.itemText(item)).join(", ")
			: selectedItem.value
				? props.itemText(selectedItem.value)
				: "";
	}

	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	function onInput(value: string | number): any {
		searchText.value = "" + value;
	}

	const selectedItem: ShallowRef<Item|undefined> = shallowRef(Array.isArray(props.modelValue) ? props.modelValue[0] : props.modelValue);
	const selectedItemList = shallowReactive(
		new Set<Item>(Array.isArray(props.modelValue) ? props.modelValue : props.modelValue ? [props.modelValue] : [])
	);
	watch(
		() => props.modelValue,
		newVal => {
			selectedItem.value = Array.isArray(newVal) ? newVal[0] : newVal;
			selectedItemList.clear();
			if (Array.isArray(newVal)) {
				newVal.forEach(item => selectedItemList.add(item));
			} else if (newVal) {
				selectedItemList.add(newVal);
			}
		}
	);

	const sortedList: ComputedRef<Item[]> = computed(() => {
		let arr
		if (Array.isArray(props.items))
			arr = props.items;
		else if (props.items instanceof Map)
			arr = [...props.items.values()];
		else
			arr = [...props.items];
		if (props.itemSort) return arr.sort(props.itemSort);
		return arr;
	});

	const filteredList: ComputedRef<Item[]> = computed(() => {
		if (props.autocomplete) {
			if (props.itemFilter) return props.itemFilter(sortedList.value, searchText.value);
			else
				return sortedList.value.filter(i => {
					return props.itemText(i).startsWith(searchText.value ?? "");
				});
		} else {
			return sortedList.value;
		}
	});

	function openListbox() {
		showList.value = true;
		if (selectedItem.value) {
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
		if (!showList.value) return;
		selectItem(filteredList.value[activeItemIndex.value]);
		if (!props.tags) closeListbox();
	}

	function selectItem(item: Item | undefined) {
		selectedItem.value = item;
		if (item) {
			if (props.tags) {
				if (selectedItemList.has(item)) {
					selectedItemList.delete(item);
				} else selectedItemList.add(item);
			} else {
				selectedItemList.clear();
				selectedItemList.add(item);
			}
		}
		emit("update:modelValue", props.tags ? [...selectedItemList] : selectedItem.value);
	}

	function removeTag(item: Item) {
		selectedItemList.delete(item);
		emit("update:modelValue", [...selectedItemList]);
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
		if (activeItemIndex.value < listLength - 1) activeItemIndex.value++;
		else activeItemIndex.value = 0;
		scrollToActiveItem();
	}

	function onArrowUp() {
		const listLength = filteredList.value.length;
		if (activeItemIndex.value === 0) activeItemIndex.value = listLength - 1;
		else if (activeItemIndex.value >= 1) activeItemIndex.value--;
		scrollToActiveItem();
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

	function scrollToActiveItem() {
		(itemRefs.value as HTMLElement[])[activeItemIndex.value]?.scrollIntoView({
			block: "nearest",
			inline: "nearest"
		});
	}

	function removeItem() {
		selectItem(undefined);
	}

	const floating = ref(null);

	const {x, y, strategy, placement} = useFloating(
		props.tags ? inputElTags : inputEl,
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
</script>

<template>
	<div class="wrapper"
		:class="{ 'z-50': showList, 'wrapper--tag-list' : tags, 'wrapper--filled': !!selectedItem || showList, 'col-span-full': span === 'full', 'wrapper--headless': headless }">
		<div class="multiselect-input-component"
			:class="{ 'with-open-list': showList, 'multiselect-input-component--statistics': statistics, 'with-value': !!selectedItem, 'multiselect-input-component--danger': danger, 'multiselect-input-component--disabled': disabled }">
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
					@keydown.esc.prevent="onEscape"
					@keydown.space="onSpace" />
			</div>
			<div v-if="tags" class="tag-list-wrapper"
				@click.self="toggleListbox" ref="inputElTags">
				<div class="tag-list" @click.self="toggleListbox">
					<slot v-if="!selectedItemList.size && !showList" name="no-content" />
					<div class="opacity-50 pl-2 text-sm" v-if="!selectedItemList.size && showList">
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
					{{ title }}
					<i-ri-bar-chart-2-line v-if="statistics" class="ml-1" />
				</span>
			</div>
			<div v-if="removable && modelValue" @click="removeItem" class="remove-icon">
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
					<span v-if="filteredList.length === 0" class="px-1 py-1 text-base opacity-50 inline-block">
						Keine Ergebnisse
					</span>
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
						{{ itemText(item) }}
						<i-ri-check-line v-if="selectedItemList.has(item)" class="multiselect--check opacity-75" />
					</li>
				</ul>
			</div>
		</Teleport>
	</div>
</template>

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

	.data-table & {
		@apply p-0;

		.icon {
			@apply rounded-none;
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

	.data-table & {
		right: 1em;
		font-size: 0.9em;
	}

	&:hover {
		@apply opacity-100;
	}

	.icon {
		@apply rounded-full;
		padding: 1px;
	}
}
</style>
