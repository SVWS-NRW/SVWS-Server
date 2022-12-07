<script setup lang="ts">
	import { type PropType, type ComputedRef } from "vue";
	import { genId } from "../../../utils";
	import TextInput from "../TextInput.vue";
	type Item = { id: number; text: string };

	const {
		placeholder,
		tags,
		autocomplete,
		disabled,
		statistics,
		items,
		itemText,
		itemSort,
		itemFilter,
		modelValue,
		headless
	} = defineProps({
		placeholder: { type: String, default: "" },
		title: { type: String, default: "" },
		tags: { type: Boolean, default: false },
		autocomplete: { type: Boolean, default: false },
		disabled: { type: Boolean, default: false },
		statistics: { type: Boolean, default: false },
		items: {
			type: [Array, Object],
			default() {
				return [];
			}
		},
		itemText: {
			type: Function as PropType<(item: Item) => string>,
			default(item: Item) {
				return item.text || "";
			}
		},
		itemSort: { type: Function as PropType<(a: Item, b: Item) => number>, default: null },
		itemFilter: { type: Function as PropType<(items: Item[], searchText: string) => Item[]>, default: null },
		// eslint-disable-next-line vue/require-default-prop
		modelValue: {
			type: [Object, Array] as PropType<Item | Item[]>
		},
		headless: { type: Boolean, default: false }
	});

	const emit = defineEmits<{
		(e: "update:modelValue", items: Array<Item | null> | Item | null): void;
		(e: "focus", event: Event): void;
		(e: "blur", event: Event): void;
	}>();

	const showList = ref(false);
	const itemRefs = shallowRef<HTMLLIElement[]>([]);
	const activeItemIndex = ref(-1);
	const listIdPrefix = genId();
	const showInput = computed(() => {
		switch (true) {
			default:
				return true;
			case tags && !autocomplete:
				return false;
			case tags && autocomplete && !showList.value:
				return false;
		}
	});

	// Input element
	const inputEl = ref<null | typeof TextInput>(null);
	const hasFocus = ref(false);
	const searchText = ref("");
	function onFocus() {
		hasFocus.value = true;
		// open();
	}
	function onBlur() {
		hasFocus.value = false;
		closeListbox();
	}
	const dynModelValue = computed<string>(() => {
		switch (true) {
			default:
				return generateInputText();
			case showList.value && autocomplete:
				return searchText.value;
		}
	});

	function generateInputText() {
		return tags
			? [...selectedItemList].map(item => itemText(item)).join(", ")
			: selectedItem.value
			? itemText(selectedItem.value)
			: "";
	}

	// eslint-disable-next-line @typescript-eslint/no-explicit-any
	function onInput(value: string | number): any {
		searchText.value = "" + value;
	}

	const selectedItem = shallowRef(Array.isArray(modelValue) ? modelValue[0] : modelValue);
	const selectedItemList = shallowReactive(
		new Set<Item>(Array.isArray(modelValue) ? modelValue : modelValue ? [modelValue] : [])
	);
	watch(
		() => modelValue,
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

	function isIterable(o: Item[] | object): o is Iterable<Item> {
		return Symbol.iterator in o;
	}
	const sortedList: ComputedRef<Item[]> = computed(() => {
		if (!isIterable(items)) return [];
		const arr: Item[] = Array.isArray(items) ? items : [...items];
		if (itemSort) return arr.sort(itemSort);
		return arr;
	});

	const filteredList: ComputedRef<Array<Item>> = computed(() => {
		if (autocomplete) {
			if (itemFilter) return itemFilter(sortedList.value, searchText.value);
			else
				return sortedList.value.filter(i => {
					return itemText(i).startsWith(searchText.value ?? "");
				});
		} else {
			return sortedList.value;
		}
	});

	function openListbox() {
		showList.value = true;
		if (selectedItem.value) {
			activeItemIndex.value = filteredList.value.findIndex(item => item === selectedItem.value);
			nextTick(() => scrollToActiveItem());
		}
		nextTick(() => {
			inputEl.value?.input.focus();
		});
	}

	function closeListbox() {
		showList.value = false;
		searchText.value = "";
		activeItemIndex.value = -1;
	}

	function selectCurrentActiveItem() {
		if (!showList.value) return;
		selectItem(filteredList.value[activeItemIndex.value]);
		if (!tags) closeListbox();
	}

	function selectItem(item: Item) {
		selectedItem.value = item;
		if (tags) {
			if (selectedItemList.has(item)) {
				selectedItemList.delete(item);
			} else selectedItemList.add(item);
		} else {
			selectedItemList.clear();
			selectedItemList.add(item);
		}
		emit("update:modelValue", tags ? [...selectedItemList] : selectedItem.value);
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

	function scrollToActiveItem() {
		(itemRefs.value as HTMLElement[])[activeItemIndex.value]?.scrollIntoView({
			block: "nearest",
			inline: "nearest"
		});
	}
</script>

<template>
	<div class="wrapper" :class="{ 'z-50': showList }">
		<div
			class="multiselect-input-component"
			:class="{ 'with-open-list': showList, 'multiselect-input-component--statistics': statistics }"
		>
			<div :class="['input', !showInput ? 'sr-only' : '']">
				<text-input
					ref="inputEl"
					:model-value="dynModelValue"
					:readonly="!autocomplete || !showInput"
					:placeholder="title"
					:statistics="statistics"
					:headless="headless"
					:disabled="disabled"
					role="combobox"
					:aria-label="placeholder"
					:aria-expanded="showList"
					aria-haspopup="listbox"
					aria-autocomplete="list"
					:aria-controls="showList ? listIdPrefix : null"
					:aria-activedescendant="activeItemIndex > -1 ? `${listIdPrefix}-${activeItemIndex}` : null"
					@update:model-value="onInput"
					@click="openListbox"
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
					@keydown.space="!autocomplete && (showList ? selectCurrentActiveItem() : openListbox())"
				/>
			</div>
			<div v-if="tags" class="tag-list-wrapper" @click.self="toggleListbox">
				<div class="tag-list" @click.self="toggleListbox">
					<slot v-if="!selectedItemList.size && !showList" name="no-content">
						<span class="py-1">{{ "Keine Auswahl" }}</span>
					</slot>
					<div v-for="(item, index) in selectedItemList" v-else :key="index" class="tag">
						<Badge size="small" variant="light" class="inline-flex items-center">
							<span>{{ itemText(item) }}</span>
							<span class="tag-remove ml-1" @click="removeTag(item)">
								<Icon class="mt-1">
									<i-ri-close-line />
								</Icon>
							</span>
						</Badge>
					</div>
				</div>
				<span v-if="!showInput" class="multiselect-tags--placeholder">
					{{ title }}
					<i-ri-bar-chart-fill v-if="statistics" class="ml-2" />
				</span>
			</div>
			<Icon class="dropdown-icon" @click="showList ? closeListbox() : openListbox()">
				<i-ri-arrow-up-s-line v-if="showList" />
				<i-ri-arrow-down-s-line v-else />
			</Icon>
		</div>
		<ul
			v-show="showList"
			:id="listIdPrefix"
			class="multiselect--items-wrapper"
			role="listbox"
			@mouseenter="activeItemIndex = -1"
		>
			<li
				v-for="(item, index) in filteredList"
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
				"
			>
				{{ itemText(item) }}
			</li>
		</ul>
	</div>
</template>

<style scoped lang="postcss">
	.multiselect-input-component {
		@apply flex;
		@apply relative;
		@apply w-full;
		@apply inline-block overflow-visible;
		@apply rounded border border-black border-opacity-20;
	}
	.multiselect-input-component--statistics {
		@apply border-purple;
		@apply bg-purple bg-opacity-5;
	}

	.multiselect-input-component :deep(.text-input--control),
	.multiselect-input-component :deep(.text-input-component:focus-within .text-input--control),
	.multiselect-input-component :deep(.text-input-component.text-input-filled .text-input--control) {
		/* hide border on the input itself since we style it in this component */
		@apply border-transparent;
	}

	.multiselect-input-component.with-open-list,
	.multiselect-input-component:focus-within {
		@apply border-opacity-100;
	}
	.multiselect-input-component.with-open-list {
		@apply rounded-b-none;
	}
	.wrapper {
		@apply relative;
		@apply bg-white;
	}

	.tags {
		@apply flex items-center justify-between;
		@apply relative z-30;
		@apply h-auto overflow-hidden;
	}
	.multiselect-tags--placeholder
	/* This class is only applied if the <text-input> is hidden, which usually porovides the placeholder */ {
		@apply absolute;
		@apply pointer-events-none;
		@apply text-input text-gray;
		@apply transform;
		@apply flex items-center;
		@apply -translate-y-2/3;
		@apply bg-white;
		@apply rounded;
		@apply px-1;
		@apply text-button;
		top: 0;
		left: -10px;
	}
	.multiselect-input-component--statistics .multiselect-tags--placeholder {
		@apply text-purple;
	}

	.dropdown-icon {
		@apply absolute py-1 px-2;
		@apply flex;
		@apply inset-y-0 right-0;
		@apply items-center justify-center;
	}

	.tag-list {
		@apply flex flex-wrap gap-1 pr-4;
	}

	.multiselect--items-wrapper {
		@apply absolute z-20 max-h-64 w-full;
		@apply divide-light divide-y;
		@apply border-gray rounded rounded-t-none border border-t-0;
		@apply -mt-2 pt-2;
		@apply overflow-y-auto overflow-x-hidden;
		@apply shadow;
	}

	.multiselect--item {
		@apply text-dark bg-white;
		@apply text-body;
		@apply py-2 px-4;
	}

	.multiselect--items-wrapper .multiselect--item.active {
		box-shadow: inset 0 0 0 2px theme("colors.orange");
	}
	.multiselect--items-wrapper .multiselect--item:hover {
		@apply bg-light;
		/* @apply border-y; */
		@apply cursor-pointer;
		@apply text-dark;
	}
	.multiselect--items-wrapper .multiselect--item.selected {
		@apply bg-primary text-white;
	}

	.multiselect--input--open {
		@apply rounded-b-none;
	}

	.tag-list-wrapper {
		@apply flex h-9 w-full items-center justify-between space-x-4 overflow-x-auto px-1;
	}
</style>
