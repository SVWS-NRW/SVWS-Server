<template>
	<div class="svws-ui-select svws-ui-multiselect" :class="{
		'svws-open': showList,
		'svws-has-value': hasSelected(),
		'svws-headless': headless,
		'svws-statistik': statistics,
		'svws-danger': danger,
		'svws-readonly': readonly,
		'svws-disabled': disabled,
		'svws-autocomplete': autocomplete
	}" v-bind="$attrs" ref="inputElTags">
		<div v-if="!headless && (data.size > 0)" class="svws-tags">
			<span v-for="(item, index) in data" :key="index" class="svws-tag">
				<span class="line-clamp-1 leading-tight break-all max-w-[14rem]">{{ itemText(item) }}</span>
				<button v-if="!readonly" role="button" class="svws-remove" @click.stop="removeTag(item)" title="Entfernen">
					<span class="icon i-ri-close-line" />
				</button>
			</span>
		</div>
		<svws-ui-text-input ref="inputEl"
			:model-value="headless ? dynModelValue : (data.size ? ' ' : '')"
			:readonly="!autocomplete || readonly"
			:is-select-input="!readonly"
			:placeholder="label || title"
			:statistics
			:headless
			:disabled
			:required
			:debounce-ms="0"
			role="combobox"
			:aria-label="label || title"
			:aria-expanded="showList"
			aria-haspopup="listbox"
			aria-autocomplete="list"
			:aria-controls="showList ? listIdPrefix : null"
			:aria-activedescendant="refList && refList.activeItemIndex > -1 ? `${listIdPrefix}-${refList.activeItemIndex}` : null"
			@update:model-value="value => searchText = value ?? ''"
			@click.stop="toggleListBox"
			@focus="onInputFocus"
			@methods="methods => methodsTextField = methods"
			@blur="onInputBlur"
			@keyup.down.prevent
			@keyup.up.prevent
			@keydown.down.prevent="onArrowDown"
			@keydown.up.prevent="onArrowUp"
			@keydown.enter.prevent="selectCurrentActiveItem"
			@keydown.backspace="onBackspace"
			@keydown.esc.prevent="toggleListBox"
			@keydown.space.prevent="onSpace"
			@keydown.tab="onTab" />
		<button v-if="!readonly" role="button" class="svws-dropdown-icon" @keydown.enter="toggleListBox" @keydown.down="toggleListBox">
			<span class="icon i-ri-expand-up-down-line" v-if="headless" />
			<span class="icon i-ri-expand-up-down-fill" v-else />
		</button>
	</div>
	<Teleport to="body" v-if="isMounted">
		<svws-ui-dropdown-list v-if="showList" :statistics :filtered-list :item-text :strategy :floating-left :floating-top :selected-item-list="data" :select-item ref="refList" />
	</Teleport>
</template>


<script setup lang="ts" generic="Item">

	import type { Ref } from "vue";
	import { computed, nextTick, ref, watch, onMounted, shallowRef, toRaw, useId } from "vue";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { MaybeElement } from "@floating-ui/vue";
	import { useFloating, autoUpdate, flip, offset, shift, size } from "@floating-ui/vue";
	import SvwsUiDropdownList from "./SvwsUiDropdownList.vue";

	const props = withDefaults(defineProps<{
		label?: string;
		title?: string;
		autocomplete?: boolean;
		disabled?: boolean;
		readonly?: boolean;
		statistics?: boolean;
		danger?: boolean;
		items: Iterable<Item> | Map<number, Item>;
		itemText: (item: Item) => string;
		itemSort?: (a: Item, b: Item) => number;
		itemFilter?: ((items: Iterable<Item>, searchText: string) => Item[]) | ((items: Item[], searchText: string) => Item[]);
		modelValue: Item[];
		useNull?: boolean;
		headless?: boolean;
		required?: boolean;
		autofocus?: boolean;
	}>(), {
		label: '',
		title: '',
		autocomplete: false,
		disabled: false,
		readonly: false,
		statistics: false,
		danger: false,
		itemSort: (a: Item, b: Item) => 0,
		itemFilter: (items: Iterable<Item> | Item[], searchText: string) => Array.isArray(items) ? items : [...items],
		useNull: false,
		headless: false,
	})

	const emit = defineEmits<{
		(e: "update:modelValue", items: Item[]): void;
		(e: "blur"): void;
	}>();

	const methodsTextField = shallowRef<{ focus: () => void } | undefined>(undefined);

	const refList = ref<ComponentExposed<typeof SvwsUiDropdownList>>();

	const showList = ref(false);
	const listIdPrefix = useId();

	const inputEl = ref(null);
	const inputElTags = ref(null);
	const hasFocus = ref(false);
	const searchText = ref("");
	const isMounted = ref(false);

	onMounted(() => {
		isMounted.value = true;
		if (props.autofocus) {
			onInputFocus();
			toggleListBox();
		}
	});

	function onInputFocus() {
		hasFocus.value = true;
	}

	function onInputBlur() {
		hasFocus.value = false;
		closeListbox();
		emit('blur');
	}

	const dynModelValue = computed<string>(() => {
		switch (true) {
			case showList.value && props.autocomplete:
				return searchText.value;
			default:
				return generateInputText();
		}
	});

	function generateInputText() {
		return [...data.value].map(item => props.itemText(item)).join(", ");
	}

	const rawModelValues = computed(() => {
		const set = new Set<Item>();
		for (const item of props.modelValue)
			set.add(toRaw(item));
		return set;
	})

	const data = shallowRef(new Set(rawModelValues.value));

	watch(rawModelValues, (value) => updateData(value, true), { immediate: false });

	function updateData(newValueSet: Set<Item>, fromModelValue: boolean) {
		if (((data.value.size === newValueSet.size) && (data.value.difference(newValueSet).size === 0)) || props.disabled)
			return;
		data.value = newValueSet;
		if (!fromModelValue)
			emit("update:modelValue", [...newValueSet]);
	}

	const selectedItem = computed({
		get: () => {
			const [ e ] = data.value.keys();
			return e;
		},
		set: (item) => {
			if ((item !== null) && (item !== undefined)) {
				const newSelectedItems = new Set(data.value);
				if (newSelectedItems.has(item))
					newSelectedItems.delete(item);
				else
					newSelectedItems.add(item);
				updateData(newSelectedItems, false);
			}
		},
	});

	function hasSelected(): boolean {
		return (selectedItem.value !== null) && (selectedItem.value !== undefined);
	}

	function selectItem(item: Item) {
		selectedItem.value = item;
		if (props.autocomplete)
			searchText.value = "";
		doFocus();
	}

	function removeTag(item: Item) {
		if (data.value.has(item))
			selectedItem.value = item;
	}

	const sortedList = computed<Item[]>(() => {
		let arr
		if (Array.isArray(props.items))
			arr = props.items;
		else if (props.items instanceof Map)
			arr = [...props.items.values()];
		else
			arr = [...props.items];
		return arr.sort(props.itemSort);
	});

	const filteredList = computed<Item[]>(() => {
		if (props.autocomplete)
			return props.itemFilter(sortedList.value, searchText.value);
		return sortedList.value;
	});

	function doFocus() {
		void nextTick(() => methodsTextField.value?.focus());
	}

	function toggleListBox() {
		if (showList.value)
			closeListbox();
		else
			openListbox();
	}

	function openListbox() {
		showList.value = true;
		if ((selectedItem.value !== null) && (selectedItem.value !== undefined) && (refList.value !== undefined) && (refList.value !== null)) {
			refList.value.activeItemIndex = filteredList.value.findIndex(item => item === selectedItem.value);
			void nextTick(() => scrollToActiveItem());
		}
		void nextTick(() => methodsTextField.value?.focus());
	}

	function closeListbox() {
		if ((refList.value !== undefined) && (refList.value !== null))
			refList.value.activeItemIndex = -1;
		showList.value = false;
	}

	function selectCurrentActiveItem() {
		if (showList.value && (refList.value !== undefined) && (refList.value !== null)) {
			if (filteredList.value.length === 0)
				toggleListBox();
			selectItem(filteredList.value[refList.value.activeItemIndex]);
		}
	}

	// Arrow Navigation
	function onArrowDown() {
		if ((!showList.value) || (refList.value === undefined) || (refList.value === null))
			return openListbox();
		const listLength = filteredList.value.length;
		if (refList.value.activeItemIndex < listLength - 1)
			refList.value.activeItemIndex++;
		else
			refList.value.activeItemIndex = 0;
		scrollToActiveItem();
	}

	function onArrowUp() {
		if ((!showList.value) || (refList.value === undefined) || (refList.value === null))
			return openListbox();
		const listLength = filteredList.value.length;
		if (refList.value.activeItemIndex <= 0)
			refList.value.activeItemIndex = listLength - 1;
		else if (refList.value.activeItemIndex >= 1)
			refList.value.activeItemIndex--;
		scrollToActiveItem();
	}

	function onBackspace() {
		if (showList.value === false)
			openListbox();
	}

	function onSpace() {
		if (!props.autocomplete)
			if (showList.value)
				selectCurrentActiveItem();
			else
				openListbox();
	}

	function onTab() {
		if (props.autocomplete && (refList.value !== undefined) && (refList.value !== null)) {
			refList.value.activeItemIndex = 0;
			selectCurrentActiveItem();
		}
	}

	function scrollToActiveItem() {
		refList.value?.itemRefs[refList.value.activeItemIndex]?.scrollIntoView();
	}

	const { x, y, strategy } = useFloating(
		inputElTags,
		refList as Readonly<Ref<MaybeElement<HTMLElement>>>,
		{
			placement: 'bottom',
			middleware: [flip(), shift(), offset(2), size({
				apply({rects, elements}) {
					Object.assign(elements.floating.style, {
						width: `${rects.reference.width}px`,
					});
				},
			})],
			whileElementsMounted: autoUpdate,
		}
	);

	const floatingTop = computed(() => `${y.value}px`);
	const floatingLeft = computed(() => `${x.value}px`);

	const content = computed<Item[]>(() => [...data.value]);

	defineExpose({content});

</script>
