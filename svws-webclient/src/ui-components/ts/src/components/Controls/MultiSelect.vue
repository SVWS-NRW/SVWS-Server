<script setup lang="ts">
import { ComputedRef } from 'vue';
import { type PropType } from 'vue';
import TextInput from './TextInput.vue';
type Item = { id: number; text: string; }

const { placeholder, tags, autocomplete, disabled, statistics, items, itemText, itemSort, itemFilter, modelValue, headless } = defineProps({
	placeholder: { type: String, default: '' },
	title: { type: String, default: '' },
	tags: { type: Boolean, default: false },
	autocomplete: { type: Boolean, default: false },
	disabled: { type: Boolean, default: false },
	statistics: { type: Boolean, default: false },
	items: { type: [Array, Object], default() { return [] } },
	itemText: { type: Function as PropType<(item: Item) => string>, default(item: Item) { return item.text || "" } },
	itemSort: { type: Function as PropType<(a: Item, b: Item) => number>, default: null },
	itemFilter: { type: Function as PropType<(items: Item[], searchText: string) => Item[]>, default: null },
	modelValue: { type: Object, default() { return null } },
	headless: { type: Boolean, default: false }
});

const emit = defineEmits<{
	(e: 'update:modelValue', items: Array<Item | null> | Item | null): void;
	(e: 'focus', event: Event): void;
	(e: 'blur', event: Event): void;
}>();
const inputEl = ref<null | typeof TextInput>(null);
const selectedItem = ref(Array.isArray(modelValue) ? modelValue[0] : modelValue);
const selectedItemList = ref(Array.isArray(modelValue) ? modelValue : [modelValue]);
const selectedItemIndex = ref(0);
const focused = ref(false);
const input = ref(!tags);
const searchText = ref('');
const itemRefs = ref([]);

const search = computed({
	get(): string {
		if (!modelValue) return "";
		return itemText(selectedItem.value);
	},
	set(v: string) {
		searchText.value = v.toString();
	}
});

watch(() => modelValue, (newVal) => {
	selectedItem.value = Array.isArray(newVal) ? newVal[0] : newVal;
	selectedItemList.value = Array.isArray(newVal) ? newVal : [newVal];
})

const list = computed(() => {
	if (autocomplete && filteredList.value.length) {
		return (
			focused.value &&
			filteredList.value.length &&
			searchText.value.length > 1
		);
	}

	return focused.value;
});

function isIterable(o: Item[] | object): o is Iterable<Item> {
	return Symbol.iterator in o;
}
const sortedList: ComputedRef<Item[]> = computed(() => {
	if (!isIterable(items)) return [];
	const arr: Item[] = Array.isArray(items) ? items : [...items]
	if (itemSort)
		return arr.sort(itemSort);
	return arr
});

const filteredList: ComputedRef<Array<Item>> = computed(() => {
	if (autocomplete) {
		if (itemFilter)
			return itemFilter(sortedList.value, searchText.value);
		else
			return sortedList.value.filter(i => { itemText(i).startsWith(searchText.value) })
	} else {
		return sortedList.value;
	}
});

function select() {
	if (!list.value) return;
	selectItem(
		filteredList.value[selectedItemIndex.value - 1]
	);
}

function selectItem(item: Item) {
	selectedItem.value = item;
	if (tags) {
		if (selectedItemList.value.includes(item)) {
			selectedItemList.value.splice(
				selectedItemList.value.indexOf(item),
				1
			);
		} else selectedItemList.value.push(item);
	} else selectedItemList.value = [item];
	inputEl.value?.input.blur();
	search.value = itemText(item);
	emit(
		"update:modelValue",
		tags ? selectedItemList.value : selectedItem.value
	);
}

function removeTag(index: number) {
	selectedItemList.value.splice(index, 1);
}

function onFocus(event: Event) {
	focused.value = true;
	emit("focus", event);
}

function onClickTags() {
	if (autocomplete) {
		if (input.value) {
			input.value = false;
			inputEl.value?.input.blur();
			focused.value = false;
		} else {
			input.value = true;
			nextTick(() => inputEl.value?.input.focus());
		}
	} else {
		focused.value = !focused.value;
	}
}

function blur() {
	inputEl.value?.input.blur();
	focused.value = false;
	search.value = "";
}

function onBlur() {
	if (!tags) focused.value = false;
	search.value = "";

	// Entferne die 'active' Klasse vom aktiven Element
	Array.from(
		document.getElementsByClassName(
			"multiselect--item active"
		)
	).forEach(element => {
		element.classList.remove("active");
	});
	// Setze den aktiven Index auf 0 um wieder oben in der Liste zu beginnen
	selectedItemIndex.value = 0;
}

function onArrowDown() {
	const list = filteredList.value.length;
	if (selectedItemIndex.value < list) selectedItemIndex.value++;
	else if (selectedItemIndex.value === list)
		selectedItemIndex.value = 1;
	scrollToActiveItem();
}

function onArrowUp() {
	const list = filteredList.value.length;
	if (
		selectedItemIndex.value === 0 ||
		selectedItemIndex.value === 1
	)
		selectedItemIndex.value = list;
	else if (selectedItemIndex.value > 1)
		selectedItemIndex.value--;
	scrollToActiveItem();
}

function scrollToActiveItem() {
	(itemRefs.value as HTMLElement[])[selectedItemIndex.value]?.scrollIntoView({
		block: "center",
		inline: "nearest"
	});
}
</script>

<template>
	<div class="wrapper" :class="{ 'z-50': list }">
		<div
v-if="tags" class="text-input--control tags" :class="{
			'multiselect--input--open': list,
			'text-input--headless': headless,
		}" tabindex="1" @blur="blur" @click.self="onClickTags">
			<div class="tag-list-wrapper" @click.self="onClickTags">
				<div class="tag-list" @click.self="onClickTags">
					<slot v-if="!selectedItemList.length" name="no-content"><span class="py-1">Keine Auswahl</span>
					</slot>
					<div v-for="(item, index) in selectedItemList" v-else :key="index" class="tag">
						<Badge size="small" variant="light" class="inline-flex items-center">
							<span>{{ itemText(item) }}</span>
							<span class="tag-remove ml-1" @click="removeTag(index)">
								<Icon class="mt-1">
									<i-ri-close-line />
								</Icon>
							</span>
						</Badge>
					</div>
				</div>
				<Icon class="dropdown--icon dropdown-icon" @click.self="onClickTags">
					<i-ri-arrow-up-s-line v-if="focused" class="pointer-events-none" />
					<i-ri-arrow-down-s-line v-else class="pointer-events-none" />
				</Icon>
			</div>
		</div>
		<div v-show="input" class="input">
			<label
class="text-input-component" :class="{
				'text-input-filled': !!modelValue,
				'text-input-focus': focused,
				'text-input-disabled': disabled
			}">
				<text-input
ref="inputEl" :class="{
					'multiselect--input--open': input
				}" :model-value="search" :readonly="!autocomplete" :placeholder="title" :statistics="statistics" :headless="headless"
					:disabled="disabled" @update:model-value="value => search = String(value)" @focus="onFocus" @blur="onBlur"
					@keydown.down.prevent="onArrowDown" @keydown.up.prevent="onArrowUp" @keydown.enter.tab.prevent="select"
					@keydown.esc.prevent="blur" />
				<span v-if="placeholder" class="text-input--placeholder">
					{{ placeholder }}
				</span>
				<Icon class="dropdown--icon">
					<i-ri-arrow-up-s-line v-if="focused" />
					<i-ri-arrow-down-s-line v-else />
				</Icon>
			</label>
		</div>
		<ul v-show="list" class="multiselect--items-wrapper">
			<li
v-for="(item, index) in filteredList" :key="index" ref="itemRefs" class="multiselect--item" :class="{
				active: selectedItemIndex === index + 1,
				selected: selectedItemList.includes(item)
			}" @mousedown.prevent @click="selectItem(item)" @mouseenter="selectedItemIndex = index + 1">
				{{ itemText(item) }}
			</li>
		</ul>
	</div>
</template>

<style scoped>
.text-input-component {
	@apply overflow-visible;
}

.wrapper {
	@apply relative;
}

.tags {
	@apply flex items-center justify-between;
	@apply relative z-30;
	@apply h-auto overflow-hidden;
}

.dropdown-icon {
	@apply absolute right-0 flex-shrink-0 bg-white py-1 px-1;
}

.tag-list {
	@apply flex flex-wrap gap-1 pr-4;
}

.multiselect--input--open {
	@apply rounded-b-none border-gray;
}

.input {
	@apply relative z-30;
}

.multiselect--items-wrapper {
	@apply absolute z-20 max-h-64 w-full;
	@apply divide-y divide-light;
	@apply rounded border border-t-0 border-gray;
	@apply -mt-2 pt-2;
	@apply overflow-y-auto overflow-x-hidden;
	@apply shadow;
}

.multiselect--item {
	@apply bg-white text-dark;
	@apply text-body;
	@apply py-2 px-4;
}

.multiselect--item:hover {
	@apply bg-light;
	@apply cursor-pointer;
}

.multiselect--item.selected {
	@apply bg-primary text-white;
}

.multiselect--input--open {
	@apply rounded-b-none;
}

.tag-list-wrapper {
	@apply flex w-full items-center justify-between space-x-4 overflow-x-auto;
}

.active {
	@apply bg-dark-20;
}
</style>
