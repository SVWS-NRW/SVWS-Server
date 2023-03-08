<script setup lang="ts">
	import { type PropType, type ComputedRef } from "vue";
	import { genId } from "../../../utils";
	import TextInput from "../SvwsUiTextInput.vue";

	type Item = Record<string, any>;

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
		headless,
		removable,
		rounded,
		danger
	} = defineProps({
		placeholder: {type: String, default: ""},
		title: {type: String, default: ""},
		tags: {type: Boolean, default: false},
		autocomplete: {type: Boolean, default: false},
		disabled: {type: Boolean, default: false},
		statistics: {type: Boolean, default: false},
		danger: {type: Boolean, default: false},
		items: {
			type: [Array, Object],
			default() {
				return [];
			}
		},
		itemText: {
			type: Function as PropType<(item: any) => string>,
			default(item: Item) {
				return item.text ?? "";
			}
		},
		itemSort: {type: Function as PropType<(a: any, b: any) => number>, default: null},
		itemFilter: {type: Function as PropType<(items: any[], searchText: string) => any[]>, default: null},
		// eslint-disable-next-line vue/require-default-prop
		modelValue: {
			type: [Object, Array] as PropType<Item | Item[]>
		},
		headless: {type: Boolean, default: false},
		removable: {type: Boolean, default: false},
		rounded: {type: Boolean, default: false}
	});

	const emit = defineEmits<{
		(e: "update:modelValue", items: Array<Item | null> | Item | null | undefined): void;
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
				return generateInputText() ?? '';
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
		let arr
		if (Array.isArray(items))
			arr = items;
		else if (items instanceof Map)
			arr = [...items.values()];
		else
			arr = [...items];
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
			void nextTick(() => scrollToActiveItem());
		}
		void nextTick(() => inputEl.value?.input.focus());
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

	function selectItem(item: Item | undefined) {
		selectedItem.value = item;
		if (item) {
			if (tags) {
				if (selectedItemList.has(item)) {
					selectedItemList.delete(item);
				} else selectedItemList.add(item);
			} else {
				selectedItemList.clear();
				selectedItemList.add(item);
			}
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

	function onSpace (e: InputEvent) {
		if (!autocomplete)	{
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
</script>

<template>
	<div class="wrapper" :class="{ 'z-50': showList, 'wrapper--tag-list' : tags, 'wrapper--filled': !!selectedItem || showList }">
		<div class="multiselect-input-component"
			:class="{ 'with-open-list': showList, 'multiselect-input-component--statistics': statistics, 'with-value': !!selectedItem, 'multiselect-input-component--danger': danger }">
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
					@keydown.space="onSpace"
					:rounded="rounded" />
			</div>
			<div v-if="tags" class="tag-list-wrapper" :class="{'tag-list-wrapper--rounded': rounded}" @click.self="toggleListbox">
				<div class="tag-list" @click.self="toggleListbox">
					<slot v-if="!selectedItemList.size && !showList" name="no-content" />
					<div v-for="(item, index) in selectedItemList" v-else :key="index" class="tag">
						<span class="tag-badge">
							<span>{{ itemText(item) }}</span>
							<span class="tag-remove ml-1" @click="removeTag(item)">
								<svws-ui-icon>
									<i-ri-close-line />
								</svws-ui-icon>
							</span>
						</span>
					</div>
				</div>
				<span v-if="!showInput" class="multiselect-tags--placeholder">
					{{ title }}
					<i-ri-bar-chart-fill v-if="statistics" class="ml-2" />
				</span>
			</div>
			<div v-if="removable && modelValue" @click="removeItem" class="remove-icon">
				<svws-ui-icon>
					<i-ri-close-line />
				</svws-ui-icon>
			</div>
			<div class="dropdown-icon" @click="showList ? closeListbox() : openListbox()">
				<svws-ui-icon>
					<i-ri-arrow-up-s-line v-if="showList" class="pb-0.5" />
					<i-ri-arrow-down-s-line v-else class="pt-0.5" />
				</svws-ui-icon>
			</div>
		</div>
		<ul v-show="showList"
			:id="listIdPrefix"
			class="multiselect--items-wrapper"
			role="listbox"
			@mouseenter="activeItemIndex = -1">
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
				{{ itemText(item) }}
			</li>
		</ul>
	</div>
</template>

<style lang="postcss" scoped>
.multiselect-input-component {
	@apply flex;
	@apply relative;
	@apply w-full;
	@apply inline-block overflow-visible;
}

.multiselect-input-component--statistics {
	@apply border-purple;
	@apply bg-purple bg-opacity-[0.02];
}

.multiselect-input-component--danger {
	@apply text-error;
}


.multiselect-input-component.with-open-list,
.multiselect-input-component.with-value,
.multiselect-input-component:focus-within {
	@apply border-opacity-100;
}

.wrapper {
	@apply relative;
}

.multiselect-input-component--statistics .multiselect-tags--placeholder {
	@apply text-purple;
}

.multiselect-tags--placeholder {
	@apply absolute;
	@apply pointer-events-none;
	@apply opacity-40;
	@apply transform;
	@apply flex items-center;

	top: 0.5em;
	left: 0.7em;
	line-height: 1.33;
}

.multiselect-input-component:not(.with-open-list):not(.with-value):not(:focus-within):hover .multiselect-tags--placeholder {
	@apply opacity-60;
}

.multiselect-input-component:focus-within .multiselect-tags--placeholder,
.with-value .multiselect-tags--placeholder {
	@apply -translate-y-1/2;
	@apply bg-white opacity-100;
	@apply rounded;
	@apply px-1;

	top: -0.2em;
	left: 0.7em;
	font-size: 0.78rem;

	&:after {
		content: '';
	}
}

.dropdown-icon {
	@apply absolute p-1;
	@apply flex;
	@apply inset-y-0 right-0;
	@apply items-center justify-center cursor-pointer text-base;
	@apply opacity-50;

	&:hover {
		@apply opacity-100;
	}

	.icon {
		@apply py-0.5;
		font-size: 1.2em;
	}

	&:hover .icon {
		@apply bg-black text-white rounded;
	}
}

.multiselect-input-component.with-open-list,
.multiselect-input-component.with-value {
	.dropdown-icon {
		@apply opacity-100;
	}
}

.tag-list {
	@apply flex flex-wrap gap-1 pr-4;
}

.multiselect--items-wrapper {
	@apply absolute z-30 w-full;
	@apply rounded-lg border border-light bg-white;
	@apply mt-0.5 px-2 py-1;
	@apply overflow-y-auto overflow-x-hidden;
	@apply shadow-md;
	-webkit-overflow-scrolling: touch;
	max-height: 36.3ex;
}

.multiselect--item {
	@apply text-black bg-white rounded-md;
	@apply text-input;
	@apply py-2 my-1 px-2;
}

.multiselect--items-wrapper .multiselect--item.active {
	@apply bg-primary bg-opacity-5;
	@apply ring ring-primary ring-opacity-50;
}

.multiselect--items-wrapper .multiselect--item:hover {
	@apply bg-light;
	@apply cursor-pointer;
	@apply text-black;
}

.multiselect--items-wrapper .multiselect--item.selected {
	@apply font-bold text-primary bg-primary bg-opacity-5;
}

.wrapper--tag-list .multiselect--items-wrapper .multiselect--item.selected {
	&:hover,
	&.active {
		&:after {
			@apply opacity-50;
			content: ' entfernen';
		}
	}
}

.tag-list-wrapper {
	@apply flex w-full items-center justify-between overflow-x-auto px-1 bg-white;
}

.tag-list-wrapper {
	@apply bg-white;
	@apply rounded-md border border-black border-opacity-20;
	@apply w-full;
	@apply text-base;
	@apply whitespace-nowrap;
	@apply h-full;
	@apply cursor-pointer;
	padding: 0.3em 1.7em 0.3em 0.35em;
	min-height: 2.25em;

	&--rounded {
		@apply rounded-full;
		padding: 0.5em 0.7em;

		.dropdown-icon {
			@apply right-0 top-0;
		}
	}
}

.tag-badge {
	@apply rounded cursor-auto relative z-10;
	@apply bg-primary bg-opacity-5 text-primary border-primary border border-opacity-10;
	@apply flex items-center leading-none;
	padding: 0.2em 0.4em 0.2em 0.7em;

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
	@apply bg-black text-white rounded;
}

.remove-icon {
	@apply absolute p-1 inset-y-0;
	@apply cursor-pointer;
	@apply flex items-center justify-center;
	right: 1.65rem;
	font-size: 0.8rem;

	.icon {
		@apply rounded-full;
		padding: 1px;
	}
}

.wrapper--tag-list .dropdown-icon {
	.icon, svg {
		@apply h-full;
	}
}

.multiselect-input-component:focus-within .tag-list-wrapper,
.with-value .tag-list-wrapper {
	@apply border-gray border-opacity-100;
	@apply outline-none;
}
</style>
