<template>
	<div class="flex gap-1 svws-ui-select svws-ui-multi-select" :class="{ 'svws-open': showList, 'svws-has-value': hasSelected(), 'svws-headless': headless, 'svws-statistik': statistics, 'svws-danger': danger, 'svws-disabled': disabled, 'svws-autocomplete': autocomplete}" v-bind="$attrs" ref="inputElTags">
		<div v-if="!headless && selectedItemList.size" class="svws-tags">
			<span v-for="(item, index) in selectedItemList" :key="index" class="svws-tag">
				<span class="line-clamp-1 leading-tight -my-0.5 break-all max-w-[14rem]">{{ itemText(item) }}</span>
				<button role="button" class="svws-remove" @click.stop="removeTag(item)" title="Entfernen">
					<i-ri-close-line />
				</button>
			</span>
		</div>
		<div class="flex-grow">
			<svws-ui-text-input ref="inputEl"
				:model-value="headless ? dynModelValue : (selectedItemList.size ? ' ' : '')"
				:readonly="!autocomplete"
				:placeholder="label || title"
				:statistics="statistics"
				:headless="headless"
				:disabled="disabled"
				:debounce-ms="0"
				role="combobox"
				:aria-label="label || title"
				:aria-expanded="showList"
				aria-haspopup="listbox"
				aria-autocomplete="list"
				:aria-controls="showList ? listIdPrefix : null"
				:aria-activedescendant="refList && refList.activeItemIndex > -1 ? `${listIdPrefix}-${refList.activeItemIndex}` : null"
				@update:model-value="value => searchText = value"
				@click="toggleListBox"
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
		</div>
		<button role="button" class="svws-dropdown-icon" tabindex="-1">
			<i-ri-expand-up-down-line v-if="headless" />
			<i-ri-expand-up-down-fill v-else />
		</button>
	</div>
	<Teleport to="body">
		<svws-ui-dropdown-list v-if="showList" :statistics="statistics" :tags="true" :filtered-list="filteredList" :item-text="itemText"
			:strategy="strategy" :floating-left="floatingLeft" :floating-top="floatingTop" :selected-item-list="selectedItemList"
			:select-item="selectItem" ref="refList" />
	</Teleport>
</template>


<script setup lang="ts" generic="Item">

	import type { ComputedRef, Ref } from "vue";
	import { computed, nextTick, ref, shallowRef, watch, Teleport, toRaw } from "vue";
	import type { ComponentExposed } from "vue-component-type-helpers";
	import type { MaybeElement } from "@floating-ui/vue";
	import { useFloating, autoUpdate, flip, offset, shift, size } from "@floating-ui/vue";
	import { genId } from "../utils";
	import type TextInput from "./SvwsUiTextInput.vue";
	import SvwsUiDropdownList from "./SvwsUiDropdownList.vue";

	const props = withDefaults(defineProps<{
		label?: string;
		title?: string;
		autocomplete?: boolean;
		disabled?: boolean;
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
	}>(), {
		label: '',
		title: '',
		autocomplete: false,
		disabled: false,
		statistics: false,
		danger: false,
		itemSort: (a: Item, b: Item) => 0,
		itemFilter: (items: Iterable<Item> | Item[], searchText: string) => Array.isArray(items) ? items : [...items],
		useNull: false,
		headless: false,
	})

	const emit = defineEmits<{
		(e: "update:modelValue", items: Item[]): void;
	}>();

	const refList = ref<ComponentExposed<typeof SvwsUiDropdownList> | null>(null);

	const showList = ref(false);
	const listIdPrefix = genId();

	const inputEl = ref(null);
	const inputElTags = ref(null);
	const hasFocus = ref(false);
	const searchText = ref("");

	function onInputFocus() {
		hasFocus.value = true;
	}

	function onInputBlur() {
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
		return [...selectedItemList.value].map(item => props.itemText(item)).join(", ");
	}

	// eslint-disable-next-line vue/no-setup-props-destructure
	const data = shallowRef<Item[]>(props.modelValue);

	watch(() => props.modelValue, (value) => updateData(value), { immediate: false });

	function updateData(value: Item[]) {
		if (((value === null) || (value === undefined)) && ((data.value === null) || (data.value === undefined)))
			return;
		const a = ((data.value === null) || (data.value === undefined)) ? [] : data.value;
		const b = ((value === null) || (value === undefined)) ? [] : value;
		if ((a.length === b.length) && (a.every((v, i) => v === b[i])))
			return;
		data.value = b;
		emit("update:modelValue", data.value);
	}

	const selectedItem = computed<Item | null | undefined>({
		get: () => data.value[0],
		set: (item) => {
			if (item !== null && item !== undefined)
				if (selectedItemList.value.has(item))
					selectedItemList.value.delete(item);
				else
					selectedItemList.value.add(item);
			updateData([...selectedItemList.value]);
		}
	});

	const selectedItemList = computed(() => new Set(toRaw(data.value)));

	function hasSelected(): boolean {
		return (selectedItem.value !== null) && (selectedItem.value !== undefined);
	}

	function selectItem(item: Item | null | undefined) {
		selectedItem.value = item;
		if (props.autocomplete)
			searchText.value = "";
		doFocus();
	}

	function removeTag(item: Item) {
		if (selectedItemList.value.has(item))
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
		if (props.itemSort)
			return arr.sort(props.itemSort);
		return arr;
	});

	const filteredList = computed<Item[]>(() => {
		if (props.autocomplete)
			if (props.itemFilter)
				return props.itemFilter(sortedList.value, searchText.value);
			else
				return sortedList.value.filter(i => props.itemText(i).startsWith(searchText.value ?? ""));
		return sortedList.value;
	});

	function doFocus() {
		const el: typeof TextInput = inputEl.value!;
		void nextTick(() => el?.input.focus());
	}

	function toggleListBox() {
		showList.value ? closeListbox() : openListbox();
	}

	function openListbox() {
		showList.value = true;
		if ((selectedItem.value !== null) && (selectedItem.value !== undefined) && refList.value !== null) {
			refList.value.activeItemIndex = filteredList.value.findIndex(item => item === selectedItem.value);
			void nextTick(() => scrollToActiveItem());
		}
		const el: typeof TextInput = inputEl.value!;
		void nextTick(() => el?.input.focus());
	}

	function closeListbox() {
		showList.value = false;
		if (refList.value !== null)
			refList.value.activeItemIndex = -1;
	}

	function selectCurrentActiveItem() {
		if (showList.value && refList.value !== null)
			selectItem(filteredList.value[refList.value.activeItemIndex]);
	}

	// Arrow Navigation
	function onArrowDown() {
		if ((!showList.value) || (refList.value === null))
			return openListbox();
		const listLength = filteredList.value.length;
		if (refList.value.activeItemIndex < listLength - 1)
			refList.value.activeItemIndex++;
		else
			refList.value.activeItemIndex = 0;
		scrollToActiveItem();
	}

	function onArrowUp() {
		if ((!showList.value) || (refList.value === null))
			return openListbox();
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

	function onSpace(e: InputEvent) {
		if (!props.autocomplete)
			showList.value ? selectCurrentActiveItem() : openListbox();
	}

	function onTab(e: InputEvent) {
		if (props.autocomplete && refList.value !== null) {
			refList.value.activeItemIndex = 0;
			selectCurrentActiveItem();
		}
	}

	function scrollToActiveItem() {
		refList.value?.itemRefs[refList.value.activeItemIndex].scrollIntoView();
	}

	const {x, y, strategy} = useFloating(
		inputElTags,
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

	const content = computed<Item[]>(() => data.value);

	defineExpose<{
		content: ComputedRef<Item[]>,
	}>({content});

</script>


<style lang="postcss">
.svws-ui-multi-select {
	&:not(.svws-headless) {
		@apply bg-white dark:bg-black;
		@apply rounded-md border border-black/5 dark:border-white/5;

		&:hover {
			@apply border-black/25 dark:border-white/25;
		}
	}

	.text-input--control {
		@apply bg-transparent border-none;
	}

  &.svws-ui-select .svws-dropdown-icon {
    height: calc(100% - 0.5rem);
  }

  &.svws-open.svws-autocomplete .svws-tags {
	@apply mb-[2rem];
  }

  &:not(.svws-headless) {
    @apply min-h-[2.25rem];

    .text-input-component {
      @apply absolute top-0 left-0 w-full h-full cursor-pointer;
    }

	&.svws-open.svws-autocomplete {
		.text-input--control {
			@apply h-[2.25rem] mt-auto pl-3;
		}
	}

	&:not(.svws-open) {
		.text-input--control {
			@apply hidden;
		}
	}
  }

  .svws-tags {
    @apply relative z-10 flex flex-wrap gap-0.5 pl-1 pt-2 pb-1 pr-7 pointer-events-none;

    .svws-remove {
      @apply relative top-0 left-0 w-auto h-auto -mx-0.5 text-black/50 dark:text-white/50 hover:text-error text-sm pointer-events-auto;
    }
  }

  .svws-tag {
    @apply inline-flex items-center gap-0.5 border border-black/10 rounded leading-none py-[0.2rem] pl-2 pr-1 text-base bg-white dark:bg-black;
  }

  &.svws-disabled {
    .svws-tags {
      @apply opacity-25;

      .svws-remove {
        @apply invisible -mr-3;
      }
    }
  }
}
</style>

<!--TODO: Duplicate CSS und JS Functions (identisch zu SvwsUiSelect) entfernen-->
<style lang="postcss">

.svws-ui-select {
  @apply relative w-full cursor-pointer flex;

  .svws-ui-table & {
    @apply p-0;
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
      @apply w-4 -left-0.5 bg-transparent dark:bg-transparent border-0 text-sm text-black/50 dark:text-white/50;
    }

    .svws-remove {
      @apply right-auto left-4 inline-flex items-center justify-center w-4;

      svg {
        @apply -m-px;
      }
    }

    .text-input-component {
      @apply pr-1;

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
