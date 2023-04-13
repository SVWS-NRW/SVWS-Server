<script setup lang="ts">
	import { type PropType, type ComputedRef, computed, nextTick, ref, shallowReactive, shallowRef, watch, Teleport } from "vue";
	import { genId } from "../../utils";
	import type TextInput from "./SvwsUiTextInput.vue";
	import { useFloating, autoUpdate, flip, offset, shift, size } from "@floating-ui/vue";

	type Item = Record<string, any>;

	const props = defineProps({
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

	const selectedItem = shallowRef(Array.isArray(props.modelValue) ? props.modelValue[0] : props.modelValue);
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

	function isIterable(o: Item[] | object): o is Iterable<Item> {
		return Symbol.iterator in o;
	}

	const sortedList: ComputedRef<Item[]> = computed(() => {
		if (!isIterable(props.items)) return [];
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

	const filteredList: ComputedRef<Array<Item>> = computed(() => {
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

	function onSpace (e: InputEvent) {
		if (!props.autocomplete)	{
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
				apply({ rects, elements }) {
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
	<div class="wrapper" :class="{ 'z-50': showList, 'wrapper--tag-list' : tags, 'wrapper--filled': !!selectedItem || showList }">
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
					@keydown.space="onSpace"
					:rounded="rounded" />
			</div>
			<div v-if="tags" class="tag-list-wrapper" :class="{'tag-list-wrapper--rounded': rounded}" @click.self="toggleListbox" ref="inputElTags">
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
			<div class="dropdown-icon" @click="toggleListbox">
				<svws-ui-icon>
					<i-ri-arrow-up-s-line v-if="showList" class="pb-0.5" />
					<i-ri-arrow-down-s-line v-else class="pt-0.5" />
				</svws-ui-icon>
			</div>
		</div>
		<Teleport to="body">
			<div v-show="showList" class="multiselect--items-wrapper" :style="{ position: strategy, top: floatingTop, left: floatingLeft }"
				ref="floating">
				<ul :id="listIdPrefix"
					class="multiselect--items-list"
					:class="{'multiselect--items-list--tags' : tags}"
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
		@apply border-purple;
		@apply bg-purple bg-opacity-[0.02];

		.multiselect-tags--placeholder {
			@apply text-purple;
		}
	}

	&--danger {
		@apply text-error;
	}

	&.with-open-list,
	&.with-value,
	&:focus-within {
		@apply border-opacity-100;

		.dropdown-icon {
			@apply opacity-100;
		}
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

	&:not(.with-open-list):not(.with-value):not(:focus-within):hover .multiselect-tags--placeholder {
		@apply opacity-60;
	}

	&.with-value,
	&:focus-within {
		.multiselect-tags--placeholder {
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

		.tag-list-wrapper {
			@apply border-gray border-opacity-100;
			@apply outline-none;
		}
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

	.multiselect-input-component--disabled & {
		@apply pointer-events-none;
	}
}

.tag-list {
	@apply flex flex-wrap gap-1 pr-4;
}

.multiselect--items-wrapper {
	@apply absolute z-50 w-full min-w-[11rem];
	@apply rounded-md border border-black bg-white;
	@apply shadow-xl shadow-black/25;
	@apply overflow-hidden;
}

.multiselect--items-list {
	@apply overflow-y-auto overflow-x-hidden;
	@apply py-1 px-2;
	max-height: 40ex;

	.multiselect--item {
		@apply text-black bg-white rounded border border-transparent;
		@apply text-base;
		@apply py-2 my-1 px-2;

		&.active {
			@apply ring ring-primary ring-opacity-25 border-primary;
		}

		&:hover {
			@apply cursor-pointer;
			@apply border-primary text-primary;
		}

		&.selected {
			@apply font-bold text-primary bg-primary bg-opacity-5;
		}
	}

	&--tags {
		.multiselect--item.selected {
			&.active {
				@apply ring-black/25;
			}

			&:hover,
			&.active {
				@apply border-black text-black bg-transparent;

				&:after {
					@apply opacity-75 font-normal;
					content: ' entfernen';
				}
			}
		}
	}
}

.tag-list-wrapper {
	@apply flex w-full items-center justify-between overflow-x-auto;
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

.wrapper--tag-list .dropdown-icon {
	.icon, svg {
		@apply h-full;
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
</style>
