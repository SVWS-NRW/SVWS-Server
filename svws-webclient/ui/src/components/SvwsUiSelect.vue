<template>
	<div class="svws-ui-select" :class="{ 'svws-open': showList, 'svws-has-value': hasSelected, 'svws-headless': headless, 'svws-statistik': statistics, 'svws-danger': danger, 'svws-disabled': disabled, 'svws-removable': removable}" v-bind="$attrs">
		<svws-ui-text-input ref="inputEl"
			:model-value="dynModelValue"
			:readonly="!autocomplete"
			:placeholder="label || title"
			:statistics="statistics"
			:headless="headless"
			:disabled="disabled"
			:removable="removable"
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
			@keydown.tab="onTab" />
		<button v-if="removable && hasSelected" role="button" @click.stop="removeItem" class="svws-remove">
			<i-ri-close-line />
		</button>
		<button role="button" class="svws-dropdown-icon" tabindex="-1">
			<i-ri-expand-up-down-line v-if="headless" />
			<i-ri-expand-up-down-fill v-else />
		</button>
	</div>
	<Teleport to="body">
		<svws-ui-dropdown-list v-if="showList" :statistics="statistics" :filtered-list="filteredList" :item-text="itemText"
			:strategy="strategy" :floating-left="floatingLeft" :floating-top="floatingTop" :selected-item-list="selectedItemList"
			:select-item="selectItem" ref="refList" :search-text="searchText" />
	</Teleport>
</template>

<script setup lang="ts" generic="Item">

	import type { ComputedRef, Ref } from "vue";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { MaybeElement } from "@floating-ui/vue";
	import type TextInput from "./SvwsUiTextInput.vue";
	import { useFloating, autoUpdate, flip, offset, shift, size } from "@floating-ui/vue";
	import { computed, nextTick, ref, shallowRef, toRaw, watch } from "vue";
	import { genId } from "../utils";
	import SvwsUiDropdownList from "./SvwsUiDropdownList.vue";

	type SelectDataType = Item | null | undefined;

	const props = withDefaults(defineProps<{
		label?: string;
		title?: string;
		autocomplete?: boolean;
		disabled?: boolean;
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
	}>(), {
		label: '',
		title: '',
		autocomplete: false,
		disabled: false,
		statistics: false,
		danger: false,
		emptyText: () => '',
		itemSort: (a: Item, b: Item) => 0,
		itemFilter: (items: Item[]) => items,
		useNull: false,
		headless: false,
		removable: false,
		indeterminate: false,
	})

	const emit = defineEmits<{
		(e: "update:modelValue", items: SelectDataType): void;
	}>();

	const refList = ref<ComponentExposed<typeof SvwsUiDropdownList> | null>(null);
	const showList = ref(false);
	const inputEl = ref(null);
	const hasFocus = ref(false);
	const searchText = ref("");
	const listIdPrefix = genId();

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

	function onInput(value: string) {
		if (props.autocomplete && (refList.value === null) && (document.hasFocus()) && (inputEl.value !== null) && (document.activeElement === inputEl.value))
			openListbox();
		const activeItem = refList.value === null ? undefined : filteredList.value.at(refList.value.activeItemIndex);
		searchText.value = "" + value;
		if (props.autocomplete) {
			void nextTick(() => {
				if (refList.value !== null) {
					let index = 0;
					if (activeItem !== undefined) {
						const tmpIndex = filteredList.value.findIndex(item => item === activeItem);
						if (tmpIndex >= 0)
							index = tmpIndex;
					} else if ((selectedItem.value !== null) || (selectedItem.value === undefined)) {
						const tmpIndex = filteredList.value.findIndex(item => item === selectedItem.value);
						if (tmpIndex >= 0)
							index = tmpIndex;
					}
					refList.value.activeItemIndex = index;
				}
			});
		}
	}

	// eslint-disable-next-line vue/no-setup-props-destructure
	const data = shallowRef<SelectDataType>(props.modelValue);

	watch(() => props.modelValue, (value: SelectDataType) => {
		updateData(toRaw(value), true);
	}, { immediate: false });

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
		set: (item) => updateData(item, false)
	});

	const selectedItemList = computed<Set<Item>>(() => {
		const set = new Set<Item>();
		if ((data.value !== null) && (data.value !== undefined))
			set.add(data.value);
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

	function reset() {
		selectedItem.value = props.useNull ? null : undefined;
		const el: typeof TextInput = inputEl.value!;
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
		if (props.itemSort)
			return arr.sort(props.itemSort);
		return arr;
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
		const el: typeof TextInput = inputEl.value!;
		el?.input.focus();
	}

	function toggleListBox() {
		showList.value ? closeListbox() : openListbox();
	}

	function openListbox() {
		doFocus();
		showList.value = true;
		void nextTick(() => {
			if (refList.value === null)
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
		showList.value = false;
		if (refList.value !== null)
			refList.value.activeItemIndex = -1;
	}

	function selectCurrentActiveItem() {
		if ((refList.value === null) || (refList.value.activeItemIndex < 0))
			return;
		selectItem(filteredList.value[refList.value.activeItemIndex]);
	}

	function onArrowDown() {
		if ((!showList.value) || (refList.value === null))
			return openListbox();
		const listLength = filteredList.value.length;
		if (refList.value.activeItemIndex < listLength - 1)
			refList.value.activeItemIndex++;
		else
			refList.value.activeItemIndex = 0;
		refList.value.itemRefs[refList.value.activeItemIndex].scrollIntoView();
	}

	function onArrowUp() {
		if ((!showList.value) || (refList.value === null))
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
		if (props.autocomplete && refList.value !== null) {
			refList.value.activeItemIndex = 0;
			selectCurrentActiveItem();
		}
	}

	const {x, y, strategy} = useFloating( inputEl, refList as Readonly<Ref<MaybeElement<HTMLElement>>>, {
		placement: 'bottom',
		middleware: [flip(), shift(), offset(2), size({ apply({rects, elements}) { Object.assign(elements.floating.style, { width: `${rects.reference.width}px` }); } })],
		whileElementsMounted: autoUpdate,
	});

	const floatingTop = computed(() => `${y.value ?? 0}px`);
	const floatingLeft = computed(() => `${x.value ?? 0}px`);

	const content = computed<SelectDataType>(() => data.value);

	defineExpose<{
		content: ComputedRef<SelectDataType>,
		reset: () => void,
	}>({ content, reset });

</script>


<style lang="postcss">

.svws-ui-select {
	@apply relative w-full cursor-pointer flex;

  .svws-ui-table & {
    @apply p-0 -my-0.5;
  }

  .svws-dropdown-icon,
  .svws-remove {
    @apply inline-flex w-5 h-7 absolute text-headline-md top-1 rounded;

    svg {
      @apply my-auto;
    }
  }

  .svws-dropdown-icon {
    @apply pointer-events-none right-1 bg-light dark:bg-white/5 border border-black/10 dark:border-white/10;
  }

  &.svws-statistik {
    .svws-dropdown-icon {
      @apply bg-violet-500/5 dark:bg-violet-500/10 text-violet-500;
    }

    .text-input-component {
      &:hover,
      &:focus-visible,
      &:focus-within {
        ~ .svws-dropdown-icon {
          @apply bg-violet-500/10 dark:bg-violet-500/20;
        }
      }
    }
  }

  .svws-remove {
    @apply right-7 text-black/50 dark:text-white/50;

    &:hover {
      @apply text-error;
    }

    &:focus,
    &:focus-visible {
      @apply outline-none;
    }

    &:focus-visible {
      @apply ring ring-error/25 bg-error text-white dark:text-white;
    }
  }

  .text-input-component {
    &:hover,
    &:focus-visible,
    &:focus-within {
      ~ .svws-dropdown-icon {
        @apply bg-black/10 dark:bg-white/10;
      }
    }
  }

  &.svws-open {
    @apply z-50;
  }

  .text-input--control,
  .text-input--headless {
    @apply text-ellipsis break-all cursor-pointer;
  }

	.text-input--control {
		@apply pr-8;
	}

  .text-input--headless {
    @apply pl-4;
  }

	&.svws-removable&.svws-has-value {
		.text-input--control {
			@apply pr-12;
		}

		.text-input--headless {
			@apply pl-[2.1rem];
		}
	}

  &.svws-headless {
    .svws-dropdown-icon,
    .svws-remove {
      @apply right-auto h-5 top-0;
    }

    .svws-dropdown-icon {
      @apply w-4 -left-0.5 bg-transparent dark:bg-transparent border-0 text-sm text-black/50 dark:text-white/50 -top-px;

      .svws-ui-table .svws-clicked & {
        @apply text-svws/50 dark:text-svws/50;
      }
    }

    .svws-remove {
      @apply right-auto left-4 inline-flex items-center justify-center w-4;

      svg {
        @apply -m-px;
      }
    }

	  .text-input-component {
		  @apply pr-1 my-0;

		  &:hover,
		  &:focus-visible,
		  &:focus-within {
			  ~ .svws-dropdown-icon {
				  @apply text-black dark:text-white;
			  }
		  }
	  }

	  &:not(.svws-open) {
		  .text-input-component {
			  &:focus-visible {
				  ~ .svws-dropdown-icon {
					  @apply ring-2 ring-black/25 dark:ring-white/25;
				  }
			  }
		  }
	  }

	  &.svws-statistik {
		  .svws-dropdown-icon {
			  @apply text-violet-500/75 dark:text-violet-500/75;
		  }

		  .text-input-component {
			  &:hover,
			  &:focus-visible,
			  &:focus-within {
				  ~ .svws-dropdown-icon {
					  @apply text-violet-500 dark:text-violet-500;
				  }
			  }

			  &:focus-visible {
				  ~ .svws-dropdown-icon {
					  @apply ring-violet-500/25 dark:ring-violet-500/25;
				  }
			  }
		  }
	  }
  }

  &.svws-danger {
    @apply text-error;

    .svws-dropdown-icon {
      @apply text-error;
    }

    .text-input--headless {
      @apply font-bold;
    }

    &.svws-headless {
      .svws-dropdown-icon {
        @apply text-error/50;
      }
    }
  }

  &.svws-disabled {
    @apply cursor-default pointer-events-none;

    .text-input--headless {
      @apply opacity-25;
    }

    .svws-dropdown-icon,
    .svws-remove {
      @apply opacity-25;
    }
  }
}

</style>
