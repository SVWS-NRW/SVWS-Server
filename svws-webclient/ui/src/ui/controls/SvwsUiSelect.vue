<template>
	<div class="svws-ui-select" :class="{
		'svws-open': showList,
		'svws-has-value': hasSelected,
		'svws-headless': headless,
		'svws-statistik': statistics,
		'svws-danger': danger,
		'svws-disabled': disabled,
		'svws-removable': removable,
		'svws-readonly': readonly,
	}" v-bind="$attrs">
		<svws-ui-text-input ref="inputEl"
			:model-value="dynModelValue"
			:readonly="!autocomplete || readonly"
			:is-select-input="!readonly"
			:placeholder="label || title"
			:statistics
			:headless
			:disabled
			:required
			role="combobox"
			:aria-label="label || title"
			:aria-expanded="showList"
			aria-haspopup="listbox"
			aria-autocomplete="list"
			:aria-controls="showList ? listIdPrefix : null"
			:aria-activedescendant="refList && refList.activeItemIndex > -1 ? `${listIdPrefix}-${refList.activeItemIndex}` : null"
			@update:model-value="onInput"
			@click.stop="toggleListBox"
			@focus="onInputFocus"
			@blur="onInputBlur"
			@keyup.down.prevent
			@keyup.up.prevent
			@keydown.down.prevent="onArrowDown"
			@keydown.up.prevent="onArrowUp"
			@keydown.enter.prevent="selectCurrentActiveItem"
			@keydown.backspace="onBackspace"
			@keydown.esc.prevent="toggleListBox"
			@keydown.space.prevent="onSpace"
			@keydown.tab="onTab"
			:focus="autofocus"
			:class="{ 'contentFocusField': focusClassContent, 'subNavigationFocusField': focusClassSubNav }" />
		<button v-if="removable && hasSelected && !readonly" role="button" @click.stop="removeItem" class="svws-remove">
			<span class="icon i-ri-close-line svws-ui-select--icon my-1" />
		</button>
		<button v-if="!readonly" role="button" class="svws-dropdown-icon" tabindex="-1">
			<span class="icon i-ri-expand-up-down-line svws-ui-select--icon" v-if="headless" />
			<span class="icon i-ri-expand-up-down-fill svws-ui-select--icon my-1" v-else />
		</button>
	</div>
	<Teleport to="body" v-if="isMounted">
		<svws-ui-dropdown-list v-if="showList" :statistics :filtered-list :item-text="itemText" :strategy :floating-left :floating-top :selected-item-list
			:select-item ref="refList" :search-text :highlight-item="(highlightItem as Item|undefined)" />
	</Teleport>
</template>

<script setup lang="ts" generic="Item">

	import type { Ref } from "vue";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { MaybeElement } from "@floating-ui/vue";
	import { useFloating, autoUpdate, flip, offset, shift, size } from "@floating-ui/vue";
	import { computed, nextTick, onMounted, ref, shallowRef, toRaw, watch, useId } from "vue";
	import SvwsUiDropdownList from "./SvwsUiDropdownList.vue";

	// eslint-disable-next-line @typescript-eslint/no-redundant-type-constituents
	type SelectDataType = Item | null | undefined;

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
		emptyText?: () => string;
		itemSort?: (a: Item, b: Item) => number;
		itemFilter?: (items: Item[], searchText: string) => Item[];
		modelValue: SelectDataType;
		useNull?: boolean;
		headless?: boolean;
		removable?: boolean;
		required?: boolean;
		indeterminate?: boolean;
		highlightItem?: Item;
		autofocus?: boolean;
		focusClassContent?: boolean;
		focusClassSubNav?: boolean;
	}>(), {
		label: '',
		title: '',
		autocomplete: false,
		disabled: false,
		readonly: false,
		statistics: false,
		danger: false,
		emptyText: () => '',
		itemSort: (a: Item, b: Item) => 0,
		itemFilter: (items: Item[]) => items,
		useNull: false,
		headless: false,
		removable: false,
		indeterminate: false,
		highlightItem: undefined,
		autofocus: false,
		focusClassContent: false,
		focusClassSubNav: false,
	})

	const emit = defineEmits<{
		(e: "update:modelValue", items: SelectDataType): void;
	}>();

	const refList = ref<ComponentExposed<typeof SvwsUiDropdownList>>();
	const showList = ref(false);
	const inputEl = ref();
	const hasFocus = ref(false);
	const searchText = ref("");
	const listIdPrefix = useId();
	const isMounted = ref(false);
	onMounted(() => isMounted.value = true);

	function onInputFocus() {
		hasFocus.value = true;
	}

	function onInputBlur() {
		hasFocus.value = false;
		closeListbox();
	}

	const dynModelValue = computed<string>(() => {
		return (showList.value && props.autocomplete) ? searchText.value : generateInputText();
	});

	function generateInputText() {
		if ((selectedItem.value === null) || (selectedItem.value === undefined))
			return props.emptyText();
		return props.itemText(selectedItem.value);
	}

	function onInput(value: string | null) {
		if (props.autocomplete && ((refList.value === undefined) || (refList.value === null)) && (document.hasFocus()) && (inputEl.value !== null) && (document.activeElement === inputEl.value))
			openListbox();
		const activeItem = ((refList.value === undefined) || (refList.value === null)) ? undefined : filteredList.value.at(refList.value.activeItemIndex);
		searchText.value = value ?? "";
		if (props.autocomplete) {
			void nextTick(() => {
				if ((refList.value !== undefined) && (refList.value !== null)) {
					let index = 0;
					if (activeItem !== undefined) {
						const tmpIndex = filteredList.value.findIndex(item => item === activeItem);
						if (tmpIndex >= 0)
							index = tmpIndex;
					} else if ((selectedItem.value !== null) && (selectedItem.value !== undefined)) {
						const tmpIndex = filteredList.value.findIndex(item => item === selectedItem.value);
						if (tmpIndex >= 0)
							index = tmpIndex;
					}
					refList.value.activeItemIndex = index;
				}
			});
		}
	}

	// eslint-disable-next-line vue/no-setup-props-reactivity-loss
	const data = shallowRef<SelectDataType>(props.modelValue);

	watch(() => props.modelValue, (value: SelectDataType) => updateData(toRaw(value), true), { immediate: false });

	function updateData(value: SelectDataType, fromModelValue : boolean) {
		if (((value === null) || (value === undefined)) && ((data.value === null) || (data.value === undefined)))
			return;
		if (data.value === value)
			return;
		data.value = value;
		if (!fromModelValue)
			emit("update:modelValue", data.value);
		if (props.indeterminate === true)
			data.value = undefined;
	}

	const selectedItem = computed<SelectDataType>({
		get: () => data.value,
		set: (item) => updateData(item, false),
	});

	const selectedItemList = computed<Set<Item>>(() => {
		const set = new Set<Item>();
		if ((data.value !== null) && (data.value !== undefined))
			set.add(toRaw(data.value));
		return set;
	});

	const hasSelected = computed(() => (selectedItem.value !== null) && (selectedItem.value !== undefined));

	function selectItem(item: SelectDataType) {
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

	function reset(originalValue?: boolean) {
		if (originalValue === true)
			selectedItem.value = props.modelValue;
		else
			selectedItem.value = props.useNull ? null : undefined;
		const el = inputEl.value;
		el?.input.blur();
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

	watch(sortedList, items => {
		for (const item of items)
			if (item === data.value)
				return;
		data.value = undefined;
	});

	const filteredList = computed<Item[]>(() => {
		if (props.autocomplete) {
			const isCurrent : boolean = (selectedItem.value !== null) && (selectedItem.value !== undefined) && (props.itemText(selectedItem.value) === searchText.value);
			if (isCurrent)
				return sortedList.value;
			return props.itemFilter(sortedList.value, searchText.value);
		}
		return sortedList.value;
	});

	function doFocus() {
		const el = inputEl.value;
		el?.input.focus();
	}

	function toggleListBox() {
		if (showList.value)
			closeListbox();
		else
			openListbox();
	}

	function openListbox() {
		doFocus();
		showList.value = true;
		void nextTick(() => {
			if (((refList.value === undefined) || (refList.value === null)))
				return;
			if ((selectedItem.value !== null) && (selectedItem.value !== undefined))
				refList.value.activeItemIndex = filteredList.value.findIndex(item => item === selectedItem.value);
			else
				refList.value.activeItemIndex = 0;
			if (refList.value.itemRefs[refList.value.activeItemIndex] !== undefined)
				refList.value.itemRefs[refList.value.activeItemIndex].scrollIntoView();
		});
	}

	function closeListbox() {
		if ((refList.value !== undefined) && (refList.value !== null))
			refList.value.activeItemIndex = -1;
		showList.value = false;
	}

	function selectCurrentActiveItem() {
		if ((refList.value === undefined) || (refList.value === null) || (refList.value.activeItemIndex < 0))
			return;
		selectItem(filteredList.value[refList.value.activeItemIndex]);
	}

	function onArrowDown() {
		if ((!showList.value) || (refList.value === undefined) || (refList.value === null))
			return openListbox();
		const listLength = filteredList.value.length;
		if (refList.value.activeItemIndex < listLength - 1)
			refList.value.activeItemIndex++;
		else
			refList.value.activeItemIndex = 0;
		refList.value.itemRefs[refList.value.activeItemIndex].scrollIntoView();
	}

	function onArrowUp() {
		if ((!showList.value) || (refList.value === undefined) || (refList.value === null))
			return openListbox();
		const listLength = filteredList.value.length;
		if (refList.value.activeItemIndex === 0)
			refList.value.activeItemIndex = listLength - 1;
		else if (refList.value.activeItemIndex >= 1)
			refList.value.activeItemIndex--;
		refList.value.itemRefs[refList.value.activeItemIndex].scrollIntoView();
	}

	function onBackspace() {
		if (showList.value === false)
			openListbox();
	}

	function onSpace() {
		if (!props.autocomplete)
			if (!showList.value)
				openListbox();
			else
				selectCurrentActiveItem();
	}

	function onTab() {
		if (props.autocomplete && (refList.value !== undefined) && (refList.value !== null)) {
			refList.value.activeItemIndex = 0;
			selectCurrentActiveItem();
		}
	}

	const { x, y, strategy } = useFloating(inputEl, refList as Readonly<Ref<MaybeElement<HTMLElement>>>, {
		placement: 'bottom',
		middleware: [flip(), shift(), offset(2), size({ apply({rects, elements}) { Object.assign(elements.floating.style, { width: `${rects.reference.width}px` }); } })],
		whileElementsMounted: autoUpdate,
	});

	const floatingTop = computed(() => `${y.value}px`);
	const floatingLeft = computed(() => `${x.value}px`);

	const content = computed<SelectDataType>(() => data.value);

	defineExpose({ content, reset });

</script>
